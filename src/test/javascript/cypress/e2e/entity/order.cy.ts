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

describe('Order e2e test', () => {
  const orderPageUrl = '/order';
  const orderPageUrlPattern = new RegExp('/order(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const orderSample = {"orderNumber":"yuck times disarm","status":"RETURNED","totalAmount":25430.45,"subtotalAmount":8121.83};

  let order;
  // let userAddress;
  // let userProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-addresses',
      body: {"addressType":"BOTH","fullName":"apud deform ectoderm","phoneNumber":"longingly","streetAddress":"nimble","streetAddress2":"joshingly","city":"Cape Coral","state":"unethically pinstripe glossy","zipCode":"02283-4567","country":"Andorra","landmark":"inject gadzooks before","isDefault":false},
    }).then(({ body }) => {
      userAddress = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"phoneNumber":"upward neglected","dateOfBirth":"2025-09-17","gender":"PREFER_NOT_TO_SAY","avatarUrl":"whereas downshift","loyaltyPoints":11478,"membershipLevel":"BRONZE","preferences":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","newsletterSubscribed":true},
    }).then(({ body }) => {
      userProfile = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/orders/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/payments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/shippings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-addresses', {
      statusCode: 200,
      body: [userAddress],
    });

    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

  });
   */

  afterEach(() => {
    if (order) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders/${order.id}`,
      }).then(() => {
        order = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (userAddress) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-addresses/${userAddress.id}`,
      }).then(() => {
        userAddress = undefined;
      });
    }
    if (userProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-profiles/${userProfile.id}`,
      }).then(() => {
        userProfile = undefined;
      });
    }
  });
   */

  it('Orders menu should load Orders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Order').should('exist');
    cy.url().should('match', orderPageUrlPattern);
  });

  describe('Order page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Order page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/order/new$'));
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/orders',
          body: {
            ...orderSample,
            shippingAddress: userAddress,
            billingAddress: userAddress,
            user: userProfile,
          },
        }).then(({ body }) => {
          order = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/orders?page=0&size=20>; rel="last",<http://localhost/api/orders?page=0&size=20>; rel="first"',
              },
              body: [order],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Order page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('order');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);
      });

      it('edit button click should load edit Order page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);
      });

      it('edit button click should load edit Order page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Order');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Order', () => {
        cy.intercept('GET', '/api/orders/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('order').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orderPageUrlPattern);

        order = undefined;
      });
    });
  });

  describe('new Order page', () => {
    beforeEach(() => {
      cy.visit(`${orderPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Order');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Order', () => {
      cy.get(`[data-cy="orderNumber"]`).type('upliftingly');
      cy.get(`[data-cy="orderNumber"]`).should('have.value', 'upliftingly');

      cy.get(`[data-cy="status"]`).select('DELIVERED');

      cy.get(`[data-cy="totalAmount"]`).type('23379.35');
      cy.get(`[data-cy="totalAmount"]`).should('have.value', '23379.35');

      cy.get(`[data-cy="subtotalAmount"]`).type('11285.95');
      cy.get(`[data-cy="subtotalAmount"]`).should('have.value', '11285.95');

      cy.get(`[data-cy="taxAmount"]`).type('18338.87');
      cy.get(`[data-cy="taxAmount"]`).should('have.value', '18338.87');

      cy.get(`[data-cy="shippingAmount"]`).type('7426.57');
      cy.get(`[data-cy="shippingAmount"]`).should('have.value', '7426.57');

      cy.get(`[data-cy="discountAmount"]`).type('13293.96');
      cy.get(`[data-cy="discountAmount"]`).should('have.value', '13293.96');

      cy.get(`[data-cy="paymentMethod"]`).select('UPI');

      cy.get(`[data-cy="paymentStatus"]`).select('REFUNDED');

      cy.get(`[data-cy="shippingMethod"]`).type('phew');
      cy.get(`[data-cy="shippingMethod"]`).should('have.value', 'phew');

      cy.get(`[data-cy="trackingNumber"]`).type('unless wire');
      cy.get(`[data-cy="trackingNumber"]`).should('have.value', 'unless wire');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="cancelReason"]`).type('sore abseil');
      cy.get(`[data-cy="cancelReason"]`).should('have.value', 'sore abseil');

      cy.get(`[data-cy="returnReason"]`).type('spook but');
      cy.get(`[data-cy="returnReason"]`).should('have.value', 'spook but');

      cy.get(`[data-cy="refundAmount"]`).type('17659.17');
      cy.get(`[data-cy="refundAmount"]`).should('have.value', '17659.17');

      cy.get(`[data-cy="estimatedDeliveryDate"]`).type('2025-09-17');
      cy.get(`[data-cy="estimatedDeliveryDate"]`).blur();
      cy.get(`[data-cy="estimatedDeliveryDate"]`).should('have.value', '2025-09-17');

      cy.get(`[data-cy="deliveredDate"]`).type('2025-09-17T14:00');
      cy.get(`[data-cy="deliveredDate"]`).blur();
      cy.get(`[data-cy="deliveredDate"]`).should('have.value', '2025-09-17T14:00');

      cy.get(`[data-cy="shippedDate"]`).type('2025-09-17T13:19');
      cy.get(`[data-cy="shippedDate"]`).blur();
      cy.get(`[data-cy="shippedDate"]`).should('have.value', '2025-09-17T13:19');

      cy.get(`[data-cy="shippingAddress"]`).select(1);
      cy.get(`[data-cy="billingAddress"]`).select(1);
      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        order = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', orderPageUrlPattern);
    });
  });
});
