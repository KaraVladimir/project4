package payments.controller.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse httpServletResponse);
}
