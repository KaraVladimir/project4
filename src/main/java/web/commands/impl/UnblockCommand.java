package web.commands.impl;

import exception.AppException;
import model.dao.DaoCommand;
import model.dao.exception.DaoException;
import model.dao.impl.AccountDaoImpl;
import model.dao.impl.DaoFactory;
import model.dao.impl.DaoManager;
import model.entities.Account;
import org.apache.log4j.Logger;
import service.AccountService;
import web.config.Attrs;
import web.config.Msgs;
import web.config.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class UnblockCommand extends AbstractCommand{
    private static final Logger LOG = Logger.getLogger(UnblockCommand.class);

    @Override
    public String proceedExecute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AppException {
        String number = request.getParameter(Attrs.ACCOUNT_ID);
        checkNullOrEmptyString(LOG,number,INCORRECT_ACCOUNT_NUMBER);

        Account account = AccountService.INSTANCE.unblockAccountByNumber(number);
        request.setAttribute(Attrs.ACCOUNT,account);

        return Pages.PAGE_ADMIN_UNBLOCK;
    }
}
