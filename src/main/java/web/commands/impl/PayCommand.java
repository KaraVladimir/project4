package web.commands.impl;

import exception.AppException;
import model.dao.exception.DaoException;
import model.entities.Account;
import model.entities.Client;
import model.entities.Payment;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import service.AccountService;
import service.UserService;
import web.config.Attrs;
import web.config.Msgs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class PayCommand extends AbstractCommand {
    private static final Logger LOG = Logger.getLogger(PayCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {

        Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
        List<Account> accounts = UserService.INSTANCE.findUserAccounts(userId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS, accounts);
        if (request.getParameter(Attrs.EXECUTE)==null||request.getParameter(Attrs.EXECUTE).equals("n")) {
            return Pages.PAGE_USER_PAY;
        }
        Integer accS_ID = getIdFromString(LOG, request.getParameter(Attrs.ACCOUNT_ID), INCORRECT_ACCOUNT_ID);
        BigDecimal amount = getBigDecimalFromString(LOG, request.getParameter(Attrs.ACC_AMOUNT), INCORRECT_AMOUNT);
        String number = request.getParameter(Attrs.ACC_NUMBER);
        checkNullOrEmptyString(LOG, number, INCORRECT_ACCOUNT_NUMBER);

        Account accountDest = AccountService.INSTANCE.findAccountByNumber(number);
        checkNullObject(LOG,accountDest, ACCOUNT_NOT_EXIST);

        Payment payment = AccountService.INSTANCE.pay(accS_ID,number, amount);
        if (payment != null) {
            request.setAttribute(Attrs.MSG, Msgs.SUCCESS);
        }
        accounts = UserService.INSTANCE.findUserAccounts(userId);
        request.setAttribute(Attrs.AVAILABLE_ACCOUNTS, accounts);

        return Pages.PAGE_USER_PAY;
    }
}
