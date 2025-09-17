import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-promotion.reducer';

export const ProductPromotionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productPromotionEntity = useAppSelector(state => state.productPromotion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productPromotionDetailsHeading">
          <Translate contentKey="evaradripApp.productPromotion.detail.title">ProductPromotion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productPromotionEntity.id}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="evaradripApp.productPromotion.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{productPromotionEntity.priority}</dd>
          <dt>
            <span id="isExclusive">
              <Translate contentKey="evaradripApp.productPromotion.isExclusive">Is Exclusive</Translate>
            </span>
          </dt>
          <dd>{productPromotionEntity.isExclusive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/product-promotion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-promotion/${productPromotionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductPromotionDetail;
