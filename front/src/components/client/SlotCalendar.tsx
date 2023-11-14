import React from "react";
import { Calendar } from "react-date-range";

interface SlotCalendarProps {
  className?: string;
  date: Date;
  onDateChange: (date: Date) => void;
}
const SlotCalendar : React.FC<SlotCalendarProps> = ({className,date, onDateChange}) => {

  return (
    <Calendar
      className={className}
      date={date}
      onChange={onDateChange}
      direction="horizontal" />
  );
};

export default SlotCalendar;