import { IUserProfile } from 'app/shared/model/user-profile.model';
import { AddressType } from 'app/shared/model/enumerations/address-type.model';

export interface IUserAddress {
  id?: number;
  addressType?: keyof typeof AddressType;
  fullName?: string;
  phoneNumber?: string;
  streetAddress?: string;
  streetAddress2?: string | null;
  city?: string;
  state?: string;
  zipCode?: string;
  country?: string;
  landmark?: string | null;
  isDefault?: boolean | null;
  user?: IUserProfile;
}

export const defaultValue: Readonly<IUserAddress> = {
  isDefault: false,
};
