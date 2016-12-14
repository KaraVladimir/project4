package web.listener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class ServletListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(ServletListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        initLog4J(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void initLog4J(ServletContext servletContext) {
        PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
        LOG.trace("Log4j has been initialized");
    }
}
