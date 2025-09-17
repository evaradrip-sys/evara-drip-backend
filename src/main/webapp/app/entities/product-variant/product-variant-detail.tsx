import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-variant.reducer';

export const ProductVariantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productVariantEntity = useAppSelector(state => state.productVariant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productVariantDetailsHeading">
          <Translate contentKey="evaradripApp.productVariant.detail.title">ProductVariant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.id}</dd>
          <dt>
            <span id="variantSize">
              <Translate contentKey="evaradripApp.productVariant.variantSize">Variant Size</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.variantSize}</dd>
          <dt>
            <span id="color">
              <Translate contentKey="evaradripApp.productVariant.color">Color</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.color}</dd>
          <dt>
            <span id="sku">
              <Translate contentKey="evaradripApp.productVariant.sku">Sku</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.sku}</dd>
          <dt>
            <span id="stockCount">
              <Translate contentKey="evaradripApp.productVariant.stockCount">Stock Count</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.stockCount}</dd>
          <dt>
            <span id="priceAdjustment">
              <Translate contentKey="evaradripApp.productVariant.priceAdjustment">Price Adjustment</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.priceAdjustment}</dd>
          <dt>
            <span id="barcode">
              <Translate contentKey="evaradripApp.productVariant.barcode">Barcode</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.barcode}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="evaradripApp.productVariant.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{productVariantEntity.weight}</dd>
          <dt>
            <Translate contentKey="evaradripApp.productVariant.product">Product</Translate>
          </dt>
          <dd>{productVariantEntity.product ? productVariantEntity.product.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-variant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-variant/${productVariantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductVariantDetail;
