package com.paulilves.deployer.service;

import com.paulilves.deployer.constants.Paths;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Service responsible for implementing Action commands from the command line
 */
public class ActionService {

    private String artifactHome;
    private String artifactName;
    private String serverHome;
    private String host;
    private int port;
    private static final Logger log = Logger.getLogger(ActionService.class.getName());

    public ActionService(String artifactHome, String artifactName, String serverHome, String host, int port) {
        this.artifactHome = artifactHome;
        this.artifactName = artifactName;
        this.serverHome = serverHome;
        this.host = host;
    }

    public void deploy() {
        File artifact = new File(artifactHome + File.separator + artifactName);
        File targetPath = new File(serverHome + File.separator + Paths.WEBAPP_FOLDER + File.separator + artifactName);
        try {
            FileUtils.copyFile(artifact, targetPath);
            log.info("Deploying " + artifactName + " to server deployment path: " + serverHome + File.separator + Paths.WEBAPP_FOLDER);
        } catch (IOException e) {
            log.severe(e.toString());
        }
        start();
    }

    public void undeploy() {
        shutdown();

        File artifact = new File(serverHome + File.separator + Paths.WEBAPP_FOLDER + File.separator + artifactName);
        if (artifact.exists()) {
            log.info("Artifact detected at path " + artifact.getAbsolutePath());
            log.info("Deleting...");
            artifact.delete();
        } else {
            log.info("Artifact not found at webapp folder...");
        }
    }

    public void shutdown() {
        log.info("Shutting down application...");
        runBatch(Paths.SHUTDOWN_BACH_FILENAME);
    }

    public void start() {
        log.info("Starting application...");
        runBatch(Paths.START_BACH_FILENAME);
    }

    public void nothing(){
        //Does absolutely nothing!
    }

    private void runBatch(String batchFileName) {
        String path = "cmd /c start " + serverHome + File.separator + Paths.BATCH_FOLDER + File.separator + batchFileName;
        try {
            Process p = Runtime.getRuntime().exec(path);
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            log.severe(e.toString());
        }
    }

    public void checkIfServerUp() {
        try {
            Socket socket = new Socket(host, 8080);
            log.info("Server is alive.");
            socket.close();
        } catch (IOException e) {
            log.info("Server is down.");
        }
    }

}
