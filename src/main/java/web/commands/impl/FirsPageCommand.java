package web.commands.impl;

import model.dao.exception.DaoException;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class FirsPageCommand extends AbstractCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws DaoException {
        request.setAttribute("command","/login");
        return "/WEB-INF/view/login.jsp";
    }
}
