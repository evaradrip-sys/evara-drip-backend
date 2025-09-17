import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './wishlist.reducer';

export const WishlistDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const wishlistEntity = useAppSelector(state => state.wishlist.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="wishlistDetailsHeading">
          <Translate contentKey="evaradripApp.wishlist.detail.title">Wishlist</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{wishlistEntity.id}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="evaradripApp.wishlist.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{wishlistEntity.priority}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="evaradripApp.wishlist.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{wishlistEntity.notes}</dd>
        </dl>
        <Button tag={Link} to="/wishlist" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/wishlist/${wishlistEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WishlistDetail;
