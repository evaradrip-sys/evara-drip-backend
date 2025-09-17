import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { ReviewStatus } from 'app/shared/model/enumerations/review-status.model';
import { createEntity, getEntity, reset, updateEntity } from './review.reducer';

export const ReviewUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const products = useAppSelector(state => state.product.entities);
  const reviewEntity = useAppSelector(state => state.review.entity);
  const loading = useAppSelector(state => state.review.loading);
  const updating = useAppSelector(state => state.review.updating);
  const updateSuccess = useAppSelector(state => state.review.updateSuccess);
  const reviewStatusValues = Object.keys(ReviewStatus);

  const handleClose = () => {
    navigate(`/review${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }
    if (values.helpfulCount !== undefined && typeof values.helpfulCount !== 'number') {
      values.helpfulCount = Number(values.helpfulCount);
    }
    if (values.notHelpfulCount !== undefined && typeof values.notHelpfulCount !== 'number') {
      values.notHelpfulCount = Number(values.notHelpfulCount);
    }
    values.responseDate = convertDateTimeToServer(values.responseDate);

    const entity = {
      ...reviewEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
      ? {
          responseDate: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...reviewEntity,
          responseDate: convertDateTimeFromServer(reviewEntity.responseDate),
          user: reviewEntity?.user?.id,
          product: reviewEntity?.product?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evaradripApp.review.home.createOrEditLabel" data-cy="ReviewCreateUpdateHeading">
            <Translate contentKey="evaradripApp.review.home.createOrEditLabel">Create or edit a Review</Translate>
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
                  id="review-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evaradripApp.review.rating')}
                id="review-rating"
                name="rating"
                data-cy="rating"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.review.title')}
                id="review-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.review.comment')}
                id="review-comment"
                name="comment"
                data-cy="comment"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.review.helpfulCount')}
                id="review-helpfulCount"
                name="helpfulCount"
                data-cy="helpfulCount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.review.notHelpfulCount')}
                id="review-notHelpfulCount"
                name="notHelpfulCount"
                data-cy="notHelpfulCount"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.review.verifiedPurchase')}
                id="review-verifiedPurchase"
                name="verifiedPurchase"
                data-cy="verifiedPurchase"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('evaradripApp.review.status')}
                id="review-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {reviewStatusValues.map(reviewStatus => (
                  <option value={reviewStatus} key={reviewStatus}>
                    {translate(`evaradripApp.ReviewStatus.${reviewStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.review.response')}
                id="review-response"
                name="response"
                data-cy="response"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.review.responseDate')}
                id="review-responseDate"
                name="responseDate"
                data-cy="responseDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="review-user"
                name="user"
                data-cy="user"
                label={translate('evaradripApp.review.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="review-product"
                name="product"
                data-cy="product"
                label={translate('evaradripApp.review.product')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/review" replace color="info">
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

export default ReviewUpdate;
