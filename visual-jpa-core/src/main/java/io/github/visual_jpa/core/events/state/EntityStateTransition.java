package io.github.visual_jpa.core.events.state;

public enum EntityStateTransition {
    TRANSIENT_TO_MANAGED,
    DETACHED_TO_MANAGED,
    MANAGED_TO_MANAGED,
    MANAGED_TO_DIRTY,
    MANAGED_TO_REMOVED,
    MANAGED_TO_DETACHED,
    LOADED_TO_MANAGED
}
