package io.github.visual_jpa.core.events.tx;

public enum TransactionEventType {
    BEGIN,
    SCOPE_BEGIN,
    SCOPE_END,
    BEFORE_COMMIT,
    COMMIT,
    ROLLBACK
}
