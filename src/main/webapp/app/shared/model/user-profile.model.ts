import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IProduct } from 'app/shared/model/product.model';
import { ICoupon } from 'app/shared/model/coupon.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { MembershipLevel } from 'app/shared/model/enumerations/membership-level.model';

export interface IUserProfile {
  id?: number;
  phoneNumber?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  gender?: keyof typeof Gender | null;
  avatarUrl?: string | null;
  loyaltyPoints?: number | null;
  membershipLevel?: keyof typeof MembershipLevel | null;
  preferences?: string | null;
  newsletterSubscribed?: boolean | null;
  user?: IUser | null;
  wishlists?: IProduct[] | null;
  coupons?: ICoupon[] | null;
}

export const defaultValue: Readonly<IUserProfile> = {
  newsletterSubscribed: false,
};
