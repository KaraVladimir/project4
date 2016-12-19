package payments.service.exception;

import payments.exception.AppException;
import org.apache.log4j.Logger;

/**
 * Service exception
 * @author kara.vladimir2@gmail.com.
 */
public class ServiceException extends AppException {
    public ServiceException(Logger logger, String message) {
        super(logger, message);
    }
}
