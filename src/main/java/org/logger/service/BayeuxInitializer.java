package org.logger.service;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.bayeux.server.BayeuxServer;
import org.logger.config.TomcatLogFileFinder;


public class BayeuxInitializer extends GenericServlet {
    @Override
    public void init() throws ServletException {
        BayeuxServer bayeux = (BayeuxServer) getServletContext().getAttribute(BayeuxServer.ATTRIBUTE);

        new LogUpdateService(bayeux, TomcatLogFileFinder.class);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }
}
