import React, {useEffect, useState} from "react";
import "react-date-range/dist/styles.css";
import "react-date-range/dist/theme/default.css";
import SlotList from "./SlotList";
import SlotCalendar from "./SlotCalendar";
import '../../styles/components/client/SlotPicker.scss';
import {AiOutlineDelete} from 'react-icons/ai';
import {VscCheck, VscChromeClose, VscArrowLeft} from "react-icons/vsc";

import {UserDTO} from "../../dto/user/user.dto";
import {SlotEvent, SlotHandler} from "../../interfaces";
import {ExceptionalSlotService, PickupOrderSevice, StandardSlotService} from "../../services";
import {SlotUtils} from "../../utils/slot.utils";
import {SlotType} from "../../dto/slot/slot.dto";
import {PickUpUtils} from "../../utils/pickUp.utils";
import {NotificationUtils} from "../../utils/notification.utils";

interface SlotPickerProps {
  userDto: UserDTO;
  allowCreation: boolean;
  onConfirm: (item: SlotEvent) => void;
}

const error = (error : any) => {
  NotificationUtils.createNotification('error', error.message);
}

const SlotPicker : React.FC<SlotPickerProps> = ({userDto,onConfirm, allowCreation}) => {

  const [date, setDate] = useState(new Date());
  const [slots, setSlots] = useState<SlotEvent[]>([]);
  const [originalSlots, setOriginalSlots] = useState<SlotEvent[]>([]);

  const [isCreationMode] = useState(allowCreation);
  const slotHandler : SlotHandler = { standard : new StandardSlotService(userDto) , exceptional : new ExceptionalSlotService(userDto) };

  const pickUpOrderService = new PickupOrderSevice(userDto);

  useEffect(() => {
    if (!isCreationMode) { // If we are in update mode, load client reservations
      loadClientReservations().catch(() =>{});
      return;
    }

    // Get all slots
    const success = (response : any) => {
      setOriginalSlots(response);
      setSlots(filterSlotsByDate(date));
    };
    SlotUtils.getAll(slotHandler,false,success,error);
  }, []);


  const filterSlotsByDate = (date: Date) => {
     return originalSlots.filter((slot) => {

       // exceptional
       if(slot.extendedProps.type === SlotType.EXCEPTIONAL) {
         if(slot.start.getTime() - date.getTime() / (1000 * 3600) < 1)
           return false;
         return slot.start.getDate() === date.getDate() && slot.start.getMonth() === date.getMonth() && slot.start.getFullYear() === date.getFullYear();
       }

       return slot.start.getDay() === date.getDay() && slot.start.getDate() === date.getDate();
     });
  }

  const onDateChange = (_date: Date) => {
    const filteredSlots = filterSlotsByDate(_date);
    setDate(_date);
    setSlots(filteredSlots);
  }

  const onSlotClicked = async (slot: SlotEvent) => {
    const success = () => {
      NotificationUtils.createNotification('success', 'Créneau réservé');
      onConfirm(slot);
    }
    const dto = PickUpUtils.buildPickUpDtoFromSlotEvent(slot,slotHandler);
    await pickUpOrderService.create(dto,success,error);
  }

  const onSlotRemove = async (slot: SlotEvent) => {
    const success = () => {
      NotificationUtils.createNotification('success', 'Créneau supprimé');
      loadClientReservations();
    }
    const error = (error : any) => {
      console.log(error);
      NotificationUtils.createNotification('error', 'Impossible de supprimer le créneau');
    }
    await pickUpOrderService.delete(slot.id,success,error);
  }

  /*const toggleCreationMode = () => {
    setIsCreationMode(!isCreationMode);
    if (isCreationMode) {
      console.log("creation mode");
      loadClientReservations();
    }
  }*/

  const loadClientReservations = async () => {
    const success = (response : any) => {
      const slots = response.map((slot : any) => {
        return PickUpUtils.buildSlotEventFromPickUpOrderDto(slot,slotHandler);
      });
      setOriginalSlots(slots);
      //setSlots(filterSlotsByDate(date));
      setSlots(slots);
    }

    const error = (error : any) => {
      NotificationUtils.createNotification('error',error.message);
    }
    await pickUpOrderService.getAll(success,error);
  }

  /*const renderActions = () => {
    if (isCreationMode) {
      return (<div className={"actions"}>
        <Button onClick={toggleCreationMode}>Gérer mes réservations</Button>
      </div>);
    } else {
      return (<div className={"actions"}>
        <Button onClick={toggleCreationMode}>Créer un créneau</Button>
      </div>);
    }
  }*/

  const creationItemButtons = [
    { "label": "Réserver", "icon": VscCheck, "onClick": onSlotClicked},
    { "label": "Annuler", "icon": VscChromeClose, "onClick": () => {}}
  ];
  const updateItemButtons = [
    { "label": "Annuler", "icon": VscArrowLeft, "onClick": () => {}},
    { "label": "Supprimer", "icon": AiOutlineDelete, "onClick": onSlotRemove}
  ];


  return (<div className={"slot-picker"}>
    <div className={"left-panel"}>
      <SlotCalendar  className={`calendar ${!isCreationMode ? 'disabled' : ''}`} date={date} onDateChange={onDateChange}/>
    </div>
    <div className={"right-panel"}>
      <SlotList items={slots}  itemButtons={isCreationMode ? creationItemButtons : updateItemButtons}/>
    </div>
  </div>);
}

export default SlotPicker;
