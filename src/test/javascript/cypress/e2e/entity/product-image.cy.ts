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

describe('ProductImage e2e test', () => {
  const productImagePageUrl = '/product-image';
  const productImagePageUrlPattern = new RegExp('/product-image(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const productImageSample = {"imageUrl":"perfection gee"};

  let productImage;
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
      body: {"name":"mutate bustling alongside","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","price":8071.37,"originalPrice":22273.91,"sku":"voluntarily","isNew":true,"isOnSale":true,"rating":0.23,"reviewsCount":27205,"stockCount":2770,"inStock":true,"features":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","metaTitle":"showy","metaDescription":"moist","metaKeywords":"to via newsprint","status":"OUT_OF_STOCK","weight":18597.12,"dimensions":"finally drab that"},
    }).then(({ body }) => {
      product = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/product-images+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-images').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-images/*').as('deleteEntityRequest');
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
    if (productImage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-images/${productImage.id}`,
      }).then(() => {
        productImage = undefined;
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

  it('ProductImages menu should load ProductImages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-image');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductImage').should('exist');
    cy.url().should('match', productImagePageUrlPattern);
  });

  describe('ProductImage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productImagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductImage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-image/new$'));
        cy.getEntityCreateUpdateHeading('ProductImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-images',
          body: {
            ...productImageSample,
            product: product,
          },
        }).then(({ body }) => {
          productImage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-images+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-images?page=0&size=20>; rel="last",<http://localhost/api/product-images?page=0&size=20>; rel="first"',
              },
              body: [productImage],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(productImagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(productImagePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ProductImage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productImage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImagePageUrlPattern);
      });

      it('edit button click should load edit ProductImage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImagePageUrlPattern);
      });

      it('edit button click should load edit ProductImage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductImage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImagePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ProductImage', () => {
        cy.intercept('GET', '/api/product-images/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productImage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImagePageUrlPattern);

        productImage = undefined;
      });
    });
  });

  describe('new ProductImage page', () => {
    beforeEach(() => {
      cy.visit(`${productImagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductImage');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ProductImage', () => {
      cy.get(`[data-cy="imageUrl"]`).type('edge that');
      cy.get(`[data-cy="imageUrl"]`).should('have.value', 'edge that');

      cy.get(`[data-cy="altText"]`).type('flimsy');
      cy.get(`[data-cy="altText"]`).should('have.value', 'flimsy');

      cy.get(`[data-cy="isPrimary"]`).should('not.be.checked');
      cy.get(`[data-cy="isPrimary"]`).click();
      cy.get(`[data-cy="isPrimary"]`).should('be.checked');

      cy.get(`[data-cy="displayOrder"]`).type('6040');
      cy.get(`[data-cy="displayOrder"]`).should('have.value', '6040');

      cy.get(`[data-cy="product"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productImage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productImagePageUrlPattern);
    });
  });
});
