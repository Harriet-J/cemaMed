
package com.cema_health_ke_v1.Backend.CEMA_health.dto;

public enum ResponseCode {
    SUCCESS(200, "Success"),
    ERROR(404, "Error");

    private final int code;
    private final String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() { return code; }
    public String getDescription() { return description; }

    public enum StatusCode {
        SUCCESS(200, "Success"),
        ERROR(404, "Error");

        private final int code;
        private final String description;

        StatusCode(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() { return code; }
        public String getDescription() { return description; }
    }

    public enum MessageCode {
        SUCCESS(1000, "Operation successful"),
        NOT_FOUND(1001, "No records found");

        private final int code;
        private final String description;

        MessageCode(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() { return code; }
        public String getDescription() { return description; }
    }
}