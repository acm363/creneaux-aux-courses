export enum Day {
  MONDAY = 'MONDAY',
  TUESDAY = 'TUESDAY',
  WEDNESDAY = 'WEDNESDAY',
  THURSDAY = 'THURSDAY',
  FRIDAY = 'FRIDAY',
  SATURDAY = 'SATURDAY',
  SUNDAY = 'SUNDAY'
}

export interface SlotDTO {
  publicId: string,
  startHour: string,
  endHour: string,
  capacity: number,
  currentCharge: number,
  remainingCapacity: number,
}

export interface StandardSlotDTO extends SlotDTO{
  dayOfWeek: Day,
}

export interface ExceptionalSlotDTO extends SlotDTO{
  date: string,
}

export enum SlotType {
  STANDARD = 'STANDARD',
  EXCEPTIONAL = 'EXCEPTIONAL',
  ALL = 'ALL' // Used for filtering
}

export interface SlotDtoRequest{
  publicId : string,
  startHour: string,
  endHour: string,
  capacity: number
}

export interface StandardSlotDtoRequest extends SlotDtoRequest{
  dayOfWeek : Day,
}

export interface ExceptionalSlotDtoRequest extends SlotDtoRequest{
  date : string,
}

/*
  Créer un les Reponses si on a pas les memes DTO pour les deux
 */

export interface SlotDtoResponse<T> {
  data: {
    slotDto: T,
    overlappingSlots: T[]
  };
}

export interface SlotDtoResponseItemList<T> {
  slotDto: T,
  overlappingSlots: T[]
}

export interface SlotDtoResponseList<T> {
  data: SlotDtoResponseItemList<T>[];
}

