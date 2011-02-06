package example.catalina;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.cometd.continuation.ContinuationCometdServlet;

public class MyCometdServlet extends ContinuationCometdServlet {
    private static final Logger LOGGER = Logger.getLogger(MyCometdServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("=== Comet GET request " + req.getRequestURI());
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse arg1) throws ServletException, IOException {
        LOGGER.info("=== Comet POST request " + req.getRequestURI());
        super.doPost(req, arg1);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("=== Comet PUT request " + req.getRequestURI());
        super.doPut(req, resp);
    }
}
