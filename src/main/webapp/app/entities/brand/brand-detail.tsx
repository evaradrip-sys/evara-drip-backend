import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './brand.reducer';

export const BrandDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const brandEntity = useAppSelector(state => state.brand.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="brandDetailsHeading">
          <Translate contentKey="evaradripApp.brand.detail.title">Brand</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{brandEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="evaradripApp.brand.name">Name</Translate>
            </span>
          </dt>
          <dd>{brandEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="evaradripApp.brand.description">Description</Translate>
            </span>
          </dt>
          <dd>{brandEntity.description}</dd>
          <dt>
            <span id="logoUrl">
              <Translate contentKey="evaradripApp.brand.logoUrl">Logo Url</Translate>
            </span>
          </dt>
          <dd>{brandEntity.logoUrl}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="evaradripApp.brand.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{brandEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/brand" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/brand/${brandEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BrandDetail;
