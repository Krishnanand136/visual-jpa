package io.github.visual_jpa.core.events.hibernate;

import io.github.visual_jpa.core.events.state.EntityStateChange;
import io.github.visual_jpa.core.context.ExecutionContext;
import io.github.visual_jpa.core.events.state.EntityStateChangeLogger;
import io.github.visual_jpa.core.events.state.EntityStateTransition;
import io.github.visual_jpa.core.events.state.TransactionIdResolver;
import org.hibernate.event.spi.EvictEvent;
import org.hibernate.event.spi.EvictEventListener;
import org.springframework.stereotype.Component;

@Component
public class VisualJpaStateEvictEventListener implements EvictEventListener {
    private final EntityStateChangeLogger logger;

    public VisualJpaStateEvictEventListener(EntityStateChangeLogger logger) {
        this.logger = logger;
    }

    @Override
    public void onEvict(EvictEvent event) {
        Object entity = event.getObject();
        String entityName = entity != null ? entity.getClass().getName() : "unknown";
        String txId = TransactionIdResolver.resolve(event.getSession());
        String originMethod = ExecutionContext.currentServiceMethod();
        String repositoryMethod = ExecutionContext.currentRepositoryMethod();
        logger.log(new EntityStateChange(
                EntityStateTransition.MANAGED_TO_DETACHED,
                entityName,
                null,
                txId,
                originMethod,
                repositoryMethod,
                "evict"
        ));
    }
}
