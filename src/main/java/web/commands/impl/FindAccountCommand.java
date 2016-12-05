package web.commands.impl;

import model.dao.exception.DaoException;
import model.entities.Account;
import service.AccountService;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class FindAccountCommand extends AbstractCommand{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws DaoException {
        String number = request.getParameter("number");
        Account account = new AccountService().findAccount(number);
        request.setAttribute("account",account);
        if (request.getHeader("referer").endsWith("/page/unblock")) {
            return Pages.PAGE_ADMIN_UNBLOCK;
        }
        return null;
    }
}
