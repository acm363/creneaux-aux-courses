import {NotificationManager} from "react-notifications";


export class NotificationUtils {
  public static createNotification(type: String, msg: String) {
    switch (type) {
      case 'info':
        return NotificationManager.info(msg);
      case 'success':
        return NotificationManager.success(msg, 'Succès', 3000);
      case 'warning':
        return NotificationManager.warning(msg, 'Attention', 3000);
      case 'error':
        return NotificationManager.error(msg, 'Erreur', 5000);
    }
  }
}