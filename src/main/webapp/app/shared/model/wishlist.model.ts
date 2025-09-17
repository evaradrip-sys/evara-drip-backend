export interface IWishlist {
  id?: number;
  priority?: number | null;
  notes?: string | null;
}

export const defaultValue: Readonly<IWishlist> = {};
