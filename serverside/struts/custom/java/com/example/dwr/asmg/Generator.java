package com.example.dwr.asmg;


import java.util.StringTokenizer;

/**
 * Generate an anti-spam mailto link from an email address.
 * The output link looks something like this (where $1 is the username part of
 * the address and $2 is the hostname part:
 * <pre>
 * Contact us using:
 * &lt;script type="text/javascript"&gt;
 * var a = $1 + "@" + $2;
 * document.write("&lt;a href='mail" + "to:" + a + "'&gt;" + a + "&lt;/a&gt;");
 * &lt;/script&gt;
 * &lt;noscript&gt;[$1 at $2]&lt;/noscript&gt;
 * </pre>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Generator
{
    /**
     * Generate an anti-spam mailto link from an email address
     * @param email The address to generate a link from
     * @return The HTML snippet
     */
    public String generateAntiSpamMailto(String email)
    {
        StringTokenizer st = new StringTokenizer(email, "@"); //$NON-NLS-1$
        if (st.countTokens() != 2)
        {
            throw new IllegalArgumentException("Invalid email address: " + email); //$NON-NLS-1$
        }

        String before = st.nextToken();
        String after = st.nextToken();

        StringBuffer buffer = new StringBuffer();

        buffer.append("Contact us using:\n"); //$NON-NLS-1$
        buffer.append("<script type='text/javascript'>\n"); //$NON-NLS-1$
        buffer.append("var a = '"); //$NON-NLS-1$
        buffer.append(before);
        buffer.append('@');
        buffer.append(after);
        buffer.append("';\n"); //$NON-NLS-1$;
        buffer.append("document.write(\"<a href='mail\" + \"to:\" + a + \"'>\" + a + \"</a>\");\n"); //$NON-NLS-1$
        buffer.append("</script>\n"); //$NON-NLS-1$
        buffer.append("<noscript>["); //$NON-NLS-1$
        buffer.append(before);
        buffer.append(" at "); //$NON-NLS-1$
        buffer.append(after);
        buffer.append("]</noscript>\n"); //$NON-NLS-1$

        return buffer.toString();
    }
}
