package com.paulilves.deployer.constants;

/**
 * Tuned for Tomcat 9 by default
 * Can be potentially re-tuned for any other application server
 */
public interface Paths {
    String WEBAPP_FOLDER = "webapps";
    String BATCH_FOLDER = "bin";
    String START_BACH_FILENAME = "startup.bat";
    String SHUTDOWN_BACH_FILENAME = "shutdown.bat";
}
