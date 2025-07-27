package gdd.sprite;

import static gdd.Global.*;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

import javax.swing.ImageIcon;

public class Explosion extends Sprite {
    private BufferedImage spriteSheet;

    private int animationCounter = 0;
    private int currentFrameIndex = 0;
    private final int ANIMATION_DELAY = 3;
    private Rectangle currentFrame;

    // private static final String ACT_FLYING = "FLYING";
    private static final String ACT_EXPLOSION = "EXPLOSION";
    private String action = ACT_EXPLOSION;

    private  Rectangle[] FlyingAlienExplosionFrames = new Rectangle[] {
        new Rectangle(382, 90, 53, 36),
        new Rectangle(321, 91, 52, 36),
        new Rectangle(262, 93, 49, 33),
        new Rectangle(211, 95, 34, 30),
        new Rectangle(150, 95, 27, 30),
        new Rectangle(90, 95, 24, 27),
        new Rectangle(31, 95, 20, 25)
};

    private  Rectangle[] explosionFrames = new Rectangle[] {
            new Rectangle(388, 173, 31, 24),
            new Rectangle(328, 173, 31, 24),
            new Rectangle(269, 172, 30, 25),
            new Rectangle(207, 173, 32, 24),
            new Rectangle(150, 171, 29, 26),
            new Rectangle(90, 171, 29, 26),
            new Rectangle(31, 169, 28, 29)
    };

    @Override
    public int getWidth() {
        return currentFrame != null ? currentFrame.width : super.getWidth();
    }

    @Override
    public int getHeight() {
        return currentFrame != null ? currentFrame.height : super.getHeight();
    }

    public Explosion(int x, int y, String type) {
        initExplosion(x, y, type);
    }
    
    private Rectangle[] currentFrameSet;

    private void initExplosion(int x, int y, String type) {
        this.x = x;
        this.y = y;
    
        // Select correct explosion sprite
        switch (type) {
            case "FlyingAlien":
                setImage(new ImageIcon(IMG_FLYING_ALIEN_EXPLOSION).getImage());
                currentFrameSet = FlyingAlienExplosionFrames;
                //explosionFrames = FlyingAlienExplosionFrames;
                break;
    
            case "AlienUFO":
            default:
                setImage(new ImageIcon(IMG_ALIEN_UFO).getImage());
                currentFrameSet = explosionFrames;
                //explosionFrames = explosionFrames;
                break;
        }
        currentFrameIndex = 0;
        currentFrame = currentFrameSet[0];
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

    public void act(int direction) {

        // this.x += direction;
    }

    @Override
    public void act() {
        animationCounter++;
        if (animationCounter % ANIMATION_DELAY == 0) {
            currentFrameIndex++;
            if (currentFrameSet == null || currentFrameIndex >= currentFrameSet.length) {
                setVisible(false);
                currentFrame = null;
            } else {
                currentFrame = currentFrameSet[currentFrameIndex];
            }
        }
    }
}
