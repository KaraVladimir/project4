package web.commands.impl;

import exception.AppException;
import model.entities.Account;
import org.apache.log4j.Logger;
import service.UserService;
import service.exception.ServiceException;
import web.config.Attrs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class FindClientAccountsCommand extends AbstractCommand{
    private static final Logger LOG = Logger.getLogger(FindClientAccountsCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);

        String ref = request.getRequestURI();
        List<Account> accounts = UserService.INSTANCE.findUserAccounts(userId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS,accounts);
        switch (ref) {
            case Pages.PATH_PAY:
                return Pages.PAGE_USER_PAY;
            case Pages.PATH_REFILL:
                return Pages.PAGE_USER_REFILL;
            case Pages.PATH_BLOCK:
                return Pages.PAGE_USER_BLOCK;
            default:
                return Pages.PAGE_USER_START;
        }
    }
}
