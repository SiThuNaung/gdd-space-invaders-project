package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import javax.swing.ImageIcon;

public class AlienUFO extends Enemy {

    private int bombCooldown = 0;
    private int animationCounter = 0;
    private int currentFrameIndex = 0;
    private final int ANIMATION_DELAY = 5;

    private static final String ACT_FLYING = "FLYING";
    private static final String ACT_EXPLOSION = "EXPLOSION";
    private String action = ACT_FLYING;

    private int dy = 1;

    // 🟡 Zig-Zag Movement Variables
    private int dx = 3; // horizontal speed
    private int zigzagCounter = 0;
    private final int ZIGZAG_INTERVAL = 600;

    private final Rectangle[] flyingFrames = new Rectangle[] {
            new Rectangle(15, 33, 28, 29),
            new Rectangle(47, 33, 28, 29),
            new Rectangle(79, 33, 28, 28),
            new Rectangle(111, 33, 28, 28),
            new Rectangle(143, 33, 28, 28),
            new Rectangle(175, 33, 28, 28),
            new Rectangle(207, 33, 28, 27),
            new Rectangle(239, 33, 28, 28),
            new Rectangle(271, 33, 28, 28),
            new Rectangle(303, 33, 28, 28),
            new Rectangle(335, 33, 28, 27),
            new Rectangle(367, 33, 28, 27),
            new Rectangle(399, 33, 28, 28),
            new Rectangle(430, 33, 29, 28),
            new Rectangle(463, 33, 28, 28),
            new Rectangle(495, 33, 28, 29),
    };

    private Rectangle currentFrame;

    public AlienUFO(int x, int y) {
        super(x, y);
        this.type = "AlienUFO";
        initAlienUFO(x, y);
    }

    public void initAlienUFO(int x, int y) {
        this.x = x;
        this.y = y;
        ImageIcon ii = new ImageIcon(IMG_ALIEN_UFO);
        setImage(ii.getImage());
        currentFrame = flyingFrames[0];
    }

    @Override
    public Image getImage() {
        if (image == null || currentFrame == null) {
            return image;
        }
        BufferedImage bImage = toBufferedImage(image);
        int frameX = currentFrame.x;
        int frameY = currentFrame.y;
        int frameWidth = currentFrame.width;
        int frameHeight = currentFrame.height;

        if (frameX + frameWidth > bImage.getWidth()) {
            frameWidth = bImage.getWidth() - frameX;
        }
        if (frameY + frameHeight > bImage.getHeight()) {
            frameHeight = bImage.getHeight() - frameY;
        }

        try {
            return bImage.getSubimage(frameX, frameY, frameWidth, frameHeight);
        } catch (RasterFormatException e) {
            return bImage;
        }
    }

    // 🟢 MODIFIED: Zig-Zag Movement Logic Added Here
    public void act(int direction) {
        // Zig-zag pattern
        x += dx;
        y += dy;

        // Bounce when time or edge is reached
        zigzagCounter++;
        if (zigzagCounter >= ZIGZAG_INTERVAL || x <= 0 || x + getWidth() >= BOARD_WIDTH) {
            dx = -dx;
            zigzagCounter = 0;
        }

        act(); // run animation logic
    }

    @Override
    public void act() {
        super.act();
        animationCounter++;

        if (ACT_FLYING.equals(action)) {
            if (animationCounter % ANIMATION_DELAY == 0) {
                currentFrameIndex = (currentFrameIndex + 1) % flyingFrames.length;
                currentFrame = flyingFrames[currentFrameIndex];
            }
        }
    }

    @Override
    public int getWidth() {
        return currentFrame != null ? currentFrame.width : super.getWidth();
    }

    @Override
    public int getHeight() {
        return currentFrame != null ? currentFrame.height : super.getHeight();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
}
