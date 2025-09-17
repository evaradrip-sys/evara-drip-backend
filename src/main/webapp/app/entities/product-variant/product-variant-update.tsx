import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { ClothingSize } from 'app/shared/model/enumerations/clothing-size.model';
import { createEntity, getEntity, reset, updateEntity } from './product-variant.reducer';

export const ProductVariantUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const products = useAppSelector(state => state.product.entities);
  const productVariantEntity = useAppSelector(state => state.productVariant.entity);
  const loading = useAppSelector(state => state.productVariant.loading);
  const updating = useAppSelector(state => state.productVariant.updating);
  const updateSuccess = useAppSelector(state => state.productVariant.updateSuccess);
  const clothingSizeValues = Object.keys(ClothingSize);

  const handleClose = () => {
    navigate(`/product-variant${location.search}`);
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
    if (values.stockCount !== undefined && typeof values.stockCount !== 'number') {
      values.stockCount = Number(values.stockCount);
    }
    if (values.priceAdjustment !== undefined && typeof values.priceAdjustment !== 'number') {
      values.priceAdjustment = Number(values.priceAdjustment);
    }
    if (values.weight !== undefined && typeof values.weight !== 'number') {
      values.weight = Number(values.weight);
    }

    const entity = {
      ...productVariantEntity,
      ...values,
      product: products.find(it => it.id.toString() === values.product?.toString()),
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
          variantSize: 'XS',
          ...productVariantEntity,
          product: productVariantEntity?.product?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evaradripApp.productVariant.home.createOrEditLabel" data-cy="ProductVariantCreateUpdateHeading">
            <Translate contentKey="evaradripApp.productVariant.home.createOrEditLabel">Create or edit a ProductVariant</Translate>
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
                  id="product-variant-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evaradripApp.productVariant.variantSize')}
                id="product-variant-variantSize"
                name="variantSize"
                data-cy="variantSize"
                type="select"
              >
                {clothingSizeValues.map(clothingSize => (
                  <option value={clothingSize} key={clothingSize}>
                    {translate(`evaradripApp.ClothingSize.${clothingSize}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.productVariant.color')}
                id="product-variant-color"
                name="color"
                data-cy="color"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.productVariant.sku')}
                id="product-variant-sku"
                name="sku"
                data-cy="sku"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.productVariant.stockCount')}
                id="product-variant-stockCount"
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
                label={translate('evaradripApp.productVariant.priceAdjustment')}
                id="product-variant-priceAdjustment"
                name="priceAdjustment"
                data-cy="priceAdjustment"
                type="text"
              />
              <ValidatedField
                label={translate('evaradripApp.productVariant.barcode')}
                id="product-variant-barcode"
                name="barcode"
                data-cy="barcode"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.productVariant.weight')}
                id="product-variant-weight"
                name="weight"
                data-cy="weight"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="product-variant-product"
                name="product"
                data-cy="product"
                label={translate('evaradripApp.productVariant.product')}
                type="select"
                required
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-variant" replace color="info">
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

export default ProductVariantUpdate;
