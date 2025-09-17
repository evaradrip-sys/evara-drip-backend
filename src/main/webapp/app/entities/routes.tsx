import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Brand from './brand';
import Category from './category';
import Product from './product';
import ProductImage from './product-image';
import ProductVariant from './product-variant';
import UserProfile from './user-profile';
import UserAddress from './user-address';
import Cart from './cart';
import CartItem from './cart-item';
import Order from './order';
import OrderItem from './order-item';
import Wishlist from './wishlist';
import Review from './review';
import Promotion from './promotion';
import ProductPromotion from './product-promotion';
import Payment from './payment';
import Shipping from './shipping';
import Inventory from './inventory';
import Coupon from './coupon';
import Notification from './notification';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="brand/*" element={<Brand />} />
        <Route path="category/*" element={<Category />} />
        <Route path="product/*" element={<Product />} />
        <Route path="product-image/*" element={<ProductImage />} />
        <Route path="product-variant/*" element={<ProductVariant />} />
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="user-address/*" element={<UserAddress />} />
        <Route path="cart/*" element={<Cart />} />
        <Route path="cart-item/*" element={<CartItem />} />
        <Route path="order/*" element={<Order />} />
        <Route path="order-item/*" element={<OrderItem />} />
        <Route path="wishlist/*" element={<Wishlist />} />
        <Route path="review/*" element={<Review />} />
        <Route path="promotion/*" element={<Promotion />} />
        <Route path="product-promotion/*" element={<ProductPromotion />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="shipping/*" element={<Shipping />} />
        <Route path="inventory/*" element={<Inventory />} />
        <Route path="coupon/*" element={<Coupon />} />
        <Route path="notification/*" element={<Notification />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
