import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="evaradripApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="evaradripApp.product.name">Name</Translate>
            </span>
          </dt>
          <dd>{productEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="evaradripApp.product.description">Description</Translate>
            </span>
          </dt>
          <dd>{productEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="evaradripApp.product.price">Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.price}</dd>
          <dt>
            <span id="originalPrice">
              <Translate contentKey="evaradripApp.product.originalPrice">Original Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.originalPrice}</dd>
          <dt>
            <span id="sku">
              <Translate contentKey="evaradripApp.product.sku">Sku</Translate>
            </span>
          </dt>
          <dd>{productEntity.sku}</dd>
          <dt>
            <span id="isNew">
              <Translate contentKey="evaradripApp.product.isNew">Is New</Translate>
            </span>
          </dt>
          <dd>{productEntity.isNew ? 'true' : 'false'}</dd>
          <dt>
            <span id="isOnSale">
              <Translate contentKey="evaradripApp.product.isOnSale">Is On Sale</Translate>
            </span>
          </dt>
          <dd>{productEntity.isOnSale ? 'true' : 'false'}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="evaradripApp.product.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{productEntity.rating}</dd>
          <dt>
            <span id="reviewsCount">
              <Translate contentKey="evaradripApp.product.reviewsCount">Reviews Count</Translate>
            </span>
          </dt>
          <dd>{productEntity.reviewsCount}</dd>
          <dt>
            <span id="stockCount">
              <Translate contentKey="evaradripApp.product.stockCount">Stock Count</Translate>
            </span>
          </dt>
          <dd>{productEntity.stockCount}</dd>
          <dt>
            <span id="inStock">
              <Translate contentKey="evaradripApp.product.inStock">In Stock</Translate>
            </span>
          </dt>
          <dd>{productEntity.inStock ? 'true' : 'false'}</dd>
          <dt>
            <span id="features">
              <Translate contentKey="evaradripApp.product.features">Features</Translate>
            </span>
          </dt>
          <dd>{productEntity.features}</dd>
          <dt>
            <span id="metaTitle">
              <Translate contentKey="evaradripApp.product.metaTitle">Meta Title</Translate>
            </span>
          </dt>
          <dd>{productEntity.metaTitle}</dd>
          <dt>
            <span id="metaDescription">
              <Translate contentKey="evaradripApp.product.metaDescription">Meta Description</Translate>
            </span>
          </dt>
          <dd>{productEntity.metaDescription}</dd>
          <dt>
            <span id="metaKeywords">
              <Translate contentKey="evaradripApp.product.metaKeywords">Meta Keywords</Translate>
            </span>
          </dt>
          <dd>{productEntity.metaKeywords}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="evaradripApp.product.status">Status</Translate>
            </span>
          </dt>
          <dd>{productEntity.status}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="evaradripApp.product.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{productEntity.weight}</dd>
          <dt>
            <span id="dimensions">
              <Translate contentKey="evaradripApp.product.dimensions">Dimensions</Translate>
            </span>
          </dt>
          <dd>{productEntity.dimensions}</dd>
          <dt>
            <Translate contentKey="evaradripApp.product.promotions">Promotions</Translate>
          </dt>
          <dd>
            {productEntity.promotions
              ? productEntity.promotions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productEntity.promotions && i === productEntity.promotions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="evaradripApp.product.brand">Brand</Translate>
          </dt>
          <dd>{productEntity.brand ? productEntity.brand.name : ''}</dd>
          <dt>
            <Translate contentKey="evaradripApp.product.category">Category</Translate>
          </dt>
          <dd>{productEntity.category ? productEntity.category.name : ''}</dd>
          <dt>
            <Translate contentKey="evaradripApp.product.wishlisted">Wishlisted</Translate>
          </dt>
          <dd>
            {productEntity.wishlisteds
              ? productEntity.wishlisteds.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productEntity.wishlisteds && i === productEntity.wishlisteds.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="evaradripApp.product.applicablePromotions">Applicable Promotions</Translate>
          </dt>
          <dd>
            {productEntity.applicablePromotions
              ? productEntity.applicablePromotions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productEntity.applicablePromotions && i === productEntity.applicablePromotions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="evaradripApp.product.featuredInCategories">Featured In Categories</Translate>
          </dt>
          <dd>
            {productEntity.featuredInCategories
              ? productEntity.featuredInCategories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productEntity.featuredInCategories && i === productEntity.featuredInCategories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
