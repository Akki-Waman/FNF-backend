package com.ensf.fnf.core.exception;

public class InvalidOtpException
        extends FnfException {

    public InvalidOtpException() {

        super(
                "Invalid OTP"
        );
    }

    public InvalidOtpException(
            String message) {

        super(
                message
        );
    }
}