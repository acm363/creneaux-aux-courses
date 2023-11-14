import axios from 'axios';
import {UserDTO} from '../dto/user/user.dto';
import {LOGIN_ROUTE} from '../routes/routes';
import {UserSessionService} from './UserSession.services';


export class AuthService {
  static async login(email: string, password: string): Promise<UserDTO> {
    try {
      // Create an axios instance.
      const axiosInstance = axios.create({
        baseURL: process.env.REACT_APP_BACKEND_BASE_URL,
      });

      // Make an API request to the backend to login.
      const response = await axiosInstance.post(LOGIN_ROUTE, {
        email,
        password,
      });

      const token = response.data.token;

      // Store pickUpOrder data in sessionStorage.
      UserSessionService.setUserSession(token);

      const userDto = UserSessionService.getUser();

      if (!userDto) {
        throw new Error('User not found');
      }
      return userDto;
    } catch (error: any) {
      // I'm not sure if this is the correct way to handle errors here.
      throw error;
    }
  }

  static logout(): void {
    // Disconnect the pickUpOrder by clearing the session.
    UserSessionService.clearUserSession();
  }
}