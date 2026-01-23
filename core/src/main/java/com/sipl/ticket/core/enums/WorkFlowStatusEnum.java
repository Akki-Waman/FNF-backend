package com.sipl.ticket.core.enums;


public enum WorkFlowStatusEnum {
    CREATED(1),            // Ticket user ne create kiya
    IN_PROGRESS(2),        // Engineer kaam kar raha hai
    APPROVED(3),           // Manager/Approver ne approve kiya
    REJECTED(4);
;
    private final int code;

    WorkFlowStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static WorkFlowStatusEnum fromCode(int code) {
        for (WorkFlowStatusEnum status : WorkFlowStatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown WorkFlowStatusEnum code: " + code);
    }
}