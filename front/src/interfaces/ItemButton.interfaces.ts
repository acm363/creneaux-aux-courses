import {IconType} from "react-icons";
import {SlotEvent} from "./SlotEvent.interfaces";

export interface ItemButton {
  label: string;
  icon: IconType;
  onClick: (item: SlotEvent) => any;
}
