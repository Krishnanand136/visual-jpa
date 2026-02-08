package io.github.visual_jpa.core.events.hibernate;

import io.github.visual_jpa.core.events.state.EntityStateChange;
import io.github.visual_jpa.core.context.ExecutionContext;
import io.github.visual_jpa.core.events.state.EntityStateChangeLogger;
import io.github.visual_jpa.core.events.state.EntityStateTransition;
import io.github.visual_jpa.core.events.state.TransactionIdResolver;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.event.spi.FlushEntityEvent;
import org.hibernate.event.spi.FlushEntityEventListener;
import org.springframework.stereotype.Component;

@Component
public class VisualJpaStateFlushEntityEventListener implements FlushEntityEventListener {
    private final EntityStateChangeLogger logger;

    public VisualJpaStateFlushEntityEventListener(EntityStateChangeLogger logger) {
        this.logger = logger;
    }

    @Override
    public void onFlushEntity(FlushEntityEvent event) throws HibernateException {
        if (!event.hasDirtyProperties() && !event.hasDirtyCollection()) {
            return;
        }

        EntityEntry entry = event.getEntityEntry();
        Object entity = event.getEntity();
        String entityName = entry != null && entry.getEntityName() != null
                ? entry.getEntityName()
                : (entity != null ? entity.getClass().getName() : "unknown");

        Object entityId = entry != null ? entry.getId() : null;
        String txId = TransactionIdResolver.resolve(event.getSession());
        String originMethod = ExecutionContext.currentServiceMethod();
        String repositoryMethod = ExecutionContext.currentRepositoryMethod();

        logger.log(new EntityStateChange(
                EntityStateTransition.MANAGED_TO_DIRTY,
                entityName,
                entityId,
                txId,
                originMethod,
                repositoryMethod,
                "flush-dirty"
        ));
    }
}
