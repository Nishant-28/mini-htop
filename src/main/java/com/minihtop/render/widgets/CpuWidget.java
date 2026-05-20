package com.minihtop.render.widgets;

import com.minihtop.model.SystemSnapshot;

public class CpuWidget implements Widget {
    private static final int BAR_WIDTH = 20;
    private static final String[] SPARK = {"▁","▂","▃","▄","▅","▆","▇","█"};

    public String render(SystemSnapshot snapshot) {
        int filledWidth = (int) ((snapshot.cpuUsage() / 100.0) * BAR_WIDTH);
        filledWidth = Math.max(0, Math.min(BAR_WIDTH, filledWidth));

        String filled = "█".repeat(filledWidth);
        String empty = "░".repeat(BAR_WIDTH - filledWidth);

        String color;

        if (snapshot.cpuUsage() >= 90)
            color = "\u001B[31m";
        else if  (snapshot.cpuUsage() >= 70)
            color = "\u001B[33m";
        else
            color = "\u001B[32m";

        StringBuilder sparkline = new StringBuilder();
        if (snapshot.cpuHistory() != null) {
            for (double val : snapshot.cpuHistory()) {
                int sparkIndex = (int) Math.min((val / 100.0) * 8.0, 7.0);
                sparkIndex = Math.max(0, Math.min(7, sparkIndex));
                sparkline.append(SPARK[sparkIndex]);
            }
        }

        return String.format("CPU    [%s%s\u001B[0m%s]  %.1f%%  History: %s",
                color, filled, empty, snapshot.cpuUsage(), sparkline.toString());
    }
}
