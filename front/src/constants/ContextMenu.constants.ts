import {ContextMenuProps} from "../interfaces";
import {SlotType} from "../dto/slot/slot.dto";

export const DEFAULT_CONTEXT_MENU_PROPS : ContextMenuProps = {
  isVisible: false,
  x: 0,
  y: 0,
  event: {
    id: '',
    start: new Date(),
    end: new Date(),
    extendedProps: {
      type: SlotType.STANDARD,
      capacity: 0,
      currentCharge: 0,
      remainingCapacity: 0
    },
    remove: () => {},
  },
};