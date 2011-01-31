package example.catalina;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import example.catalina.config.ConfigException;
import example.catalina.config.LogFileFinder;

public class LogServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LogServlet.class.getName());

    private VelocityContext context = null;
    private VelocityEngine engine = null;

    private static Properties getVelocityProperties() {
        return new Properties() {
            {
                setProperty("resource.loader", "webapp");
                setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
                setProperty("webapp.resource.loader.path", "/WEB-INF/velocity");
            }
        };
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            engine = new VelocityEngine();
            engine.setApplicationAttribute("javax.servlet.ServletContext", config.getServletContext());
            engine.init(getVelocityProperties());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String logpage = "/logpage.vm";

        try {

            LogFileFinder logFinder = new LogFileFinder(Runtime.getRuntime());

            List<String> logs = logFinder.getLogs();
            if (logs.size() == 0) {
                response.setStatus(503);
                response.getOutputStream().write((new String("No tomcat process found.")).getBytes());
                return;
            }
            String firstLog = logs.get(0);

            LOGGER.info("Reading logfile " + firstLog);
            InputStream is = new FileInputStream(firstLog);
            int avail = is.available();

            is.skip(avail - 80 * 80);

            byte[] data = new byte[is.available()];
            is.read(data);

            context = new VelocityContext();
            context.put("name", new String("Velocity"));
            context.put("log", new String(data));
            Template template = null;

            template = engine.getTemplate(logpage);
            StringWriter stringWriter = new StringWriter();
            template.merge(context, stringWriter);
            response.getOutputStream().write(stringWriter.toString().getBytes("iso-8859-1"));

        } catch (ConfigException e) {
            LOGGER.log(Level.WARNING, "An error occurred.", e);
        } catch (ResourceNotFoundException e) {
            LOGGER.log(Level.WARNING, "couldn't find the template " + logpage, e);
        } catch (ParseErrorException e) {
            LOGGER.log(Level.WARNING, "syntax error: problem parsing the template " + logpage, e);
        } catch (MethodInvocationException e) {
            LOGGER.log(Level.WARNING, "something invoked in the template threw an exception " + logpage, e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unexpected error loading " + logpage, e);
        }

    }

}
