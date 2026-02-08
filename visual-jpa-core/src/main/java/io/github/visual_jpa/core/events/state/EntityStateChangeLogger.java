package io.github.visual_jpa.core.events.state;

public interface EntityStateChangeLogger {
    void log(EntityStateChange change);
}
