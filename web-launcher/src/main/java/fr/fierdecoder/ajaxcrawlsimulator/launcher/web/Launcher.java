package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import fr.fierdecoder.ajaxcrawlsimulator.web.application.WebApplication;

public class Launcher {
    public static void main(String[] args) {
        LauncherOptions options = new LauncherOptions();
        try {
            JCommander jCommander = new JCommander(options, args);
            if (options.isHelp()) {
                jCommander.usage();
            } else {
                startServer(options);
            }
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void startServer(LauncherOptions options) {
        LauncherModule launcherModule = new LauncherModule(options);
        WebApplication webApplication = new WebApplication(launcherModule);
        webApplication.start();
    }
}
