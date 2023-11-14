// Abstract pour empêcher l'instanciation de la classe
import {SlotEvent, SlotHandler} from "../interfaces";
import {PickupOrderDTO, PickupOrderDtoRequest} from "../dto/pickUpOrder/pickupOrder.dto";
import {SlotUtils} from "./slot.utils";

export abstract class PickUpUtils {

  public static buildPickUpOrderRequestFromSlotEvent(slotEvent: SlotEvent): PickupOrderDtoRequest{
    return {
      slotId: slotEvent.id,
      date: slotEvent.start.toISOString().substring(0, 10),
    }
  }

  public static buildPickUpDtoFromSlotEvent(slotEvent: SlotEvent,slotHandler : SlotHandler): PickupOrderDTO{
    return {
      pickUpOrderId: '',
      slot: slotHandler.standard.buildSlotDtoFromSlotEvent(slotEvent),
      userPublicId: '',
    }
  }

  public static buildSlotEventFromPickUpOrderDto(pickUpOrderDto : PickupOrderDTO, slotHandler : SlotHandler) : SlotEvent {

    pickUpOrderDto.slot.publicId = pickUpOrderDto.pickUpOrderId; // TODO : change this, it's ugly
    if (SlotUtils.isStandardSlotDTO(pickUpOrderDto.slot)) {
      return slotHandler.standard.buildSlotEvent({slot:pickUpOrderDto.slot, date : null});
    } else {
      return slotHandler.exceptional.buildSlotEvent({slot:pickUpOrderDto.slot, date : null});
    }
  }

}
