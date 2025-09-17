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

import { getEntities } from './order.reducer';

export const Order = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const orderList = useAppSelector(state => state.order.entities);
  const loading = useAppSelector(state => state.order.loading);
  const totalItems = useAppSelector(state => state.order.totalItems);

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
      <h2 id="order-heading" data-cy="OrderHeading">
        <Translate contentKey="evaradripApp.order.home.title">Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evaradripApp.order.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evaradripApp.order.home.createLabel">Create new Order</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {orderList && orderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="evaradripApp.order.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('orderNumber')}>
                  <Translate contentKey="evaradripApp.order.orderNumber">Order Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderNumber')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="evaradripApp.order.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('totalAmount')}>
                  <Translate contentKey="evaradripApp.order.totalAmount">Total Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalAmount')} />
                </th>
                <th className="hand" onClick={sort('subtotalAmount')}>
                  <Translate contentKey="evaradripApp.order.subtotalAmount">Subtotal Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subtotalAmount')} />
                </th>
                <th className="hand" onClick={sort('taxAmount')}>
                  <Translate contentKey="evaradripApp.order.taxAmount">Tax Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('taxAmount')} />
                </th>
                <th className="hand" onClick={sort('shippingAmount')}>
                  <Translate contentKey="evaradripApp.order.shippingAmount">Shipping Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shippingAmount')} />
                </th>
                <th className="hand" onClick={sort('discountAmount')}>
                  <Translate contentKey="evaradripApp.order.discountAmount">Discount Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountAmount')} />
                </th>
                <th className="hand" onClick={sort('paymentMethod')}>
                  <Translate contentKey="evaradripApp.order.paymentMethod">Payment Method</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentMethod')} />
                </th>
                <th className="hand" onClick={sort('paymentStatus')}>
                  <Translate contentKey="evaradripApp.order.paymentStatus">Payment Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentStatus')} />
                </th>
                <th className="hand" onClick={sort('shippingMethod')}>
                  <Translate contentKey="evaradripApp.order.shippingMethod">Shipping Method</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shippingMethod')} />
                </th>
                <th className="hand" onClick={sort('trackingNumber')}>
                  <Translate contentKey="evaradripApp.order.trackingNumber">Tracking Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('trackingNumber')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="evaradripApp.order.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th className="hand" onClick={sort('cancelReason')}>
                  <Translate contentKey="evaradripApp.order.cancelReason">Cancel Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cancelReason')} />
                </th>
                <th className="hand" onClick={sort('returnReason')}>
                  <Translate contentKey="evaradripApp.order.returnReason">Return Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('returnReason')} />
                </th>
                <th className="hand" onClick={sort('refundAmount')}>
                  <Translate contentKey="evaradripApp.order.refundAmount">Refund Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('refundAmount')} />
                </th>
                <th className="hand" onClick={sort('estimatedDeliveryDate')}>
                  <Translate contentKey="evaradripApp.order.estimatedDeliveryDate">Estimated Delivery Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estimatedDeliveryDate')} />
                </th>
                <th className="hand" onClick={sort('deliveredDate')}>
                  <Translate contentKey="evaradripApp.order.deliveredDate">Delivered Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deliveredDate')} />
                </th>
                <th className="hand" onClick={sort('shippedDate')}>
                  <Translate contentKey="evaradripApp.order.shippedDate">Shipped Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shippedDate')} />
                </th>
                <th>
                  <Translate contentKey="evaradripApp.order.shippingAddress">Shipping Address</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="evaradripApp.order.billingAddress">Billing Address</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="evaradripApp.order.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {orderList.map((order, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/order/${order.id}`} color="link" size="sm">
                      {order.id}
                    </Button>
                  </td>
                  <td>{order.orderNumber}</td>
                  <td>
                    <Translate contentKey={`evaradripApp.OrderStatus.${order.status}`} />
                  </td>
                  <td>{order.totalAmount}</td>
                  <td>{order.subtotalAmount}</td>
                  <td>{order.taxAmount}</td>
                  <td>{order.shippingAmount}</td>
                  <td>{order.discountAmount}</td>
                  <td>
                    <Translate contentKey={`evaradripApp.PaymentMethod.${order.paymentMethod}`} />
                  </td>
                  <td>
                    <Translate contentKey={`evaradripApp.PaymentStatus.${order.paymentStatus}`} />
                  </td>
                  <td>{order.shippingMethod}</td>
                  <td>{order.trackingNumber}</td>
                  <td>{order.notes}</td>
                  <td>{order.cancelReason}</td>
                  <td>{order.returnReason}</td>
                  <td>{order.refundAmount}</td>
                  <td>
                    {order.estimatedDeliveryDate ? (
                      <TextFormat type="date" value={order.estimatedDeliveryDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{order.deliveredDate ? <TextFormat type="date" value={order.deliveredDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{order.shippedDate ? <TextFormat type="date" value={order.shippedDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {order.shippingAddress ? <Link to={`/user-address/${order.shippingAddress.id}`}>{order.shippingAddress.id}</Link> : ''}
                  </td>
                  <td>
                    {order.billingAddress ? <Link to={`/user-address/${order.billingAddress.id}`}>{order.billingAddress.id}</Link> : ''}
                  </td>
                  <td>{order.user ? <Link to={`/user-profile/${order.user.id}`}>{order.user.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/order/${order.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/order/${order.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/order/${order.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="evaradripApp.order.home.notFound">No Orders found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={orderList && orderList.length > 0 ? '' : 'd-none'}>
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

export default Order;
