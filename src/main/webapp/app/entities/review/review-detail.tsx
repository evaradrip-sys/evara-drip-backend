import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './review.reducer';

export const ReviewDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reviewEntity = useAppSelector(state => state.review.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reviewDetailsHeading">
          <Translate contentKey="evaradripApp.review.detail.title">Review</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.id}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="evaradripApp.review.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.rating}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="evaradripApp.review.title">Title</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.title}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="evaradripApp.review.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.comment}</dd>
          <dt>
            <span id="helpfulCount">
              <Translate contentKey="evaradripApp.review.helpfulCount">Helpful Count</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.helpfulCount}</dd>
          <dt>
            <span id="notHelpfulCount">
              <Translate contentKey="evaradripApp.review.notHelpfulCount">Not Helpful Count</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.notHelpfulCount}</dd>
          <dt>
            <span id="verifiedPurchase">
              <Translate contentKey="evaradripApp.review.verifiedPurchase">Verified Purchase</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.verifiedPurchase ? 'true' : 'false'}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="evaradripApp.review.status">Status</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.status}</dd>
          <dt>
            <span id="response">
              <Translate contentKey="evaradripApp.review.response">Response</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.response}</dd>
          <dt>
            <span id="responseDate">
              <Translate contentKey="evaradripApp.review.responseDate">Response Date</Translate>
            </span>
          </dt>
          <dd>
            {reviewEntity.responseDate ? <TextFormat value={reviewEntity.responseDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="evaradripApp.review.user">User</Translate>
          </dt>
          <dd>{reviewEntity.user ? reviewEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="evaradripApp.review.product">Product</Translate>
          </dt>
          <dd>{reviewEntity.product ? reviewEntity.product.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/review" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/review/${reviewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReviewDetail;
