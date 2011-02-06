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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream file = null;
        try {
            file = getServletContext().getResourceAsStream(request.getRequestURI().replace(request.getContextPath(), ""));
            if (file == null) {
                response.setStatus(404);
                return;
            }
            byte[] data = new byte[file.available()];

            file.read(data);

            response.setContentType(this.getServletContext().getMimeType(request.getRequestURI()));
            response.getOutputStream().write(data);
            response.setStatus(200);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Test.", e);
        } finally {
            if (file != null)
                file.close();
        }

    }
}
