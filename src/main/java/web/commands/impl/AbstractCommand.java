package web.commands.impl;

import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * @author kara.vladimir2@gmail.com.
 */
public abstract class AbstractCommand implements Command {

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
