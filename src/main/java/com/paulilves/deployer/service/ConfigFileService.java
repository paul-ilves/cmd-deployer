package com.paulilves.deployer.service;

import com.paulilves.deployer.constants.Names;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Service responsible for parsing the config file and returning its content as elements of a nap.
 */
public class ConfigFileService {

    private static final Logger log = Logger.getLogger(ConfigFileService.class.getName());

    public static Map<String, String> parseConfigFile(String propertiesPath) {
        Properties prop = new Properties();
        HashMap<String, String> propertiesMap = new HashMap<>(4);
        try (InputStream input = ConfigFileService.class.getClassLoader().getResourceAsStream(propertiesPath)) {
            if (input == null)
                throw new RuntimeException("Sorry, unable to find " + propertiesPath);

            prop.load(input);

            String artifactHome = prop.getProperty(Names.ARTIFACT_HOME);
            String artifactName = prop.getProperty(Names.ARTIFACT_NAME);
            String serverHome = prop.getProperty(Names.SERVER_HOME);
            String host = prop.getProperty(Names.HOST);
            String port = prop.getProperty(Names.PORT);

            if (artifactHome == null || artifactName == null || serverHome == null || host == null || port == null)
                throw new RuntimeException("Invalid config file: some critical properties are null!");

            propertiesMap.put(Names.ARTIFACT_HOME, artifactHome);
            propertiesMap.put(Names.ARTIFACT_NAME, artifactName);
            propertiesMap.put(Names.SERVER_HOME, serverHome);
            propertiesMap.put(Names.HOST, host);
            propertiesMap.put(Names.PORT, port);
        } catch (IOException ex) {
            log.severe(ex.toString());
        }
        return propertiesMap;

    }
}
