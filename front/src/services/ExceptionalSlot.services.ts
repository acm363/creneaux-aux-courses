import { UserDTO } from '../dto/user/user.dto';
import { ExceptionalSlotDTO, ExceptionalSlotDtoRequest, SlotType } from '../dto/slot/slot.dto';
import { EventImpl, EventSelectionInfo, SlotEvent } from '../interfaces';
import SlotService from './Slot.services';
import { DateTime, Settings } from 'luxon';

export class ExceptionalSlotService extends SlotService<ExceptionalSlotDTO, ExceptionalSlotDtoRequest> {

  readonly TITLE = 'Créneau exceptionnel';

  constructor(userDto: UserDTO) {
    super(userDto, 'exceptional');
    this._params = '?overSevenDays=true';
  }

  // Override
  buildRequestFromDto(dto: ExceptionalSlotDTO): ExceptionalSlotDtoRequest {
    return {
      publicId: dto.publicId,
      date: dto.date,
      startHour: dto.startHour,
      endHour: dto.endHour,
      capacity: dto.capacity,
    };
  }

  buildSlotDtoFromSlotEvent(slotEvent: SlotEvent): ExceptionalSlotDTO {
    return {
      publicId: slotEvent.id,
      date: slotEvent.start.toISOString().substring(0, 10),
      startHour: this.extractTime(slotEvent.start),
      endHour: this.extractTime(slotEvent.end),
      capacity: slotEvent.extendedProps.capacity,
      currentCharge: slotEvent.extendedProps.currentCharge,
      remainingCapacity: slotEvent.extendedProps.remainingCapacity,
    };
  }

  buildSlotDtoFromEventSelection(eventSelectionInfo: EventSelectionInfo): ExceptionalSlotDTO {
    return {
      publicId: '',
      date: eventSelectionInfo.startStr.substring(0, 10),
      startHour: eventSelectionInfo.startStr.substring(11, 16),
      endHour: eventSelectionInfo.endStr.substring(11, 16),
      capacity: eventSelectionInfo.capacity || this._defaultCapacity,
      currentCharge: 0,
      remainingCapacity: eventSelectionInfo.capacity || this._defaultCapacity,
    };
  };

  buildSlotDtoRequestFromSlotDto(slotDto: ExceptionalSlotDTO): ExceptionalSlotDtoRequest {
    return {
      publicId: slotDto.publicId,
      date: slotDto.date,
      startHour: slotDto.startHour,
      endHour: slotDto.endHour,
      capacity: slotDto.capacity,
    };
  }

  buildSlotEvent(props: { slot: ExceptionalSlotDTO, date: Date | null | undefined }): SlotEvent {
    Settings.defaultZone = 'local';
    const slot = props.slot;

    const dateStart = DateTime.fromISO(`${slot.date}T${slot.startHour}`, { zone: 'local' }).toJSDate();
    const dateEnd = DateTime.fromISO(`${slot.date}T${slot.endHour}`, { zone: 'local' }).toJSDate();
    return {
      id: slot.publicId,
      title: this.TITLE,
      start: dateStart,
      end: dateEnd,
      extendedProps: {
        capacity: slot.capacity,
        currentCharge: slot.currentCharge,
        remainingCapacity: slot.remainingCapacity,
        type: SlotType.EXCEPTIONAL,
      },
      color: '#FF5B75',
    };
  }

  extractEventData(data: EventImpl): ExceptionalSlotDTO {
    const publicId = data.id;
    const date = new Date(data.start).toISOString().substring(0, 10);
    // extract yyyy mm dd from date
    const startHour = this.formatHour(new Date(data.start));
    const endHour = this.formatHour(new Date(data.end));
    const capacity = data.extendedProps.capacity || this._defaultCapacity;
    const currentCharge = data.extendedProps.currentCharge || 0;
    const remainingCapacity = data.extendedProps.remainingCapacity || capacity;

    return {
      publicId,
      date,
      startHour,
      endHour,
      capacity,
      currentCharge,
      remainingCapacity,
    };
  };

}
