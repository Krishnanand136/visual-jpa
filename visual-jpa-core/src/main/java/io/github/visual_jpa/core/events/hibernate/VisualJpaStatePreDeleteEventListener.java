package io.github.visual_jpa.core.events.hibernate;

import io.github.visual_jpa.core.events.state.EntityStateChange;
import io.github.visual_jpa.core.context.ExecutionContext;
import io.github.visual_jpa.core.events.state.EntityStateChangeLogger;
import io.github.visual_jpa.core.events.state.EntityStateTransition;
import io.github.visual_jpa.core.events.state.TransactionIdResolver;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.springframework.stereotype.Component;

@Component
public class VisualJpaStatePreDeleteEventListener implements PreDeleteEventListener {
    private final EntityStateChangeLogger logger;

    public VisualJpaStatePreDeleteEventListener(EntityStateChangeLogger logger) {
        this.logger = logger;
    }

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        Object entity = event.getEntity();
        String entityName = entity != null ? entity.getClass().getName() : "unknown";
        String txId = TransactionIdResolver.resolve(event.getSession());
        String originMethod = ExecutionContext.currentServiceMethod();
        String repositoryMethod = ExecutionContext.currentRepositoryMethod();
        logger.log(new EntityStateChange(
                EntityStateTransition.MANAGED_TO_REMOVED,
                entityName,
                event.getId(),
                txId,
                originMethod,
                repositoryMethod,
                "pre-delete"
        ));
        return false;
    }
}
