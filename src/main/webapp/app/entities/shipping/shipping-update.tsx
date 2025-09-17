import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getOrders } from 'app/entities/order/order.reducer';
import { ShippingStatus } from 'app/shared/model/enumerations/shipping-status.model';
import { createEntity, getEntity, reset, updateEntity } from './shipping.reducer';

export const ShippingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const orders = useAppSelector(state => state.order.entities);
  const shippingEntity = useAppSelector(state => state.shipping.entity);
  const loading = useAppSelector(state => state.shipping.loading);
  const updating = useAppSelector(state => state.shipping.updating);
  const updateSuccess = useAppSelector(state => state.shipping.updateSuccess);
  const shippingStatusValues = Object.keys(ShippingStatus);

  const handleClose = () => {
    navigate(`/shipping${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOrders({}));
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
    values.actualDelivery = convertDateTimeToServer(values.actualDelivery);
    if (values.shippingCost !== undefined && typeof values.shippingCost !== 'number') {
      values.shippingCost = Number(values.shippingCost);
    }

    const entity = {
      ...shippingEntity,
      ...values,
      order: orders.find(it => it.id.toString() === values.order?.toString()),
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
          actualDelivery: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...shippingEntity,
          actualDelivery: convertDateTimeFromServer(shippingEntity.actualDelivery),
          order: shippingEntity?.order?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evaradripApp.shipping.home.createOrEditLabel" data-cy="ShippingCreateUpdateHeading">
            <Translate contentKey="evaradripApp.shipping.home.createOrEditLabel">Create or edit a Shipping</Translate>
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
                  id="shipping-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evaradripApp.shipping.carrier')}
                id="shipping-carrier"
                name="carrier"
                data-cy="carrier"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.shipping.trackingNumber')}
                id="shipping-trackingNumber"
                name="trackingNumber"
                data-cy="trackingNumber"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.shipping.estimatedDelivery')}
                id="shipping-estimatedDelivery"
                name="estimatedDelivery"
                data-cy="estimatedDelivery"
                type="date"
              />
              <ValidatedField
                label={translate('evaradripApp.shipping.actualDelivery')}
                id="shipping-actualDelivery"
                name="actualDelivery"
                data-cy="actualDelivery"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('evaradripApp.shipping.shippingCost')}
                id="shipping-shippingCost"
                name="shippingCost"
                data-cy="shippingCost"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.shipping.status')}
                id="shipping-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {shippingStatusValues.map(shippingStatus => (
                  <option value={shippingStatus} key={shippingStatus}>
                    {translate(`evaradripApp.ShippingStatus.${shippingStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.shipping.notes')}
                id="shipping-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="shipping-order"
                name="order"
                data-cy="order"
                label={translate('evaradripApp.shipping.order')}
                type="select"
              >
                <option value="" key="0" />
                {orders
                  ? orders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/shipping" replace color="info">
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

export default ShippingUpdate;
