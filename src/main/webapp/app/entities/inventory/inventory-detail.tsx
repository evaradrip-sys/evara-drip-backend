import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './inventory.reducer';

export const InventoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const inventoryEntity = useAppSelector(state => state.inventory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inventoryDetailsHeading">
          <Translate contentKey="evaradripApp.inventory.detail.title">Inventory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="evaradripApp.inventory.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.quantity}</dd>
          <dt>
            <span id="reservedQuantity">
              <Translate contentKey="evaradripApp.inventory.reservedQuantity">Reserved Quantity</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.reservedQuantity}</dd>
          <dt>
            <span id="warehouse">
              <Translate contentKey="evaradripApp.inventory.warehouse">Warehouse</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.warehouse}</dd>
          <dt>
            <span id="lastRestocked">
              <Translate contentKey="evaradripApp.inventory.lastRestocked">Last Restocked</Translate>
            </span>
          </dt>
          <dd>
            {inventoryEntity.lastRestocked ? (
              <TextFormat value={inventoryEntity.lastRestocked} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="reorderLevel">
              <Translate contentKey="evaradripApp.inventory.reorderLevel">Reorder Level</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.reorderLevel}</dd>
          <dt>
            <span id="reorderQuantity">
              <Translate contentKey="evaradripApp.inventory.reorderQuantity">Reorder Quantity</Translate>
            </span>
          </dt>
          <dd>{inventoryEntity.reorderQuantity}</dd>
          <dt>
            <Translate contentKey="evaradripApp.inventory.product">Product</Translate>
          </dt>
          <dd>{inventoryEntity.product ? inventoryEntity.product.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/inventory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inventory/${inventoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InventoryDetail;
