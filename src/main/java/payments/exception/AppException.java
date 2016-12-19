package payments.exception;

import org.apache.log4j.Logger;

/**
 * Common exception for application
 * @author kara.vladimir2@gmail.com.
 */
public class AppException extends Exception {
    private Logger logger;

    protected AppException(Logger logger, String message) {
        super(message);
        this.logger = logger;
    }

    protected AppException(Logger logger, String message, Throwable cause) {
        super(message, cause);
        this.logger = logger;
    }

    protected AppException(Logger logger, Throwable cause) {
        super(cause);
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }
}
