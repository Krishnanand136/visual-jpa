package io.github.visual_jpa.core.events.state;

public class EntityStateChange {
    private final EntityStateTransition transition;
    private final String entityName;
    private final Object entityId;
    private final String transactionId;
    private final String originMethod;
    private final String repositoryMethod;
    private final String details;

    public EntityStateChange(
            EntityStateTransition transition,
            String entityName,
            Object entityId,
            String transactionId,
            String originMethod,
            String repositoryMethod,
            String details
    ) {
        this.transition = transition;
        this.entityName = entityName;
        this.entityId = entityId;
        this.transactionId = transactionId;
        this.originMethod = originMethod;
        this.repositoryMethod = repositoryMethod;
        this.details = details;
    }

    public EntityStateTransition getTransition() {
        return transition;
    }

    public String getEntityName() {
        return entityName;
    }

    public Object getEntityId() {
        return entityId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOriginMethod() {
        return originMethod;
    }

    public String getRepositoryMethod() {
        return repositoryMethod;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "EntityStateChange{" +
                "transition=" + transition +
                ", entityName='" + entityName + '\'' +
                ", entityId=" + entityId +
                ", transactionId='" + transactionId + '\'' +
                ", originMethod='" + originMethod + '\'' +
                ", repositoryMethod='" + repositoryMethod + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
