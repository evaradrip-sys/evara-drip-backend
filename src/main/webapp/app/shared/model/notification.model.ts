import { IUserProfile } from 'app/shared/model/user-profile.model';
import { NotificationType } from 'app/shared/model/enumerations/notification-type.model';

export interface INotification {
  id?: number;
  type?: keyof typeof NotificationType;
  title?: string;
  message?: string;
  isRead?: boolean | null;
  metadata?: string | null;
  user?: IUserProfile;
}

export const defaultValue: Readonly<INotification> = {
  isRead: false,
};
