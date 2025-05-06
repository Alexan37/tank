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
    private TankClient tc;
    private Tank.Direction dir;

    public boolean isLive() {
        return live;
    }

    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
        this(x, y, dir);
        this.good = good;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.missiles.remove(this);
            return;
        }
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
        if (this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
            t.setLife(t.getLife() - 20);
            if (t.getLife() <= 0) {
                t.setLive(false);
            }
            this.live = false;
            tc.explodes.add(new Explode(x, y, tc));
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks) {
        for (Tank t : tanks) {
            if (collidesWithTank(t)) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesWithWall(Wall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.live = false;
            return true;
        }
        return false;
    }
}
