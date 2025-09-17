import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserAddresses } from 'app/entities/user-address/user-address.reducer';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { PaymentMethod } from 'app/shared/model/enumerations/payment-method.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';
import { createEntity, getEntity, reset, updateEntity } from './order.reducer';

export const OrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userAddresses = useAppSelector(state => state.userAddress.entities);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const orderEntity = useAppSelector(state => state.order.entity);
  const loading = useAppSelector(state => state.order.loading);
  const updating = useAppSelector(state => state.order.updating);
  const updateSuccess = useAppSelector(state => state.order.updateSuccess);
  const orderStatusValues = Object.keys(OrderStatus);
  const paymentMethodValues = Object.keys(PaymentMethod);
  const paymentStatusValues = Object.keys(PaymentStatus);

  const handleClose = () => {
    navigate(`/order${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserAddresses({}));
    dispatch(getUserProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.totalAmount !== undefined && typeof values.totalAmount !== 'number') {
      values.totalAmount = Number(values.totalAmount);
    }
    if (values.subtotalAmount !== undefined && typeof values.subtotalAmount !== 'number') {
      values.subtotalAmount = Number(values.subtotalAmount);
    }
    if (values.taxAmount !== undefined && typeof values.taxAmount !== 'number') {
      values.taxAmount = Number(values.taxAmount);
    }
    if (values.shippingAmount !== undefined && typeof values.shippingAmount !== 'number') {
      values.shippingAmount = Number(values.shippingAmount);
    }
    if (values.discountAmount !== undefined && typeof values.discountAmount !== 'number') {
      values.discountAmount = Number(values.discountAmount);
    }
    if (values.refundAmount !== undefined && typeof values.refundAmount !== 'number') {
      values.refundAmount = Number(values.refundAmount);
    }
    values.deliveredDate = convertDateTimeToServer(values.deliveredDate);
    values.shippedDate = convertDateTimeToServer(values.shippedDate);

    const entity = {
      ...orderEntity,
      ...values,
      shippingAddress: userAddresses.find(it => it.id.toString() === values.shippingAddress?.toString()),
      billingAddress: userAddresses.find(it => it.id.toString() === values.billingAddress?.toString()),
      user: userProfiles.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          deliveredDate: displayDefaultDateTime(),
          shippedDate: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          paymentMethod: 'CREDIT_CARD',
          paymentStatus: 'PENDING',
          ...orderEntity,
          deliveredDate: convertDateTimeFromServer(orderEntity.deliveredDate),
          shippedDate: convertDateTimeFromServer(orderEntity.shippedDate),
          shippingAddress: orderEntity?.shippingAddress?.id,
          billingAddress: orderEntity?.billingAddress?.id,
          user: orderEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evaradripApp.order.home.createOrEditLabel" data-cy="OrderCreateUpdateHeading">
            <Translate contentKey="evaradripApp.order.home.createOrEditLabel">Create or edit a Order</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evaradripApp.order.orderNumber')}
                id="order-orderNumber"
                name="orderNumber"
                data-cy="orderNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField label={translate('evaradripApp.order.status')} id="order-status" name="status" data-cy="status" type="select">
                {orderStatusValues.map(orderStatus => (
                  <option value={orderStatus} key={orderStatus}>
                    {translate(`evaradripApp.OrderStatus.${orderStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.order.totalAmount')}
                id="order-totalAmount"
                name="totalAmount"
                data-cy="totalAmount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.subtotalAmount')}
                id="order-subtotalAmount"
                name="subtotalAmount"
                data-cy="subtotalAmount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.taxAmount')}
                id="order-taxAmount"
                name="taxAmount"
                data-cy="taxAmount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.shippingAmount')}
                id="order-shippingAmount"
                name="shippingAmount"
                data-cy="shippingAmount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.discountAmount')}
                id="order-discountAmount"
                name="discountAmount"
                data-cy="discountAmount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.paymentMethod')}
                id="order-paymentMethod"
                name="paymentMethod"
                data-cy="paymentMethod"
                type="select"
              >
                {paymentMethodValues.map(paymentMethod => (
                  <option value={paymentMethod} key={paymentMethod}>
                    {translate(`evaradripApp.PaymentMethod.${paymentMethod}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.order.paymentStatus')}
                id="order-paymentStatus"
                name="paymentStatus"
                data-cy="paymentStatus"
                type="select"
              >
                {paymentStatusValues.map(paymentStatus => (
                  <option value={paymentStatus} key={paymentStatus}>
                    {translate(`evaradripApp.PaymentStatus.${paymentStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.order.shippingMethod')}
                id="order-shippingMethod"
                name="shippingMethod"
                data-cy="shippingMethod"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.trackingNumber')}
                id="order-trackingNumber"
                name="trackingNumber"
                data-cy="trackingNumber"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField label={translate('evaradripApp.order.notes')} id="order-notes" name="notes" data-cy="notes" type="textarea" />
              <ValidatedField
                label={translate('evaradripApp.order.cancelReason')}
                id="order-cancelReason"
                name="cancelReason"
                data-cy="cancelReason"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.returnReason')}
                id="order-returnReason"
                name="returnReason"
                data-cy="returnReason"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.refundAmount')}
                id="order-refundAmount"
                name="refundAmount"
                data-cy="refundAmount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.order.estimatedDeliveryDate')}
                id="order-estimatedDeliveryDate"
                name="estimatedDeliveryDate"
                data-cy="estimatedDeliveryDate"
                type="date"
              />
              <ValidatedField
                label={translate('evaradripApp.order.deliveredDate')}
                id="order-deliveredDate"
                name="deliveredDate"
                data-cy="deliveredDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('evaradripApp.order.shippedDate')}
                id="order-shippedDate"
                name="shippedDate"
                data-cy="shippedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="order-shippingAddress"
                name="shippingAddress"
                data-cy="shippingAddress"
                label={translate('evaradripApp.order.shippingAddress')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userAddresses
                  ? userAddresses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="order-billingAddress"
                name="billingAddress"
                data-cy="billingAddress"
                label={translate('evaradripApp.order.billingAddress')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userAddresses
                  ? userAddresses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="order-user"
                name="user"
                data-cy="user"
                label={translate('evaradripApp.order.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrderUpdate;
