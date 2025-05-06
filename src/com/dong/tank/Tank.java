Tank.java:
import java.awt.*;
import java.awt.event.*;

public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private int x, y;
    private boolean good;
    private boolean bL = false, bU = false, bR = false, bD = false;
    private Direction dir = Direction.STOP;
    private Direction ptDir = Direction.D;

    TankClient tc;

    enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, TankClient tc) {
        this(x, y, good);
        this.tc = tc;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(good ? Color.RED : Color.BLUE);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);

        int cx = x + WIDTH / 2;
        int cy = y + HEIGHT / 2;

        switch (ptDir) {
            case L:  g.drawLine(cx, cy, x, cy); break;
            case LU: g.drawLine(cx, cy, x, y); break;
            case U:  g.drawLine(cx, cy, cx, y); break;
            case RU: g.drawLine(cx, cy, x + WIDTH, y); break;
            case R:  g.drawLine(cx, cy, x + WIDTH, cy); break;
            case RD: g.drawLine(cx, cy, x + WIDTH, y + HEIGHT); break;
            case D:  g.drawLine(cx, cy, cx, y + HEIGHT); break;
            case LD: g.drawLine(cx, cy, x, y + HEIGHT); break;
            default: break;
        }

        move();
    }

    void move() {
        switch (dir) {
            case L:  x -= XSPEED; break;
            case LU: x -= XSPEED; y -= YSPEED; break;
            case U:  y -= YSPEED; break;
            case RU: x += XSPEED; y -= YSPEED; break;
            case R:  x += XSPEED; break;
            case RD: x += XSPEED; y += YSPEED; break;
            case D:  y += YSPEED; break;
            case LD: x -= XSPEED; y += YSPEED; break;
            case STOP: break;
        }

        if (dir != Direction.STOP) {
            ptDir = dir;
        }

        // Ограничение по границам окна
        if (x < 0) x = 0;
        if (y < 25) y = 25;
        if (x + WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - WIDTH;
        if (y + HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - HEIGHT;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT: bL = true; break;
            case KeyEvent.VK_UP: bU = true; break;
            case KeyEvent.VK_RIGHT: bR = true; break;
            case KeyEvent.VK_DOWN: bD = true; break;
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_LEFT: bL = false; break;
            case KeyEvent.VK_UP: bU = false; break;
            case KeyEvent.VK_RIGHT: bR = false; break;
            case KeyEvent.VK_DOWN: bD = false; break;
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
        int mx = this.x + WIDTH / 2 - Missile.WIDTH / 2;
        int my = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(mx, my, ptDir, this.tc);
        tc.missiles.add(m);
        return m;
    }
}
