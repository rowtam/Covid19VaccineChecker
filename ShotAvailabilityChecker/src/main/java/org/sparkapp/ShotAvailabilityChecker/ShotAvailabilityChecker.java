/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sparkapp.ShotAvailabilityChecker;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author rowta
 */
public class ShotAvailabilityChecker {

    public static void main(String[] args) {
        String filePath = Paths.get(args[0]).toAbsolutePath().toString();
        System.out.println("Loading: "+filePath);
        Configuration config = Configuration.getInstance();
        config.loadConfiguration(filePath);
        System.setProperty("webdriver.chrome.driver", config.getWebDriverLocation());
        // Run every Period # of minutes
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for(OklahomaCovid19Registration ok:config.getOklahomaList())
                {
                    ok.process();
                }
            }
            // initial delay, number of time units, unit of time
        }, 0, config.getPeriod(), TimeUnit.MINUTES);

    }
}
