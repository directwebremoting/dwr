package uk.ltd.getahead.dwr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler is a bit like a servlet except that we don't need to ask users to
 * put stuff into WEB-INF/web.xml we can configure it ourselves. We also don't
 * distinguish between GET and POST at a method level.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Processor
{
    /**
     * Handle a request, the equivalent of doGet and doPost
     * @param req The HTTP request parameters
     * @param resp The HTTP response data
     * @throws IOException If i/o fails
     * @throws ServletException Other failures
     */
    void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
