@Override
public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    switch (key) {
        case KeyEvent.VK_LEFT:
            bL = false;
            break;
        case KeyEvent.VK_UP:
            bU = false;
            break;
        case KeyEvent.VK_RIGHT:
            bR = false;
            break;
        case KeyEvent.VK_DOWN:
            bD = false;
            break;
    }
    locateDirection();
}
