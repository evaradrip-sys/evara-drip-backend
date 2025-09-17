import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './product.reducer';

export const Product = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const productList = useAppSelector(state => state.product.entities);
  const loading = useAppSelector(state => state.product.loading);
  const totalItems = useAppSelector(state => state.product.totalItems);

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
      <h2 id="product-heading" data-cy="ProductHeading">
        <Translate contentKey="evaradripApp.product.home.title">Products</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evaradripApp.product.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evaradripApp.product.home.createLabel">Create new Product</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productList && productList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="evaradripApp.product.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="evaradripApp.product.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="evaradripApp.product.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="evaradripApp.product.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('originalPrice')}>
                  <Translate contentKey="evaradripApp.product.originalPrice">Original Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('originalPrice')} />
                </th>
                <th className="hand" onClick={sort('sku')}>
                  <Translate contentKey="evaradripApp.product.sku">Sku</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('sku')} />
                </th>
                <th className="hand" onClick={sort('isNew')}>
                  <Translate contentKey="evaradripApp.product.isNew">Is New</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isNew')} />
                </th>
                <th className="hand" onClick={sort('isOnSale')}>
                  <Translate contentKey="evaradripApp.product.isOnSale">Is On Sale</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isOnSale')} />
                </th>
                <th className="hand" onClick={sort('rating')}>
                  <Translate contentKey="evaradripApp.product.rating">Rating</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rating')} />
                </th>
                <th className="hand" onClick={sort('reviewsCount')}>
                  <Translate contentKey="evaradripApp.product.reviewsCount">Reviews Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reviewsCount')} />
                </th>
                <th className="hand" onClick={sort('stockCount')}>
                  <Translate contentKey="evaradripApp.product.stockCount">Stock Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stockCount')} />
                </th>
                <th className="hand" onClick={sort('inStock')}>
                  <Translate contentKey="evaradripApp.product.inStock">In Stock</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('inStock')} />
                </th>
                <th className="hand" onClick={sort('features')}>
                  <Translate contentKey="evaradripApp.product.features">Features</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('features')} />
                </th>
                <th className="hand" onClick={sort('metaTitle')}>
                  <Translate contentKey="evaradripApp.product.metaTitle">Meta Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('metaTitle')} />
                </th>
                <th className="hand" onClick={sort('metaDescription')}>
                  <Translate contentKey="evaradripApp.product.metaDescription">Meta Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('metaDescription')} />
                </th>
                <th className="hand" onClick={sort('metaKeywords')}>
                  <Translate contentKey="evaradripApp.product.metaKeywords">Meta Keywords</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('metaKeywords')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="evaradripApp.product.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('weight')}>
                  <Translate contentKey="evaradripApp.product.weight">Weight</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('weight')} />
                </th>
                <th className="hand" onClick={sort('dimensions')}>
                  <Translate contentKey="evaradripApp.product.dimensions">Dimensions</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dimensions')} />
                </th>
                <th>
                  <Translate contentKey="evaradripApp.product.brand">Brand</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="evaradripApp.product.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productList.map((product, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product/${product.id}`} color="link" size="sm">
                      {product.id}
                    </Button>
                  </td>
                  <td>{product.name}</td>
                  <td>{product.description}</td>
                  <td>{product.price}</td>
                  <td>{product.originalPrice}</td>
                  <td>{product.sku}</td>
                  <td>{product.isNew ? 'true' : 'false'}</td>
                  <td>{product.isOnSale ? 'true' : 'false'}</td>
                  <td>{product.rating}</td>
                  <td>{product.reviewsCount}</td>
                  <td>{product.stockCount}</td>
                  <td>{product.inStock ? 'true' : 'false'}</td>
                  <td>{product.features}</td>
                  <td>{product.metaTitle}</td>
                  <td>{product.metaDescription}</td>
                  <td>{product.metaKeywords}</td>
                  <td>
                    <Translate contentKey={`evaradripApp.ProductStatus.${product.status}`} />
                  </td>
                  <td>{product.weight}</td>
                  <td>{product.dimensions}</td>
                  <td>{product.brand ? <Link to={`/brand/${product.brand.id}`}>{product.brand.name}</Link> : ''}</td>
                  <td>{product.category ? <Link to={`/category/${product.category.id}`}>{product.category.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product/${product.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product/${product.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/product/${product.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="evaradripApp.product.home.notFound">No Products found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={productList && productList.length > 0 ? '' : 'd-none'}>
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

export default Product;
