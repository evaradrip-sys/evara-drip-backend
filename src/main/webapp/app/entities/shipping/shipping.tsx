import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './shipping.reducer';

export const Shipping = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const shippingList = useAppSelector(state => state.shipping.entities);
  const loading = useAppSelector(state => state.shipping.loading);
  const totalItems = useAppSelector(state => state.shipping.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="shipping-heading" data-cy="ShippingHeading">
        <Translate contentKey="evaradripApp.shipping.home.title">Shippings</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evaradripApp.shipping.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/shipping/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evaradripApp.shipping.home.createLabel">Create new Shipping</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {shippingList && shippingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="evaradripApp.shipping.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('carrier')}>
                  <Translate contentKey="evaradripApp.shipping.carrier">Carrier</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('carrier')} />
                </th>
                <th className="hand" onClick={sort('trackingNumber')}>
                  <Translate contentKey="evaradripApp.shipping.trackingNumber">Tracking Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('trackingNumber')} />
                </th>
                <th className="hand" onClick={sort('estimatedDelivery')}>
                  <Translate contentKey="evaradripApp.shipping.estimatedDelivery">Estimated Delivery</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estimatedDelivery')} />
                </th>
                <th className="hand" onClick={sort('actualDelivery')}>
                  <Translate contentKey="evaradripApp.shipping.actualDelivery">Actual Delivery</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('actualDelivery')} />
                </th>
                <th className="hand" onClick={sort('shippingCost')}>
                  <Translate contentKey="evaradripApp.shipping.shippingCost">Shipping Cost</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shippingCost')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="evaradripApp.shipping.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="evaradripApp.shipping.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="evaradripApp.shipping.order">Order</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shippingList.map((shipping, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/shipping/${shipping.id}`} color="link" size="sm">
                      {shipping.id}
                    </Button>
                  </td>
                  <td>{shipping.carrier}</td>
                  <td>{shipping.trackingNumber}</td>
                  <td>
                    {shipping.estimatedDelivery ? (
                      <TextFormat type="date" value={shipping.estimatedDelivery} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {shipping.actualDelivery ? <TextFormat type="date" value={shipping.actualDelivery} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{shipping.shippingCost}</td>
                  <td>
                    <Translate contentKey={`evaradripApp.ShippingStatus.${shipping.status}`} />
                  </td>
                  <td>{shipping.notes}</td>
                  <td>{shipping.order ? <Link to={`/order/${shipping.order.id}`}>{shipping.order.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/shipping/${shipping.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/shipping/${shipping.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/shipping/${shipping.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="evaradripApp.shipping.home.notFound">No Shippings found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={shippingList && shippingList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Shipping;
