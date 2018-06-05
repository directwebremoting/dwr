package org.directwebremoting.dwrp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ServerException;

/**
 * A default implementation of FileUpload for cases when Commons File-Upload is
 * not available. This implementation does not do file uploads, however it does
 * allow the system to carry on without classpath issues.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class UnsupportedFileUpload implements FileUpload
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.FileUpload#parseRequest(javax.servlet.http.HttpServletRequest, java.io.File)
     */
    public Map<String, FormField> parseRequest(HttpServletRequest req) throws ServerException
    {
        log.error("Commons File Upload jar file not found. Aborting request.");
        throw new UnsupportedOperationException("File uploads not supported");
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(UnsupportedFileUpload.class);
}
