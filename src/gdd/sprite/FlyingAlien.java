package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import javax.swing.ImageIcon;

public class FlyingAlien extends Enemy {
    private int animationCounter = 0;
    private int currentFrameIndex = 0;
    private final int ANIMATION_DELAY = 10;
    private Rectangle currentFrame;

    private final Rectangle[] flyingFrames = new Rectangle[] {
            new Rectangle(203, 15, 51, 28),
            new Rectangle(138, 17, 51, 24),

    };

    public FlyingAlien(int x, int y) {
        super(x, y);
        this.type = "FlyingAlien";
        ImageIcon ii = new ImageIcon(IMG_FLYING_ALIEN);
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

    @Override
    public void act(int direction) {
        this.y++;
        act();
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
        animationCounter++;

        if (animationCounter % ANIMATION_DELAY == 0) {
            currentFrameIndex = (currentFrameIndex + 1) % flyingFrames.length;
            currentFrame = flyingFrames[currentFrameIndex];
        }
        // y += dy;

    }

}
