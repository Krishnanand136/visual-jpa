package io.github.visual_jpa.core.events.hibernate;

import io.github.visual_jpa.core.events.state.EntityStateChange;
import io.github.visual_jpa.core.context.ExecutionContext;
import io.github.visual_jpa.core.events.state.EntityStateChangeLogger;
import io.github.visual_jpa.core.events.state.EntityStateTransition;
import io.github.visual_jpa.core.events.state.TransactionIdResolver;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeContext;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.springframework.stereotype.Component;

@Component
public class VisualJpaStateMergeEventListener implements MergeEventListener {
    private final EntityStateChangeLogger logger;

    public VisualJpaStateMergeEventListener(EntityStateChangeLogger logger) {
        this.logger = logger;
    }

    @Override
    public void onMerge(MergeEvent event) throws HibernateException {
        logMerge(event);
    }

    @Override
    public void onMerge(MergeEvent event, MergeContext mergeContext) throws HibernateException {
        logMerge(event);
    }

    private void logMerge(MergeEvent event) {
        Object entity = event.getOriginal() != null ? event.getOriginal() : event.getEntity();
        String entityName = entity != null ? entity.getClass().getName() : event.getEntityName();
        String txId = TransactionIdResolver.resolve(event.getSession());
        String originMethod = ExecutionContext.currentServiceMethod();
        String repositoryMethod = ExecutionContext.currentRepositoryMethod();
        boolean isManaged = event.getSession() != null && entity != null && event.getSession().contains(entity);
        EntityStateTransition transition = isManaged
                ? EntityStateTransition.MANAGED_TO_MANAGED
                : EntityStateTransition.DETACHED_TO_MANAGED;
        logger.log(new EntityStateChange(
                transition,
                entityName,
                event.getRequestedId(),
                txId,
                originMethod,
                repositoryMethod,
                "merge"
        ));
    }
}
