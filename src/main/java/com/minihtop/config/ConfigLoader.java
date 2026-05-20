package com.minihtop.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {

    public static DashboardConfig load() throws IOException {
        InputStream is = ConfigLoader.class.getResourceAsStream("/config.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(is, DashboardConfig.class);
    }
}
