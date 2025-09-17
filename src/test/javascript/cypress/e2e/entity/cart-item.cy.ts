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

describe('CartItem e2e test', () => {
  const cartItemPageUrl = '/cart-item';
  const cartItemPageUrlPattern = new RegExp('/cart-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const cartItemSample = {"quantity":31284,"addedPrice":13755.8};

  let cartItem;
  // let product;
  // let cart;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {"name":"galvanize definite whoa","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","price":25704.64,"originalPrice":13644.61,"sku":"bleakly past","isNew":false,"isOnSale":false,"rating":0.23,"reviewsCount":20553,"stockCount":39,"inStock":true,"features":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","metaTitle":"questionably gadzooks never","metaDescription":"pfft","metaKeywords":"famously hm aha","status":"OUT_OF_STOCK","weight":6682.86,"dimensions":"slump with vainly"},
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/carts',
      body: {"sessionId":"fall","status":"ABANDONED","expiresAt":"2025-09-17T01:30:06.284Z"},
    }).then(({ body }) => {
      cart = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/cart-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cart-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cart-items/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/product-variants', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/carts', {
      statusCode: 200,
      body: [cart],
    });

  });
   */

  afterEach(() => {
    if (cartItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cart-items/${cartItem.id}`,
      }).then(() => {
        cartItem = undefined;
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
    if (cart) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/carts/${cart.id}`,
      }).then(() => {
        cart = undefined;
      });
    }
  });
   */

  it('CartItems menu should load CartItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cart-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CartItem').should('exist');
    cy.url().should('match', cartItemPageUrlPattern);
  });

  describe('CartItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cartItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CartItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cart-item/new$'));
        cy.getEntityCreateUpdateHeading('CartItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cartItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cart-items',
          body: {
            ...cartItemSample,
            product: product,
            cart: cart,
          },
        }).then(({ body }) => {
          cartItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cart-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cart-items?page=0&size=20>; rel="last",<http://localhost/api/cart-items?page=0&size=20>; rel="first"',
              },
              body: [cartItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cartItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(cartItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CartItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cartItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cartItemPageUrlPattern);
      });

      it('edit button click should load edit CartItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CartItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cartItemPageUrlPattern);
      });

      it('edit button click should load edit CartItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CartItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cartItemPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of CartItem', () => {
        cy.intercept('GET', '/api/cart-items/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cartItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cartItemPageUrlPattern);

        cartItem = undefined;
      });
    });
  });

  describe('new CartItem page', () => {
    beforeEach(() => {
      cy.visit(`${cartItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CartItem');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of CartItem', () => {
      cy.get(`[data-cy="quantity"]`).type('20667');
      cy.get(`[data-cy="quantity"]`).should('have.value', '20667');

      cy.get(`[data-cy="addedPrice"]`).type('23420.83');
      cy.get(`[data-cy="addedPrice"]`).should('have.value', '23420.83');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="cart"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cartItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', cartItemPageUrlPattern);
    });
  });
});
