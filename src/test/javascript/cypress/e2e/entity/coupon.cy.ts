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

describe('Coupon e2e test', () => {
  const couponPageUrl = '/coupon';
  const couponPageUrlPattern = new RegExp('/coupon(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const couponSample = {
    code: 'although incidentally',
    discountType: 'FREE_SHIPPING',
    discountValue: 12639.62,
    validFrom: '2025-09-17T17:29:49.706Z',
    validUntil: '2025-09-17T09:00:10.799Z',
  };

  let coupon;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/coupons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/coupons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/coupons/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (coupon) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/coupons/${coupon.id}`,
      }).then(() => {
        coupon = undefined;
      });
    }
  });

  it('Coupons menu should load Coupons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('coupon');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Coupon').should('exist');
    cy.url().should('match', couponPageUrlPattern);
  });

  describe('Coupon page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(couponPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Coupon page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/coupon/new$'));
        cy.getEntityCreateUpdateHeading('Coupon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', couponPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/coupons',
          body: couponSample,
        }).then(({ body }) => {
          coupon = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/coupons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/coupons?page=0&size=20>; rel="last",<http://localhost/api/coupons?page=0&size=20>; rel="first"',
              },
              body: [coupon],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(couponPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Coupon page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('coupon');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', couponPageUrlPattern);
      });

      it('edit button click should load edit Coupon page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Coupon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', couponPageUrlPattern);
      });

      it('edit button click should load edit Coupon page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Coupon');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', couponPageUrlPattern);
      });

      it('last delete button click should delete instance of Coupon', () => {
        cy.intercept('GET', '/api/coupons/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('coupon').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', couponPageUrlPattern);

        coupon = undefined;
      });
    });
  });

  describe('new Coupon page', () => {
    beforeEach(() => {
      cy.visit(`${couponPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Coupon');
    });

    it('should create an instance of Coupon', () => {
      cy.get(`[data-cy="code"]`).type('tomorrow carefully');
      cy.get(`[data-cy="code"]`).should('have.value', 'tomorrow carefully');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="discountType"]`).select('BUY_X_GET_Y');

      cy.get(`[data-cy="discountValue"]`).type('15934.57');
      cy.get(`[data-cy="discountValue"]`).should('have.value', '15934.57');

      cy.get(`[data-cy="validFrom"]`).type('2025-09-17T10:50');
      cy.get(`[data-cy="validFrom"]`).blur();
      cy.get(`[data-cy="validFrom"]`).should('have.value', '2025-09-17T10:50');

      cy.get(`[data-cy="validUntil"]`).type('2025-09-17T03:42');
      cy.get(`[data-cy="validUntil"]`).blur();
      cy.get(`[data-cy="validUntil"]`).should('have.value', '2025-09-17T03:42');

      cy.get(`[data-cy="maxUses"]`).type('31676');
      cy.get(`[data-cy="maxUses"]`).should('have.value', '31676');

      cy.get(`[data-cy="currentUses"]`).type('14131');
      cy.get(`[data-cy="currentUses"]`).should('have.value', '14131');

      cy.get(`[data-cy="minOrderValue"]`).type('24580.7');
      cy.get(`[data-cy="minOrderValue"]`).should('have.value', '24580.7');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        coupon = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', couponPageUrlPattern);
    });
  });
});
