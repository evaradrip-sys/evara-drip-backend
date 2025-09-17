import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './product-variant.reducer';

export const ProductVariant = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const productVariantList = useAppSelector(state => state.productVariant.entities);
  const loading = useAppSelector(state => state.productVariant.loading);
  const totalItems = useAppSelector(state => state.productVariant.totalItems);

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
      <h2 id="product-variant-heading" data-cy="ProductVariantHeading">
        <Translate contentKey="evaradripApp.productVariant.home.title">Product Variants</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evaradripApp.productVariant.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-variant/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evaradripApp.productVariant.home.createLabel">Create new Product Variant</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productVariantList && productVariantList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="evaradripApp.productVariant.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('variantSize')}>
                  <Translate contentKey="evaradripApp.productVariant.variantSize">Variant Size</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('variantSize')} />
                </th>
                <th className="hand" onClick={sort('color')}>
                  <Translate contentKey="evaradripApp.productVariant.color">Color</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('color')} />
                </th>
                <th className="hand" onClick={sort('sku')}>
                  <Translate contentKey="evaradripApp.productVariant.sku">Sku</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sku')} />
                </th>
                <th className="hand" onClick={sort('stockCount')}>
                  <Translate contentKey="evaradripApp.productVariant.stockCount">Stock Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stockCount')} />
                </th>
                <th className="hand" onClick={sort('priceAdjustment')}>
                  <Translate contentKey="evaradripApp.productVariant.priceAdjustment">Price Adjustment</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('priceAdjustment')} />
                </th>
                <th className="hand" onClick={sort('barcode')}>
                  <Translate contentKey="evaradripApp.productVariant.barcode">Barcode</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('barcode')} />
                </th>
                <th className="hand" onClick={sort('weight')}>
                  <Translate contentKey="evaradripApp.productVariant.weight">Weight</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('weight')} />
                </th>
                <th>
                  <Translate contentKey="evaradripApp.productVariant.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productVariantList.map((productVariant, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-variant/${productVariant.id}`} color="link" size="sm">
                      {productVariant.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`evaradripApp.ClothingSize.${productVariant.variantSize}`} />
                  </td>
                  <td>{productVariant.color}</td>
                  <td>{productVariant.sku}</td>
                  <td>{productVariant.stockCount}</td>
                  <td>{productVariant.priceAdjustment}</td>
                  <td>{productVariant.barcode}</td>
                  <td>{productVariant.weight}</td>
                  <td>
                    {productVariant.product ? <Link to={`/product/${productVariant.product.id}`}>{productVariant.product.name}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product-variant/${productVariant.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-variant/${productVariant.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/product-variant/${productVariant.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="evaradripApp.productVariant.home.notFound">No Product Variants found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={productVariantList && productVariantList.length > 0 ? '' : 'd-none'}>
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

export default ProductVariant;
