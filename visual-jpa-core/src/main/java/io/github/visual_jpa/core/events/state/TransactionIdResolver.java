package io.github.visual_jpa.core.events.state;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

public final class TransactionIdResolver {

    private TransactionIdResolver() {
    }

    public static String resolve(SharedSessionContractImplementor session) {
        if (session == null) {
            return null;
        }
        Object id = session.getSessionIdentifier();
        return id != null ? id.toString() : null;
    }
}
