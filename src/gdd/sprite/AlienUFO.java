package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

public class AlienUFO extends Enemy {

    private List<Bomb> bombs = new ArrayList<>();
    private int bombCooldown = 0;

    private int animationCounter = 0;
    private int currentFrameIndex = 0;
    private final int ANIMATION_DELAY = 5;

    private static final String ACT_FLYING = "FLYING";
    private static final String ACT_EXPLOSION = "EXPLOSION";
    private String action = ACT_FLYING;

    private int dy = 3;

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
        // bomb = new Bomb(x, y);
        this.type = "AlienUFO";
        initAlienUFO(x, y);
        // ImageIcon ii = new ImageIcon(IMG_ALIEN_UFO);
        // setImage(ii.getImage());
        // currentFrame = flyingFrames[0];

    }

    public void initAlienUFO(int x, int y) {
        this.x = x;
        this.y = y;

        ImageIcon ii = new ImageIcon(IMG_ALIEN_UFO);
        setImage(ii.getImage());
        currentFrame = flyingFrames[0];
//        bomb = new Bomb(x, y);
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

        // Clamp frame dimensions to image bounds to avoid exceptions
        if (frameX + frameWidth > bImage.getWidth()) {
            frameWidth = bImage.getWidth() - frameX;
        }
        if (frameY + frameHeight > bImage.getHeight()) {
            frameHeight = bImage.getHeight() - frameY;
        }

        try {
            return bImage.getSubimage(frameX, frameY, frameWidth, frameHeight);
        } catch (RasterFormatException e) {
            // Fallback if something still goes wrong
            return bImage;
        }
    }

    // @Override
    // public void act(int direction) {
    // super.act(direction);
    // act();
    // }

    public void act(int direction) {
        this.y++;
        act();
        // Bomb dropping logic
        tickCooldown();
        if (canDropBomb() && Math.random() < 0.02) { // 2% chance per frame
            dropBomb();
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
    public void act() {
        super.act();
        animationCounter++;

        if (ACT_FLYING.equals(action)) {
            if (animationCounter % ANIMATION_DELAY == 0) {
                currentFrameIndex = (currentFrameIndex + 1) % flyingFrames.length;
                currentFrame = flyingFrames[currentFrameIndex];
            }
        }
        // // Drop bombs periodically
        // if (bombCooldown <= 0) {
        // dropBomb();
        // bombCooldown = 120; // Adjust delay between bombs
        // } else {
        // bombCooldown--;
        // }

        // // Update bombs
        // Iterator<Bomb> iter = bombs.iterator();
        // while (iter.hasNext()) {
        // Bomb b = iter.next();
        // b.act();
        // if (!b.isVisible()) {
        // iter.remove();
        // }
        // }

    }

    // public void dropBomb() {
    // bombs.add(new Bomb(x + getWidth() / 2, y + getHeight()));
    // }

    // public List<Bomb> getBombs() {
    // return bombs;
    // }

    // public class Bomb extends Sprite {
    // private boolean destroyed;
    // public Bomb(int x, int y) {
    // this.x = x;
    // this.y = y;
    // destroyed = false;

    // var ii = new ImageIcon("src/images/bomb.png");
    // setImage(ii.getImage());
    // }

    // @Override
    // public void act() {
    // y += 4; // Bomb fall speed
    // if (y > BOARD_HEIGHT) {
    // setVisible(false);
    // }
    // }

    // public boolean isDestroyed() {
    // return destroyed;
    // }

    // public void setDestroyed(boolean val) {
    // destroyed = val;
    // }
    // }

}
