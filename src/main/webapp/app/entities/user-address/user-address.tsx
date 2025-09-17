import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-address.reducer';

export const UserAddress = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userAddressList = useAppSelector(state => state.userAddress.entities);
  const loading = useAppSelector(state => state.userAddress.loading);
  const totalItems = useAppSelector(state => state.userAddress.totalItems);

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
      <h2 id="user-address-heading" data-cy="UserAddressHeading">
        <Translate contentKey="evaradripApp.userAddress.home.title">User Addresses</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evaradripApp.userAddress.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-address/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evaradripApp.userAddress.home.createLabel">Create new User Address</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userAddressList && userAddressList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="evaradripApp.userAddress.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('addressType')}>
                  <Translate contentKey="evaradripApp.userAddress.addressType">Address Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addressType')} />
                </th>
                <th className="hand" onClick={sort('fullName')}>
                  <Translate contentKey="evaradripApp.userAddress.fullName">Full Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fullName')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="evaradripApp.userAddress.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('streetAddress')}>
                  <Translate contentKey="evaradripApp.userAddress.streetAddress">Street Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('streetAddress')} />
                </th>
                <th className="hand" onClick={sort('streetAddress2')}>
                  <Translate contentKey="evaradripApp.userAddress.streetAddress2">Street Address 2</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('streetAddress2')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="evaradripApp.userAddress.city">City</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('state')}>
                  <Translate contentKey="evaradripApp.userAddress.state">State</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('state')} />
                </th>
                <th className="hand" onClick={sort('zipCode')}>
                  <Translate contentKey="evaradripApp.userAddress.zipCode">Zip Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('zipCode')} />
                </th>
                <th className="hand" onClick={sort('country')}>
                  <Translate contentKey="evaradripApp.userAddress.country">Country</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('country')} />
                </th>
                <th className="hand" onClick={sort('landmark')}>
                  <Translate contentKey="evaradripApp.userAddress.landmark">Landmark</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('landmark')} />
                </th>
                <th className="hand" onClick={sort('isDefault')}>
                  <Translate contentKey="evaradripApp.userAddress.isDefault">Is Default</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDefault')} />
                </th>
                <th>
                  <Translate contentKey="evaradripApp.userAddress.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userAddressList.map((userAddress, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-address/${userAddress.id}`} color="link" size="sm">
                      {userAddress.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`evaradripApp.AddressType.${userAddress.addressType}`} />
                  </td>
                  <td>{userAddress.fullName}</td>
                  <td>{userAddress.phoneNumber}</td>
                  <td>{userAddress.streetAddress}</td>
                  <td>{userAddress.streetAddress2}</td>
                  <td>{userAddress.city}</td>
                  <td>{userAddress.state}</td>
                  <td>{userAddress.zipCode}</td>
                  <td>{userAddress.country}</td>
                  <td>{userAddress.landmark}</td>
                  <td>{userAddress.isDefault ? 'true' : 'false'}</td>
                  <td>{userAddress.user ? <Link to={`/user-profile/${userAddress.user.id}`}>{userAddress.user.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-address/${userAddress.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-address/${userAddress.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/user-address/${userAddress.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="evaradripApp.userAddress.home.notFound">No User Addresses found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userAddressList && userAddressList.length > 0 ? '' : 'd-none'}>
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

export default UserAddress;
