package com.es.slots.pick_up_order.services;

import com.es.slots.authentication.services.AuthenticationUserService;
import com.es.slots.pick_up_order.dto.requests.PickUpOrderRequest;
import com.es.slots.pick_up_order.entities.PickUpOrder;
import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderAlreadyExistException;
import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderNotFoundException;
import com.es.slots.pick_up_order.repositories.PickUpOrderRepository;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.entities.StandardSlot;
import com.es.slots.slot.exceptions.customs.FullSlotException;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import com.es.slots.slot.repositories.ExceptionalSlotRepository;
import com.es.slots.slot.repositories.StandardSlotRepository;
import com.es.slots.user.entities.Admin;
import com.es.slots.user.entities.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Tests en tant que Client
 * Tests en tant qu'Admin
 */
@SpringBootTest
class PickUpOrderServiceTest {

    @Autowired
    private PickUpOrderService pickUpOrderService;

    @MockBean
    private AuthenticationUserService authenticationUserService;

    @MockBean
    private StandardSlotRepository standardSlotRepository;

    @MockBean
    private ExceptionalSlotRepository exceptionalSlotRepository;

    @MockBean
    private PickUpOrderRepository pickUpOrderRepository;

    private List<PickUpOrder> pickUpOrderList;


    @BeforeEach
    public void setup() {
        pickUpOrderList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pickUpOrderList.add(new PickUpOrder());
        }

        Mockito.when(pickUpOrderRepository.findAll()).thenReturn(pickUpOrderList);
    }

    @Test
    void getAllPickUpOrdersByUser_AsAdmin() {
        getAdminAsUserAuthenticated();

        List<PickUpOrder> results = pickUpOrderService.getAllPickUpOrdersByUser();
        Assertions.assertEquals(results.size(), pickUpOrderList.size());

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
    }

    @Test
    void getPickUpOrderByIdAndUser_AsAdmin() throws PickUpOrderNotFoundException {
        getAdminAsUserAuthenticated();
        Mockito.when(pickUpOrderRepository.findByPickUpOrderId(anyString())).thenReturn(Optional.of(pickUpOrderList.get(0)));

        PickUpOrder result = pickUpOrderService.getPickUpOrderByIdAndUser("publicId");
        Assertions.assertEquals(result, pickUpOrderList.get(0));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
    }

    @Test
    void getPickUpOrderByIdAndUser_AsAdmin_ShouldThrowPickUpOrderNotFoundException() {
        getAdminAsUserAuthenticated();
        Mockito.when(pickUpOrderRepository.findByPickUpOrderId(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(PickUpOrderNotFoundException.class,
                () -> pickUpOrderService.getPickUpOrderByIdAndUser("publicId"));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
    }

    @Test
    void removePickUpOrder_AsAdmin() throws PickUpOrderNotFoundException {
        getAdminAsUserAuthenticated();

        Mockito.when(pickUpOrderRepository.findByPickUpOrderId(anyString())).thenReturn(Optional.of(pickUpOrderList.get(0)));
        Mockito.doAnswer(answer -> {
            pickUpOrderList.remove(0);
            return null;
        }).when(pickUpOrderRepository).delete(any());

        Assertions.assertEquals(5, pickUpOrderList.size());
        pickUpOrderService.removePickUpOrder("publicId");
        Assertions.assertEquals(4, pickUpOrderList.size());

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
    }

    @Test
    void removePickUpOrder_AsAdmin_ShouldThrowPickUpOrderNotFoundException() {
        getAdminAsUserAuthenticated();
        Mockito.when(pickUpOrderRepository.findByPickUpOrderId(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(PickUpOrderNotFoundException.class,
                () -> pickUpOrderService.removePickUpOrder("publicId"));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
    }


    @Test
    void getAllPickUpOrdersByUser_AsClient() {
        Client client = getClientAsUserAuthenticated();
        client.getPickUpOrderList().add(pickUpOrderList.get(0));

        StandardSlot standardSlot = new StandardSlot();
        standardSlot.setPublicId("publicId");
        pickUpOrderList.get(0).setSlot(standardSlot);

        Mockito.when(pickUpOrderRepository.findAllByClient(client)).thenReturn(client.getPickUpOrderList());
        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(Optional.of(standardSlot));

        List<PickUpOrder> results = pickUpOrderService.getAllPickUpOrdersByUser();
        Assertions.assertEquals(client.getPickUpOrderList(), results);

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(pickUpOrderRepository, Mockito.times(1)).findAllByClient(any());
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(0)).findByPublicId(any());
    }

    @Test
    void getAllPickUpOrdersByUser_AsClient_WithExceptional() {
        Client client = getClientAsUserAuthenticated();
        client.getPickUpOrderList().add(pickUpOrderList.get(0));

        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        exceptionalSlot.setPublicId("publicId");
        pickUpOrderList.get(0).setSlot(exceptionalSlot);

        Mockito.when(pickUpOrderRepository.findAllByClient(client)).thenReturn(client.getPickUpOrderList());
        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(Optional.empty());
        Mockito.when(exceptionalSlotRepository.findByPublicId(any())).thenReturn(Optional.of(exceptionalSlot));

        List<PickUpOrder> results = pickUpOrderService.getAllPickUpOrdersByUser();
        Assertions.assertEquals(client.getPickUpOrderList(), results);

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(pickUpOrderRepository, Mockito.times(1)).findAllByClient(any());
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(any());
    }



    @Test
    void getPickUpOrderByIdAndUser_AsClient() throws PickUpOrderNotFoundException {
        Client client = getClientAsUserAuthenticated();
        PickUpOrder pickUpOrder = pickUpOrderList.get(0);
        pickUpOrder.setPickUpOrderId("publicId");

        client.getPickUpOrderList().add(pickUpOrder);

        PickUpOrder result = pickUpOrderService.getPickUpOrderByIdAndUser("publicId");

        Assertions.assertEquals(pickUpOrder, result);
        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
    }

    @Test
    void getPickUpOrderByIdAndUser_AsClient_shouldThrowPickUpOrderNotFoundException() {
        getClientAsUserAuthenticated();

        Assertions.assertThrows(PickUpOrderNotFoundException.class,
                () -> pickUpOrderService.getPickUpOrderByIdAndUser("publicId"));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
    }


    @Test
    void createNewPickUpOrder_AsClient_UsingStandardSlot() throws SlotNotFoundException, FullSlotException, PickUpOrderAlreadyExistException {
        Client client = getClientAsUserAuthenticated();
        StandardSlot standardSlot = new StandardSlot();
        standardSlot.setCapacity(5);
        standardSlot.getPickUpOrderList().clear();
        Optional<StandardSlot> optionalStandardSlot = Optional.of(standardSlot);

        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setClient(client);
        pickUpOrder.setSlot(standardSlot);
        pickUpOrder.setPickUpOrderId(UUID.randomUUID().toString());

        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(optionalStandardSlot);
        Mockito.when(pickUpOrderRepository.save(any())).thenReturn(pickUpOrder);

        PickUpOrder result = pickUpOrderService.createNewPickUpOrder(new PickUpOrderRequest());

        Assertions.assertEquals(pickUpOrder, result);

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(0)).findByPublicId(any());
        Mockito.verify(pickUpOrderRepository, Mockito.times(1)).save(any());
    }

    @Test
    void createNewPickUpOrder_AsClient_UsingExceptionalSlot() throws SlotNotFoundException, FullSlotException, PickUpOrderAlreadyExistException {
        Client client = getClientAsUserAuthenticated();
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        exceptionalSlot.setCapacity(5);
        exceptionalSlot.getPickUpOrderList().clear();
        Optional<ExceptionalSlot> optionalExceptionalSlot = Optional.of(exceptionalSlot);

        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setClient(client);
        pickUpOrder.setSlot(exceptionalSlot);
        pickUpOrder.setPickUpOrderId(UUID.randomUUID().toString());

        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(Optional.empty());
        Mockito.when(exceptionalSlotRepository.findByPublicId(any())).thenReturn(optionalExceptionalSlot);
        Mockito.when(pickUpOrderRepository.save(any())).thenReturn(pickUpOrder);

        PickUpOrder result = pickUpOrderService.createNewPickUpOrder(new PickUpOrderRequest());

        Assertions.assertEquals(pickUpOrder, result);

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(pickUpOrderRepository, Mockito.times(1)).save(any());
    }

    @Test
    void createNewPickUpOrder_AsClient_shouldThrowSlotNotFoundException() {
        getClientAsUserAuthenticated();
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();

        Mockito.when(standardSlotRepository.findByPublicId(anyString())).thenReturn(Optional.empty());
        Mockito.when(exceptionalSlotRepository.findByPublicId(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(SlotNotFoundException.class,
                () -> pickUpOrderService.createNewPickUpOrder(pickUpOrderRequest));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(any());
    }

    @Test
    void createNewPickUpOrder_AsClient_shouldThrowFullSlotException() {
        getClientAsUserAuthenticated();
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        pickUpOrderRequest.setDate(LocalDate.now());

        StandardSlot standardSlot = new StandardSlot();
        standardSlot.setCapacity(3);
        for (int i = 0; i < standardSlot.getCapacity() + 1; i++) {
            PickUpOrder pickUpOrder = new PickUpOrder();
            pickUpOrder.setLocalDate(LocalDate.now());
            standardSlot.getPickUpOrderList().add(pickUpOrder);
        }

        Optional<StandardSlot> optionalStandardSlot = Optional.of(standardSlot);

        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(optionalStandardSlot);

        Assertions.assertThrows(FullSlotException.class,
                () -> pickUpOrderService.createNewPickUpOrder(pickUpOrderRequest));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(0)).findByPublicId(any());
    }

    @Test
    void createNewPickUpOrder_AsClient_shouldThrowPickUpOrderAlreadyExistException() {
        getClientAsUserAuthenticated();
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        exceptionalSlot.setCapacity(5);
        exceptionalSlot.getPickUpOrderList().clear();
        Optional<ExceptionalSlot> optionalExceptionalSlot = Optional.of(exceptionalSlot);

        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(Optional.empty());
        Mockito.when(exceptionalSlotRepository.findByPublicId(any())).thenReturn(optionalExceptionalSlot);
        Mockito.when(pickUpOrderRepository.save(any())).thenThrow(new RuntimeException());

        Assertions.assertThrows(PickUpOrderAlreadyExistException.class,
                () -> pickUpOrderService.createNewPickUpOrder(new PickUpOrderRequest()));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(pickUpOrderRepository, Mockito.times(1)).save(any());
    }



    @Test
    void updatePickUpOrder_AsClient() throws SlotNotFoundException, PickUpOrderNotFoundException, FullSlotException, PickUpOrderAlreadyExistException {
        Client client = getClientAsUserAuthenticated();
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        pickUpOrderRequest.setSlotId("newSlotId");
        pickUpOrderRequest.setDate(LocalDate.now());

        StandardSlot standardSlot = new StandardSlot();
        standardSlot.setPublicId("oldSlotId");
        standardSlot.setCapacity(3);

        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setLocalDate(LocalDate.now());
        pickUpOrder.setSlot(standardSlot);
        pickUpOrder.setPickUpOrderId("pickUpPublicId");

        client.getPickUpOrderList().add(pickUpOrder);

        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        exceptionalSlot.setPublicId("newSlotId");
        exceptionalSlot.setCapacity(3);

        Optional<ExceptionalSlot> optionalSlot = Optional.of(exceptionalSlot);
        Mockito.when(standardSlotRepository.findByPublicId("newSlotId")).thenReturn(Optional.empty());
        Mockito.when(exceptionalSlotRepository.findByPublicId("newSlotId")).thenReturn(optionalSlot);
        Mockito.when(pickUpOrderRepository.save(any())).thenReturn(null);

        Assertions.assertNotEquals(exceptionalSlot, pickUpOrder.getSlot());
        pickUpOrderService.updatePickUpOrder(pickUpOrderRequest, "pickUpPublicId");
        Assertions.assertEquals(exceptionalSlot, pickUpOrder.getSlot());

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(any());
    }

    @Test
    void updatePickUpOrder_AsClient_shouldThrowSlotNotFoundException_IfSlotRequestIdDoesntExist() {
        getClientAsUserAuthenticated();
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();

        Mockito.when(standardSlotRepository.findByPublicId(anyString())).thenReturn(Optional.empty());
        Mockito.when(exceptionalSlotRepository.findByPublicId(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(SlotNotFoundException.class,
                () -> pickUpOrderService.updatePickUpOrder(pickUpOrderRequest, "publicId"));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(any());
    }

    @Test
    void updatePickUpOrder_AsClient_shouldThrowFullSlotException_usingExceptionalSlot() {
        getClientAsUserAuthenticated();
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        pickUpOrderRequest.setDate(LocalDate.now());

        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        exceptionalSlot.setCapacity(3);
        for (int i = 0; i < exceptionalSlot.getCapacity() + 1; i++) {
            PickUpOrder pickUpOrder = new PickUpOrder();
            pickUpOrder.setLocalDate(LocalDate.now());
            exceptionalSlot.getPickUpOrderList().add(pickUpOrder);
        }

        Optional<ExceptionalSlot> optionalExceptionalSlot = Optional.of(exceptionalSlot);

        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(Optional.empty());
        Mockito.when(exceptionalSlotRepository.findByPublicId(any())).thenReturn(optionalExceptionalSlot);

        Assertions.assertThrows(FullSlotException.class,
                () -> pickUpOrderService.updatePickUpOrder(pickUpOrderRequest, "publicId"));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(any());
    }

    @Test
    void updatePickUpOrder_AsClient_ShouldThrowPickUpOrderNotFoundException_IfClientDoesntHaveTheSlotToEdit() {
        getClientAsUserAuthenticated();
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        pickUpOrderRequest.setDate(LocalDate.now());

        StandardSlot standardSlot = new StandardSlot();
        standardSlot.setCapacity(3);

        Optional<StandardSlot> optionalStandardSlot = Optional.of(standardSlot);
        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(optionalStandardSlot);

        Assertions.assertThrows(PickUpOrderNotFoundException.class,
                () -> pickUpOrderService.updatePickUpOrder(pickUpOrderRequest, "publicId"));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(0)).findByPublicId(any());
    }

    @Test
    void updatePickUpOrder_AsClient_ShouldThrowPickUpOrderAlreadyExistException_IfClientAlreadyHaveTheNewSlot() {
        Client client = getClientAsUserAuthenticated();
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        pickUpOrderRequest.setSlotId("newSlotId");
        pickUpOrderRequest.setDate(LocalDate.now());

        StandardSlot standardSlot = new StandardSlot();
        standardSlot.setPublicId("newSlotId");
        standardSlot.setCapacity(3);

        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setLocalDate(LocalDate.now());
        pickUpOrder.setSlot(standardSlot);
        pickUpOrder.setPickUpOrderId("pickUpPublicId");

        client.getPickUpOrderList().add(pickUpOrder);

        Optional<StandardSlot> optionalStandardSlot = Optional.of(standardSlot);
        Mockito.when(standardSlotRepository.findByPublicId(any())).thenReturn(optionalStandardSlot);

        Assertions.assertThrows(PickUpOrderAlreadyExistException.class,
                () -> pickUpOrderService.updatePickUpOrder(pickUpOrderRequest, "pickUpPublicId"));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(standardSlotRepository, Mockito.times(1)).findByPublicId(any());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(0)).findByPublicId(any());
    }


    @Test
    void removePickUpOrder_AsClient() throws PickUpOrderNotFoundException {
        Client client = getClientAsUserAuthenticated();

        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setPickUpOrderId("publicId");
        client.getPickUpOrderList().add(pickUpOrder);

        Mockito.doNothing().when(pickUpOrderRepository).delete(any());

        Assertions.assertEquals(1, client.getPickUpOrderList().size());
        pickUpOrderService.removePickUpOrder("publicId");
        Assertions.assertEquals(0, client.getPickUpOrderList().size());

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
        Mockito.verify(pickUpOrderRepository, Mockito.times(1)).delete(any());
    }

    @Test
    void removePickUpOrder_AsClient_ShouldThrowPickUpOrderNotFoundException() {
        Client client = getClientAsUserAuthenticated();

        PickUpOrder pickUpOrder = new PickUpOrder();
        pickUpOrder.setPickUpOrderId("publicId");
        client.getPickUpOrderList().add(pickUpOrder);

        Assertions.assertThrows(PickUpOrderNotFoundException.class,
                () -> pickUpOrderService.removePickUpOrder("public"));

        Mockito.verify(authenticationUserService, Mockito.times(1)).getUserAuthenticated();
    }


    /**
     * Get an Admin instance as UserAuthenticated
     */
    private void getAdminAsUserAuthenticated() {
        Mockito.when(authenticationUserService.getUserAuthenticated()).thenReturn(new Admin());
    }


    /**
     * Get a Client instance as UserAuthenticated
     * @return a Client instance
     */
    private Client getClientAsUserAuthenticated() {
        Client client = new Client();
        Mockito.when(authenticationUserService.getUserAuthenticated()).thenReturn(client);
        return client;
    }

}