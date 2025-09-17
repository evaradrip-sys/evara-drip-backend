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

describe('ProductVariant e2e test', () => {
  const productVariantPageUrl = '/product-variant';
  const productVariantPageUrlPattern = new RegExp('/product-variant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const productVariantSample = {"variantSize":"XL","color":"white","sku":"tall through","stockCount":20164};

  let productVariant;
  // let product;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {"name":"towards frilly shy","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","price":24987.4,"originalPrice":15214.35,"sku":"unearth stunning","isNew":false,"isOnSale":true,"rating":1.54,"reviewsCount":9275,"stockCount":12417,"inStock":true,"features":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","metaTitle":"what mmm","metaDescription":"baritone","metaKeywords":"where which","status":"DISCONTINUED","weight":20876.06,"dimensions":"fooey"},
    }).then(({ body }) => {
      product = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/product-variants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-variants').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-variants/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

  });
   */

  afterEach(() => {
    if (productVariant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-variants/${productVariant.id}`,
      }).then(() => {
        productVariant = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('ProductVariants menu should load ProductVariants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-variant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductVariant').should('exist');
    cy.url().should('match', productVariantPageUrlPattern);
  });

  describe('ProductVariant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productVariantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductVariant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-variant/new$'));
        cy.getEntityCreateUpdateHeading('ProductVariant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productVariantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-variants',
          body: {
            ...productVariantSample,
            product: product,
          },
        }).then(({ body }) => {
          productVariant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-variants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-variants?page=0&size=20>; rel="last",<http://localhost/api/product-variants?page=0&size=20>; rel="first"',
              },
              body: [productVariant],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(productVariantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(productVariantPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ProductVariant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productVariant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productVariantPageUrlPattern);
      });

      it('edit button click should load edit ProductVariant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductVariant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productVariantPageUrlPattern);
      });

      it('edit button click should load edit ProductVariant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductVariant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productVariantPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ProductVariant', () => {
        cy.intercept('GET', '/api/product-variants/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productVariant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productVariantPageUrlPattern);

        productVariant = undefined;
      });
    });
  });

  describe('new ProductVariant page', () => {
    beforeEach(() => {
      cy.visit(`${productVariantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductVariant');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ProductVariant', () => {
      cy.get(`[data-cy="variantSize"]`).select('XS');

      cy.get(`[data-cy="color"]`).type('blue');
      cy.get(`[data-cy="color"]`).should('have.value', 'blue');

      cy.get(`[data-cy="sku"]`).type('clueless');
      cy.get(`[data-cy="sku"]`).should('have.value', 'clueless');

      cy.get(`[data-cy="stockCount"]`).type('1837');
      cy.get(`[data-cy="stockCount"]`).should('have.value', '1837');

      cy.get(`[data-cy="priceAdjustment"]`).type('27760.64');
      cy.get(`[data-cy="priceAdjustment"]`).should('have.value', '27760.64');

      cy.get(`[data-cy="barcode"]`).type('which');
      cy.get(`[data-cy="barcode"]`).should('have.value', 'which');

      cy.get(`[data-cy="weight"]`).type('8937.9');
      cy.get(`[data-cy="weight"]`).should('have.value', '8937.9');

      cy.get(`[data-cy="product"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productVariant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productVariantPageUrlPattern);
    });
  });
});
