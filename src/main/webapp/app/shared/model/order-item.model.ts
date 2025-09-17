import { IProduct } from 'app/shared/model/product.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IOrderItem {
  id?: number;
  quantity?: number;
  unitPrice?: number;
  totalPrice?: number;
  discountAmount?: number | null;
  taxAmount?: number | null;
  productSnapshot?: string | null;
  product?: IProduct;
  variant?: IProductVariant | null;
  order?: IOrder;
}

export const defaultValue: Readonly<IOrderItem> = {};
