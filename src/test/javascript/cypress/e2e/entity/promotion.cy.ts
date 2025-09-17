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

describe('Promotion e2e test', () => {
  const promotionPageUrl = '/promotion';
  const promotionPageUrlPattern = new RegExp('/promotion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const promotionSample = {
    name: 'hm hunt confusion',
    discountType: 'PERCENTAGE',
    discountValue: 20956.08,
    startDate: '2025-09-17T11:17:42.208Z',
    endDate: '2025-09-16T23:31:23.795Z',
  };

  let promotion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/promotions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/promotions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/promotions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (promotion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/promotions/${promotion.id}`,
      }).then(() => {
        promotion = undefined;
      });
    }
  });

  it('Promotions menu should load Promotions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('promotion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Promotion').should('exist');
    cy.url().should('match', promotionPageUrlPattern);
  });

  describe('Promotion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(promotionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Promotion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/promotion/new$'));
        cy.getEntityCreateUpdateHeading('Promotion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', promotionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/promotions',
          body: promotionSample,
        }).then(({ body }) => {
          promotion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/promotions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/promotions?page=0&size=20>; rel="last",<http://localhost/api/promotions?page=0&size=20>; rel="first"',
              },
              body: [promotion],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(promotionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Promotion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('promotion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', promotionPageUrlPattern);
      });

      it('edit button click should load edit Promotion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Promotion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', promotionPageUrlPattern);
      });

      it('edit button click should load edit Promotion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Promotion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', promotionPageUrlPattern);
      });

      it('last delete button click should delete instance of Promotion', () => {
        cy.intercept('GET', '/api/promotions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('promotion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', promotionPageUrlPattern);

        promotion = undefined;
      });
    });
  });

  describe('new Promotion page', () => {
    beforeEach(() => {
      cy.visit(`${promotionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Promotion');
    });

    it('should create an instance of Promotion', () => {
      cy.get(`[data-cy="name"]`).type('ah strong punctually');
      cy.get(`[data-cy="name"]`).should('have.value', 'ah strong punctually');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="promoCode"]`).type('absent tidy');
      cy.get(`[data-cy="promoCode"]`).should('have.value', 'absent tidy');

      cy.get(`[data-cy="discountType"]`).select('BUY_X_GET_Y');

      cy.get(`[data-cy="discountValue"]`).type('19036.96');
      cy.get(`[data-cy="discountValue"]`).should('have.value', '19036.96');

      cy.get(`[data-cy="minPurchaseAmount"]`).type('18956.12');
      cy.get(`[data-cy="minPurchaseAmount"]`).should('have.value', '18956.12');

      cy.get(`[data-cy="maxDiscountAmount"]`).type('15990.7');
      cy.get(`[data-cy="maxDiscountAmount"]`).should('have.value', '15990.7');

      cy.get(`[data-cy="startDate"]`).type('2025-09-17T16:38');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2025-09-17T16:38');

      cy.get(`[data-cy="endDate"]`).type('2025-09-17T01:01');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-09-17T01:01');

      cy.get(`[data-cy="usageLimit"]`).type('29448');
      cy.get(`[data-cy="usageLimit"]`).should('have.value', '29448');

      cy.get(`[data-cy="usageCount"]`).type('25939');
      cy.get(`[data-cy="usageCount"]`).should('have.value', '25939');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="applicableCategories"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="applicableCategories"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="excludedProducts"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="excludedProducts"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="termsAndConditions"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="termsAndConditions"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        promotion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', promotionPageUrlPattern);
    });
  });
});
