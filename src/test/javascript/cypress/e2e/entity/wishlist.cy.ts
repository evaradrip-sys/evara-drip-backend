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

describe('Wishlist e2e test', () => {
  const wishlistPageUrl = '/wishlist';
  const wishlistPageUrlPattern = new RegExp('/wishlist(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const wishlistSample = {};

  let wishlist;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/wishlists+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/wishlists').as('postEntityRequest');
    cy.intercept('DELETE', '/api/wishlists/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (wishlist) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/wishlists/${wishlist.id}`,
      }).then(() => {
        wishlist = undefined;
      });
    }
  });

  it('Wishlists menu should load Wishlists page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('wishlist');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Wishlist').should('exist');
    cy.url().should('match', wishlistPageUrlPattern);
  });

  describe('Wishlist page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(wishlistPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Wishlist page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/wishlist/new$'));
        cy.getEntityCreateUpdateHeading('Wishlist');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishlistPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/wishlists',
          body: wishlistSample,
        }).then(({ body }) => {
          wishlist = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/wishlists+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/wishlists?page=0&size=20>; rel="last",<http://localhost/api/wishlists?page=0&size=20>; rel="first"',
              },
              body: [wishlist],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(wishlistPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Wishlist page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('wishlist');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishlistPageUrlPattern);
      });

      it('edit button click should load edit Wishlist page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Wishlist');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishlistPageUrlPattern);
      });

      it('edit button click should load edit Wishlist page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Wishlist');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishlistPageUrlPattern);
      });

      it('last delete button click should delete instance of Wishlist', () => {
        cy.intercept('GET', '/api/wishlists/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('wishlist').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wishlistPageUrlPattern);

        wishlist = undefined;
      });
    });
  });

  describe('new Wishlist page', () => {
    beforeEach(() => {
      cy.visit(`${wishlistPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Wishlist');
    });

    it('should create an instance of Wishlist', () => {
      cy.get(`[data-cy="priority"]`).type('2');
      cy.get(`[data-cy="priority"]`).should('have.value', '2');

      cy.get(`[data-cy="notes"]`).type('impassioned tenderly');
      cy.get(`[data-cy="notes"]`).should('have.value', 'impassioned tenderly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        wishlist = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', wishlistPageUrlPattern);
    });
  });
});
