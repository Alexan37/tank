import java.awt.*;
import java.awt.event.*;
public class TankClient extends Frame {
    int x = 50, y = 50;
//paint这个方法不需要被调用，一旦要被重画的时会被自动 调用
4
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x, y, 30, 30);
        g.setColor(c);
        y += 5;
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
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}