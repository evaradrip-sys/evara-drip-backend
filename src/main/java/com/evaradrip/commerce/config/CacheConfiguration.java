package com.evaradrip.commerce.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.evaradrip.commerce.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.evaradrip.commerce.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.evaradrip.commerce.domain.User.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Authority.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.User.class.getName() + ".authorities");
            createCache(cm, com.evaradrip.commerce.domain.Brand.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Brand.class.getName() + ".products");
            createCache(cm, com.evaradrip.commerce.domain.Category.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Category.class.getName() + ".products");
            createCache(cm, com.evaradrip.commerce.domain.Category.class.getName() + ".subcategories");
            createCache(cm, com.evaradrip.commerce.domain.Category.class.getName() + ".featuredProducts");
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName() + ".images");
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName() + ".variants");
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName() + ".reviews");
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName() + ".inventories");
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName() + ".promotions");
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName() + ".wishlisteds");
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName() + ".applicablePromotions");
            createCache(cm, com.evaradrip.commerce.domain.Product.class.getName() + ".featuredInCategories");
            createCache(cm, com.evaradrip.commerce.domain.ProductImage.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.ProductVariant.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.UserProfile.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.UserProfile.class.getName() + ".addresses");
            createCache(cm, com.evaradrip.commerce.domain.UserProfile.class.getName() + ".orders");
            createCache(cm, com.evaradrip.commerce.domain.UserProfile.class.getName() + ".notifications");
            createCache(cm, com.evaradrip.commerce.domain.UserProfile.class.getName() + ".wishlists");
            createCache(cm, com.evaradrip.commerce.domain.UserProfile.class.getName() + ".coupons");
            createCache(cm, com.evaradrip.commerce.domain.UserAddress.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Cart.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Cart.class.getName() + ".items");
            createCache(cm, com.evaradrip.commerce.domain.CartItem.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Order.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Order.class.getName() + ".items");
            createCache(cm, com.evaradrip.commerce.domain.Order.class.getName() + ".payments");
            createCache(cm, com.evaradrip.commerce.domain.Order.class.getName() + ".shippings");
            createCache(cm, com.evaradrip.commerce.domain.OrderItem.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Wishlist.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Review.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Promotion.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Promotion.class.getName() + ".applicableProducts");
            createCache(cm, com.evaradrip.commerce.domain.Promotion.class.getName() + ".products");
            createCache(cm, com.evaradrip.commerce.domain.ProductPromotion.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Payment.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Shipping.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Inventory.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Coupon.class.getName());
            createCache(cm, com.evaradrip.commerce.domain.Coupon.class.getName() + ".users");
            createCache(cm, com.evaradrip.commerce.domain.Notification.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
