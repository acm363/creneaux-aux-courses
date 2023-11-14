import React, {useEffect, useRef, useState} from 'react';

import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import bootstrap5Plugin from '@fullcalendar/bootstrap5';
import interactionPlugin from '@fullcalendar/interaction'; // needed for dayClick
import {AiFillEdit, AiOutlineDelete} from 'react-icons/ai';
import {Button} from 'react-bootstrap';

import ContextMenu from '../../common/ContextMenu';
import NumberInput from '../../common/NumberInput';

import '../../../styles/components/admin/CalendarComponent.scss';
import 'react-notifications/lib/notifications.css';
import {Day, SlotType} from "../../../dto/slot/slot.dto";

import {CalendarProps, ContextMenuProps, EventSelectionInfo, SlotEvent, SlotEventStandard} from "../../../interfaces";
import {DEFAULT_CONTEXT_MENU_PROPS} from "../../../constants";
import FullCalendarApi from "@fullcalendar/react";
import {ExceptionalSlotService, StandardSlotService} from "../../../services";
import {NotificationUtils} from "../../../utils/notification.utils";
import {SlotUtils} from "../../../utils/slot.utils";
import Popup from "../Popup";


const CalendarComponent: React.FC<CalendarProps> = ({ userDto }) => {
  const [events, setEvents] = useState<(SlotEvent|SlotEventStandard)[]>([]);
  const [popupProps, setPopupProps] = useState<{ isVisible: boolean, type: String, slotType: SlotType | null }>({
    isVisible: false,
    type: 'create',
    slotType: SlotType.STANDARD,
  });

  const [defaultCapacity, setDefaultCapacity] = useState<number>(10);
  const [contextMenuProps, setContextMenuProps] = useState<ContextMenuProps>(DEFAULT_CONTEXT_MENU_PROPS);
  const calendarRef = useRef<FullCalendarApi | null>(null);
  const [eventTypeFilter, setEventTypeFilter] = useState<SlotType>(SlotType.ALL);
  const [showAddEventButton, setShowAddEventButton] = useState<boolean>(false);

  const slotHandler = { standard : new StandardSlotService(userDto), exceptional : new ExceptionalSlotService(userDto)}

  useEffect(() => {
    loadSlots().catch((error) => {
      error(error);
    });
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      setShowAddEventButton(false);
    }, 3000);
    return () => clearTimeout(timer);
  }, [showAddEventButton]);

  const error = (error : any) => { // callback error for slot requests
    NotificationUtils.createNotification('error', error.message);
  }

  const loadSlots = async () => {
    const success = (response : any) => {
      setEvents([...events, ...response]);
    };
    await SlotUtils.getAll(slotHandler,true,success,error);
  }

  const addEvent = (event: SlotEvent | SlotEventStandard) => {
    if (calendarRef.current) {
      const calendarApi = calendarRef.current.getApi();
      calendarApi.addEvent(event);
      setEvents([...events, event]);
    }
  };

  const onSelectSlot = async (info: EventSelectionInfo) => {

    const success = (response : any) => {
      const event = SlotUtils.buildCustomSlotEvent(response.slotDto, eventTypeFilter, slotHandler);
      addEvent(event);
      if (response.overlappingSlots && response.overlappingSlots.length > 0) {
        NotificationUtils.createNotification('warning', 'Créneau créé avec succès mais il y a des créneaux en conflit');
      }
      if (event.extendedProps.type === SlotType.EXCEPTIONAL) {
        NotificationUtils.createNotification('info', 'Créneau exceptionnel créé avec succès');
      } else {
        NotificationUtils.createNotification('info', 'Créneau créé standard avec succès');
      }
    }
    await SlotUtils.createFromEventSelection({...info, capacity : defaultCapacity}, eventTypeFilter, slotHandler, success, error);
  };

  const closeContextMenu = () => {
    setContextMenuProps({
      ...contextMenuProps,
      isVisible: false,
    });
  };

  const handleContextMenu = (jsEvent: PointerEvent, event: any) => { // TODO : revoir la position du context menu
    jsEvent.preventDefault();
    // get cordonates of target
    setContextMenuProps({
      isVisible: true,
      x: jsEvent.pageX,
      y: jsEvent.pageY,
      event: event.event,
    });
  };

  // method to update local event (from events state) when event is updated
  const removeLocalEvent = (eventId: string) => {
    const index = events.findIndex((event) => event.id === eventId);
    if (index !== -1) {
      events.splice(index, 1);
    }
  };

  const updateEvent = () => { // reserved to context menu
    setPopupProps({ isVisible: true, type: 'update', slotType: null });
  };

  const deleteEvent = async () => { // reserved to context menu
    closeContextMenu();
    if (!contextMenuProps.event)
      return NotificationUtils.createNotification('error', 'Erreur lors de la suppression du créneau');

    const success = () => {
      contextMenuProps.event.remove();
      removeLocalEvent(contextMenuProps.event.id);
      NotificationUtils.createNotification('info', 'Créneau supprimé avec succès');
    }
    await SlotUtils.delete(contextMenuProps.event.id,contextMenuProps.event.extendedProps.type, slotHandler, success, error);
  };


  const renderEvent = (arg: any) => {
    return (
      <div className={`event-content`}>
        {arg.event.title}
        <br />
        Capacité : {arg.event.extendedProps.currentCharge}/{arg.event.extendedProps.capacity}
      </div>
    );
  }

  const closePopup = () => { // callback lors d'un exit
    setPopupProps({ isVisible: false, type: 'create', slotType: null });
  };

  const renderPopup = () => {

    const confirm = async (slotParams: any, type : SlotType) => { // callback async lors d'un confirm
      const createSuccess = (response : any) => {
        if (response.overlappingSlots && response.overlappingSlots.length > 0) {
          NotificationUtils.createNotification('warning', 'Créneau créé avec succès mais il y a des créneaux en conflit');
        }
        const event = SlotUtils.buildCustomSlotEvent(response.slotDto, type, slotHandler);
        addEvent(event);
        if (type === SlotType.EXCEPTIONAL) {
          NotificationUtils.createNotification('info', 'Créneau exceptionnel créé avec succès');
        } else {
          NotificationUtils.createNotification('info', 'Créneau standard créé avec succès');
        }
        closePopup();
      }

      const updateSuccess = (response : any) => {
        if (response.overlappingSlots && response.overlappingSlots.length > 0) {
          NotificationUtils.createNotification('warning', 'Créneau créé avec succès mais il y a des créneaux en conflit');
        }
        contextMenuProps.event.remove();
        const event = SlotUtils.buildCustomSlotEvent(response, type, slotHandler);
        removeLocalEvent(event.id);
        addEvent(event);
        if (event.extendedProps.type === SlotType.EXCEPTIONAL) {
          NotificationUtils.createNotification('info', 'Créneau exceptionnel modifié avec succès');
        } else {
          NotificationUtils.createNotification('info', 'Créneau standard modifié avec succès');
        }
        closePopup();
      }

      switch (popupProps.type) {
        case 'create':
          await SlotUtils.createFromSlotEvent(SlotUtils.buildSlotEvent(slotParams,type,slotHandler), slotHandler, createSuccess, error);
          break;
        case 'update':
          await SlotUtils.updateFromSlotEvent(SlotUtils.buildSlotEvent(slotParams,type,slotHandler), slotHandler, updateSuccess, error);
          break;
        default:
          break;
      }
    };

    const today = new Date();

    let defaultValues;

    if (popupProps.slotType === SlotType.EXCEPTIONAL) {
      defaultValues = {
        publicId: '',
        startHour: '08:00',
        endHour: '09:00',
        capacity: defaultCapacity,
        date :  `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`
      };
    }
    else {
      defaultValues = {
        publicId: '',
        startHour: '08:00',
        endHour: '09:00',
        capacity: defaultCapacity,
        dayOfWeek: Day.MONDAY,
      }
    }


    const focusedSlot = contextMenuProps.event.id ? SlotUtils.extractEventData(contextMenuProps.event,popupProps.slotType, slotHandler) : null;
    return (
      <Popup
        title={popupProps.type === 'update' ? 'Modifier un créneau' : 'Réserver un créneau'}
        confirmButtonText='OK'
        onClosePopup={closePopup}
        onConfirm={confirm}
        defaultValues={focusedSlot || defaultValues}
        slotType={popupProps.slotType || eventTypeFilter}
      />
    );
  };

  const onClickUpdate = () => {
    closeContextMenu();
    updateEvent();
  };

  const onClickDelete = async () => {
    closeContextMenu();
    await deleteEvent();
  };

  const handleEventChange = async (event: any) => {

    //const type = event.event.extendedProps.type;
    const type = eventTypeFilter === SlotType.EXCEPTIONAL ? SlotType.EXCEPTIONAL : SlotType.STANDARD;
    const success = (res : any) => {
      const event = SlotUtils.buildSlotEvent(res.slotDto, type, slotHandler);
      removeLocalEvent(event.id);
      events.push(event);
      if (event.extendedProps.type === SlotType.EXCEPTIONAL) {
        NotificationUtils.createNotification('info', 'créneau exceptionnel modifié avec succès');
      } else {
        NotificationUtils.createNotification('info', 'créneau modifié avec succès');
      }
    }

    await SlotUtils.updateFromSlotEvent(event, slotHandler, success, error);
  };

  const onDefaultCapacityChange = (number: number) => {
    setDefaultCapacity(number);
  };

  const onClickAddEvent = (slotType: SlotType) => {
    // To avoid the pickUpOrder to create a standard event when he has selected the exceptional filter.
    //setEventTypeFilter(slotType);
    setPopupProps({ isVisible: true, type: 'create', slotType });
  };

  const addEventButton = () => {
    if (!showAddEventButton) {
      return (
        <Button className={'mb-5'} variant={'primary'} onClick={() => setShowAddEventButton(true)}>Ajouter un
          créneau</Button>
      );
    }
    return (
      <div className={'addEvent-container'}>
        <Button className={'mb-5'} variant={'primary'}
                onClick={() => onClickAddEvent(SlotType.STANDARD)}>Standard</Button>
        <Button className={'mb-5'} variant={'danger'}
                onClick={() => onClickAddEvent(SlotType.EXCEPTIONAL)}>Exceptionnel</Button>
      </div>
    );
  };

  const handleTodayButtonClick = () => {
    if (calendarRef.current) {
      calendarRef.current.getApi().today();
    }
  };

  return (
    <div>
      <div className='actions'>
        {addEventButton()}
        <div className='default-capacity'>
          <label>Capacité par défaut :</label>
          <NumberInput defaultValue={defaultCapacity} onChange={(number) => onDefaultCapacityChange(number)}
                       minValue={1} maxValue={9999} />
        </div>
      </div>
      {popupProps.isVisible && renderPopup()}
      <ContextMenu
        items={[
          { label: 'Modifier', onClick: () => onClickUpdate(), icon: AiFillEdit },
          { label: 'Supprimer', onClick: () => onClickDelete(), icon: AiOutlineDelete },
        ]}
        {...contextMenuProps}
        onClose={closeContextMenu}
      />
      <div className='calendar-container'>
        <div className='calendar-header'>
          <Button className={'mb-5 today-button'} variant={'primary'}
                  onClick={handleTodayButtonClick}>Aujourd'hui</Button>
          <div className={'filter-by-type'}>
            <div className={'btn-group'} role={'group'} aria-label={'Basic radio toggle button group'}>
              <input type='radio' className='btn-check' name='options' id='all' autoComplete='off'
                     onChange={() => setEventTypeFilter(SlotType.ALL)} checked={eventTypeFilter === SlotType.ALL} />
              <label className='btn btn-dark' htmlFor='all'>Tout</label>
              <input type='radio' className='btn-check' name='options' id='standard' autoComplete='off'
                     onChange={() => setEventTypeFilter(SlotType.STANDARD)}
                     checked={eventTypeFilter === SlotType.STANDARD} />
              <label className='btn btn-primary' htmlFor='standard'>Standard</label>
              <input type='radio' className='btn-check' name='options' id='exceptional' autoComplete='off'
                     onChange={() => setEventTypeFilter(SlotType.EXCEPTIONAL)}
                     checked={eventTypeFilter === SlotType.EXCEPTIONAL} />
              <label className='btn btn-danger' htmlFor='exceptional'>Exceptionnel</label>
            </div>
          </div>
        </div>
        <FullCalendar
          ref={calendarRef}
          plugins={[timeGridPlugin, bootstrap5Plugin, interactionPlugin]}
          headerToolbar={{
            left: 'prev',
            center: 'title',
            right: 'next',
          }}
          initialView='timeGridWeek'
          themeSystem={'bootstrap5'}
          allDaySlot={false}
          slotMinTime={'08:00:00'}
          slotMaxTime={'20:00:00'}
          expandRows={true}
          slotLabelFormat={{ hour: 'numeric', minute: '2-digit', hour12: false }}
          dayHeaderFormat={{ weekday: 'long', day: 'numeric', month: '2-digit' }}
          titleFormat={{ day: 'numeric', year: 'numeric', month: 'long' }}
          locale={'fr'}
          firstDay={new Date().getDay()}
          eventClick={(event: any) => {
            handleContextMenu(event.jsEvent, event);
          }}
          eventChange={async (event: any) => {
            await handleEventChange(event.event);
          }}
          eventContent={renderEvent}
          select={onSelectSlot}
          editable={false}
          selectable={true}
          events={events.filter((event) => event.extendedProps.type === eventTypeFilter || eventTypeFilter === SlotType.ALL) || []}
        ></FullCalendar>
      </div>
    </div>
  );
};

export default CalendarComponent;
