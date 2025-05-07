// ✅ Explode.java
package com.dong.tank;

import java.awt.*;

public class Explode {
    int x, y;
    private boolean live = true;
    private TankClient tc;

    int[] diameter = {4, 8, 16, 24, 32, 40, 48, 32, 16, 8};
    int step = 0;

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.explodes.remove(this);
            return;
        }

        if (step >= diameter.length) {
            live = false;
            step = 0;
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.ORANGE);
        g.fillOval(x - diameter[step] / 2, y - diameter[step] / 2, diameter[step], diameter[step]);
        g.setColor(c);
        step++;
    }
}
