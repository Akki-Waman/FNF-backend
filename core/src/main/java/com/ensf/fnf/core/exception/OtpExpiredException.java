package com.ensf.fnf.core.exception;

public class OtpExpiredException
        extends FnfException {

    public OtpExpiredException() {

        super(
                "OTP expired"
        );
    }
}
