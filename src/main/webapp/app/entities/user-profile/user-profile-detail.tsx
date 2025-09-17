import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="evaradripApp.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="evaradripApp.userProfile.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.phoneNumber}</dd>
          <dt>
            <span id="dateOfBirth">
              <Translate contentKey="evaradripApp.userProfile.dateOfBirth">Date Of Birth</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.dateOfBirth ? (
              <TextFormat value={userProfileEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="gender">
              <Translate contentKey="evaradripApp.userProfile.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.gender}</dd>
          <dt>
            <span id="avatarUrl">
              <Translate contentKey="evaradripApp.userProfile.avatarUrl">Avatar Url</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.avatarUrl}</dd>
          <dt>
            <span id="loyaltyPoints">
              <Translate contentKey="evaradripApp.userProfile.loyaltyPoints">Loyalty Points</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.loyaltyPoints}</dd>
          <dt>
            <span id="membershipLevel">
              <Translate contentKey="evaradripApp.userProfile.membershipLevel">Membership Level</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.membershipLevel}</dd>
          <dt>
            <span id="preferences">
              <Translate contentKey="evaradripApp.userProfile.preferences">Preferences</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.preferences}</dd>
          <dt>
            <span id="newsletterSubscribed">
              <Translate contentKey="evaradripApp.userProfile.newsletterSubscribed">Newsletter Subscribed</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.newsletterSubscribed ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="evaradripApp.userProfile.user">User</Translate>
          </dt>
          <dd>{userProfileEntity.user ? userProfileEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="evaradripApp.userProfile.wishlist">Wishlist</Translate>
          </dt>
          <dd>
            {userProfileEntity.wishlists
              ? userProfileEntity.wishlists.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.wishlists && i === userProfileEntity.wishlists.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="evaradripApp.userProfile.coupons">Coupons</Translate>
          </dt>
          <dd>
            {userProfileEntity.coupons
              ? userProfileEntity.coupons.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userProfileEntity.coupons && i === userProfileEntity.coupons.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
