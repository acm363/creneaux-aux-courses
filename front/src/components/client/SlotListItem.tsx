import React from 'react';
import { ListItem, ListItemButton, ListItemText } from '@mui/material';
import {ItemButton, SlotEvent} from "../../interfaces";
import {BsFillBasket2Fill} from "react-icons/bs";
import '../../styles/components/client/SlotListItem.scss';
import ListItemIcon from "@mui/material/ListItemIcon";


interface SlotListItemProps {
  item: SlotEvent;
  itemButtons: ItemButton[];
}

const SlotListItem: React.FC<SlotListItemProps> = ({ item, itemButtons }) => {

      const [showButtons, setShowButtons] = React.useState(false);

      const handleItemClick = () => {
        setShowButtons(!showButtons);
      }

      // const isSlotFull = () => {
      //   return item.remainingCapacity === 0;
      // }

      const isOldSlot = () => {
        const today = new Date();
        const slotDate = new Date(item.start);
        return today > slotDate;
      }

      const extractTime = (date: Date) => {
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${hours}:${minutes}`;
      }

      const renderItemContent = () => {
        if (!showButtons){
          const startHour = extractTime(item.start);
          const endHour = extractTime(item.end);
          // remove seconds
          return (
          <ListItemButton onClick={handleItemClick} disabled={isOldSlot()}>
            <ListItemIcon className={"slot-list-item-icon"}><BsFillBasket2Fill /></ListItemIcon>
                <ListItemText
                  // primary={`${startHour} - ${endHour} Capacité : ${item.capacity}`}
                    secondary={`${item.title}`}
                />
            <ListItemText className={"slot-list-item-hour"}
              primary={`${startHour} - ${endHour}`}
              // secondary={`${item.day}`}
            />
            <ListItemText className={"slot-list-item-capacity"}
                          primary={`Places restantes : ${item.extendedProps.remainingCapacity}/${item.extendedProps.capacity}`}
              // secondary={`${item.day}`}
            />

          </ListItemButton>
            );
        }
        return (
          <>
            { itemButtons.map((itemButton) => {
                return (
                <ListItemButton className="slot-list-item-button" onClick={() => {
                    handleItemClick();
                    itemButton.onClick(item)}}
                  >
                    {itemButton.icon && <ListItemIcon className={"slot-list-item-icon"}><itemButton.icon /></ListItemIcon>}
                    {itemButton.label && <ListItemText className={"slot-list-item-text"} primary={itemButton.label} />}
                  </ListItemButton>
                );
              })
            }
            </>
        );

      }

      return (
        <ListItem className={'slot-list-item'} component="div">
          <div className={"slot-list-item-content"}>
          {renderItemContent()}
          </div>
        </ListItem>
      );

}

export default SlotListItem;
