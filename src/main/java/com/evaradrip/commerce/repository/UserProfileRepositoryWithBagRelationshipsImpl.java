package com.evaradrip.commerce.repository;

import com.evaradrip.commerce.domain.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UserProfileRepositoryWithBagRelationshipsImpl implements UserProfileRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String USERPROFILES_PARAMETER = "userProfiles";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserProfile> fetchBagRelationships(Optional<UserProfile> userProfile) {
        return userProfile.map(this::fetchWishlists).map(this::fetchCoupons);
    }

    @Override
    public Page<UserProfile> fetchBagRelationships(Page<UserProfile> userProfiles) {
        return new PageImpl<>(
            fetchBagRelationships(userProfiles.getContent()),
            userProfiles.getPageable(),
            userProfiles.getTotalElements()
        );
    }

    @Override
    public List<UserProfile> fetchBagRelationships(List<UserProfile> userProfiles) {
        return Optional.of(userProfiles).map(this::fetchWishlists).map(this::fetchCoupons).orElse(Collections.emptyList());
    }

    UserProfile fetchWishlists(UserProfile result) {
        return entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.wishlists where userProfile.id = :id",
                UserProfile.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserProfile> fetchWishlists(List<UserProfile> userProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userProfiles.size()).forEach(index -> order.put(userProfiles.get(index).getId(), index));
        List<UserProfile> result = entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.wishlists where userProfile in :userProfiles",
                UserProfile.class
            )
            .setParameter(USERPROFILES_PARAMETER, userProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    UserProfile fetchCoupons(UserProfile result) {
        return entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.coupons where userProfile.id = :id",
                UserProfile.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserProfile> fetchCoupons(List<UserProfile> userProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userProfiles.size()).forEach(index -> order.put(userProfiles.get(index).getId(), index));
        List<UserProfile> result = entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.coupons where userProfile in :userProfiles",
                UserProfile.class
            )
            .setParameter(USERPROFILES_PARAMETER, userProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
