package com.minihtop;

import com.minihtop.collector.JvmMetricsCollector;
import com.minihtop.config.ConfigLoader;
import com.minihtop.config.DashboardConfig;
import com.minihtop.engine.RefreshEngine;
import com.minihtop.model.SystemSnapshot;
import com.minihtop.render.DashboardRenderer;
import com.sun.management.OperatingSystemMXBean;

import java.io.IOException;
import java.lang.management.ManagementFactory;

public class App
{
    public static int refreshIntervalSeconds;
    public static int maxProcesses;
    public static int sparklineLength;
    public static java.util.List<String> widgets;

    static {
        try {
            DashboardConfig config = ConfigLoader.load();
            refreshIntervalSeconds = config.refreshIntervalSeconds();
            maxProcesses = config.maxProcesses();
            sparklineLength = config.sparklineLength();
            widgets = config.widgets();
        } catch (IOException e) {
            System.err.println("Failed to load config, using defaults.");
            refreshIntervalSeconds = 1;
            maxProcesses = 10;
            sparklineLength = 10;
            widgets = java.util.List.of("cpu", "memory", "process");
        }
    }

    public static void main( String[] args ) throws InterruptedException {
        JvmMetricsCollector jvmMetricsCollector = new JvmMetricsCollector(
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean(),
                ManagementFactory.getMemoryMXBean()
        );

        RefreshEngine engine = new RefreshEngine(jvmMetricsCollector);
        DashboardRenderer renderer = new DashboardRenderer();

        // Register shutdown hook to stop engine and restore terminal cursor...
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            engine.stop();
            System.out.print("\033[?25h");
            System.out.flush();
        }));

        System.out.print("\033[?25l");
        engine.start();

        while(true) {
            SystemSnapshot snap = engine.getLatestSnapshot();
            if (snap != null) {
                System.out.print(renderer.render(snap));
                System.out.flush();
            }
            Thread.sleep(refreshIntervalSeconds * 1000L);
        }
    }
}
