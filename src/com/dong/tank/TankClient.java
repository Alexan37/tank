import java.awt.*;
import java.awt.event.*;
public class TankClient extends Frame {
    int x = 50, y = 50;
    //这是一张虚拟图片
    Image offScreenImage = null;
    //paint这个方法不需要被调用，一旦要被重画的时会被自动 调用
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x, y, 30, 30);
        g.setColor(c);
        y += 5;
    }
    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(800, 600);
        }
//拿到这个图片的画笔
        Graphics gOffScreen = offScreenImage.getGraphics();
        6
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.GREEN);
        gOffScreen.fillRect(0, 0, 800, 600);
        gOffScreen.setColor(c);
        print(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }
    public void launchFrame() {
        this.setLocation(300, 50);
        this.setSize(800, 600);
        this.setTitle("TankWar");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.GREEN);
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
                }
            }
        }
    }
}