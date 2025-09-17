import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './promotion.reducer';

export const PromotionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const promotionEntity = useAppSelector(state => state.promotion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="promotionDetailsHeading">
          <Translate contentKey="evaradripApp.promotion.detail.title">Promotion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="evaradripApp.promotion.name">Name</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="evaradripApp.promotion.description">Description</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.description}</dd>
          <dt>
            <span id="promoCode">
              <Translate contentKey="evaradripApp.promotion.promoCode">Promo Code</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.promoCode}</dd>
          <dt>
            <span id="discountType">
              <Translate contentKey="evaradripApp.promotion.discountType">Discount Type</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.discountType}</dd>
          <dt>
            <span id="discountValue">
              <Translate contentKey="evaradripApp.promotion.discountValue">Discount Value</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.discountValue}</dd>
          <dt>
            <span id="minPurchaseAmount">
              <Translate contentKey="evaradripApp.promotion.minPurchaseAmount">Min Purchase Amount</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.minPurchaseAmount}</dd>
          <dt>
            <span id="maxDiscountAmount">
              <Translate contentKey="evaradripApp.promotion.maxDiscountAmount">Max Discount Amount</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.maxDiscountAmount}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="evaradripApp.promotion.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {promotionEntity.startDate ? <TextFormat value={promotionEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="evaradripApp.promotion.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.endDate ? <TextFormat value={promotionEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="usageLimit">
              <Translate contentKey="evaradripApp.promotion.usageLimit">Usage Limit</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.usageLimit}</dd>
          <dt>
            <span id="usageCount">
              <Translate contentKey="evaradripApp.promotion.usageCount">Usage Count</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.usageCount}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="evaradripApp.promotion.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="applicableCategories">
              <Translate contentKey="evaradripApp.promotion.applicableCategories">Applicable Categories</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.applicableCategories}</dd>
          <dt>
            <span id="excludedProducts">
              <Translate contentKey="evaradripApp.promotion.excludedProducts">Excluded Products</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.excludedProducts}</dd>
          <dt>
            <span id="termsAndConditions">
              <Translate contentKey="evaradripApp.promotion.termsAndConditions">Terms And Conditions</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.termsAndConditions}</dd>
          <dt>
            <Translate contentKey="evaradripApp.promotion.applicableProducts">Applicable Products</Translate>
          </dt>
          <dd>
            {promotionEntity.applicableProducts
              ? promotionEntity.applicableProducts.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {promotionEntity.applicableProducts && i === promotionEntity.applicableProducts.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="evaradripApp.promotion.products">Products</Translate>
          </dt>
          <dd>
            {promotionEntity.products
              ? promotionEntity.products.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {promotionEntity.products && i === promotionEntity.products.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/promotion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/promotion/${promotionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PromotionDetail;
