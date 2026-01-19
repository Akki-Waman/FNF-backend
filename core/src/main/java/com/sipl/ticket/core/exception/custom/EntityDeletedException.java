package com.sipl.ticket.core.exception.custom;

public class EntityDeletedException extends RuntimeException {

    public EntityDeletedException(String entityName) {
        super(entityName + " is deleted and cannot be modified");
    }

    public EntityDeletedException(String entityName, String identifier) {
        super(entityName + " '" + identifier + "' is deleted and cannot be modified");
    }

}

