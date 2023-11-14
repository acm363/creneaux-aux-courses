import React, {useEffect, useState} from 'react';

import '../../styles/components/admin/Popup.scss';
import {SlotType, Day, ExceptionalSlotDTO} from '../../dto/slot/slot.dto';
import {NotificationUtils} from "../../utils/notification.utils";

interface PopupProps {
  title: string;
  confirmButtonText: string;
  onClosePopup: () => void;
  onConfirm: (values: any,type: SlotType) => any;
  defaultValues: any,
  slotType: SlotType;
}

const Popup: React.FC<PopupProps> = ({
                                       title,
                                       confirmButtonText,
                                       onClosePopup,
                                       onConfirm,
                                       defaultValues,
                                       slotType,
                                     }) => {

    const [date, setDate] = useState(new Date());
    const [dayOfWeek, setDayOfWeek] = useState<Day>(Day.MONDAY);
    const [startHour, setStartHour] = useState(defaultValues.startHour);
    const [endHour, setEndTime] = useState(defaultValues.endHour);
    const [capacity, setCapacity] = useState<number>(defaultValues.capacity);
    const [type] = useState<SlotType>(slotType);



  useEffect(() => {
    if (type === SlotType.EXCEPTIONAL) {
      setDate(new Date(defaultValues.date));
    }
    else {
      if (defaultValues.dayOfWeek)
        setDayOfWeek(defaultValues.dayOfWeek);
    }
  }, []);

    const timeToMinutes = (time: string): number => {
      const [hours, minutes] = time.split(':').map(Number);
      return (hours || 0) * 60 + (minutes || 0);
    };

    const isValidTimeRange = (start: string, end: string): boolean => {
      return timeToMinutes(start) < timeToMinutes(end);
    };

    const startTimeIsAfter8am = (start: string): boolean => {
      return timeToMinutes(start) >= timeToMinutes('08:00');
    };

    const endTimeIsBefore8pm = (end: string): boolean => {
      return timeToMinutes(end) <= timeToMinutes('20:00');
    };

    const handleConfirm = () => {
      if (!isValidTimeRange(startHour, endHour)) {
        NotificationUtils.createNotification('error', 'L\'heure de début doit être inférieure à l\'heure de fin');
        return;
      }
      if (!startTimeIsAfter8am(startHour)) {
        NotificationUtils.createNotification('error', 'L\'heure de début doit être supérieure ou égale à 8h');
        return;
      }
      if (!endTimeIsBefore8pm(endHour)) {
        NotificationUtils.createNotification('error', 'L\'heure de fin doit être inférieure ou égale à 20h');
        return;
      }
      if (!capacity) {
        NotificationUtils.createNotification('error', 'Veuillez renseigner une capacité');
        return;
      }

      let data = {};

      if (type === SlotType.EXCEPTIONAL) {
        data = {
          publicId: defaultValues.publicId,
          date: `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`,
          startHour,
          endHour,
          capacity,
          currentCharge: defaultValues.currentCharge,
          remainingCapacity: defaultValues.remainingCapacity,
        };
      }
      else {
         data = {
          publicId: defaultValues.publicId,
          dayOfWeek,
          startHour,
          endHour,
          capacity,
          currentCharge: defaultValues.currentCharge,
          remainingCapacity: defaultValues.remainingCapacity,
        };
      }
      onConfirm(data, type);
    };


    const onDayChange = (e: any) => {
      setDayOfWeek(e.target.value);
    }
    const renderDayOrDate = () => {
      if (type === SlotType.EXCEPTIONAL) {
        return (
          <div className='date'>
            <label>Date : </label>
            <input
              type='date'
              defaultValue={(defaultValues as ExceptionalSlotDTO).date}
              onChange={(e) => setDate(new Date(e.target.value))}
              id='date-input'
              name='date'
              required
            />
          </div>
        );
      }
      return (
        <div className='day'>
          <label>Jour : </label>
          <select
            defaultValue={dayOfWeek}
            onChange={onDayChange}
            id='day-input'
            name='day'
            required
          >
            <option value={Day.MONDAY}>Lundi</option>
            <option value={Day.TUESDAY}>Mardi</option>
            <option value={Day.WEDNESDAY}>Mercredi</option>
            <option value={Day.THURSDAY}>Jeudi</option>
            <option value={Day.FRIDAY}>Vendredi</option>
            <option value={Day.SATURDAY}>Samedi</option>
            <option value={Day.SUNDAY}>Dimanche</option>
          </select>
        </div>);
    }

    return (
      <div className='popup-container'>
        <div className='popup-content'>
          <div className='popup-header'>
            <h2>{title}</h2>
            <button className='close-btn' onClick={onClosePopup}>×</button>
          </div>
          <div className='popup-body'>
            {renderDayOrDate()}
            <div className='start-time'>
              <label>Heure début : </label>
              <input
                type='time'
                defaultValue={defaultValues.startHour}
                onChange={(e) => setStartHour(e.target.value)}
                id='start-time-input'
                name='start-time'
                min='08:00'
                max='20:00'
                required
              />
            </div>
            <div className='end-time'>
              <label>Heure fin : </label>
              <input
                type='time'
                defaultValue={defaultValues.endHour}
                onChange={(e) => setEndTime(e.target.value)}
                id='end-time-input'
                name='end-time'
                min='08:00'
                max='20:00'
                required
              />
            </div>
            <div className='capacity'>
              <label>Capacité : </label>
              <input
                type='number'
                defaultValue={defaultValues.capacity}
                onChange={(e) => setCapacity(Number(e.target.value))}
                id='capacity-input'
                name='capacity'
                min='1'
                max='999'
                required
              />
            </div>
          </div>
          <div className='popup-actions'>
            <button className='btn btn-primary' onClick={handleConfirm}>{confirmButtonText}</button>
          </div>
        </div>
      </div>
    );
  }
;

export default Popup;

