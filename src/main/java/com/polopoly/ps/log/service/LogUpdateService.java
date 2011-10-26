package com.polopoly.ps.log.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

import com.polopoly.ps.log.config.LogFileFinder;

public class LogUpdateService extends AbstractService implements LogReaderDelegate {
    private static final Logger LOGGER = Logger.getLogger(LogUpdateService.class.getName());
    public final String serviceResource;
    private final Class<LogFileFinder> logFinderType;

    @SuppressWarnings("unchecked")
    public LogUpdateService(BayeuxServer bayeux, Class logFinderType, String serviceName) {
        super(bayeux, serviceName);
        this.logFinderType = logFinderType;
        this.serviceResource = "/" + serviceName;
        addService("/service" + serviceResource, "connect");
    }

    public void connect(ServerSession remote, Message message) {
        LogReader logReader = (LogReader) remote.getAttribute("logReader");
        Thread logReaderThread = (Thread) remote.getAttribute("logReaderThread");
        String logfile = null;
        try {
            LogFileFinder logFinder = logFinderType.newInstance();
            List<String> logs = logFinder.getLogs();

            if (logs.size() != 0) {
                logfile = logs.get(0);
                LOGGER.info("Reading log " + logfile);
                logReader = new LogReader(logfile, this, remote);
                logReaderThread = new Thread(logReader);
                logReaderThread.start();
                remote.setAttribute("logReader", logReader);
                remote.setAttribute("logReaderThread", logReaderThread);

                remote.addListener(new StopThreadRemoveListener());

            } else {
                append("Could not find a valid logfile with " + logFinderType.getName()
                       + ". Service is probably not running.", remote);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class StopThreadRemoveListener implements ServerSession.RemoveListener {

        @Override
        public void removed(ServerSession remote, boolean arg1) {
            LOGGER.info("Stopping reader");
            LogReader logReader = (LogReader) remote.getAttribute("logReader");
            logReader.setIsRunning(false);
        }

    }

    @Override
    public void append(String lines, Object userData) {
        ServerSession remote = (ServerSession) userData;

        Map<String, String> output = new HashMap<String, String>();
        output.put("update", lines);
        remote.deliver(getServerSession(), serviceResource, output, null);
    }
}
