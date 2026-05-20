package com.minihtop.collector;

import com.minihtop.App;
import com.minihtop.model.ProcessInfo;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalLong;

public class ProcessCollector {

    public List<ProcessInfo> topProcesses() {
        return ProcessHandle.allProcesses()
                .filter(ph -> ph.info().command().isPresent())
                .sorted(Comparator.comparingLong(ProcessHandle::pid))
                .limit(App.maxProcesses)
                .map(ph -> {
                    String command = ph.info().command().orElse("Unknown");
                    String name = command;
                    int lastSlash = command.lastIndexOf('\\');
                    if (lastSlash == -1) {
                        lastSlash = command.lastIndexOf('/');
                    }
                    if (lastSlash != -1) {
                        name = command.substring(lastSlash + 1);
                    }
                    return new ProcessInfo(
                            ph.pid(),
                            name,
                            OptionalLong.empty()
                    );
                })
                .toList();
    }
}
