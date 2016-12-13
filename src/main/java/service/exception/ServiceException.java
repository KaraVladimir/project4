package service.exception;

import exception.AppException;
import org.apache.log4j.Logger;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class ServiceException extends AppException{
    public ServiceException(Logger logger,String message) {
        super(logger,message);
    }
}
