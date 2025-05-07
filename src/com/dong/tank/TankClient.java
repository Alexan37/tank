// ✅ TankClient.java — оптимизированный и обновлённый
package com.dong.tank;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    Tank myTank = new Tank(300, 300, true, Tank.Direction.STOP, this);

    List<Wall> walls = new ArrayList<>();
    List<Blood> bloods = new ArrayList<>();
    List<Explode> explodes = new ArrayList<>();
    List<Missile> missiles = new ArrayList<>();
    List<Tank> tanks = new ArrayList<>();

    Image offScreenImage = null;

    public void paint(Graphics g) {
        g.drawString("missiles count: " + missiles.size(), 10, 50);
        g.drawString("explodes count: " + explodes.size(), 10, 70);
        g.drawString("tanks count: " + tanks.size(), 10, 90);
        g.drawString("tank life: " + myTank.getLife(), 10, 110);

        if(tanks.size() <= 0) {
            for(int i = 0; i < 5; i++) {
                tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Tank.Direction.D, this));
            }
        }

        for (Missile m : new ArrayList<>(missiles)) {
            m.collidesWithTanks(tanks);
            m.collidesWithTank(myTank);
            for (Wall wall : walls) m.collidesWithWall(wall);
            m.draw(g);
        }

        for (Explode e : new ArrayList<>(explodes)) e.draw(g);
        for (Tank t : new ArrayList<>(tanks)) {
            for (Wall wall : walls) t.collidesWithWall(wall);
            t.collidesWithTanks(tanks);
            t.draw(g);
        }

        myTank.draw(g);

        for (Wall wall : walls) wall.draw(g);
        for (Blood b : new ArrayList<>(bloods)) {
            if (myTank.eat(b)) break;
            b.draw(g);
        }
    }

    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOff = offScreenImage.getGraphics();
        Color c = gOff.getColor();
        gOff.setColor(new Color(144, 238, 144));
        gOff.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOff.setColor(c);
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void launchFrame() {
        // стены (S, B, O в виде больших букв)
        walls.add(new Wall(100, 100, 20, 200, this)); // S
        walls.add(new Wall(100, 100, 100, 20, this));
        //walls.add(new Wall(100, 190, 100, 20, this));
        walls.add(new Wall(100, 280, 100, 20, this));

        walls.add(new Wall(250, 100, 20, 200, this)); // B
        walls.add(new Wall(250, 100, 100, 20, this));
        walls.add(new Wall(250, 190, 100, 20, this));
        walls.add(new Wall(250, 280, 100, 20, this));
        walls.add(new Wall(330, 100, 20, 100, this));
        walls.add(new Wall(330, 190, 20, 110, this));

        walls.add(new Wall(450, 100, 20, 200, this)); // O
        walls.add(new Wall(450, 100, 100, 20, this));
        walls.add(new Wall(450, 280, 100, 20, this));
        walls.add(new Wall(530, 100, 20, 200, this));

        // аптечки в 4 углах
        bloods.add(new Blood(20, 40));
        bloods.add(new Blood(750, 40));
        bloods.add(new Blood(20, 550));
        bloods.add(new Blood(750, 550));

        for(int i = 0; i < 10; i++) {
            tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Tank.Direction.D, this));
        }

        this.setLocation(300, 50);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        this.setResizable(false);
        this.setBackground(new Color(144, 238, 144));
        this.addKeyListener(new KeyMonitor());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
        new Thread(new PaintThread()).start();
    }

    public static void main(String[] args) {
        new TankClient().launchFrame();
    }

    private class PaintThread implements Runnable {
        public void run() {
            while(true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }
}
