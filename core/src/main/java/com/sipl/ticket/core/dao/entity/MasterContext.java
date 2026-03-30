package com.sipl.ticket.core.dao.entity;

import java.util.Map;

public class MasterContext {
    private final Map<Integer, String> priorityMap;
    private final Map<Integer, String> statusMap;
    private final Map<Integer, String> channelMap;

    public MasterContext(Map<Integer, String> priorityMap,
                         Map<Integer, String> statusMap,
                         Map<Integer, String> channelMap) {
        this.priorityMap = priorityMap;
        this.statusMap = statusMap;
        this.channelMap = channelMap;

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

}
