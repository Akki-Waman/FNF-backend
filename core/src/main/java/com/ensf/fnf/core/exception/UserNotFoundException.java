package com.ensf.fnf.core.exception;

public class UserNotFoundException
        extends FnfException {

    public UserNotFoundException() {

        super(
                "User not found"
        );
    }
}
