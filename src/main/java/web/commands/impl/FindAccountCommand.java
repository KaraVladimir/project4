package web.commands.impl;

import exception.AppException;
import model.entities.Account;
import org.apache.log4j.Logger;
import service.AccountService;
import web.config.Attrs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class FindAccountCommand extends AbstractCommand{
    private static final Logger LOG = Logger.getLogger(FindAccountCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        String number = request.getParameter(Attrs.ACC_NUMBER);
        checkNullOrEmptyString(LOG,number,INCORRECT_ACCOUNT_NUMBER);
        Account account = AccountService.INSTANCE.findAccountByNumber(number);
        request.setAttribute(Attrs.ACCOUNT,account);
        return Pages.PAGE_ADMIN_UNBLOCK;
    }
}
