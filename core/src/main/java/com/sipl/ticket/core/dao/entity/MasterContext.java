package com.sipl.ticket.core.dao.entity;

import java.util.Map;

public class MasterContext {
    private final Map<Integer, String> priorityMap;
    private final Map<Integer, String> statusMap;
    private final Map<Integer, String> channelMap;
    private final Map<Integer, String> recurrenceMap;

    public MasterContext(Map<Integer, String> priorityMap,
                         Map<Integer, String> statusMap,
                         Map<Integer, String> channelMap,
                         Map<Integer, String> recurrenceMap) {
        this.priorityMap = priorityMap;
        this.statusMap = statusMap;
        this.channelMap = channelMap;
        this.recurrenceMap = recurrenceMap;
    }

    public String resolvePriority(Integer code) {
        return priorityMap.get(code);
    }

    public String resolveStatus(Integer code) {
        return statusMap.get(code);
    }

    public String resolveChannel(Integer code) {
        return channelMap.get(code);
    }

    public String resolveRecurrence(Integer code) {
        return recurrenceMap != null ? recurrenceMap.get(code) : null;
    }

}
