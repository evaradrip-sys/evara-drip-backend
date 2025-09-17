import { IProduct } from 'app/shared/model/product.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { ICart } from 'app/shared/model/cart.model';

export interface ICartItem {
  id?: number;
  quantity?: number;
  addedPrice?: number;
  product?: IProduct;
  variant?: IProductVariant | null;
  cart?: ICart;
}

export const defaultValue: Readonly<ICartItem> = {};
