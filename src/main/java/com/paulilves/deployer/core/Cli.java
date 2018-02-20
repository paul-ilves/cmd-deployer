package com.paulilves.deployer.core;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paulilves.deployer.constants.Names;
import com.paulilves.deployer.service.ActionService;
import com.paulilves.deployer.service.ConfigFileService;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * A controller-like class tasked with both parsing command line arguments and delegating the job to business layer
 */
public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args;
    private Options options = new Options();

    private String artifactHome;
    private String artifactName;
    private String serverHome;
    private String host;
    private int port;

    public Cli(String[] args) {

        this.args = args;

        options.addOption("c", "config", true, "Location of the config file.");
        options.addOption("a", "action", true, "Action to take.");
        options.addOption(Names.ARTIFACT_NAME_ARG, "war", true, "Name of the war file.");
        options.addOption(Names.ARTIFACT_HOME_ARG, true, "Home directory of the war file.");
        options.addOption(Names.SERVER_HOME_ARG, true, "Server home directory.");
        options.addOption(Names.HOST, true, "Host");
        options.addOption(Names.PORT, true, "Port number.");

    }

    public void parse() {
        CommandLineParser parser = new BasicParser();

        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

//            if (cmd.hasOption("h"))
//                help();


            //config from file
            Map<String, String> propertiesMap = config(cmd);

            artifactHome = propertiesMap.get(Names.ARTIFACT_HOME);
            artifactName = propertiesMap.get(Names.ARTIFACT_NAME);
            serverHome = propertiesMap.get(Names.SERVER_HOME);
            host = propertiesMap.get(Names.HOST);
            port = Integer.parseInt(propertiesMap.get(Names.PORT));

            //config from command-line parameters have a higher priority
            if (cmd.hasOption(Names.ARTIFACT_HOME_ARG))
                artifactHome = cmd.getOptionValue(Names.ARTIFACT_HOME_ARG);
            if (cmd.hasOption(Names.ARTIFACT_NAME_ARG))
                artifactName = cmd.getOptionValue(Names.ARTIFACT_NAME_ARG);
            if (cmd.hasOption(Names.SERVER_HOME_ARG))
                serverHome = cmd.getOptionValue(Names.SERVER_HOME_ARG);
            if (cmd.hasOption(Names.HOST))
                host = cmd.getOptionValue(Names.HOST);
            if (cmd.hasOption(Names.PORT))
                port = Integer.parseInt(cmd.getOptionValue(Names.PORT));

            if (cmd.hasOption("action")) {
                String actionVal = cmd.getOptionValue("action");
                action(actionVal);
            } else {
                log.log(Level.SEVERE, "Missing action option");
                help();
            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties", e);
            help();
        }
    }

    private void action(String actionVal) {
        ActionService actionService = new ActionService(artifactHome, artifactName, serverHome, host, port);

        switch (actionVal) {
            case "deploy":
                actionService.deploy();
                break;
            case "undeploy":
                actionService.undeploy();
                break;
            case "start":
                actionService.start();
                break;
            case "shutdown":
                actionService.shutdown();
                break;
            case "check":
                actionService.checkIfServerUp();
                break;
            case "nothing":
                actionService.nothing();
                break;
        }
    }

    private Map<String, String> config(CommandLine cmd) {

        Map<String, String> configMap;

        if (cmd.hasOption("config")) {
            String configVal = cmd.getOptionValue("config");
            configMap = ConfigFileService.parseConfigFile(configVal);
            log.info("Config file at path " + configVal + " was provided.");
        } else {
            configMap = ConfigFileService.parseConfigFile(Names.DEFAULT_CONFIG_PATH);
            log.info("No config file was provided. Using default classpath config file.");
        }

        if (configMap == null) throw new RuntimeException("Config file null!");

        return configMap;
    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("Main", options);
        System.exit(0);
    }
}