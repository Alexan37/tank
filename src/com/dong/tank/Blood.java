










package com.dong.tank;

import java.awt.*;

public class Blood {
    int x, y, w, h;
    private int step = 0;
    private boolean live = true;

    private final int[][] pos = {
            {350, 300}, {360, 300}, {370, 275},
            {400, 200}, {360, 270}, {365, 290}, {350, 340}
    };

    public Blood() {
        x = pos[0][0];
        y = pos[0][1];
        w = h = 15;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void draw(Graphics g) {
        if (!live) return;
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);
        move();
    }

    private void move() {
        step++;
        if (step == pos.length) step = 0;
        x = pos[step][0];
        y = pos[step][1];
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, w, h);
    }
}







