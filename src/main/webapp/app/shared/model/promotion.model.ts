import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';
import { DiscountType } from 'app/shared/model/enumerations/discount-type.model';

export interface IPromotion {
  id?: number;
  name?: string;
  description?: string | null;
  promoCode?: string | null;
  discountType?: keyof typeof DiscountType;
  discountValue?: number;
  minPurchaseAmount?: number | null;
  maxDiscountAmount?: number | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  usageLimit?: number | null;
  usageCount?: number | null;
  isActive?: boolean | null;
  applicableCategories?: string | null;
  excludedProducts?: string | null;
  termsAndConditions?: string | null;
  applicableProducts?: IProduct[] | null;
  products?: IProduct[] | null;
}

export const defaultValue: Readonly<IPromotion> = {
  isActive: false,
};
