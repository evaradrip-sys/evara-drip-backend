import { IProduct } from 'app/shared/model/product.model';

export interface ICategory {
  id?: number;
  name?: string;
  description?: string | null;
  imageUrl?: string | null;
  href?: string;
  isFeatured?: boolean | null;
  displayOrder?: number | null;
  featuredProducts?: IProduct[] | null;
  parent?: ICategory | null;
}

export const defaultValue: Readonly<ICategory> = {
  isFeatured: false,
};
