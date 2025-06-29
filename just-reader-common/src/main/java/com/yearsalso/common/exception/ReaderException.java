package com.yearsalso.common.exception;

import lombok.Data;

/**
* @author
 */
@Data
public class ReaderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected final String message;

    public ReaderException(String message)
    {
        this.message = message;
    }

    public ReaderException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

}
