package com.minihtop.engine;

import com.minihtop.App;
import com.minihtop.collector.MetricsCollector;
import com.minihtop.model.SystemSnapshot;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class RefreshEngine {
    private volatile SystemSnapshot latestSnapshot;
    private final Deque<Double> cpuHistory = new ArrayDeque<>();
    private final ScheduledExecutorService scheduler;
    private final MetricsCollector collector;
    private volatile long uptimeSeconds = 0;

    public RefreshEngine(MetricsCollector collector) {
        this.collector = collector;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    private synchronized void tick() {
        SystemSnapshot raw = collector.collect();
        uptimeSeconds++;

        cpuHistory.add(raw.cpuUsage());
        if (cpuHistory.size() > App.sparklineLength) {
            cpuHistory.poll();
        }

        latestSnapshot = new SystemSnapshot(
                raw.cpuUsage(),
                raw.usedMemoryBytes(),
                raw.totalMemoryBytes(),
                raw.processes(),
                raw.timestamp(),
                new ArrayList<>(cpuHistory),
                uptimeSeconds
        );
    }

    public void start() {
        try {
            tick();
        } catch (Exception e) {
            System.err.println("Initial collect failed: " + e.getMessage());
        }

        scheduler.scheduleAtFixedRate(
            () -> {
                try {
                    tick();
                } catch (Exception e) {
                    System.err.println("Collect failed: " + e.getMessage());
                }
            },
            App.refreshIntervalSeconds,
            App.refreshIntervalSeconds,
            java.util.concurrent.TimeUnit.SECONDS
        );
    }

    public SystemSnapshot getLatestSnapshot() {
        return latestSnapshot;
    }

    public void stop() {
        scheduler.shutdown();
    }
}
