import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';

export interface IInventory {
  id?: number;
  quantity?: number;
  reservedQuantity?: number | null;
  warehouse?: string | null;
  lastRestocked?: dayjs.Dayjs | null;
  reorderLevel?: number | null;
  reorderQuantity?: number | null;
  product?: IProduct;
}

export const defaultValue: Readonly<IInventory> = {};
