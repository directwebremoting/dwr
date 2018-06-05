package org.directwebremoting.extend;

/**
 * An interface to resolve the fact that many webservers treat blah/index.html
 * the same as blah/.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface PageNormalizer
{
    /**
     * Take an un-normalized URL and turn it into the canonical form for that
     * URL. In general this will work by stripping off extra components like
     * <code>index.html</code> rather than adding them in.
     * @param unnormalized The raw string from the browser
     * @return A canonical form that DWR uses to compare pages for equivalence.
     */
    public String normalizePage(String unnormalized);
}
