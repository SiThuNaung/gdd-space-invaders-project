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

    private Player targetPlayer;
    private double angleToPlayer;
    private double speed = 3; // flying speed
    private boolean isMoving = true;

    public FlyingAlien(int x, int y, Player player) {
        super(x, y);
        this.type = "FlyingAlien";
        this.targetPlayer = player;
        ImageIcon ii = new ImageIcon(IMG_FLYING_ALIEN);
        setImage(ii.getImage());
        currentFrame = flyingFrames[0];
        updateAngleToPlayer();
    }

    private void updateAngleToPlayer() {
        if (targetPlayer != null && targetPlayer.isVisible()) {
            int px = targetPlayer.getX() + targetPlayer.getWidth() / 2;
            int py = targetPlayer.getY() + targetPlayer.getHeight() / 2;
            int mx = this.x + getWidth() / 2;
            int my = this.y + getHeight() / 2;

            double dx = px - mx;
            double dy = py - my;

            angleToPlayer = Math.atan2(dy, dx);
            isMoving = Math.sqrt(dx * dx + dy * dy) > 5;
        }
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
        updateAngleToPlayer();

        // Move toward player if not too close
        if (isMoving) {
            this.x += (int)(Math.cos(angleToPlayer) * speed);
            this.y += (int)(Math.sin(angleToPlayer) * speed);
        }

        // Animation frame switching
        animationCounter++;
        if (animationCounter % ANIMATION_DELAY == 0) {
            currentFrameIndex = (currentFrameIndex + 1) % flyingFrames.length;
            currentFrame = flyingFrames[currentFrameIndex];
        }

        // Keep within screen bounds
        if (this.x < 0) this.x = 0;
        if (this.x > BOARD_WIDTH - getWidth()) this.x = BOARD_WIDTH - getWidth();
        if (this.y < 0) this.y = 0;
        if (this.y > BOARD_HEIGHT - getHeight()) this.y = BOARD_HEIGHT - getHeight();
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
//        animationCounter++;
//
//        if (animationCounter % ANIMATION_DELAY == 0) {
//            currentFrameIndex = (currentFrameIndex + 1) % flyingFrames.length;
//            currentFrame = flyingFrames[currentFrameIndex];
//        }
//        // y += dy;

    }

}
