import { SlotType, Day } from '../dto/slot/slot.dto';

export interface SlotEvent { // Used by FullCalendar
  id: string,
  title: string,
  start: Date,
  end: Date,
  color: string,
  extendedProps: {
    capacity: number,
    currentCharge: number,
    remainingCapacity: number
    type: SlotType,
    dayOfWeek?: Day,
  }
}

export interface SlotEventStandard {
  id: string,
  title: string,
  daysOfWeek: string[],
  startTime: string,
  endTime: string,
  color: string,
  extendedProps: {
    capacity: number,
    currentCharge: number,
    remainingCapacity: number
    type: SlotType,
    dayOfWeek?: Day,
  }
}