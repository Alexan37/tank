package com.dong.tank;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    Tank myTank = new Tank(300, 300, true, Tank.Direction.STOP, this);
    Blood b = new Blood();
    Wall w1 = new Wall(100, 200, 20, 150, this),
            w2 = new Wall(300, 100, 300, 20, this);

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

        for(int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.collidesWithTanks(tanks);
            m.collidesWithTank(myTank);
            m.collidesWithWall(w1);
            m.collidesWithWall(w2);
            m.draw(g);
        }

        for(Explode e : explodes) e.draw(g);
        for(Tank t : tanks) {
            t.collidesWithWall(w1);
            t.collidesWithWall(w2);
            t.collidesWithTanks(tanks);
            t.draw(g);
        }

        myTank.draw(g);
        myTank.eat(b);
        w1.draw(g);
        w2.draw(g);
        b.draw(g);
    }

    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOff = offScreenImage.getGraphics();
        Color c = gOff.getColor();
        gOff.setColor(Color.GREEN);
        gOff.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOff.setColor(c);
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void launchFrame() {
        for(int i = 0; i < 10; i++) {
            tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Tank.Direction.D, this));
        }
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

