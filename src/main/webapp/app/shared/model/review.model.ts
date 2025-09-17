import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IProduct } from 'app/shared/model/product.model';
import { ReviewStatus } from 'app/shared/model/enumerations/review-status.model';

export interface IReview {
  id?: number;
  rating?: number;
  title?: string | null;
  comment?: string | null;
  helpfulCount?: number | null;
  notHelpfulCount?: number | null;
  verifiedPurchase?: boolean | null;
  status?: keyof typeof ReviewStatus | null;
  response?: string | null;
  responseDate?: dayjs.Dayjs | null;
  user?: IUser;
  product?: IProduct;
}

export const defaultValue: Readonly<IReview> = {
  verifiedPurchase: false,
};
