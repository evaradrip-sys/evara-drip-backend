import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './coupon.reducer';

export const CouponDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const couponEntity = useAppSelector(state => state.coupon.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="couponDetailsHeading">
          <Translate contentKey="evaradripApp.coupon.detail.title">Coupon</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{couponEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="evaradripApp.coupon.code">Code</Translate>
            </span>
          </dt>
          <dd>{couponEntity.code}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="evaradripApp.coupon.description">Description</Translate>
            </span>
          </dt>
          <dd>{couponEntity.description}</dd>
          <dt>
            <span id="discountType">
              <Translate contentKey="evaradripApp.coupon.discountType">Discount Type</Translate>
            </span>
          </dt>
          <dd>{couponEntity.discountType}</dd>
          <dt>
            <span id="discountValue">
              <Translate contentKey="evaradripApp.coupon.discountValue">Discount Value</Translate>
            </span>
          </dt>
          <dd>{couponEntity.discountValue}</dd>
          <dt>
            <span id="validFrom">
              <Translate contentKey="evaradripApp.coupon.validFrom">Valid From</Translate>
            </span>
          </dt>
          <dd>{couponEntity.validFrom ? <TextFormat value={couponEntity.validFrom} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="validUntil">
              <Translate contentKey="evaradripApp.coupon.validUntil">Valid Until</Translate>
            </span>
          </dt>
          <dd>{couponEntity.validUntil ? <TextFormat value={couponEntity.validUntil} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="maxUses">
              <Translate contentKey="evaradripApp.coupon.maxUses">Max Uses</Translate>
            </span>
          </dt>
          <dd>{couponEntity.maxUses}</dd>
          <dt>
            <span id="currentUses">
              <Translate contentKey="evaradripApp.coupon.currentUses">Current Uses</Translate>
            </span>
          </dt>
          <dd>{couponEntity.currentUses}</dd>
          <dt>
            <span id="minOrderValue">
              <Translate contentKey="evaradripApp.coupon.minOrderValue">Min Order Value</Translate>
            </span>
          </dt>
          <dd>{couponEntity.minOrderValue}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="evaradripApp.coupon.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{couponEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="evaradripApp.coupon.users">Users</Translate>
          </dt>
          <dd>
            {couponEntity.users
              ? couponEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {couponEntity.users && i === couponEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/coupon" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/coupon/${couponEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CouponDetail;
