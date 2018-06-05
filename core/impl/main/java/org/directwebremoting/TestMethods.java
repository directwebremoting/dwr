package org.directwebremoting;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestMethods
{
    public static void main(String[] args) {
        String normalized = "http://www.test.com:8181;jsessionid=234234324?param=1";
        String normalized2 = "http://www.test.com:8181;JSESSIONID=234234324?param=1";
        String normalized3 = "http://www.test.com:8181?param=1;jsessionid=234234324";
        String session = "jsessionid";
        if(normalized.matches("(?i).*;" + session + "=.*"))
        {
            normalized = normalized.replaceFirst(";" + session + "=.*","");
        }
        System.out.println(normalized);
        if(normalized2.matches("(?i).*(?i);" + session + "=.*"))
        {
            normalized2 = normalized2.replaceFirst("(?i);" + session + "=.*","");
        }
        System.out.println(normalized2);
        if(normalized3.matches("(?i).*;" + session + "=.*"))
        {
            normalized3 = normalized3.replaceFirst("(?i);" + session + "=.*","");
        }
        System.out.println(normalized3);
    }
}

