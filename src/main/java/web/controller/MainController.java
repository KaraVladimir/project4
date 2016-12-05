package web.controller;

import model.dao.exception.DaoException;
import org.apache.log4j.Logger;
import web.config.Pages;
import web.commands.Command;
import web.commands.CommandKeeper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class is main controller for this application
 * @author kara.vladimir2@gmail.com.
 */
public class MainController extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(MainController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("Controller starts");
        Command command = CommandKeeper.get(req.getRequestURI());
        LOG.trace("URI:" + req.getRequestURI());
        LOG.trace("Command:" + command);

        if (command == null) {
//            resp.sendRedirect(Pages.LOGIN_PATH);
            return;
        }
        try {
            String forward = command.execute(req, resp);
            LOG.trace("Forward:"+forward);
            req.getRequestDispatcher(forward).forward(req,resp);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return;
    }
}
