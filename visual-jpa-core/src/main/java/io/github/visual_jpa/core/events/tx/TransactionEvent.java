package io.github.visual_jpa.core.events.tx;

public class TransactionEvent {
    private final TransactionEventType type;
    private final String transactionName;
    private final String transactionId;
    private final boolean readOnly;
    private final Integer managedEntityCount;
    private final String propagation;
    private final Integer nestingLevel;
    private final Boolean newTransaction;
    private final Boolean hasSavepoint;
    private final Boolean transactionActive;
    private final String originMethod;
    private final String details;

    public TransactionEvent(
            TransactionEventType type,
            String transactionName,
            String transactionId,
            boolean readOnly,
            Integer managedEntityCount,
            String propagation,
            Integer nestingLevel,
            Boolean newTransaction,
            Boolean hasSavepoint,
            Boolean transactionActive,
            String originMethod,
            String details
    ) {
        this.type = type;
        this.transactionName = transactionName;
        this.transactionId = transactionId;
        this.readOnly = readOnly;
        this.managedEntityCount = managedEntityCount;
        this.propagation = propagation;
        this.nestingLevel = nestingLevel;
        this.newTransaction = newTransaction;
        this.hasSavepoint = hasSavepoint;
        this.transactionActive = transactionActive;
        this.originMethod = originMethod;
        this.details = details;
    }

    public TransactionEventType getType() {
        return type;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Integer getManagedEntityCount() {
        return managedEntityCount;
    }

    public String getPropagation() {
        return propagation;
    }

    public Integer getNestingLevel() {
        return nestingLevel;
    }

    public Boolean getNewTransaction() {
        return newTransaction;
    }

    public Boolean getHasSavepoint() {
        return hasSavepoint;
    }

    public Boolean getTransactionActive() {
        return transactionActive;
    }

    public String getOriginMethod() {
        return originMethod;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "TransactionEvent{" +
                "type=" + type +
                ", transactionName='" + transactionName + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", readOnly=" + readOnly +
                ", managedEntityCount=" + managedEntityCount +
                ", propagation='" + propagation + '\'' +
                ", nestingLevel=" + nestingLevel +
                ", newTransaction=" + newTransaction +
                ", hasSavepoint=" + hasSavepoint +
                ", transactionActive=" + transactionActive +
                ", originMethod='" + originMethod + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
