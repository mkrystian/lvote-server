package lvote.mprezes.student.agh.edu.pl.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.Voting.class.getName(), jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.Voting.class.getName() + ".votes", jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.VotingContent.class.getName(), jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.UserGroup.class.getName(), jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.Vote.class.getName(), jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.EncryptionData.class.getName(), jcacheConfiguration);
            cm.createCache(lvote.mprezes.student.agh.edu.pl.domain.UserGroup.class.getName() + ".members", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
