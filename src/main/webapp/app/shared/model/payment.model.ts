import { IOrder } from 'app/shared/model/order.model';
import { PaymentMethod } from 'app/shared/model/enumerations/payment-method.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IPayment {
  id?: number;
  transactionId?: string;
  amount?: number;
  currency?: string;
  method?: keyof typeof PaymentMethod;
  status?: keyof typeof PaymentStatus;
  gatewayResponse?: string | null;
  referenceNumber?: string | null;
  failureReason?: string | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IPayment> = {};
