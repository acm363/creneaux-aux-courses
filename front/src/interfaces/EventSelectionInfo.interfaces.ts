export interface EventSelectionInfo { // Used by FullCalendar, Object return by the eventClick callback
  start: Date,
  end: Date,
  startStr: string,
  endStr: string,
  capacity?: number,
}