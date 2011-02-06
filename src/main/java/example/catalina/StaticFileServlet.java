package example.catalina;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticFileServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StaticFileServlet.class.getName());

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("=== loading " + getServletContext().getRealPath("/")
                    + req.getRequestURI().replace("/catalina", ""));

        InputStream file = null;
        try {
            file = getServletContext().getResourceAsStream(req.getRequestURI().replace("/catalina", ""));
            if (file == null) {
                resp.setStatus(404);
                return;
            }
            byte[] data = new byte[file.available()];

            file.read(data);

            resp.getOutputStream().write(data);
            resp.setStatus(200);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Test.", e);
        } finally {
            if (file != null)
                file.close();
        }

    }
}
