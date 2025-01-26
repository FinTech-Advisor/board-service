package org.advisor.board.exceptions;

import org.advisor.global.exceptions.BadRequestException;

public class GuestPasswordCheckException extends BadRequestException {
    public GuestPasswordCheckException() {
        super("Required.guestPassword");
        setErrorCode(true);
    }
}
