import {ExceptionalSlotService, StandardSlotService} from "../services";

export interface SlotHandler {
  standard: StandardSlotService,
  exceptional: ExceptionalSlotService
}