package io.github.visual_jpa.core.events.tx;

public interface TransactionEventLogger {
    void log(TransactionEvent event);
}
