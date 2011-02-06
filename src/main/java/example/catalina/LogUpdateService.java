package example.catalina;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

public class LogUpdateService extends AbstractService {
    private static final Logger LOGGER = Logger.getLogger(LogUpdateService.class.getName());
    public static final String SERVICE_NAME = "logupdate";
    public static final String SERVICE = "/" + SERVICE_NAME;

    public LogUpdateService(BayeuxServer bayeux) {
        super(bayeux, SERVICE_NAME);
        addService("/service" + SERVICE, "processHello");
    }

    public void processHello(ServerSession remote, Message message) {
        LOGGER.info("=== Hello called.");
        Map<String, Object> input = message.getDataAsMap();
        String name = (String) input.get("name");

        Map<String, Object> output = new HashMap<String, Object>();
        output.put("greeting", "Hello, " + name);
        remote.deliver(getServerSession(), SERVICE, output, null);
    }
}
