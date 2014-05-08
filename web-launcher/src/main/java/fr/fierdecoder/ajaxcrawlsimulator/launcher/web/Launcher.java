package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.beust.jcommander.JCommander;
import fr.fierdecoder.ajaxcrawlsimulator.web.application.WebApplication;

public class Launcher {
    public static void main(String[] args) {
        LauncherOptions options = new LauncherOptions();
        new JCommander(options, args);
        LauncherModule launcherModule = new LauncherModule(options);
        WebApplication webApplication = new WebApplication(launcherModule);
        webApplication.start();
    }
}
