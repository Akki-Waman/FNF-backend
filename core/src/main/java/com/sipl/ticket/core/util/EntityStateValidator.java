package com.sipl.ticket.core.util;

import com.sipl.ticket.core.exception.custom.EntityDeletedException;

public final class EntityStateValidator {

    private EntityStateValidator() {}

    public static void checkNotDeleted(Boolean isDeleted, String entityName) {
        if (Boolean.TRUE.equals(isDeleted)) {
            throw new EntityDeletedException(entityName);
        }
    }

    public static void checkNotDeleted(
            Boolean isDeleted,
            String entityName,
            String identifier
    ) {
        if (Boolean.TRUE.equals(isDeleted)) {
            throw new EntityDeletedException(entityName, identifier);
        }
    }
}

