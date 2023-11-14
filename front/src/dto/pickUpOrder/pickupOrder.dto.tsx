import {ExceptionalSlotDTO, StandardSlotDTO} from "../slot/slot.dto";

export interface PickupOrderDTO {
  pickUpOrderId: string;
  slot: StandardSlotDTO | ExceptionalSlotDTO;
  userPublicId: string;
}

export interface PickupOrderDtoResponse{
  data: PickupOrderDTO;
}

export interface PickupOrderDtoResponseList{
  data: PickupOrderDTO[];
}

export interface PickupOrderDtoRequest{
  slotId: string;
  date: string;
}