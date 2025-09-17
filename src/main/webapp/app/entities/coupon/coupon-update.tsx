import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { DiscountType } from 'app/shared/model/enumerations/discount-type.model';
import { createEntity, getEntity, reset, updateEntity } from './coupon.reducer';

export const CouponUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const couponEntity = useAppSelector(state => state.coupon.entity);
  const loading = useAppSelector(state => state.coupon.loading);
  const updating = useAppSelector(state => state.coupon.updating);
  const updateSuccess = useAppSelector(state => state.coupon.updateSuccess);
  const discountTypeValues = Object.keys(DiscountType);

  const handleClose = () => {
    navigate(`/coupon${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.discountValue !== undefined && typeof values.discountValue !== 'number') {
      values.discountValue = Number(values.discountValue);
    }
    values.validFrom = convertDateTimeToServer(values.validFrom);
    values.validUntil = convertDateTimeToServer(values.validUntil);
    if (values.maxUses !== undefined && typeof values.maxUses !== 'number') {
      values.maxUses = Number(values.maxUses);
    }
    if (values.currentUses !== undefined && typeof values.currentUses !== 'number') {
      values.currentUses = Number(values.currentUses);
    }
    if (values.minOrderValue !== undefined && typeof values.minOrderValue !== 'number') {
      values.minOrderValue = Number(values.minOrderValue);
    }

    const entity = {
      ...couponEntity,
      ...values,
      users: mapIdList(values.users),
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
          validFrom: displayDefaultDateTime(),
          validUntil: displayDefaultDateTime(),
        }
      : {
          discountType: 'PERCENTAGE',
          ...couponEntity,
          validFrom: convertDateTimeFromServer(couponEntity.validFrom),
          validUntil: convertDateTimeFromServer(couponEntity.validUntil),
          users: couponEntity?.users?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evaradripApp.coupon.home.createOrEditLabel" data-cy="CouponCreateUpdateHeading">
            <Translate contentKey="evaradripApp.coupon.home.createOrEditLabel">Create or edit a Coupon</Translate>
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
                  id="coupon-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evaradripApp.coupon.code')}
                id="coupon-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.description')}
                id="coupon-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.discountType')}
                id="coupon-discountType"
                name="discountType"
                data-cy="discountType"
                type="select"
              >
                {discountTypeValues.map(discountType => (
                  <option value={discountType} key={discountType}>
                    {translate(`evaradripApp.DiscountType.${discountType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.coupon.discountValue')}
                id="coupon-discountValue"
                name="discountValue"
                data-cy="discountValue"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.validFrom')}
                id="coupon-validFrom"
                name="validFrom"
                data-cy="validFrom"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.validUntil')}
                id="coupon-validUntil"
                name="validUntil"
                data-cy="validUntil"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.maxUses')}
                id="coupon-maxUses"
                name="maxUses"
                data-cy="maxUses"
                type="text"
                validate={{
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.currentUses')}
                id="coupon-currentUses"
                name="currentUses"
                data-cy="currentUses"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.minOrderValue')}
                id="coupon-minOrderValue"
                name="minOrderValue"
                data-cy="minOrderValue"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.isActive')}
                id="coupon-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('evaradripApp.coupon.users')}
                id="coupon-users"
                data-cy="users"
                type="select"
                multiple
                name="users"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/coupon" replace color="info">
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

export default CouponUpdate;
