package example.catalina;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.catalina.config.ConfigException;
import example.catalina.config.LogFileFinder;

public class LogServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LogServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            LogFileFinder logFinder = new LogFileFinder(Runtime.getRuntime());
            String logFile = logFinder.getLogs().get(0);

            LOGGER.info("Reading logfile " + logFile);
            InputStream is = new FileInputStream(logFile);
            int avail = is.available();

            is.skip(avail - 80 * 80);

            byte[] data = new byte[is.available()];
            is.read(data);
            resp.getOutputStream().write(data);

        } catch (ConfigException e) {
            LOGGER.log(Level.WARNING, "An error occurred.", e);
        }

    }
}
