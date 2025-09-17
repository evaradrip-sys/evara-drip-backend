import { IPromotion } from 'app/shared/model/promotion.model';
import { IBrand } from 'app/shared/model/brand.model';
import { ICategory } from 'app/shared/model/category.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ProductStatus } from 'app/shared/model/enumerations/product-status.model';

export interface IProduct {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  originalPrice?: number | null;
  sku?: string | null;
  isNew?: boolean | null;
  isOnSale?: boolean | null;
  rating?: number | null;
  reviewsCount?: number | null;
  stockCount?: number;
  inStock?: boolean | null;
  features?: string | null;
  metaTitle?: string | null;
  metaDescription?: string | null;
  metaKeywords?: string | null;
  status?: keyof typeof ProductStatus | null;
  weight?: number | null;
  dimensions?: string | null;
  promotions?: IPromotion[] | null;
  brand?: IBrand | null;
  category?: ICategory;
  wishlisteds?: IUserProfile[] | null;
  applicablePromotions?: IPromotion[] | null;
  featuredInCategories?: ICategory[] | null;
}

export const defaultValue: Readonly<IProduct> = {
  isNew: false,
  isOnSale: false,
  inStock: false,
};
