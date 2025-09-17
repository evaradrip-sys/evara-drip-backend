import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPromotions } from 'app/entities/promotion/promotion.reducer';
import { getEntities as getBrands } from 'app/entities/brand/brand.reducer';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { ProductStatus } from 'app/shared/model/enumerations/product-status.model';
import { createEntity, getEntity, reset, updateEntity } from './product.reducer';

export const ProductUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const promotions = useAppSelector(state => state.promotion.entities);
  const brands = useAppSelector(state => state.brand.entities);
  const categories = useAppSelector(state => state.category.entities);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const productEntity = useAppSelector(state => state.product.entity);
  const loading = useAppSelector(state => state.product.loading);
  const updating = useAppSelector(state => state.product.updating);
  const updateSuccess = useAppSelector(state => state.product.updateSuccess);
  const productStatusValues = Object.keys(ProductStatus);

  const handleClose = () => {
    navigate(`/product${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPromotions({}));
    dispatch(getBrands({}));
    dispatch(getCategories({}));
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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.originalPrice !== undefined && typeof values.originalPrice !== 'number') {
      values.originalPrice = Number(values.originalPrice);
    }
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }
    if (values.reviewsCount !== undefined && typeof values.reviewsCount !== 'number') {
      values.reviewsCount = Number(values.reviewsCount);
    }
    if (values.stockCount !== undefined && typeof values.stockCount !== 'number') {
      values.stockCount = Number(values.stockCount);
    }
    if (values.weight !== undefined && typeof values.weight !== 'number') {
      values.weight = Number(values.weight);
    }

    const entity = {
      ...productEntity,
      ...values,
      promotions: mapIdList(values.promotions),
      brand: brands.find(it => it.id.toString() === values.brand?.toString()),
      category: categories.find(it => it.id.toString() === values.category?.toString()),
      wishlisteds: mapIdList(values.wishlisteds),
      applicablePromotions: mapIdList(values.applicablePromotions),
      featuredInCategories: mapIdList(values.featuredInCategories),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'ACTIVE',
          ...productEntity,
          promotions: productEntity?.promotions?.map(e => e.id.toString()),
          brand: productEntity?.brand?.id,
          category: productEntity?.category?.id,
          wishlisteds: productEntity?.wishlisteds?.map(e => e.id.toString()),
          applicablePromotions: productEntity?.applicablePromotions?.map(e => e.id.toString()),
          featuredInCategories: productEntity?.featuredInCategories?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evaradripApp.product.home.createOrEditLabel" data-cy="ProductCreateUpdateHeading">
            <Translate contentKey="evaradripApp.product.home.createOrEditLabel">Create or edit a Product</Translate>
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
                  id="product-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evaradripApp.product.name')}
                id="product-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.description')}
                id="product-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.product.price')}
                id="product-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.originalPrice')}
                id="product-originalPrice"
                name="originalPrice"
                data-cy="originalPrice"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.sku')}
                id="product-sku"
                name="sku"
                data-cy="sku"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.isNew')}
                id="product-isNew"
                name="isNew"
                data-cy="isNew"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('evaradripApp.product.isOnSale')}
                id="product-isOnSale"
                name="isOnSale"
                data-cy="isOnSale"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('evaradripApp.product.rating')}
                id="product-rating"
                name="rating"
                data-cy="rating"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.reviewsCount')}
                id="product-reviewsCount"
                name="reviewsCount"
                data-cy="reviewsCount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.stockCount')}
                id="product-stockCount"
                name="stockCount"
                data-cy="stockCount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.inStock')}
                id="product-inStock"
                name="inStock"
                data-cy="inStock"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('evaradripApp.product.features')}
                id="product-features"
                name="features"
                data-cy="features"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.product.metaTitle')}
                id="product-metaTitle"
                name="metaTitle"
                data-cy="metaTitle"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.metaDescription')}
                id="product-metaDescription"
                name="metaDescription"
                data-cy="metaDescription"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.metaKeywords')}
                id="product-metaKeywords"
                name="metaKeywords"
                data-cy="metaKeywords"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.status')}
                id="product-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {productStatusValues.map(productStatus => (
                  <option value={productStatus} key={productStatus}>
                    {translate(`evaradripApp.ProductStatus.${productStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.product.weight')}
                id="product-weight"
                name="weight"
                data-cy="weight"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.dimensions')}
                id="product-dimensions"
                name="dimensions"
                data-cy="dimensions"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.product.promotions')}
                id="product-promotions"
                data-cy="promotions"
                type="select"
                multiple
                name="promotions"
              >
                <option value="" key="0" />
                {promotions
                  ? promotions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="product-brand" name="brand" data-cy="brand" label={translate('evaradripApp.product.brand')} type="select">
                <option value="" key="0" />
                {brands
                  ? brands.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-category"
                name="category"
                data-cy="category"
                label={translate('evaradripApp.product.category')}
                type="select"
                required
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                label={translate('evaradripApp.product.wishlisted')}
                id="product-wishlisted"
                data-cy="wishlisted"
                type="select"
                multiple
                name="wishlisteds"
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
              <ValidatedField
                label={translate('evaradripApp.product.applicablePromotions')}
                id="product-applicablePromotions"
                data-cy="applicablePromotions"
                type="select"
                multiple
                name="applicablePromotions"
              >
                <option value="" key="0" />
                {promotions
                  ? promotions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.product.featuredInCategories')}
                id="product-featuredInCategories"
                data-cy="featuredInCategories"
                type="select"
                multiple
                name="featuredInCategories"
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product" replace color="info">
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

export default ProductUpdate;
