import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order.reducer';

export const OrderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderEntity = useAppSelector(state => state.order.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailsHeading">
          <Translate contentKey="evaradripApp.order.detail.title">Order</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderEntity.id}</dd>
          <dt>
            <span id="orderNumber">
              <Translate contentKey="evaradripApp.order.orderNumber">Order Number</Translate>
            </span>
          </dt>
          <dd>{orderEntity.orderNumber}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="evaradripApp.order.status">Status</Translate>
            </span>
          </dt>
          <dd>{orderEntity.status}</dd>
          <dt>
            <span id="totalAmount">
              <Translate contentKey="evaradripApp.order.totalAmount">Total Amount</Translate>
            </span>
          </dt>
          <dd>{orderEntity.totalAmount}</dd>
          <dt>
            <span id="subtotalAmount">
              <Translate contentKey="evaradripApp.order.subtotalAmount">Subtotal Amount</Translate>
            </span>
          </dt>
          <dd>{orderEntity.subtotalAmount}</dd>
          <dt>
            <span id="taxAmount">
              <Translate contentKey="evaradripApp.order.taxAmount">Tax Amount</Translate>
            </span>
          </dt>
          <dd>{orderEntity.taxAmount}</dd>
          <dt>
            <span id="shippingAmount">
              <Translate contentKey="evaradripApp.order.shippingAmount">Shipping Amount</Translate>
            </span>
          </dt>
          <dd>{orderEntity.shippingAmount}</dd>
          <dt>
            <span id="discountAmount">
              <Translate contentKey="evaradripApp.order.discountAmount">Discount Amount</Translate>
            </span>
          </dt>
          <dd>{orderEntity.discountAmount}</dd>
          <dt>
            <span id="paymentMethod">
              <Translate contentKey="evaradripApp.order.paymentMethod">Payment Method</Translate>
            </span>
          </dt>
          <dd>{orderEntity.paymentMethod}</dd>
          <dt>
            <span id="paymentStatus">
              <Translate contentKey="evaradripApp.order.paymentStatus">Payment Status</Translate>
            </span>
          </dt>
          <dd>{orderEntity.paymentStatus}</dd>
          <dt>
            <span id="shippingMethod">
              <Translate contentKey="evaradripApp.order.shippingMethod">Shipping Method</Translate>
            </span>
          </dt>
          <dd>{orderEntity.shippingMethod}</dd>
          <dt>
            <span id="trackingNumber">
              <Translate contentKey="evaradripApp.order.trackingNumber">Tracking Number</Translate>
            </span>
          </dt>
          <dd>{orderEntity.trackingNumber}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="evaradripApp.order.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{orderEntity.notes}</dd>
          <dt>
            <span id="cancelReason">
              <Translate contentKey="evaradripApp.order.cancelReason">Cancel Reason</Translate>
            </span>
          </dt>
          <dd>{orderEntity.cancelReason}</dd>
          <dt>
            <span id="returnReason">
              <Translate contentKey="evaradripApp.order.returnReason">Return Reason</Translate>
            </span>
          </dt>
          <dd>{orderEntity.returnReason}</dd>
          <dt>
            <span id="refundAmount">
              <Translate contentKey="evaradripApp.order.refundAmount">Refund Amount</Translate>
            </span>
          </dt>
          <dd>{orderEntity.refundAmount}</dd>
          <dt>
            <span id="estimatedDeliveryDate">
              <Translate contentKey="evaradripApp.order.estimatedDeliveryDate">Estimated Delivery Date</Translate>
            </span>
          </dt>
          <dd>
            {orderEntity.estimatedDeliveryDate ? (
              <TextFormat value={orderEntity.estimatedDeliveryDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deliveredDate">
              <Translate contentKey="evaradripApp.order.deliveredDate">Delivered Date</Translate>
            </span>
          </dt>
          <dd>
            {orderEntity.deliveredDate ? <TextFormat value={orderEntity.deliveredDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="shippedDate">
              <Translate contentKey="evaradripApp.order.shippedDate">Shipped Date</Translate>
            </span>
          </dt>
          <dd>{orderEntity.shippedDate ? <TextFormat value={orderEntity.shippedDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="evaradripApp.order.shippingAddress">Shipping Address</Translate>
          </dt>
          <dd>{orderEntity.shippingAddress ? orderEntity.shippingAddress.id : ''}</dd>
          <dt>
            <Translate contentKey="evaradripApp.order.billingAddress">Billing Address</Translate>
          </dt>
          <dd>{orderEntity.billingAddress ? orderEntity.billingAddress.id : ''}</dd>
          <dt>
            <Translate contentKey="evaradripApp.order.user">User</Translate>
          </dt>
          <dd>{orderEntity.user ? orderEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order/${orderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderDetail;
