package com.polopoly.ps.log.service;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.bayeux.server.BayeuxServer;

import com.polopoly.ps.log.config.PolopolyIndexserverLogFileFinder;
import com.polopoly.ps.log.config.PolopolyXmlserverLogFileFinder;
import com.polopoly.ps.log.config.TomcatLogFileFinder;

public class ServiceInitializer extends GenericServlet {
    @Override
    public void init() throws ServletException {
        BayeuxServer bayeux = (BayeuxServer) getServletContext().getAttribute(BayeuxServer.ATTRIBUTE);

        new LogUpdateService(bayeux, TomcatLogFileFinder.class, "tomcat");
        new LogUpdateService(bayeux, PolopolyIndexserverLogFileFinder.class, "indexserver");
        new LogUpdateService(bayeux, PolopolyXmlserverLogFileFinder.class, "xmlserver");
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }
}
