import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface Command extends KeyListener {
    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    void openInventory();

    void openOptions();

    @Override
    default void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                moveUp();
                break;
            case KeyEvent.VK_S:
                moveDown();
                break;
            case KeyEvent.VK_A:
                moveLeft();
                break;
            case KeyEvent.VK_D:
                moveRight();
                break;
            case KeyEvent.VK_E:
                openInventory();
                break;
            case KeyEvent.VK_ESCAPE:
                openOptions();
                break;
        }
    }

    @Override
    default void keyReleased(KeyEvent e) {
    }

    @Override
    default void keyTyped(KeyEvent e) {
    }
}