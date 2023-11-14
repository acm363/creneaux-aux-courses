package com.es.slots.pick_up_order;

import com.es.slots.pick_up_order.dto.requests.PickUpOrderRequest;
import com.es.slots.pick_up_order.dto.response.PickUpOrderResponse;
import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderAlreadyExistException;
import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderNotFoundException;
import com.es.slots.pick_up_order.mapper.PickUpOrderMapper;
import com.es.slots.pick_up_order.services.PickUpOrderService;
import com.es.slots.slot.exceptions.customs.FullSlotException;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pick-up-order")
@AllArgsConstructor
public class PickUpOrderController {

    private PickUpOrderService pickUpOrderService;
    private PickUpOrderMapper pickUpOrderMapper;


    /**
     * Get all retrait commands from User
     */
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public List<PickUpOrderResponse> getAllPickUpOrdersByUser() {
        return pickUpOrderMapper.buildPickUpOrderResponseDTOList(pickUpOrderService.getAllPickUpOrdersByUser());
    }


    /**
     * Get retrait command from User by id
     * @param pickUpOrderPublicId Retrait command id
     * @throws PickUpOrderNotFoundException If no pickupOrder exist for that id in the list of user
     */
    @GetMapping("{pickUpOrderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public PickUpOrderResponse getPickUpOrderByIdAndUser(@PathVariable("pickUpOrderId") String pickUpOrderPublicId) throws PickUpOrderNotFoundException {
        return pickUpOrderMapper.buildPickUpOrderResponseDTO(pickUpOrderService.getPickUpOrderByIdAndUser(pickUpOrderPublicId));
    }


    /**
     * Create new retrait to User from slot
     * @param pickUpOrderRequest The retrait command request
     */
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public PickUpOrderResponse createPickUpOrder(@RequestBody PickUpOrderRequest pickUpOrderRequest) throws SlotNotFoundException, PickUpOrderAlreadyExistException, FullSlotException {
        return pickUpOrderMapper.buildPickUpOrderResponseDTO(pickUpOrderService.createNewPickUpOrder(pickUpOrderRequest));
    }


    /**
     * Update retrait command
     * @param pickUpOrderRequest The retrait command request
     */
    @PatchMapping("{pickUpOrderId}")
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public void updatePickUpOrder(@RequestBody PickUpOrderRequest pickUpOrderRequest, @PathVariable("pickUpOrderId") String pickUpOrderId) throws SlotNotFoundException, PickUpOrderNotFoundException, PickUpOrderAlreadyExistException, FullSlotException {
        pickUpOrderService.updatePickUpOrder(pickUpOrderRequest, pickUpOrderId);
    }


    /**
     * Remove retrait command
     * @param pickUpOrderId Retrait command id to remove
     */
    @DeleteMapping("{pickUpOrderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public void removePickUpOrder(@PathVariable("pickUpOrderId") String pickUpOrderId) throws PickUpOrderNotFoundException {
        pickUpOrderService.removePickUpOrder(pickUpOrderId);
    }

}
