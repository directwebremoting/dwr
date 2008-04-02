package uk.ltd.getahead.dwrdoc;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Various File based utilities.
 * @author Joe Walker [joe at getahead ltd dot uk]
 */
public class FileUtil
{
    /**
     * Prevent construction
     */
    private FileUtil()
    {
    }

    /**
     * Recursive version of File.list()
     * @see java.io.File#listFiles(java.io.FileFilter)
     */
    public static File[] recursiveListFiles(File root, FileFilter filter, boolean pruneonmatch, int maxdepth)
    {
        List<File> reply = new ArrayList<File>();

        recursiveListFilesInternal(reply, root, filter, pruneonmatch, maxdepth, 0);

        return reply.toArray(new File[reply.size()]);
    }

    /**
     * An implementation of {@link FileUtil#recursiveListFiles(File, FileFilter, boolean, int)}
     * that uses Lists rather than arrays so it can call itself.
     */
    private static void recursiveListFilesInternal(List<File> reply, File dir, FileFilter filter, boolean pruneonmatch, int maxdepth, int depth)
    {
        // Add the files in the current directory
        File[] matches = dir.listFiles(filter);
        reply.addAll(Arrays.asList(matches));
        //log.debug(""+matches.length+" matches in "+dir.getAbsolutePath());

        if (depth > maxdepth)
        {
            //log.debug("- ignoring "+dir.getAbsolutePath()+" because depth="+depth);
        }
        else
        {
            if (pruneonmatch && matches.length != 0)
            {
                //log.debug("- ignoring "+dir.getAbsolutePath()+" because match found");
            }
            else
            {
                File[] dirs = dir.listFiles(new DirectoryFileFilter());
                for (int i = 0; i < dirs.length; i++)
                {
                    recursiveListFilesInternal(reply, dirs[i], filter, pruneonmatch, maxdepth, depth+1);
                }
            }
        }
    }

    /**
     * A FileFilter that accepts directories.
     */
    protected static final class DirectoryFileFilter implements FileFilter
    {
        /* (non-Javadoc)
         * @see java.io.FileFilter#accept(java.io.File)
         */
        public boolean accept(File pathname)
        {
            return pathname.isDirectory();
        }
    }

    /**
     * The log stream
     */
    //private static final Logger log = Logger.getLogger(FileUtil.class);
}
