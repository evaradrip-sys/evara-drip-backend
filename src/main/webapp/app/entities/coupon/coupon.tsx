import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './coupon.reducer';

export const Coupon = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const couponList = useAppSelector(state => state.coupon.entities);
  const loading = useAppSelector(state => state.coupon.loading);
  const totalItems = useAppSelector(state => state.coupon.totalItems);

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
      <h2 id="coupon-heading" data-cy="CouponHeading">
        <Translate contentKey="evaradripApp.coupon.home.title">Coupons</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evaradripApp.coupon.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/coupon/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evaradripApp.coupon.home.createLabel">Create new Coupon</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {couponList && couponList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="evaradripApp.coupon.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="evaradripApp.coupon.code">Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="evaradripApp.coupon.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('discountType')}>
                  <Translate contentKey="evaradripApp.coupon.discountType">Discount Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountType')} />
                </th>
                <th className="hand" onClick={sort('discountValue')}>
                  <Translate contentKey="evaradripApp.coupon.discountValue">Discount Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountValue')} />
                </th>
                <th className="hand" onClick={sort('validFrom')}>
                  <Translate contentKey="evaradripApp.coupon.validFrom">Valid From</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('validFrom')} />
                </th>
                <th className="hand" onClick={sort('validUntil')}>
                  <Translate contentKey="evaradripApp.coupon.validUntil">Valid Until</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('validUntil')} />
                </th>
                <th className="hand" onClick={sort('maxUses')}>
                  <Translate contentKey="evaradripApp.coupon.maxUses">Max Uses</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('maxUses')} />
                </th>
                <th className="hand" onClick={sort('currentUses')}>
                  <Translate contentKey="evaradripApp.coupon.currentUses">Current Uses</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currentUses')} />
                </th>
                <th className="hand" onClick={sort('minOrderValue')}>
                  <Translate contentKey="evaradripApp.coupon.minOrderValue">Min Order Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('minOrderValue')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="evaradripApp.coupon.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {couponList.map((coupon, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/coupon/${coupon.id}`} color="link" size="sm">
                      {coupon.id}
                    </Button>
                  </td>
                  <td>{coupon.code}</td>
                  <td>{coupon.description}</td>
                  <td>
                    <Translate contentKey={`evaradripApp.DiscountType.${coupon.discountType}`} />
                  </td>
                  <td>{coupon.discountValue}</td>
                  <td>{coupon.validFrom ? <TextFormat type="date" value={coupon.validFrom} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{coupon.validUntil ? <TextFormat type="date" value={coupon.validUntil} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{coupon.maxUses}</td>
                  <td>{coupon.currentUses}</td>
                  <td>{coupon.minOrderValue}</td>
                  <td>{coupon.isActive ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/coupon/${coupon.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/coupon/${coupon.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/coupon/${coupon.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="evaradripApp.coupon.home.notFound">No Coupons found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={couponList && couponList.length > 0 ? '' : 'd-none'}>
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

export default Coupon;
