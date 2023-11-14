import {UserDTO} from '../dto/user/user.dto';
import jwtDecode, {JwtPayload} from 'jwt-decode';


const USER_SESSION_KEY = 'user';

export class UserSessionService {
  private static EXPIRATION_MINUTES = 60; // 60 minutes.

  // Set the pickUpOrder session data in sessionStorage.
  static setUserSession(token: string) {
    const expirationTime = new Date().getTime() + UserSessionService.EXPIRATION_MINUTES * 60 * 1000; // Convert minutes to milliseconds.
    const sessionData = {
      token,
      expirationTime,
    };
    sessionStorage.setItem(USER_SESSION_KEY, JSON.stringify(sessionData));
  }

  // Get the pickUpOrder data from sessionStorage and check if it's still valid.
  static getUser(): UserDTO | null {
    const sessionDataJSON = sessionStorage.getItem(USER_SESSION_KEY);
    if (!sessionDataJSON) {
      return null; // No session data available
    }

    const sessionData = JSON.parse(sessionDataJSON);
    const currentTime = new Date().getTime();

    if (sessionData.expirationTime && currentTime < sessionData.expirationTime) {
      // Session is still valid.
      const token = sessionData.token;

      const userDto = UserSessionService.decodeToken(token);

      if (!Object.keys(userDto).length) {
        return null;
      }


      return userDto;

    } else {
      // Session has expired, clear it.
      this.clearUserSession();
      return null;
    }
  }

  // Clear the pickUpOrder session data from sessionStorage.
  static clearUserSession() {
    sessionStorage.removeItem(USER_SESSION_KEY);
  }

  // Check if a pickUpOrder session exists.
  static hasUserSession() {
    return !!sessionStorage.getItem(USER_SESSION_KEY);
  }

  // Check if the token is verified and valid.
  // Returns the pickUpOrder data if the token is valid, otherwise returns false.
  private static decodeToken(token: string): UserDTO {
    let payload: any;

    try {
      payload = jwtDecode<JwtPayload>(token);
      // Check if the token is expired.
      const currentTime = new Date().getTime();
      if (payload && payload.exp && currentTime > payload.exp * 1000) {
        return {} as UserDTO;
      }
    } catch (e: any) {
      // In case of expired token, it will throw an error.
      // We will return false for all errors cases.
      return {} as UserDTO;
    }

    const userDto = {} as UserDTO;

    if (payload && typeof payload === 'object') {
      userDto.type = payload.type;
      userDto.name = payload.name;
      userDto.email = payload.email;
      userDto.publicId = payload.publicId;
      userDto.token = token;
    }

    return userDto;
  }

}