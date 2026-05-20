package com.minihtop.model;

import java.time.Instant;
import java.util.List;

public record SystemSnapshot(
        double cpuUsage,
        long usedMemoryBytes,
        long totalMemoryBytes,
        List<ProcessInfo> processes,
        Instant timestamp,
        List<Double> cpuHistory,
        long uptimeSeconds
) {}