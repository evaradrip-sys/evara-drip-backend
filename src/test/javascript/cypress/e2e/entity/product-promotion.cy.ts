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

describe('ProductPromotion e2e test', () => {
  const productPromotionPageUrl = '/product-promotion';
  const productPromotionPageUrlPattern = new RegExp('/product-promotion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productPromotionSample = {};

  let productPromotion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-promotions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-promotions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-promotions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productPromotion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-promotions/${productPromotion.id}`,
      }).then(() => {
        productPromotion = undefined;
      });
    }
  });

  it('ProductPromotions menu should load ProductPromotions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-promotion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductPromotion').should('exist');
    cy.url().should('match', productPromotionPageUrlPattern);
  });

  describe('ProductPromotion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productPromotionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductPromotion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-promotion/new$'));
        cy.getEntityCreateUpdateHeading('ProductPromotion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPromotionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-promotions',
          body: productPromotionSample,
        }).then(({ body }) => {
          productPromotion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-promotions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-promotions?page=0&size=20>; rel="last",<http://localhost/api/product-promotions?page=0&size=20>; rel="first"',
              },
              body: [productPromotion],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productPromotionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductPromotion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productPromotion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPromotionPageUrlPattern);
      });

      it('edit button click should load edit ProductPromotion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductPromotion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPromotionPageUrlPattern);
      });

      it('edit button click should load edit ProductPromotion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductPromotion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPromotionPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductPromotion', () => {
        cy.intercept('GET', '/api/product-promotions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productPromotion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productPromotionPageUrlPattern);

        productPromotion = undefined;
      });
    });
  });

  describe('new ProductPromotion page', () => {
    beforeEach(() => {
      cy.visit(`${productPromotionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductPromotion');
    });

    it('should create an instance of ProductPromotion', () => {
      cy.get(`[data-cy="priority"]`).type('13335');
      cy.get(`[data-cy="priority"]`).should('have.value', '13335');

      cy.get(`[data-cy="isExclusive"]`).should('not.be.checked');
      cy.get(`[data-cy="isExclusive"]`).click();
      cy.get(`[data-cy="isExclusive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productPromotion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productPromotionPageUrlPattern);
    });
  });
});
