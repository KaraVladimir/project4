package payments.controller.commands.impl;

import payments.exception.AppException;
import payments.model.entities.Account;
import org.apache.log4j.Logger;
import payments.service.impl.AccountServiceImpl;
import payments.config.Attrs;
import payments.config.Pages;

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
        Account account = AccountServiceImpl.getInstance().findAccountByNumber(number);
        request.setAttribute(Attrs.ACCOUNT,account);
        return Pages.PAGE_ADMIN_UNBLOCK;
    }
}
