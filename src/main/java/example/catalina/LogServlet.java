package example.catalina;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            response.getOutputStream().write(data);

        } catch (ConfigException e) {
            LOGGER.log(Level.WARNING, "An error occurred.", e);
        }

    }
}
