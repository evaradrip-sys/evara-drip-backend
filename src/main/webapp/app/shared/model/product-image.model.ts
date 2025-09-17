import { IProduct } from 'app/shared/model/product.model';

export interface IProductImage {
  id?: number;
  imageUrl?: string;
  altText?: string | null;
  isPrimary?: boolean | null;
  displayOrder?: number | null;
  product?: IProduct;
}

export const defaultValue: Readonly<IProductImage> = {
  isPrimary: false,
};
