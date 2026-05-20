package com.minihtop.render.widgets;

import com.minihtop.model.SystemSnapshot;

public interface Widget {
    String render(SystemSnapshot snapshot);
}
