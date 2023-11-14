import { EventImpl } from './EventImpl.interfaces';

export interface ContextMenuProps {
  isVisible: boolean,
  x: number,
  y: number,
  event: EventImpl
}
