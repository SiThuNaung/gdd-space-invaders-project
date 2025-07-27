package gdd.sprite;

import static gdd.Global.*;

import javax.swing.ImageIcon;
import java.awt.Image;

public class Shot extends Sprite {
    private static final int H_SPACE = 20;
    private static final int V_SPACE = 1;
    private int dx = 0;  // horizontal velocity
    private int dy = -2; // vertical velocity (negative = up)

    private static final int V_OFFSET = 5;
    private static final double SCALE = 3.0;

    public Shot() {}

    public Shot(int shipX, int shipY, int shipWidth) {
        initShot(shipX, shipY, shipWidth);
    }

    private void initShot(int shipX, int shipY, int shipWidth) {
        var ii = new ImageIcon(IMG_SHOT);
        var baseImage = ii.getImage();

        int scaledWidth = (int)(baseImage.getWidth(null) * SCALE);
        int scaledHeight = (int)(baseImage.getHeight(null) * SCALE);

        Image scaledShot = baseImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        setImage(scaledShot);

        // Position shot at center of ship
        int shotX = shipX + (shipWidth - scaledWidth) / 2;
        int shotY = shipY - V_OFFSET;

        setX(shotX);
        setY(shotY);
    }

    public Shot(int x, int y, int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        initShot(x, y);
    }

    private void initShot(int x, int y) {
        var ii = new ImageIcon(IMG_SHOT);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);

        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }

    @Override
    public void act() {
        // Move bullet based on its velocity
        setX(getX() + dx);
        setY(getY() + dy);

        // Move the shot upward
        y -= 10; // adjust shot speed if needed
        if (y < 0) visible = false;
    }

    // Getters for velocity (if needed elsewhere)
    public int getDx() { return dx; }
    public int getDy() { return dy; }
}