export interface IProductPromotion {
  id?: number;
  priority?: number | null;
  isExclusive?: boolean | null;
}

export const defaultValue: Readonly<IProductPromotion> = {
  isExclusive: false,
};
