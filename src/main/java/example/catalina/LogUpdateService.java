package example.catalina;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

import example.catalina.config.ConfigException;
import example.catalina.config.LogFileFinder;

public class LogUpdateService extends AbstractService implements LogReaderDelegate {
    private static final Logger LOGGER = Logger.getLogger(LogUpdateService.class.getName());
    public static final String SERVICE_NAME = "logupdate";
    public static final String SERVICE = "/" + SERVICE_NAME;
    private LogReader logReader = null;
    private Thread logReaderThread = null;

    public LogUpdateService(BayeuxServer bayeux) {
        super(bayeux, SERVICE_NAME);
        addService("/service" + SERVICE, "connect");
    }

    public void connect(ServerSession remote, Message message) {
        LOGGER.info("Whatever.");
        if (!remote.isConnected() && logReader != null) {
            LOGGER.info("Stopping reader..");
            logReader.stopRunning();
            logReaderThread = null;
        } else if (logReaderThread == null) {
            LOGGER.info("hej.");
            String logfile = null;
            try {
                LogFileFinder logFinder = new LogFileFinder(Runtime.getRuntime());
                List<String> logs = logFinder.getLogs();
                if (logs.size() != 0) {
                    logfile = logs.get(0);
                    logReader = new LogReader(logfile, this, remote);
                    logReaderThread = new Thread(logReader);
                    logReaderThread.start();

                }
            } catch (ConfigException e) {
                LOGGER.log(Level.WARNING, "Could find logfile", e);
            }
        }
    }

    @Override
    public void append(String lines, Object userData) {
        LOGGER.info("Udate.");

        ServerSession remote = (ServerSession) userData;

        Map<String, String> output = new HashMap<String, String>();
        output.put("update", lines);
        remote.deliver(getServerSession(), SERVICE, output, null);
    }
}
