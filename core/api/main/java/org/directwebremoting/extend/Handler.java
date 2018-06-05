package org.directwebremoting.extend;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A handler is a very simple servlet that does not differentiate between GET
 * and POST, or need complex setup beyond what the container can provide
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Handler
{
    /**
     * Handle a URL request that has been mapped to this Handler
     * @param request The HTTP request data
     * @param response Where we write the HTTP response data
     * @throws IOException If the write process fails
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
