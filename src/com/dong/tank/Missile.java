// ✅ Missile.java — оптимизированный
package com.dong.tank;

import java.awt.*;
import java.util.List;

public class Missile {
    public static final int XSPEED = 10;
    public static final int YSPEED = 10;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    private int x, y;
    private boolean good;
    private boolean live = true;
    private final TankClient tc;
    private final Tank.Direction dir;

    public boolean isLive() {
        return live;
    }

    public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.good = good;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) return;
        Color c = g.getColor();
        g.setColor(good ? Color.RED : Color.BLACK);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);
        move();
    }

    private void move() {
        switch (dir) {
            case L -> x -= XSPEED;
            case LU -> { x -= XSPEED; y -= YSPEED; }
            case U -> y -= YSPEED;
            case RU -> { x += XSPEED; y -= YSPEED; }
            case R -> x += XSPEED;
            case RD -> { x += XSPEED; y += YSPEED; }
            case D -> y += YSPEED;
            case LD -> { x -= XSPEED; y += YSPEED; }
        }

        if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
            live = false;
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean collidesWithTank(Tank t) {
        if (!live || !t.isLive() || good == t.isGood() || !getRect().intersects(t.getRect())) return false;
        if (t.isGood()) {
            t.setLife(t.getLife() - 20);
            if (t.getLife() <= 0) t.setLive(false);
        } else {
            t.setLive(false);
        }
        this.live = false;
        tc.explodes.add(new Explode(x, y, tc));
        return true;
    }

    public boolean collidesWithTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            if (collidesWithTank(tanks.get(i))) return true;
        }
        return false;
    }

    public boolean collidesWithWall(Wall w) {
        if (live && getRect().intersects(w.getRect())) {
            live = false;
            return true;
        }
        return false;
    }
}
