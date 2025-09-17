import dayjs from 'dayjs';
import { IUserAddress } from 'app/shared/model/user-address.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { PaymentMethod } from 'app/shared/model/enumerations/payment-method.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IOrder {
  id?: number;
  orderNumber?: string;
  status?: keyof typeof OrderStatus;
  totalAmount?: number;
  subtotalAmount?: number;
  taxAmount?: number | null;
  shippingAmount?: number | null;
  discountAmount?: number | null;
  paymentMethod?: keyof typeof PaymentMethod | null;
  paymentStatus?: keyof typeof PaymentStatus | null;
  shippingMethod?: string | null;
  trackingNumber?: string | null;
  notes?: string | null;
  cancelReason?: string | null;
  returnReason?: string | null;
  refundAmount?: number | null;
  estimatedDeliveryDate?: dayjs.Dayjs | null;
  deliveredDate?: dayjs.Dayjs | null;
  shippedDate?: dayjs.Dayjs | null;
  shippingAddress?: IUserAddress;
  billingAddress?: IUserAddress;
  user?: IUserProfile;
}

export const defaultValue: Readonly<IOrder> = {};
