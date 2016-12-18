package payments.controller.controller;

import org.apache.log4j.Logger;
import payments.controller.commands.Command;
import payments.controller.commands.CommandKeeper;
import payments.controller.commands.impl.CommandKeeperImpl;

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
    CommandKeeper commandKeeper;

    @Override
    public void init() throws ServletException {
        super.init();
        commandKeeper = new CommandKeeperImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.trace(request.getRequestURI());
        String forwardPage = null;
        Command command = commandKeeper.get(request.getRequestURI());
        forwardPage = command.execute(request, response);
        if (forwardPage != null) {
            LOG.trace(command);
            request.getRequestDispatcher(forwardPage).forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath());
        }
    }

}
