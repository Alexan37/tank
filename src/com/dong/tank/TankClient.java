import java.awt.*;
import java.awt.event.*;
public class TankClient extends Frame {
    //paint这个方法不需要被调用，一旦要被重画的时会被自动 调用
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(50, 50, 30, 30);
        g.setColor(c);
    }
    public void launchFrame() {
        3
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
    }
    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }
}