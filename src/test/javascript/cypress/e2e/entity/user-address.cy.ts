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

describe('UserAddress e2e test', () => {
  const userAddressPageUrl = '/user-address';
  const userAddressPageUrlPattern = new RegExp('/user-address(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userAddressSample = {
    addressType: 'BOTH',
    fullName: 'zowie pish debit',
    phoneNumber: 'lumpy the tempting',
    streetAddress: 'sans crushing',
    city: 'Lake Estell',
    state: 'regal',
    zipCode: '07824-0100',
    country: 'Saint Vincent and the Grenadines',
  };

  let userAddress;
  let userProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {
        phoneNumber: 'chiffonier',
        dateOfBirth: '2025-09-17',
        gender: 'FEMALE',
        avatarUrl: 'remark',
        loyaltyPoints: 24522,
        membershipLevel: 'GOLD',
        preferences: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        newsletterSubscribed: false,
      },
    }).then(({ body }) => {
      userProfile = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-addresses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-addresses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-addresses/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });
  });

  afterEach(() => {
    if (userAddress) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-addresses/${userAddress.id}`,
      }).then(() => {
        userAddress = undefined;
      });
    }
  });

  afterEach(() => {
    if (userProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-profiles/${userProfile.id}`,
      }).then(() => {
        userProfile = undefined;
      });
    }
  });

  it('UserAddresses menu should load UserAddresses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-address');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserAddress').should('exist');
    cy.url().should('match', userAddressPageUrlPattern);
  });

  describe('UserAddress page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userAddressPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserAddress page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-address/new$'));
        cy.getEntityCreateUpdateHeading('UserAddress');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-addresses',
          body: {
            ...userAddressSample,
            user: userProfile,
          },
        }).then(({ body }) => {
          userAddress = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-addresses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-addresses?page=0&size=20>; rel="last",<http://localhost/api/user-addresses?page=0&size=20>; rel="first"',
              },
              body: [userAddress],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userAddressPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserAddress page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userAddress');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);
      });

      it('edit button click should load edit UserAddress page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAddress');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);
      });

      it('edit button click should load edit UserAddress page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAddress');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);
      });

      it('last delete button click should delete instance of UserAddress', () => {
        cy.intercept('GET', '/api/user-addresses/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userAddress').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);

        userAddress = undefined;
      });
    });
  });

  describe('new UserAddress page', () => {
    beforeEach(() => {
      cy.visit(`${userAddressPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserAddress');
    });

    it('should create an instance of UserAddress', () => {
      cy.get(`[data-cy="addressType"]`).select('BILLING');

      cy.get(`[data-cy="fullName"]`).type('until');
      cy.get(`[data-cy="fullName"]`).should('have.value', 'until');

      cy.get(`[data-cy="phoneNumber"]`).type('towards waft white');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'towards waft white');

      cy.get(`[data-cy="streetAddress"]`).type('longingly gah legitimize');
      cy.get(`[data-cy="streetAddress"]`).should('have.value', 'longingly gah legitimize');

      cy.get(`[data-cy="streetAddress2"]`).type('utter whoever descent');
      cy.get(`[data-cy="streetAddress2"]`).should('have.value', 'utter whoever descent');

      cy.get(`[data-cy="city"]`).type('Fort Fredafort');
      cy.get(`[data-cy="city"]`).should('have.value', 'Fort Fredafort');

      cy.get(`[data-cy="state"]`).type('however');
      cy.get(`[data-cy="state"]`).should('have.value', 'however');

      cy.get(`[data-cy="zipCode"]`).type('96422-4448');
      cy.get(`[data-cy="zipCode"]`).should('have.value', '96422-4448');

      cy.get(`[data-cy="country"]`).type('Ghana');
      cy.get(`[data-cy="country"]`).should('have.value', 'Ghana');

      cy.get(`[data-cy="landmark"]`).type('mostly fatal excitedly');
      cy.get(`[data-cy="landmark"]`).should('have.value', 'mostly fatal excitedly');

      cy.get(`[data-cy="isDefault"]`).should('not.be.checked');
      cy.get(`[data-cy="isDefault"]`).click();
      cy.get(`[data-cy="isDefault"]`).should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userAddress = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userAddressPageUrlPattern);
    });
  });
});
