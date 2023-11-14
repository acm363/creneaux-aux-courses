import {UserDTO} from '../dto/user/user.dto';
import {Day, SlotType, StandardSlotDTO, StandardSlotDtoRequest} from '../dto/slot/slot.dto';
import {EventImpl, EventSelectionInfo, SlotEvent, SlotEventStandard} from '../interfaces';
import SlotService from "./Slot.services";

export class StandardSlotService extends SlotService<StandardSlotDTO,StandardSlotDtoRequest> {

  readonly TITLE = 'Créneau standard';

  constructor(userDto: UserDTO) {
    super(userDto, 'standard');
  }

  buildRequestFromDto(slot: StandardSlotDTO): StandardSlotDtoRequest {
    // TODO : hour should be 2 digits
    return {
      publicId : slot.publicId, // Used by update/delete
      dayOfWeek: slot.dayOfWeek,
      startHour: slot.startHour,
      endHour: slot.endHour,
      capacity: slot.capacity,
    }
  }

  buildSlotDtoFromSlotEvent(slotEvent: SlotEvent): StandardSlotDTO {
    return {
      publicId: slotEvent.id,
      startHour: this.extractTime(slotEvent.start),
      endHour: this.extractTime(slotEvent.end),
      capacity: slotEvent.extendedProps.capacity,
      currentCharge : slotEvent.extendedProps.currentCharge,
      remainingCapacity : slotEvent.extendedProps.remainingCapacity,
      dayOfWeek : slotEvent.extendedProps.dayOfWeek || this.extractDay(new Date(slotEvent.start)),
    };
  }

  buildSlotDtoFromEventSelection(eventSelectionInfo: EventSelectionInfo): StandardSlotDTO {
    return {
      publicId: '',
      dayOfWeek: this.extractDay(new Date(eventSelectionInfo.startStr)),
      startHour: eventSelectionInfo.startStr.substring(11, 16),
      endHour: eventSelectionInfo.endStr.substring(11, 16),
      capacity: eventSelectionInfo.capacity || this._defaultCapacity,
      currentCharge : 0,
      remainingCapacity : eventSelectionInfo.capacity || this._defaultCapacity,
    };
  };

  buildSlotDtoRequestFromSlotDto(slotDto: StandardSlotDTO): StandardSlotDtoRequest {
    return {
      publicId : slotDto.publicId,
      dayOfWeek : slotDto.dayOfWeek,
      startHour: slotDto.startHour,
      endHour: slotDto.endHour,
      capacity: slotDto.capacity,
    };
  }

  buildSlotEvent(props : {slot: StandardSlotDTO, date : Date|null|undefined}): SlotEvent {
    const slot = props.slot;
    const _date = props.date || new Date();
    // add days to date until it matches the day of the week
    const currentDay = _date.getDay();
    const targetDay = this.dayToNumber(slot.dayOfWeek);
    const daysToAdd = (targetDay - currentDay + 7) % 7;
    _date.setDate(_date.getDate() + daysToAdd);
    const start = new Date(`${_date.toISOString().substring(0, 10)}T${slot.startHour}`);
    const end = new Date(`${_date.toISOString().substring(0, 10)}T${slot.endHour}`);
    return {
      id: slot.publicId,
      title: this.TITLE,
      start: start,
      end: end,
      extendedProps: {
        capacity: slot.capacity,
        currentCharge: slot.currentCharge,
        remainingCapacity: slot.remainingCapacity,
        type: SlotType.STANDARD,
      },
      color: '#3788D8',
    };
  }

  extractEventData(data: EventImpl): StandardSlotDTO {
    const publicId = data.id;
    const startHour = this.formatHour(new Date(data.start));
    const endHour = this.formatHour(new Date(data.end));
    const capacity = data.extendedProps.capacity || this._defaultCapacity;
    const currentCharge = data.extendedProps.currentCharge || 0;
    const remainingCapacity = data.extendedProps.remainingCapacity || capacity;
    const dayOfWeek = data.extendedProps.dayOfWeek || this.extractDay(new Date(data.start));

    return {
      publicId,
      dayOfWeek,
      startHour,
      endHour,
      capacity,
      currentCharge,
      remainingCapacity,
    };
  };

  private extractDay(date : Date) : Day {
    const day = date.getDay();
    if (day > 6 || day < 0) {
      throw new Error('Invalid day number');
    }
    const days = [Day.SUNDAY, Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY];
    return days[day] || Day.SUNDAY;
  }

  /*private numberToDay(day : number) : Day {
    if (day > 6 || day < 0) {
      throw new Error('Invalid day number');
    }
    const days = [Day.SUNDAY, Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY];
    return days[day] || Day.SUNDAY;
  }*/

  buildSlotEventStandard(slot: StandardSlotDTO): SlotEventStandard {
    return {
      id: slot.publicId,
      title: this.TITLE,
      daysOfWeek: [this.dayToNumber(slot.dayOfWeek).toString()],
      startTime: slot.startHour,
      endTime: slot.endHour,
      extendedProps: {
        capacity: slot.capacity,
        currentCharge: slot.currentCharge,
        remainingCapacity: slot.remainingCapacity,
        type: SlotType.STANDARD,
      },
      color: '#3788D8',
    };
  }

}
