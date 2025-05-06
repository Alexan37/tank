import java.awt.*;
import java.awt.event.*;
public class TankClient extends Frame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    Tank myTank = new Tank(50, 50);
    //这是一张虚拟图片
    Image offScreenImage = null;
    //paint这个方法不需要被调用，一旦要被重画的时会被自动 调用
    public void paint(Graphics g) {
        myTank.draw(g);
    }
    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH,
                    GAME_HEIGHT);
        }
        15
//拿到这个图片的画笔
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.GREEN);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        print(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }
    public void launchFrame() {
        this.setLocation(300, 50);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.GREEN);
        this.addKeyListener(new KeyMonitor());
        setVisible(true);
        new Thread(new PaintThread()).start();
    }
    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }
    private class PaintThread implements Runnable {
        public void run() {
            while(true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    16
                }
            }
        }
    }
    private class KeyMonitor extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            myTank.KyePressed(e);
        }
    }
}
e) Tank.java
import java.awt.*;
        import java.awt.event.*;
public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;
    private int x, y;
    //是否按下了4个方向键
    private boolean bL = false,
            bU = false,
            bR = false,
            bD = false;
//成员变量：方向
    enum {L, LU, U, RU, R, RD, D, LD, STOP};
    private Direction dir = Direction.STOP;
    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
        Direction }
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x, y, 30, 30);
        17
        g.setColor(c);
        move();
    }
    void move() {
        switch(dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
        }
    }
    public void KyePressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_LEFT:
                18
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locateDirection();
    }
    void locateDirection() {
        if(bL && !bU && !bR && !bD) dir = Direction.L;
        else if(bL && bU && !bR && !bD) dir = Direction.LU;
        else if(!bL && bU && !bR && !bD) dir = Direction.U;
        else if(!bL && bU && bR && !bD) dir = Direction.RU;
        else if(!bL && !bU && bR && !bD) dir = Direction.R;
        else if(!bL && !bU && bR && bD) dir = Direction.RD;
        else if(!bL && !bU && !bR && bD) dir = Direction.D;
        else if(bL && !bU && !bR && bD) dir = Direction.LD;
        else if(!bL && !bU && !bR && !bD) dir =
                Direction.STOP;
    }
}