package com.minihtop.model;

import java.util.OptionalLong;

public record ProcessInfo(long pid, String name, OptionalLong memoryBytes) {
}
