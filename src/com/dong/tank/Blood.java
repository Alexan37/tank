// ✅ Blood.java — оптимизированная версия
package com.dong.tank;

import java.awt.*;

public class Blood {
    private final int x, y;
    private static final int SIZE = 15;
    private boolean live = true;

    public Blood(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void draw(Graphics g) {
        if (!live) return;
        Color oldColor = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, SIZE, SIZE);
        g.setColor(oldColor);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
}
