package com.minihtop.render.widgets;

import com.minihtop.model.SystemSnapshot;

import java.util.stream.Collectors;

public class ProcessWidget implements Widget {
    public String render(SystemSnapshot snapshot) {
        String header = String.format("%-8s %-30s", "PID", "NAME");
        String body = snapshot
                .processes()
                .stream()
                .map(p -> String.format("%-8d %-30s", p.pid(), p.name()))
                .collect(Collectors.joining("\n"));

        return header + "\n" + body;
    }
}
