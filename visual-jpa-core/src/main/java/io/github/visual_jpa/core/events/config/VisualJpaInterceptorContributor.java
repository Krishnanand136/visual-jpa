package io.github.visual_jpa.core.events.config;

import io.github.visual_jpa.core.events.hibernate.VisualJpaEntityInterceptor;
import org.hibernate.cfg.SessionEventSettings;
import org.hibernate.service.spi.ServiceContributor;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class VisualJpaInterceptorContributor implements ServiceContributor {
    @Override
    public void contribute(StandardServiceRegistryBuilder serviceRegistryBuilder) {
        serviceRegistryBuilder.applySetting(
                SessionEventSettings.INTERCEPTOR,
                new VisualJpaEntityInterceptor()
        );
    }
}
