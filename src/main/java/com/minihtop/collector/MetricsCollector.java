package com.minihtop.collector;

import com.minihtop.model.SystemSnapshot;

public interface MetricsCollector {
    SystemSnapshot collect();
}
