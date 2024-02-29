package com.cairone.pg.base.exception;

public class AppServerException extends RuntimeException {

    private final String technicalMessage;

    private AppServerException(String clientMessage, String technicalMessage, Throwable cause) {
        super(clientMessage, cause);
        this.technicalMessage = technicalMessage;
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }

    public static class Builder {

        private String clientMessage;
        private String technicalMessage;
        private Throwable cause;

        public Builder withMessage(String clientMessage) {
            this.clientMessage = clientMessage;
            return this;
        }

        public Builder withTechnicalMessage(String technicalMessage) {
            this.technicalMessage = technicalMessage;
            return this;
        }

        public Builder withCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public AppServerException build() {
            return new AppServerException(clientMessage, technicalMessage, cause);
        }
    }
}
