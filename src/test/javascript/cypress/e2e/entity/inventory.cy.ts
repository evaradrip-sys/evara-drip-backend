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

describe('Inventory e2e test', () => {
  const inventoryPageUrl = '/inventory';
  const inventoryPageUrlPattern = new RegExp('/inventory(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const inventorySample = {"quantity":27045};

  let inventory;
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
      body: {"name":"inasmuch","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","price":10423.54,"originalPrice":10655.99,"sku":"disconnection mmm","isNew":false,"isOnSale":true,"rating":4.59,"reviewsCount":7029,"stockCount":3741,"inStock":false,"features":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","metaTitle":"instead before","metaDescription":"when","metaKeywords":"petticoat sunbathe minus","status":"INACTIVE","weight":28939.3,"dimensions":"sleepily"},
    }).then(({ body }) => {
      product = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/inventories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventories/*').as('deleteEntityRequest');
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
    if (inventory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventories/${inventory.id}`,
      }).then(() => {
        inventory = undefined;
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

  it('Inventories menu should load Inventories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventory');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Inventory').should('exist');
    cy.url().should('match', inventoryPageUrlPattern);
  });

  describe('Inventory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Inventory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/inventory/new$'));
        cy.getEntityCreateUpdateHeading('Inventory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventories',
          body: {
            ...inventorySample,
            product: product,
          },
        }).then(({ body }) => {
          inventory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inventories?page=0&size=20>; rel="last",<http://localhost/api/inventories?page=0&size=20>; rel="first"',
              },
              body: [inventory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(inventoryPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Inventory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryPageUrlPattern);
      });

      it('edit button click should load edit Inventory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Inventory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryPageUrlPattern);
      });

      it('edit button click should load edit Inventory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Inventory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Inventory', () => {
        cy.intercept('GET', '/api/inventories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('inventory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventoryPageUrlPattern);

        inventory = undefined;
      });
    });
  });

  describe('new Inventory page', () => {
    beforeEach(() => {
      cy.visit(`${inventoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Inventory');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Inventory', () => {
      cy.get(`[data-cy="quantity"]`).type('14833');
      cy.get(`[data-cy="quantity"]`).should('have.value', '14833');

      cy.get(`[data-cy="reservedQuantity"]`).type('13985');
      cy.get(`[data-cy="reservedQuantity"]`).should('have.value', '13985');

      cy.get(`[data-cy="warehouse"]`).type('corny');
      cy.get(`[data-cy="warehouse"]`).should('have.value', 'corny');

      cy.get(`[data-cy="lastRestocked"]`).type('2025-09-17T12:44');
      cy.get(`[data-cy="lastRestocked"]`).blur();
      cy.get(`[data-cy="lastRestocked"]`).should('have.value', '2025-09-17T12:44');

      cy.get(`[data-cy="reorderLevel"]`).type('16046');
      cy.get(`[data-cy="reorderLevel"]`).should('have.value', '16046');

      cy.get(`[data-cy="reorderQuantity"]`).type('2789');
      cy.get(`[data-cy="reorderQuantity"]`).should('have.value', '2789');

      cy.get(`[data-cy="product"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', inventoryPageUrlPattern);
    });
  });
});
