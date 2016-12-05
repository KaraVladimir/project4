package web.commands;

import model.dao.exception.DaoException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Kara_Vladimir.
 */
public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse httpServletResponse) throws DaoException;
}
