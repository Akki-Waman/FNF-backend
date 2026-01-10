package com.sipl.ticket.core.dao.entity;

import java.util.Map;

public class MasterContext {
    private final Map<Integer, String> priorityMap;
    private final Map<Integer, String> statusMap;

    public MasterContext(Map<Integer, String> priorityMap,
                         Map<Integer, String> statusMap) {
        this.priorityMap = priorityMap;
        this.statusMap = statusMap;
    }

    public String resolvePriority(Integer code) {
        return priorityMap.get(code);
    }

    public String resolveStatus(Integer code) {
        return statusMap.get(code);
    }

}
