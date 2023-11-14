import {UserDTO} from '../dto/user/user.dto';
import AbstractService from "./Abstract.services";
import {PickupOrderDTO, PickupOrderDtoRequest, PickupOrderDtoResponse, PickupOrderDtoResponseList} from "../dto/pickUpOrder/pickupOrder.dto";
import {ExceptionalSlotDTO, StandardSlotDTO} from "../dto/slot/slot.dto";
import {SlotUtils} from "../utils/slot.utils";

export class PickupOrderSevice extends AbstractService<PickupOrderDTO, PickupOrderDtoRequest, PickupOrderDtoResponse, PickupOrderDtoResponseList>{

  constructor(userDto: UserDTO) {
    super(userDto, 'pick-up-order');
  }

  buildRequestFromDto(dto: PickupOrderDTO): PickupOrderDtoRequest {
    return {
      slotId: dto.slot.publicId,
      date: this.extractDateFromSlot(dto.slot),
    }
  }

  private extractDateFromSlot(slot: StandardSlotDTO | ExceptionalSlotDTO): string {
    if (SlotUtils.isStandardSlotDTO(slot)) {
      const _date =  new Date();
      const currentDay = _date.getDay();
      const targetDay = this.dayToNumber(slot.dayOfWeek);
      const daysToAdd = (targetDay - currentDay + 6) % 6;
      _date.setDate(_date.getDate() + daysToAdd);
      return _date.toISOString().substring(0, 10);
    } else {
      return slot.date;
    }
  }

}


