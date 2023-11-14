import React from 'react';
import { Box, List } from '@mui/material';
import SlotListItem from "./SlotListItem";
import {ItemButton} from "../../interfaces";


import '../../styles/components/client/SlotList.scss';
import {SlotEvent} from "../../interfaces";
import {AiOutlineWarning} from "react-icons/ai";


interface SlotListProps {
  items: SlotEvent[];
  itemButtons: ItemButton[];
}

const SlotList: React.FC<SlotListProps> = ({ items, itemButtons }) => {

  const renderItems = () => {
    return items.map((item) => {
      return ( <SlotListItem key={item.id} item={item} itemButtons={itemButtons}/> );
    });
  }

  return (
    <div className={"slot-list"}>
      <Box className={items && items.length > 0 ? "slot-list-box" : "no-slots"}
           sx={{ width: '100%', height: 400, bgcolor: 'background.paper', overflow: 'auto' }}
      >
        {items && items.length > 0 ? (
            <List>
              {renderItems()}
            </List>
          )
          : (<div className={"no-slots-content"}>
            <AiOutlineWarning className={"no-slots-icon"}/>
            <span>Pas de créneaux</span>
          </div>)}
      </Box>
    </div>
  );

}

export default SlotList;