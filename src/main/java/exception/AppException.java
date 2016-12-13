package exception;

import org.apache.log4j.Logger;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class AppException extends Exception {
    Logger logger;
    public AppException(Logger logger,String message) {
        super(message);
        this.logger = logger;
    }

    public AppException(Logger logger,String message, Throwable cause) {
        super(message, cause);
        this.logger = logger;
    }

    public AppException(Logger logger,Throwable cause) {
        super(cause);
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }
}
