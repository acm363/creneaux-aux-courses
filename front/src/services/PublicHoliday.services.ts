import axios, {AxiosInstance} from 'axios';
import {UserDTO} from '../dto/user/user.dto';
import {PublicHolidayRequestDTO, PublicHolidayResponseDTO} from '../dto/publicHoliday/public.holiday.dto';

export class PublicHolidayService {
  readonly BEARER = 'Bearer';
  private userDto: UserDTO;
  private axiosInstance: AxiosInstance;

  constructor(userDto: UserDTO) {
    this.userDto = userDto;
    this.axiosInstance = this._createAxiosInstance();
  }

  // Create and return an axios instance
  _createAxiosInstance(): AxiosInstance {
    return axios.create({
      baseURL: process.env.REACT_APP_BACKEND_BASE_URL || 'http://localhost:8080',
      headers: {
        Authorization: `${this.BEARER} ${this.userDto.token}`,
        'Content-Type': 'application/json',
      },
    });
  }

  async getPublicHolidays() {
    try {
      const response = await this.axiosInstance.get('/public-holiday');
      return response.data as PublicHolidayResponseDTO[];
    } catch (error) {
      throw error;
    }
  }

  async createPublicHoliday(publicHolidayData: PublicHolidayRequestDTO) {
    try {
      const response = await this.axiosInstance.post('/public-holiday', publicHolidayData);
      return response.data as PublicHolidayResponseDTO;
    } catch (error) {
      throw error;
    }
  }

  async updatePublicHoliday(publicId: string, publicHolidayRequestDto: PublicHolidayRequestDTO) {
    try {
      const response = await this.axiosInstance.patch(`/public-holiday/${publicId}`, publicHolidayRequestDto);
      return response.data as PublicHolidayResponseDTO;
    } catch (error) {
      throw error;
    }
  }

  async deletePublicHoliday(publicId: string) {
    try {
      await this.axiosInstance.delete(`/public-holiday/${publicId}`);
    } catch (error) {
      throw error;
    }
  }
}