package com.minihtop.collector;

import com.minihtop.model.ProcessInfo;
import com.minihtop.model.SystemSnapshot;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.time.Instant;
import java.util.List;

public class JvmMetricsCollector implements MetricsCollector {

    private final OperatingSystemMXBean osBean;
    private final MemoryMXBean memoryBean;

    public JvmMetricsCollector(
            OperatingSystemMXBean osBean,
            MemoryMXBean memoryBean
    ) {
        this.osBean = osBean;
        this.memoryBean = memoryBean;
    }

    @Override
    public SystemSnapshot collect() {
        double cpuLoad = osBean.getCpuLoad();
        double cpuPercent = cpuLoad < 0 ? 0 : cpuLoad * 100;

        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();

        long usedMemory = heapMemoryUsage.getUsed();
        long totalMemory = heapMemoryUsage.getMax();

        ProcessCollector collector = new ProcessCollector();
        List<ProcessInfo> processInfos = collector.topProcesses();

        return new SystemSnapshot(cpuPercent, usedMemory, totalMemory, processInfos, Instant.now(), List.of(), 0L);
    }
}
