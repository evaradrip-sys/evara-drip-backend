import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './shipping.reducer';

export const ShippingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const shippingEntity = useAppSelector(state => state.shipping.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shippingDetailsHeading">
          <Translate contentKey="evaradripApp.shipping.detail.title">Shipping</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.id}</dd>
          <dt>
            <span id="carrier">
              <Translate contentKey="evaradripApp.shipping.carrier">Carrier</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.carrier}</dd>
          <dt>
            <span id="trackingNumber">
              <Translate contentKey="evaradripApp.shipping.trackingNumber">Tracking Number</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.trackingNumber}</dd>
          <dt>
            <span id="estimatedDelivery">
              <Translate contentKey="evaradripApp.shipping.estimatedDelivery">Estimated Delivery</Translate>
            </span>
          </dt>
          <dd>
            {shippingEntity.estimatedDelivery ? (
              <TextFormat value={shippingEntity.estimatedDelivery} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="actualDelivery">
              <Translate contentKey="evaradripApp.shipping.actualDelivery">Actual Delivery</Translate>
            </span>
          </dt>
          <dd>
            {shippingEntity.actualDelivery ? (
              <TextFormat value={shippingEntity.actualDelivery} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="shippingCost">
              <Translate contentKey="evaradripApp.shipping.shippingCost">Shipping Cost</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.shippingCost}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="evaradripApp.shipping.status">Status</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.status}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="evaradripApp.shipping.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{shippingEntity.notes}</dd>
          <dt>
            <Translate contentKey="evaradripApp.shipping.order">Order</Translate>
          </dt>
          <dd>{shippingEntity.order ? shippingEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/shipping" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shipping/${shippingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShippingDetail;
