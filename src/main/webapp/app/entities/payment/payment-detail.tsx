import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentEntity = useAppSelector(state => state.payment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="evaradripApp.payment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.id}</dd>
          <dt>
            <span id="transactionId">
              <Translate contentKey="evaradripApp.payment.transactionId">Transaction Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.transactionId}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="evaradripApp.payment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="evaradripApp.payment.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.currency}</dd>
          <dt>
            <span id="method">
              <Translate contentKey="evaradripApp.payment.method">Method</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.method}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="evaradripApp.payment.status">Status</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.status}</dd>
          <dt>
            <span id="gatewayResponse">
              <Translate contentKey="evaradripApp.payment.gatewayResponse">Gateway Response</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.gatewayResponse}</dd>
          <dt>
            <span id="referenceNumber">
              <Translate contentKey="evaradripApp.payment.referenceNumber">Reference Number</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.referenceNumber}</dd>
          <dt>
            <span id="failureReason">
              <Translate contentKey="evaradripApp.payment.failureReason">Failure Reason</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.failureReason}</dd>
          <dt>
            <Translate contentKey="evaradripApp.payment.order">Order</Translate>
          </dt>
          <dd>{paymentEntity.order ? paymentEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetail;
