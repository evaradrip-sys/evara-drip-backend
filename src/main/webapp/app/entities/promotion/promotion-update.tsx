import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { DiscountType } from 'app/shared/model/enumerations/discount-type.model';
import { createEntity, getEntity, reset, updateEntity } from './promotion.reducer';

export const PromotionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const products = useAppSelector(state => state.product.entities);
  const promotionEntity = useAppSelector(state => state.promotion.entity);
  const loading = useAppSelector(state => state.promotion.loading);
  const updating = useAppSelector(state => state.promotion.updating);
  const updateSuccess = useAppSelector(state => state.promotion.updateSuccess);
  const discountTypeValues = Object.keys(DiscountType);

  const handleClose = () => {
    navigate(`/promotion${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProducts({}));
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
    if (values.minPurchaseAmount !== undefined && typeof values.minPurchaseAmount !== 'number') {
      values.minPurchaseAmount = Number(values.minPurchaseAmount);
    }
    if (values.maxDiscountAmount !== undefined && typeof values.maxDiscountAmount !== 'number') {
      values.maxDiscountAmount = Number(values.maxDiscountAmount);
    }
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    if (values.usageLimit !== undefined && typeof values.usageLimit !== 'number') {
      values.usageLimit = Number(values.usageLimit);
    }
    if (values.usageCount !== undefined && typeof values.usageCount !== 'number') {
      values.usageCount = Number(values.usageCount);
    }

    const entity = {
      ...promotionEntity,
      ...values,
      applicableProducts: mapIdList(values.applicableProducts),
      products: mapIdList(values.products),
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
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          discountType: 'PERCENTAGE',
          ...promotionEntity,
          startDate: convertDateTimeFromServer(promotionEntity.startDate),
          endDate: convertDateTimeFromServer(promotionEntity.endDate),
          applicableProducts: promotionEntity?.applicableProducts?.map(e => e.id.toString()),
          products: promotionEntity?.products?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evaradripApp.promotion.home.createOrEditLabel" data-cy="PromotionCreateUpdateHeading">
            <Translate contentKey="evaradripApp.promotion.home.createOrEditLabel">Create or edit a Promotion</Translate>
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
                  id="promotion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evaradripApp.promotion.name')}
                id="promotion-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.description')}
                id="promotion-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.promoCode')}
                id="promotion-promoCode"
                name="promoCode"
                data-cy="promoCode"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.discountType')}
                id="promotion-discountType"
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
                label={translate('evaradripApp.promotion.discountValue')}
                id="promotion-discountValue"
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
                label={translate('evaradripApp.promotion.minPurchaseAmount')}
                id="promotion-minPurchaseAmount"
                name="minPurchaseAmount"
                data-cy="minPurchaseAmount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.maxDiscountAmount')}
                id="promotion-maxDiscountAmount"
                name="maxDiscountAmount"
                data-cy="maxDiscountAmount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.startDate')}
                id="promotion-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.endDate')}
                id="promotion-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.usageLimit')}
                id="promotion-usageLimit"
                name="usageLimit"
                data-cy="usageLimit"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.usageCount')}
                id="promotion-usageCount"
                name="usageCount"
                data-cy="usageCount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.isActive')}
                id="promotion-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.applicableCategories')}
                id="promotion-applicableCategories"
                name="applicableCategories"
                data-cy="applicableCategories"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.excludedProducts')}
                id="promotion-excludedProducts"
                name="excludedProducts"
                data-cy="excludedProducts"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.termsAndConditions')}
                id="promotion-termsAndConditions"
                name="termsAndConditions"
                data-cy="termsAndConditions"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.promotion.applicableProducts')}
                id="promotion-applicableProducts"
                data-cy="applicableProducts"
                type="select"
                multiple
                name="applicableProducts"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.promotion.products')}
                id="promotion-products"
                data-cy="products"
                type="select"
                multiple
                name="products"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/promotion" replace color="info">
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

export default PromotionUpdate;
