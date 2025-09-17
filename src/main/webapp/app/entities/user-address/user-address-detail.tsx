import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-address.reducer';

export const UserAddressDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userAddressEntity = useAppSelector(state => state.userAddress.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userAddressDetailsHeading">
          <Translate contentKey="evaradripApp.userAddress.detail.title">UserAddress</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.id}</dd>
          <dt>
            <span id="addressType">
              <Translate contentKey="evaradripApp.userAddress.addressType">Address Type</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.addressType}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="evaradripApp.userAddress.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.fullName}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="evaradripApp.userAddress.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.phoneNumber}</dd>
          <dt>
            <span id="streetAddress">
              <Translate contentKey="evaradripApp.userAddress.streetAddress">Street Address</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.streetAddress}</dd>
          <dt>
            <span id="streetAddress2">
              <Translate contentKey="evaradripApp.userAddress.streetAddress2">Street Address 2</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.streetAddress2}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="evaradripApp.userAddress.city">City</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.city}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="evaradripApp.userAddress.state">State</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.state}</dd>
          <dt>
            <span id="zipCode">
              <Translate contentKey="evaradripApp.userAddress.zipCode">Zip Code</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.zipCode}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="evaradripApp.userAddress.country">Country</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.country}</dd>
          <dt>
            <span id="landmark">
              <Translate contentKey="evaradripApp.userAddress.landmark">Landmark</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.landmark}</dd>
          <dt>
            <span id="isDefault">
              <Translate contentKey="evaradripApp.userAddress.isDefault">Is Default</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.isDefault ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="evaradripApp.userAddress.user">User</Translate>
          </dt>
          <dd>{userAddressEntity.user ? userAddressEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-address" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-address/${userAddressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserAddressDetail;
