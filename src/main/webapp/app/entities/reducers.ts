import brand from 'app/entities/brand/brand.reducer';
import category from 'app/entities/category/category.reducer';
import product from 'app/entities/product/product.reducer';
import productImage from 'app/entities/product-image/product-image.reducer';
import productVariant from 'app/entities/product-variant/product-variant.reducer';
import userProfile from 'app/entities/user-profile/user-profile.reducer';
import userAddress from 'app/entities/user-address/user-address.reducer';
import cart from 'app/entities/cart/cart.reducer';
import cartItem from 'app/entities/cart-item/cart-item.reducer';
import order from 'app/entities/order/order.reducer';
import orderItem from 'app/entities/order-item/order-item.reducer';
import wishlist from 'app/entities/wishlist/wishlist.reducer';
import review from 'app/entities/review/review.reducer';
import promotion from 'app/entities/promotion/promotion.reducer';
import productPromotion from 'app/entities/product-promotion/product-promotion.reducer';
import payment from 'app/entities/payment/payment.reducer';
import shipping from 'app/entities/shipping/shipping.reducer';
import inventory from 'app/entities/inventory/inventory.reducer';
import coupon from 'app/entities/coupon/coupon.reducer';
import notification from 'app/entities/notification/notification.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  brand,
  category,
  product,
  productImage,
  productVariant,
  userProfile,
  userAddress,
  cart,
  cartItem,
  order,
  orderItem,
  wishlist,
  review,
  promotion,
  productPromotion,
  payment,
  shipping,
  inventory,
  coupon,
  notification,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
