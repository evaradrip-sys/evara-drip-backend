import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntities as getCoupons } from 'app/entities/coupon/coupon.reducer';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { MembershipLevel } from 'app/shared/model/enumerations/membership-level.model';
import { createEntity, getEntity, reset, updateEntity } from './user-profile.reducer';

export const UserProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const products = useAppSelector(state => state.product.entities);
  const coupons = useAppSelector(state => state.coupon.entities);
  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  const loading = useAppSelector(state => state.userProfile.loading);
  const updating = useAppSelector(state => state.userProfile.updating);
  const updateSuccess = useAppSelector(state => state.userProfile.updateSuccess);
  const genderValues = Object.keys(Gender);
  const membershipLevelValues = Object.keys(MembershipLevel);

  const handleClose = () => {
    navigate(`/user-profile${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getProducts({}));
    dispatch(getCoupons({}));
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
    if (values.loyaltyPoints !== undefined && typeof values.loyaltyPoints !== 'number') {
      values.loyaltyPoints = Number(values.loyaltyPoints);
    }

    const entity = {
      ...userProfileEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      wishlists: mapIdList(values.wishlists),
      coupons: mapIdList(values.coupons),
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
          gender: 'MALE',
          membershipLevel: 'BRONZE',
          ...userProfileEntity,
          user: userProfileEntity?.user?.id,
          wishlists: userProfileEntity?.wishlists?.map(e => e.id.toString()),
          coupons: userProfileEntity?.coupons?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evaradripApp.userProfile.home.createOrEditLabel" data-cy="UserProfileCreateUpdateHeading">
            <Translate contentKey="evaradripApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
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
                  id="user-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evaradripApp.userProfile.phoneNumber')}
                id="user-profile-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.userProfile.dateOfBirth')}
                id="user-profile-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="date"
              />
              <ValidatedField
                label={translate('evaradripApp.userProfile.gender')}
                id="user-profile-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {genderValues.map(gender => (
                  <option value={gender} key={gender}>
                    {translate(`evaradripApp.Gender.${gender}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.userProfile.avatarUrl')}
                id="user-profile-avatarUrl"
                name="avatarUrl"
                data-cy="avatarUrl"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.userProfile.loyaltyPoints')}
                id="user-profile-loyaltyPoints"
                name="loyaltyPoints"
                data-cy="loyaltyPoints"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evaradripApp.userProfile.membershipLevel')}
                id="user-profile-membershipLevel"
                name="membershipLevel"
                data-cy="membershipLevel"
                type="select"
              >
                {membershipLevelValues.map(membershipLevel => (
                  <option value={membershipLevel} key={membershipLevel}>
                    {translate(`evaradripApp.MembershipLevel.${membershipLevel}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evaradripApp.userProfile.preferences')}
                id="user-profile-preferences"
                name="preferences"
                data-cy="preferences"
                type="textarea"
              />
              <ValidatedField
                label={translate('evaradripApp.userProfile.newsletterSubscribed')}
                id="user-profile-newsletterSubscribed"
                name="newsletterSubscribed"
                data-cy="newsletterSubscribed"
                check
                type="checkbox"
              />
              <ValidatedField
                id="user-profile-user"
                name="user"
                data-cy="user"
                label={translate('evaradripApp.userProfile.user')}
                type="select"
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
              <ValidatedField
                label={translate('evaradripApp.userProfile.wishlist')}
                id="user-profile-wishlist"
                data-cy="wishlist"
                type="select"
                multiple
                name="wishlists"
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
                label={translate('evaradripApp.userProfile.coupons')}
                id="user-profile-coupons"
                data-cy="coupons"
                type="select"
                multiple
                name="coupons"
              >
                <option value="" key="0" />
                {coupons
                  ? coupons.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-profile" replace color="info">
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

export default UserProfileUpdate;
