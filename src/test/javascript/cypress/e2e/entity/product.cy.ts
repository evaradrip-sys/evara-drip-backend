import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Product e2e test', () => {
  const productPageUrl = '/product';
  const productPageUrlPattern = new RegExp('/product(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productSample = { name: 'infinite onset', price: 5330.15, stockCount: 3280 };

  let product;
  let category;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/categories',
      body: {
        name: 'ack',
        description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        imageUrl: 'outrageous',
        href: '/ufum',
        isFeatured: false,
        displayOrder: 10437,
      },
    }).then(({ body }) => {
      category = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/products/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/product-images', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-variants', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/reviews', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/inventories', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/promotions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/brands', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/categories', {
      statusCode: 200,
      body: [category],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
      });
    }
  });

  afterEach(() => {
    if (category) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/categories/${category.id}`,
      }).then(() => {
        category = undefined;
      });
    }
  });

  it('Products menu should load Products page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Product').should('exist');
    cy.url().should('match', productPageUrlPattern);
  });

  describe('Product page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Product page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product/new$'));
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/products',
          body: {
            ...productSample,
            category,
          },
        }).then(({ body }) => {
          product = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/products+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/products?page=0&size=20>; rel="last",<http://localhost/api/products?page=0&size=20>; rel="first"',
              },
              body: [product],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Product page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('product');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);
      });

      it('edit button click should load edit Product page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);
      });

      it('edit button click should load edit Product page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);
      });

      it('last delete button click should delete instance of Product', () => {
        cy.intercept('GET', '/api/products/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('product').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPageUrlPattern);

        product = undefined;
      });
    });
  });

  describe('new Product page', () => {
    beforeEach(() => {
      cy.visit(`${productPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Product');
    });

    it('should create an instance of Product', () => {
      cy.get(`[data-cy="name"]`).type('flu till');
      cy.get(`[data-cy="name"]`).should('have.value', 'flu till');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="price"]`).type('24052.48');
      cy.get(`[data-cy="price"]`).should('have.value', '24052.48');

      cy.get(`[data-cy="originalPrice"]`).type('25211.83');
      cy.get(`[data-cy="originalPrice"]`).should('have.value', '25211.83');

      cy.get(`[data-cy="sku"]`).type('whose');
      cy.get(`[data-cy="sku"]`).should('have.value', 'whose');

      cy.get(`[data-cy="isNew"]`).should('not.be.checked');
      cy.get(`[data-cy="isNew"]`).click();
      cy.get(`[data-cy="isNew"]`).should('be.checked');

      cy.get(`[data-cy="isOnSale"]`).should('not.be.checked');
      cy.get(`[data-cy="isOnSale"]`).click();
      cy.get(`[data-cy="isOnSale"]`).should('be.checked');

      cy.get(`[data-cy="rating"]`).type('2.83');
      cy.get(`[data-cy="rating"]`).should('have.value', '2.83');

      cy.get(`[data-cy="reviewsCount"]`).type('24604');
      cy.get(`[data-cy="reviewsCount"]`).should('have.value', '24604');

      cy.get(`[data-cy="stockCount"]`).type('12265');
      cy.get(`[data-cy="stockCount"]`).should('have.value', '12265');

      cy.get(`[data-cy="inStock"]`).should('not.be.checked');
      cy.get(`[data-cy="inStock"]`).click();
      cy.get(`[data-cy="inStock"]`).should('be.checked');

      cy.get(`[data-cy="features"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="features"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="metaTitle"]`).type('zowie um');
      cy.get(`[data-cy="metaTitle"]`).should('have.value', 'zowie um');

      cy.get(`[data-cy="metaDescription"]`).type('numeracy while disgorge');
      cy.get(`[data-cy="metaDescription"]`).should('have.value', 'numeracy while disgorge');

      cy.get(`[data-cy="metaKeywords"]`).type('porter behind thump');
      cy.get(`[data-cy="metaKeywords"]`).should('have.value', 'porter behind thump');

      cy.get(`[data-cy="status"]`).select('COMING_SOON');

      cy.get(`[data-cy="weight"]`).type('18487.13');
      cy.get(`[data-cy="weight"]`).should('have.value', '18487.13');

      cy.get(`[data-cy="dimensions"]`).type('swiftly amongst');
      cy.get(`[data-cy="dimensions"]`).should('have.value', 'swiftly amongst');

      cy.get(`[data-cy="category"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        product = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productPageUrlPattern);
    });
  });
});
