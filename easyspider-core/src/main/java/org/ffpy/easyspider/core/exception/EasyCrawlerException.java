package org.ffpy.easyspider.core.exception;

public class EasyCrawlerException extends RuntimeException {
    public EasyCrawlerException() {
    }

    public EasyCrawlerException(String message) {
        super(message);
    }

    public EasyCrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public EasyCrawlerException(Throwable cause) {
        super(cause);
    }

    public EasyCrawlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
