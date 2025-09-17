package com.evaradrip.commerce.repository;

import com.evaradrip.commerce.domain.ProductPromotion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductPromotion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, Long>, JpaSpecificationExecutor<ProductPromotion> {}
