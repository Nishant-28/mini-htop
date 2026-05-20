package com.minihtop.render;

import com.minihtop.App;
import com.minihtop.model.SystemSnapshot;
import com.minihtop.render.widgets.CpuWidget;
import com.minihtop.render.widgets.MemoryWidget;
import com.minihtop.render.widgets.ProcessWidget;

public class DashboardRenderer {
    private final CpuWidget cpuWidget;
    private final MemoryWidget memoryWidget;
    private final ProcessWidget processWidget;

    public DashboardRenderer() {
        this.cpuWidget = App.widgets.contains("cpu") ? new CpuWidget() : null;
        this.memoryWidget = App.widgets.contains("memory") ? new MemoryWidget() : null;
        this.processWidget = App.widgets.contains("process") ? new ProcessWidget() : null;
    }

    public String render(SystemSnapshot snapshot) {
        long uptimeSecs = snapshot.uptimeSeconds();
        long hours = uptimeSecs / 3600;
        long minutes = (uptimeSecs % 3600) / 60;
        long seconds = uptimeSecs % 60;
        String formattedUptime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        String header = "mini-htop | Uptime: " + formattedUptime + " | Timestamp: " + snapshot.timestamp();
        StringBuilder sb = new StringBuilder();
        sb.append("\033[2J\033[H");
        sb.append(header);
        sb.append("\n");

        if (cpuWidget != null) {
            sb.append(cpuWidget.render(snapshot));
            sb.append("\n");
        }
        if (memoryWidget != null) {
            sb.append(memoryWidget.render(snapshot));
            sb.append("\n");
        }
        if (processWidget != null) {
            sb.append(processWidget.render(snapshot));
            sb.append("\n");
        }

//        sb.append(cpuWidget.render(snapshot));
//        sb.append("\n");
//        sb.append(memoryWidget.render(snapshot));
//        sb.append("\n");
//        sb.append(processWidget.render(snapshot));

        return sb.toString();
    }
}
