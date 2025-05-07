// ✅ Tank.java — флаг России у игрока, флаг Украины у врагов, шкала жизни только у игрока
package com.dong.tank;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private boolean live = true;
    private int life = 100;
    private TankClient tc = null;

    private int x, y;
    private int oldX, oldY;
    private boolean good;
    private static Random r = new Random();

    private boolean bL = false, bU = false, bR = false, bD = false;

    public enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}
    private Direction dir = Direction.STOP;
    private Direction ptDir = Direction.D;

    private int step = r.nextInt(12) + 3;

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y, good);
        this.dir = dir;
        this.oldX = x;
        this.oldY = y;
        this.tc = tc;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isGood() {
        return good;
    }

    public void draw(Graphics g) {
        if (!live) {
            if (!good) tc.tanks.remove(this);
            return;
        }

        Color c = g.getColor();

        if (good) {
            // Флаг России: бело-сине-красный
            g.setColor(Color.WHITE);
            g.fillRect(x, y, WIDTH, HEIGHT / 3);
            g.setColor(Color.BLUE);
            g.fillRect(x, y + HEIGHT / 3, WIDTH, HEIGHT / 3);
            g.setColor(Color.RED);
            g.fillRect(x, y + 2 * HEIGHT / 3, WIDTH, HEIGHT / 3);
        } else {
            // Флаг Украины: жёлто-синий
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, WIDTH, HEIGHT / 2);
            g.setColor(Color.BLUE);
            g.fillRect(x, y + HEIGHT / 2, WIDTH, HEIGHT / 2);
        }

        g.setColor(c);

        if (good) drawBloodBar(g);
        move();

        int cx = x + WIDTH / 2;
        int cy = y + HEIGHT / 2;
        switch (ptDir) {
            case L -> g.drawLine(cx, cy, x, cy);
            case LU -> g.drawLine(cx, cy, x, y);
            case U -> g.drawLine(cx, cy, cx, y);
            case RU -> g.drawLine(cx, cy, x + WIDTH, y);
            case R -> g.drawLine(cx, cy, x + WIDTH, cy);
            case RD -> g.drawLine(cx, cy, x + WIDTH, y + HEIGHT);
            case D -> g.drawLine(cx, cy, cx, y + HEIGHT);
            case LD -> g.drawLine(cx, cy, x, y + HEIGHT);
        }
    }

    private void drawBloodBar(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.drawRect(x, y - 10, WIDTH, 10);
        int w = WIDTH * life / 100;
        g.fillRect(x, y - 10, w, 10);
        g.setColor(c);
    }

    void move() {
        oldX = x;
        oldY = y;

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

        if (dir != Direction.STOP) ptDir = dir;

        if (x < 0) x = 0;
        if (y < 25) y = 25;
        if (x + WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - WIDTH;
        if (y + HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - HEIGHT;

        if (!good) {
            if (step == 0) {
                step = r.nextInt(12) + 3;
                dir = Direction.values()[r.nextInt(8)];
            }
            step--;
            if (r.nextInt(10) > 2) fire();
        }
    }

    private void stay() {
        x = oldX;
        y = oldY;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> bL = true;
            case KeyEvent.VK_W -> bU = true;
            case KeyEvent.VK_D -> bR = true;
            case KeyEvent.VK_S -> bD = true;
            case KeyEvent.VK_R -> {
                if (!live) {
                    live = true;
                    life = 100;
                }
            }
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER -> fire();
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> bL = false;
            case KeyEvent.VK_UP, KeyEvent.VK_W -> bU = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> bR = false;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> bD = false;
            case KeyEvent.VK_P -> superFire();
        }
        locateDirection();
    }

    void locateDirection() {
        if (bL && !bU && !bR && !bD) dir = Direction.L;
        else if (bL && bU && !bR && !bD) dir = Direction.LU;
        else if (!bL && bU && !bR && !bD) dir = Direction.U;
        else if (!bL && bU && bR && !bD) dir = Direction.RU;
        else if (!bL && !bU && bR && !bD) dir = Direction.R;
        else if (!bL && !bU && bR && bD) dir = Direction.RD;
        else if (!bL && !bU && !bR && bD) dir = Direction.D;
        else if (bL && !bU && !bR && bD) dir = Direction.LD;
        else dir = Direction.STOP;
    }

    public Missile fire() {
        if (!live) return null;
        int mx = x + WIDTH / 2 - Missile.WIDTH / 2;
        int my = y + HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(mx, my, good, ptDir, tc);
        tc.missiles.add(m);
        return m;
    }

    public Missile fire(Direction dir) {
        if (!live) return null;
        int mx = x + WIDTH / 2 - Missile.WIDTH / 2;
        int my = y + HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(mx, my, good, dir, tc);
        tc.missiles.add(m);
        return m;
    }

    private void superFire() {
        for (Direction d : Direction.values()) {
            fire(d);
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean collidesWithWall(Wall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks) {
        for (Tank t : tanks) {
            if (this != t && this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
                this.stay();
                t.stay();
                return true;
            }
        }
        return false;
    }

    public boolean eat(Blood b) {
        if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
            this.life = 100;
            b.setLive(false);
            return true;
        }
        return false;
    }
}
