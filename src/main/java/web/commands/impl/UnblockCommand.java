package web.commands.impl;

import model.dao.exception.DaoException;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class UnblockCommand extends AbstractCommand{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws DaoException {
        return Pages.PAGE_ADMIN_UNBLOCK;
    }
}
