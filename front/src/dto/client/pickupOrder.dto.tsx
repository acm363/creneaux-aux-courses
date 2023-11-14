import {SlotDTO} from "../slot/slot.dto";

export interface PickupOrderDTO {
  pickUpOrderId: string;
  slot: SlotDTO;
  userPublicId: string;
}

export interface PickupOrderDtoResponse{
  data: PickupOrderDTO;
}

export interface PickupOrderDtoResponseList{
  data: PickupOrderDTO[];
}

export interface PickupOrderDtoRequest{
  slotPublicId: string;
}