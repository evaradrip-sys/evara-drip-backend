import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductPromotion from './product-promotion';
import ProductPromotionDetail from './product-promotion-detail';
import ProductPromotionUpdate from './product-promotion-update';
import ProductPromotionDeleteDialog from './product-promotion-delete-dialog';

const ProductPromotionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductPromotion />} />
    <Route path="new" element={<ProductPromotionUpdate />} />
    <Route path=":id">
      <Route index element={<ProductPromotionDetail />} />
      <Route path="edit" element={<ProductPromotionUpdate />} />
      <Route path="delete" element={<ProductPromotionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductPromotionRoutes;
