import {UserDTO} from '../dto/user/user.dto';
import {SlotDtoResponse, SlotDtoResponseList, SlotType} from '../dto/slot/slot.dto';
import {SlotEvent,EventSelectionInfo,EventImpl} from '../interfaces';
import AbstractService from "./Abstract.services";

export default abstract class SlotService<Dto,Request> extends AbstractService<Dto,Request,SlotDtoResponse<Dto>,SlotDtoResponseList<Dto>>{

  readonly ROUTE_PREFIX = '/slot';

  protected _defaultCapacity: number = 10;

  protected constructor(userDto: UserDTO, route : string) {
    super(userDto, route);
    this._route = `${this.ROUTE_PREFIX}/${route}`;
  }

  abstract buildSlotEvent(props : {slot: Dto, date : Date|null|undefined}): SlotEvent;

  abstract buildSlotDtoFromSlotEvent(slotEvent: SlotEvent): Dto;

  abstract buildSlotDtoFromEventSelection(eventSelectionInfo: EventSelectionInfo, type: SlotType): Dto;

  abstract buildSlotDtoRequestFromSlotDto(slotDto: Dto): Request;

  abstract extractEventData(data: EventImpl): Dto;

  protected extractTime(date : Date) : string { // TODO : voir pq on a -1 heure
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  }

  protected formatHour(date: Date)  : string {
    const hours = String(date.getUTCHours()).padStart(2, '0');
    const minutes = String(date.getUTCMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  };
}
