import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { CartStatus } from 'app/shared/model/enumerations/cart-status.model';

export interface ICart {
  id?: number;
  sessionId?: string | null;
  status?: keyof typeof CartStatus | null;
  expiresAt?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ICart> = {};
