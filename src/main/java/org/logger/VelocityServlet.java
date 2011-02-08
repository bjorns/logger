package org.logger;

import java.io.IOException;
import java.io.StringWriter;
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
import org.logger.config.ConfigException;


public class VelocityServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(VelocityServlet.class.getName());

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
            e1.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String logpage = "/logpage.vm";

        try {

            context = new VelocityContext();
            context.put("name", new String("Velocity"));
            context.put("contextPath", request.getContextPath());

            byte[] pageData = mergeTemplate(logpage, context);
            response.getOutputStream().write(pageData);

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

    private byte[] mergeTemplate(String logpage, VelocityContext context2) throws ResourceNotFoundException,
        ParseErrorException, Exception {
        Template template = engine.getTemplate(logpage);
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        return stringWriter.toString().getBytes("iso-8859-1");
    }
}
