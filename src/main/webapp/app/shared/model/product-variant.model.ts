import { IProduct } from 'app/shared/model/product.model';
import { ClothingSize } from 'app/shared/model/enumerations/clothing-size.model';

export interface IProductVariant {
  id?: number;
  variantSize?: keyof typeof ClothingSize;
  color?: string;
  sku?: string;
  stockCount?: number;
  priceAdjustment?: number | null;
  barcode?: string | null;
  weight?: number | null;
  product?: IProduct;
}

export const defaultValue: Readonly<IProductVariant> = {};
