
public boolean hitTank(Tank t) {
    if (this.getRect().intersects(t.getRect()) && t.isLive()) {
        t.setLive(false);
        this.live = false;
        // создаём взрыв в центре танка
        Explode e = new Explode(t.getX() + Tank.WIDTH / 2, t.getY() + Tank.HEIGHT / 2, tc);
        tc.explodes.add(e);
        return true;
    }
    return false;
}

// === Исправленный метод paint() в TankClient.java ===
public void paint(Graphics g) {
    // вывод количества снарядов
    g.drawString("missiles count: " + missiles.size(), 10, 50);
    // вывод количества взрывов
    g.drawString("explodes count: " + explodes.size(), 10, 70);

    // отрисовка снарядов и проверка попадания
    for (int i = 0; i < missiles.size(); i++) {
        Missile m = missiles.get(i);
        m.hitTank(enemyTank);
        m.draw(g);
    }

    // отрисовка взрывов
    for (int i = 0; i < explodes.size(); i++) {
        Explode e = explodes.get(i);
        if (e.isLive()) {
            e.draw(g);
        } else {
            explodes.remove(i);
            i--;
        }
    }

    // отрисовка танков
    myTank.draw(g);
    enemyTank.draw(g);
}
