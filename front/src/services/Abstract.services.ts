import axios, {AxiosInstance} from 'axios';
import {UserDTO} from '../dto/user/user.dto';
import {Day} from "../dto/slot/slot.dto";


export type successCallback<T> = (data: T) => any;
export type errorCallback<T> = (error: T) => any;


export default abstract class AbstractService<Dto,Request,Response,ResponseList> {

  readonly BEARER = 'Bearer';

  protected userDto: UserDTO;
  protected axiosInstance: AxiosInstance;
  protected _route: string;
  protected _params: string = '';

  protected constructor(userDto: UserDTO, route : string) {
    this.userDto = userDto;
    this.axiosInstance = this._createAxiosInstance();
    this._route = route;
  }

  // Create and return an axios instance
  private _createAxiosInstance(): AxiosInstance {
    return axios.create({
      baseURL: process.env.REACT_APP_BACKEND_BASE_URL || 'http://localhost:8080',
      headers: {
        Authorization: `${this.BEARER} ${this.userDto.token}`,
        'Content-Type': 'application/json',
      },
    });
  }

  // gerer l'id dans le dto
  async update(id: string, dto: Dto, succes : successCallback<Response>, error : errorCallback<Response>): Promise<Response>{
    return await this.axiosInstance.patch(`${this._route}/${id}`, this.buildRequestFromDto(dto)).
    then((response) => {
      succes(response.data);
      return response.data;
    }).catch((err) => {
      error(err);
      return error;
    });
  }

  async create(dto: Dto, succes : successCallback<Response>, error : errorCallback<Response>): Promise<Response> { // TODO : corriger le type, passer requete au lieu de dto
    return await this.axiosInstance.post(`${this._route}`, this.buildRequestFromDto(dto)).
    then((response) => {
      succes(response.data);
      return response.data;
    }).catch((err) => {
      error(err);
      return error;
    });
  }

  // overSevenDays : true if we want to get the slots for the next 7 days, false if we want to get the slots for the current day
  async getAll(success : successCallback<ResponseList>, error : errorCallback<ResponseList>): Promise<ResponseList> {
    return await this.axiosInstance.get(`${this._route}${this._params}`).
    then((response) => {
      success(response.data);
      return response.data;
    }).catch((err) => {
      error(err);
      return error;
    });
  }

  async get(id: string, success : successCallback<Response>, error : errorCallback<Response>): Promise<Response> {
    return await this.axiosInstance.get(`${this._route}/${id}`).
    then((response) => {
      success(response.data);
      return response.data;
    }).catch((err) => {
      error(err);
      return error;
    });
  }

  async delete(id: string, success : successCallback<Response>, error : errorCallback<Response>): Promise<Response> {
    return await this.axiosInstance.delete(`${this._route}/${id}`).
    then((response) => {
      success(response.data);
      return response.data;
    }).catch((err) => {
      error(err);
      return error;
    });
  }

  abstract buildRequestFromDto(dto: Dto): Request;

  protected dayToNumber(day : Day) : number {
    const days = [Day.SUNDAY, Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY];
    return days.indexOf(day);
  }

}
