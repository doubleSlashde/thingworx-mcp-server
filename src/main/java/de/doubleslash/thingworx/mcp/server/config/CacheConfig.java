package de.doubleslash.thingworx.mcp.server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "app.cache.enabled", havingValue = "false")
public class CacheConfig {

    /**
     * A no-op CacheManager that effectively disables caching while keeping
     * the @Cacheable/@CacheEvict annotations harmless.
     */
    @Bean
    public CacheManager cacheManager() {
        return new NoOpCacheManager();
    }
}
