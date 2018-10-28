package com.baosong.supplyme.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.baosong.supplyme.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Project.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Project.class.getName() + ".demands", jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Material.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Material.class.getName() + ".availabilities", jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Material.class.getName() + ".demands", jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Supplier.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Supplier.class.getName() + ".names", jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.MaterialAvailability.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Demand.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.PurchaseOrder.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.PurchaseOrderLine.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.DeliveryNote.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.DeliveryNoteLine.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.Invoice.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.InvoiceLine.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.MutableProperties.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.DemandStatusChange.class.getName(), jcacheConfiguration);
            cm.createCache(com.baosong.supplyme.domain.MaterialCategory.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
