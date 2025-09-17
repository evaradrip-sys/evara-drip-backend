import dayjs from 'dayjs';
import { IOrder } from 'app/shared/model/order.model';
import { ShippingStatus } from 'app/shared/model/enumerations/shipping-status.model';

export interface IShipping {
  id?: number;
  carrier?: string;
  trackingNumber?: string | null;
  estimatedDelivery?: dayjs.Dayjs | null;
  actualDelivery?: dayjs.Dayjs | null;
  shippingCost?: number | null;
  status?: keyof typeof ShippingStatus | null;
  notes?: string | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IShipping> = {};
