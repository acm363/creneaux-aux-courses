package com.es.slots.pick_up_order.services;

import com.es.slots.authentication.services.AuthenticationUserService;
import com.es.slots.pick_up_order.dto.requests.PickUpOrderRequest;
import com.es.slots.pick_up_order.entities.PickUpOrder;
import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderAlreadyExistException;
import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderNotFoundException;
import com.es.slots.pick_up_order.repositories.PickUpOrderRepository;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.entities.Slot;
import com.es.slots.slot.entities.StandardSlot;
import com.es.slots.slot.exceptions.customs.FullSlotException;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import com.es.slots.slot.repositories.ExceptionalSlotRepository;
import com.es.slots.slot.repositories.StandardSlotRepository;
import com.es.slots.user.entities.Admin;
import com.es.slots.user.entities.Client;
import com.es.slots.user.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PickUpOrderService {

    private final AuthenticationUserService authenticationUserService;
    private final StandardSlotRepository standardSlotRepository;
    private final ExceptionalSlotRepository exceptionalSlotRepository;
    private final PickUpOrderRepository pickUpOrderRepository;


    /**
     * Get all pickUpOrder commands from User
     *
     * @return List of PickUpOrder
     */
    public List<PickUpOrder> getAllPickUpOrdersByUser() {
        User user = authenticationUserService.getUserAuthenticated();
        if (user instanceof Admin) {
            return pickUpOrderRepository.findAll();
        }

        Client client = (Client) user;
        List<PickUpOrder> allByClient = pickUpOrderRepository.findAllByClient(client);

        for (PickUpOrder pickUpOrder : allByClient) {
            Optional<StandardSlot> standardSlot = standardSlotRepository.findByPublicId(pickUpOrder.getSlot().getPublicId());
            standardSlot.ifPresent(pickUpOrder::setSlot);
            if (standardSlot.isPresent()) {
                return allByClient;
            } else {
                Optional<ExceptionalSlot> exceptionalSlot = exceptionalSlotRepository.findByPublicId(pickUpOrder.getSlot().getPublicId());
                exceptionalSlot.ifPresent(pickUpOrder::setSlot);
            }
        }
        return allByClient;
    }


    /**
     * Get pickUpOrder command from User by id
     *
     * @param pickUpOrderId Retrait command id
     * @return PickUpOrder
     */
    public PickUpOrder getPickUpOrderByIdAndUser(String pickUpOrderId) throws PickUpOrderNotFoundException {
        User user = authenticationUserService.getUserAuthenticated();
        if (user instanceof Admin) {
            Optional<PickUpOrder> optionalPickUpOrder = pickUpOrderRepository.findByPickUpOrderId(pickUpOrderId);
            if (optionalPickUpOrder.isEmpty()) {
                throw new PickUpOrderNotFoundException("No pickUps found");
            }
            return optionalPickUpOrder.get();
        }

        Client client = (Client) user;
        return client.getPickUpOrderList()
                .stream()
                .filter(pickUpOrder -> pickUpOrder.getPickUpOrderId().equals(pickUpOrderId))
                .findFirst()
                .orElseThrow(() -> new PickUpOrderNotFoundException("No pickUps found"));
    }


    /**
     * Create new pickUpOrder to User from slot
     *
     * @param pickUpOrderRequest Slot request data
     * @return PickUpOrder
     * @throws SlotNotFoundException If the slot is not found
     */
    public PickUpOrder createNewPickUpOrder(PickUpOrderRequest pickUpOrderRequest) throws SlotNotFoundException, PickUpOrderAlreadyExistException, FullSlotException {
        Client client = (Client) authenticationUserService.getUserAuthenticated();
        Slot slot = this.getSlotByPublicId(pickUpOrderRequest.getSlotId());

        if (checkSlotIsFullAtDate(slot, pickUpOrderRequest.getDate())) {
            throw new FullSlotException("Slot is full. Could create new PickUpOrder with this slot.");
        };

        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setClient(client);
        pickUpOrder.setSlot(slot);
        pickUpOrder.setPickUpOrderId(UUID.randomUUID().toString());
        pickUpOrder.setLocalDate(pickUpOrderRequest.getDate());

        try {
            return pickUpOrderRepository.save(pickUpOrder);
        } catch (Exception e) {
            throw new PickUpOrderAlreadyExistException("PickUpOrder already exist");
        }
    }


    /**
     * Update pickUpOrder command
     * Free the oldSlot to take the new one
     *
     * @param pickUpOrderRequest Slot request data
     * @param pickUpOrderId Retrait command id to update
     */
    public void updatePickUpOrder(PickUpOrderRequest pickUpOrderRequest, String pickUpOrderId) throws SlotNotFoundException, PickUpOrderNotFoundException, PickUpOrderAlreadyExistException, FullSlotException {
        Client client = (Client) authenticationUserService.getUserAuthenticated();
        Slot newSlot = this.getSlotByPublicId(pickUpOrderRequest.getSlotId());

        if (checkSlotIsFullAtDate(newSlot, pickUpOrderRequest.getDate())) {
            throw new FullSlotException("Slot is full. Could create new PickUpOrder with this slot.");
        };

        PickUpOrder pickUpOrder = client.getPickUpOrderList()
                .stream()
                .filter(pick -> pick.getPickUpOrderId().equals(pickUpOrderId))
                .findFirst()
                .orElseThrow(() -> new PickUpOrderNotFoundException("No PickUpOrder found with slot id " + pickUpOrderId));

        if (pickUpOrder.getSlot().getPublicId().equals(pickUpOrderRequest.getSlotId())) {
            throw new PickUpOrderAlreadyExistException("PickUpOrder already exists for this user");
        }

        pickUpOrder.setSlot(newSlot);
        pickUpOrderRepository.save(pickUpOrder);
    }


    /**
     * Remove pickUpOrder command
     *
     * @param pickUpOrderId Retrait command id to remove
     * @throws PickUpOrderNotFoundException If no pickupOrder exist for that id in the list of user
     */
    public void removePickUpOrder(String pickUpOrderId) throws PickUpOrderNotFoundException {
        User user = authenticationUserService.getUserAuthenticated();
        if (user instanceof Admin) {
            Optional<PickUpOrder> pickUpOrder = pickUpOrderRepository.findByPickUpOrderId(pickUpOrderId);
            if (pickUpOrder.isEmpty()) {
                throw new PickUpOrderNotFoundException("Could not find pickUpOrder with this id");
            }
            pickUpOrderRepository.delete(pickUpOrder.get());
        }
        else {
            Client client = (Client) user;
            PickUpOrder pickUpOrder = client.getPickUpOrderList().stream().filter(pickOrder -> pickOrder.getPickUpOrderId().equals(pickUpOrderId)).findFirst().orElseThrow(() -> new PickUpOrderNotFoundException("Could not find pickUpOrder with this id"));
            if (pickUpOrder != null) {
                client.getPickUpOrderList().remove(pickUpOrder);
                pickUpOrderRepository.delete(pickUpOrder);
            }
        }
    }


    /**
     * @param slotId The public id of the slot
     * @return The slot
     * @throws SlotNotFoundException If the slot is not found
     */
    private Slot getSlotByPublicId(String slotId) throws SlotNotFoundException {
        Optional<StandardSlot> optional = standardSlotRepository.findByPublicId(slotId);
        if (optional.isPresent()) {
            return optional.get();
        }

        Optional<ExceptionalSlot> exceptionalSlot = exceptionalSlotRepository.findByPublicId(slotId);
        if (exceptionalSlot.isPresent()) {
            return exceptionalSlot.get();
        }
        throw new SlotNotFoundException(slotId);
    }


    /**
     * Verify that a Slot is not full.
     *
     * @param slot The slot to check
     * @param date The date of reservation
     * @return If the slot is not full.
     */
    private boolean checkSlotIsFullAtDate(Slot slot, LocalDate date) {
        return slot.getPickUpOrderList().stream().filter(pickUpOrder -> pickUpOrder.getLocalDate().isEqual(date)).count() >= slot.getCapacity();
    }
}
