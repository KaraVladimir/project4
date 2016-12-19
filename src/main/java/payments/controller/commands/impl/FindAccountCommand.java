package payments.controller.commands.impl;

import payments.helper.Msgs;
import payments.exception.AppException;
import payments.model.entities.Account;
import org.apache.log4j.Logger;
import payments.service.impl.AccountServiceImpl;
import payments.helper.Attrs;
import payments.helper.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Find account by account number
 * @author kara.vladimir2@gmail.com.
 */
public class FindAccountCommand extends AbstractCommand {
    private static final Logger LOG = Logger.getLogger(FindAccountCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        String number = request.getParameter(Attrs.ACC_NUMBER);
        checkNullOrEmptyString(LOG, number, Msgs.INCORRECT_ACCOUNT_NUMBER);
        Account account = AccountServiceImpl.getInstance().findAccountByNumber(number);
        request.setAttribute(Attrs.ACCOUNT, account);
        return Pages.PAGE_ADMIN_UNBLOCK;
    }
}
