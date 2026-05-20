package com.minihtop.render.widgets;

import com.minihtop.model.SystemSnapshot;

public class MemoryWidget implements Widget {
    private static final int BAR_WIDTH = 20;

    public String render(SystemSnapshot snapshot) {
        long totalBytes = snapshot.totalMemoryBytes();
        long usedBytes = snapshot.usedMemoryBytes();

        long totalMB = totalBytes / 1024 / 1024;
        long usedMB = usedBytes / 1024 / 1024;

        double memPercent = totalBytes > 0 ? (usedBytes * 100.0) / totalBytes : 0.0;

        int filledWidth = (int) ((memPercent / 100.0) * BAR_WIDTH);
        filledWidth = Math.max(0, Math.min(BAR_WIDTH, filledWidth));

        String filled = "█".repeat(filledWidth);
        String empty = "░".repeat(BAR_WIDTH - filledWidth);

        String color;
        if (memPercent >= 90) {
            color = "\u001B[31m";
        } else if (memPercent >= 70) {
            color = "\u001B[33m";
        } else {
            color = "\u001B[32m";
        }

        return String.format("Memory [%s%s\u001B[0m%s]  %.1f%%  (%d MB / %d MB)",
                color, filled, empty, memPercent, usedMB, totalMB);
    }
}

// Bengaluru, India, 560076

// inet.nishant@gmail.com

// https://www.linkedin.com/in/thenishantsingh/

// https://github.com/Nishant-28/

// – Engineered RESTful APIs for core HR SaaS modules (Dashboard, Customer, Product) using Spring Boot, implementing a layered architecture to ensure scalability and maintainability across 5 corporate client deployments.
// – Developed service layer business logic in a microservices architecture to streamline HR operations including employee lifecycle and attendance management, reducing manual processing steps by ∼30%.
// – Identified and resolved 8 bugs across backend services, improving system stability and reducing service-layer failures across 4 backend modules in a live SaaS production environment.
// – Collaborated with the engineering team using Git for version control, designing new features and participating in code reviews to maintain clean, modular code across sprint cycles.
