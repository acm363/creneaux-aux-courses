import {Day, SlotType} from "../dto/slot/slot.dto";

export interface EventImpl {
  id: string,
  start: Date,
  end: Date,
  extendedProps: {
    type: SlotType,
    capacity: number,
    currentCharge: number,
    remainingCapacity: number,
    dayOfWeek?: Day,
  },
  remove: () => void,
}