package payments.controller.controller;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import payments.helper.Attrs;
import payments.helper.Msgs;
import payments.controller.commands.Command;
import payments.controller.commands.CommandKeeper;
import payments.controller.commands.impl.CommandKeeperImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Class is main controller for this application
 *
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

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.trace(request.getRequestURI());
        String forwardPage;
        try {
            Command command = commandKeeper.get(request.getRequestURI());
            forwardPage = command.execute(request, response);
            if (forwardPage != null) {
                LOG.trace(command);
                request.getRequestDispatcher(forwardPage).forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath());
            }
        } catch (Exception e) {
            request.setAttribute(Attrs.MSG, Msgs.UNKNOWN_ERR);
            Integer userId = (Integer) request.getSession().getAttribute(Attrs.USER_ID);
            MDC.put("userID", (userId == null) ? "n/a" : userId);
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            LOG.error(e.getMessage());
            LOG.error(errors.toString());
        }
    }
}
