package org.gradle.sample;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Greeter {
    public String getGreeting() throws Exception {

        InputStream greetingStr = getClass().getResourceAsStream("/greeting.txt");
        try {
            return IOUtils.toString(greetingStr).trim();
        }
        finally {
            greetingStr.close();
        }
    }
}