/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

