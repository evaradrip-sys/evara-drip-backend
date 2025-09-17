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

describe('Shipping e2e test', () => {
  const shippingPageUrl = '/shipping';
  const shippingPageUrlPattern = new RegExp('/shipping(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const shippingSample = { carrier: 'among' };

  let shipping;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shippings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shippings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shippings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shipping) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shippings/${shipping.id}`,
      }).then(() => {
        shipping = undefined;
      });
    }
  });

  it('Shippings menu should load Shippings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shipping');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Shipping').should('exist');
    cy.url().should('match', shippingPageUrlPattern);
  });

  describe('Shipping page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shippingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Shipping page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/shipping/new$'));
        cy.getEntityCreateUpdateHeading('Shipping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shippings',
          body: shippingSample,
        }).then(({ body }) => {
          shipping = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shippings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shippings?page=0&size=20>; rel="last",<http://localhost/api/shippings?page=0&size=20>; rel="first"',
              },
              body: [shipping],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shippingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Shipping page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shipping');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingPageUrlPattern);
      });

      it('edit button click should load edit Shipping page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Shipping');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingPageUrlPattern);
      });

      it('edit button click should load edit Shipping page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Shipping');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingPageUrlPattern);
      });

      it('last delete button click should delete instance of Shipping', () => {
        cy.intercept('GET', '/api/shippings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('shipping').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', shippingPageUrlPattern);

        shipping = undefined;
      });
    });
  });

  describe('new Shipping page', () => {
    beforeEach(() => {
      cy.visit(`${shippingPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Shipping');
    });

    it('should create an instance of Shipping', () => {
      cy.get(`[data-cy="carrier"]`).type('shyly um though');
      cy.get(`[data-cy="carrier"]`).should('have.value', 'shyly um though');

      cy.get(`[data-cy="trackingNumber"]`).type('gut');
      cy.get(`[data-cy="trackingNumber"]`).should('have.value', 'gut');

      cy.get(`[data-cy="estimatedDelivery"]`).type('2025-09-17');
      cy.get(`[data-cy="estimatedDelivery"]`).blur();
      cy.get(`[data-cy="estimatedDelivery"]`).should('have.value', '2025-09-17');

      cy.get(`[data-cy="actualDelivery"]`).type('2025-09-17T09:11');
      cy.get(`[data-cy="actualDelivery"]`).blur();
      cy.get(`[data-cy="actualDelivery"]`).should('have.value', '2025-09-17T09:11');

      cy.get(`[data-cy="shippingCost"]`).type('6686.64');
      cy.get(`[data-cy="shippingCost"]`).should('have.value', '6686.64');

      cy.get(`[data-cy="status"]`).select('OUT_FOR_DELIVERY');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shipping = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', shippingPageUrlPattern);
    });
  });
});
