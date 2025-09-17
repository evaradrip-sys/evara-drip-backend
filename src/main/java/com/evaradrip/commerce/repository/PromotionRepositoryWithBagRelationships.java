package com.evaradrip.commerce.repository;

import com.evaradrip.commerce.domain.Promotion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PromotionRepositoryWithBagRelationships {
    Optional<Promotion> fetchBagRelationships(Optional<Promotion> promotion);

    List<Promotion> fetchBagRelationships(List<Promotion> promotions);

    Page<Promotion> fetchBagRelationships(Page<Promotion> promotions);
}
