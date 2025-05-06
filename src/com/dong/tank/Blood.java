import java.awt.*;
69
public class Blood {
    int x, y, w, h;
    TankClient tc;
    int step = 0;
    private boolean live = true;
    public boolean isLive() {
        return live;
    }
    public void setLive(boolean live) {
        this.live = live;
    }
    private int[][] pos = {
            {350, 300}, {360, 300}, {370, 275},
            {400, 200}, {360, 270}, {365, 290}, {350, 340}
    };
    public Blood() {
        x = pos[0][0];
        y = pos[0][1];
        w = h = 15;
    }
    public void draw(Graphics g) {
        if(!live) {
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);
        move();
    }
    private void move() {
        step++;
        if(step == pos.length) {
            70
            step = 0;
        }
        x = pos[step][0];
        y = pos[step][1];
    }
    public Rectangle getRect() {
        return new Rectangle(x, y, w, h);
    }
}
c) 在 Tank.java 中增加一个吃血条的方法：
public boolean eat(Blood b) {
    if(this.live && b.isLive() &&
            this.getRect().intersects(b.getRect())) {
        this.life = 100;
        b.setLive(false);
        return true;
    }
    return false;
}Adding "blood blocks"