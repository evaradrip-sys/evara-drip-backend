import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-image.reducer';

export const ProductImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productImageEntity = useAppSelector(state => state.productImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productImageDetailsHeading">
          <Translate contentKey="evaradripApp.productImage.detail.title">ProductImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.id}</dd>
          <dt>
            <span id="imageUrl">
              <Translate contentKey="evaradripApp.productImage.imageUrl">Image Url</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.imageUrl}</dd>
          <dt>
            <span id="altText">
              <Translate contentKey="evaradripApp.productImage.altText">Alt Text</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.altText}</dd>
          <dt>
            <span id="isPrimary">
              <Translate contentKey="evaradripApp.productImage.isPrimary">Is Primary</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.isPrimary ? 'true' : 'false'}</dd>
          <dt>
            <span id="displayOrder">
              <Translate contentKey="evaradripApp.productImage.displayOrder">Display Order</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.displayOrder}</dd>
          <dt>
            <Translate contentKey="evaradripApp.productImage.product">Product</Translate>
          </dt>
          <dd>{productImageEntity.product ? productImageEntity.product.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-image/${productImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductImageDetail;
