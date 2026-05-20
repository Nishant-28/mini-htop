package com.minihtop.config;

import java.util.List;

public record DashboardConfig(
        int refreshIntervalSeconds,
        int maxProcesses,
        int sparklineLength,
        List<String> widgets
) {
}
