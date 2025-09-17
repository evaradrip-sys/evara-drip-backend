import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { DiscountType } from 'app/shared/model/enumerations/discount-type.model';

export interface ICoupon {
  id?: number;
  code?: string;
  description?: string | null;
  discountType?: keyof typeof DiscountType;
  discountValue?: number;
  validFrom?: dayjs.Dayjs;
  validUntil?: dayjs.Dayjs;
  maxUses?: number | null;
  currentUses?: number | null;
  minOrderValue?: number | null;
  isActive?: boolean | null;
  users?: IUserProfile[] | null;
}

export const defaultValue: Readonly<ICoupon> = {
  isActive: false,
};
