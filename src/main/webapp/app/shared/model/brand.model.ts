export interface IBrand {
  id?: number;
  name?: string;
  description?: string | null;
  logoUrl?: string | null;
  isActive?: boolean | null;
}

export const defaultValue: Readonly<IBrand> = {
  isActive: false,
};
