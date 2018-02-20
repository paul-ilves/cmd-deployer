package com.paulilves.deployer.core;

import java.util.logging.Logger;

/**
 * Entry point
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        new Cli(args).parse();
        log.info("All tasks finished...");
    }

}