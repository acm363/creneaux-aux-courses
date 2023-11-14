import {ExceptionalSlotDTO, SlotType, StandardSlotDTO} from '../dto/slot/slot.dto';
import {EventImpl, SlotEvent, SlotEventStandard, SlotHandler} from '../interfaces';

type callback = (prop : any) => any;
// Abstract pour empêcher l'instanciation de la classe
export abstract class SlotUtils{

    public static async createFromEventSelection(slot : any, type: SlotType, slotHandler : SlotHandler, success : callback, error : callback ) : Promise<any> {
      if(type === SlotType.EXCEPTIONAL){
        const data = slotHandler.exceptional.buildSlotDtoFromEventSelection(slot);
        await slotHandler.exceptional.create(data,success,error);
      }
      else {
        const data = slotHandler.standard.buildSlotDtoFromEventSelection(slot);
        await slotHandler.standard.create(data,success,error);
      }
    }

    public static async updateFromEventSelection(slot : any, type: SlotType, slotHandler : SlotHandler, success : callback, error : callback ) : Promise<any> {
      if(type === SlotType.EXCEPTIONAL){
        const data = slotHandler.exceptional.buildSlotDtoFromEventSelection(slot);
        await slotHandler.exceptional.update(data.publicId,data,success,error);
      }
      else {
        const data = slotHandler.standard.buildSlotDtoFromEventSelection(slot);
        await slotHandler.standard.update(data.publicId,data,success,error);
      }
    }

    public static async createFromSlotEvent(slot :SlotEvent, slotHandler : SlotHandler, success : callback, error : callback ) : Promise<any> {
      if(slot.extendedProps.type === SlotType.EXCEPTIONAL){
        const data = slotHandler.exceptional.buildSlotDtoFromSlotEvent(slot);
        await slotHandler.exceptional.create(data,success,error);
      }
      else {
        const data = slotHandler.standard.buildSlotDtoFromSlotEvent(slot);
        await slotHandler.standard.create(data,success,error);
      }
    }

    public static async updateFromSlotEvent(slot : SlotEvent, slotHandler : SlotHandler, success : callback, error : callback ) : Promise<any> {
      if(slot.extendedProps.type === SlotType.EXCEPTIONAL){
        const data = slotHandler.exceptional.buildSlotDtoFromSlotEvent(slot);
        await slotHandler.exceptional.update(data.publicId,data,success,error);
      }
      else {
        const data = slotHandler.standard.buildSlotDtoFromSlotEvent(slot);
        await slotHandler.standard.update(data.publicId,data,success,error);
      }
    }

    public static async delete(id:string, type : SlotType, slotHandler : SlotHandler, success : callback, error : callback ) : Promise<any> {
      if(type === SlotType.EXCEPTIONAL){
        await slotHandler.exceptional.delete(id,success,error);
      }
      else {
        await slotHandler.standard.delete(id,success,error);
      }
    }

    public static buildSlotEvent(slot: any, type: SlotType | null, slotHandler: SlotHandler): SlotEvent {
      if(type === SlotType.EXCEPTIONAL){
        return slotHandler.exceptional.buildSlotEvent({slot:slot, date : null});
      }
      else {
        return slotHandler.standard.buildSlotEvent({slot:slot, date : null});
      }
    }

    public static buildCustomSlotEvent(slot: any, type: SlotType | null, slotHandler: SlotHandler): SlotEvent | SlotEventStandard {
      if(type === SlotType.EXCEPTIONAL){
        return slotHandler.exceptional.buildSlotEvent({slot:slot, date : null});
      }
      else {
        return slotHandler.standard.buildSlotEventStandard(slot);
      }
    }

    public static extractEventData(slotEvent : EventImpl, type: SlotType | null, slotHandler: SlotHandler) : any {
      if(type === SlotType.EXCEPTIONAL){
        return slotHandler.exceptional.extractEventData(slotEvent);
      }
      else {
        return slotHandler.standard.extractEventData(slotEvent);
      }
    }


    public static async getAll(slotHandler : SlotHandler,useCustom : boolean, success : callback, error : callback ) : Promise<any> {
      const standardSuccess = (standard : any) => {

        const _standard = standard.map((slot : any) => {
          if(useCustom){
            return slotHandler.standard.buildSlotEventStandard(slot);
          }
          return slotHandler.standard.buildSlotEvent({slot:slot,date: null});
        });
        const exceptionalSuccess = (exceptional : any) => {
          const _exceptional = exceptional.map((slot : any) => {
            return slotHandler.exceptional.buildSlotEvent({slot:slot, date : null});
          });
          success(_exceptional.concat(_standard));
        }
        slotHandler.exceptional.getAll(exceptionalSuccess,error);
      }
      await slotHandler.standard.getAll(standardSuccess,error);
    }

    public static buildSlotDtoFromSlotEvent(slotEvent: SlotEvent, slotHandler : SlotHandler): StandardSlotDTO | ExceptionalSlotDTO {
      if (slotEvent.extendedProps.type === SlotType.EXCEPTIONAL) {
        return slotHandler.exceptional.buildSlotDtoFromSlotEvent(slotEvent);
      } else {
        return slotHandler.standard.buildSlotDtoFromSlotEvent(slotEvent);
      }
    }


   public static isStandardSlotDTO(slot: any): slot is StandardSlotDTO {
    return slot && slot.dayOfWeek && typeof slot.publicId === "string";
  }


}
