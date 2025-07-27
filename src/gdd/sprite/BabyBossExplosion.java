package gdd.sprite;

import static gdd.Global.*;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.List;
import javax.swing.ImageIcon;

public class BabyBossExplosion extends Sprite {
    private int animationCounter = 0;
    private int currentFrameIndex = 0;
    private final int ANIMATION_DELAY = 3; // Increased from 4 to 8 for slower animation
    private Rectangle currentFrame;

    // Explosion frames for 395×632 sprite sheet with 101×108 frames
    private Rectangle[] explosionFrames = new Rectangle[] {
        // Row 1
        new Rectangle(0, 0, 101, 108),     // Frame 1
        new Rectangle(101, 0, 101, 108),   // Frame 2
        new Rectangle(202, 0, 101, 108),   // Frame 3
        new Rectangle(303, 0, 101, 108),   // Frame 4
        
        // Row 2
        new Rectangle(0, 108, 111, 108),   // Frame 5
        new Rectangle(111, 108, 111, 108), // Frame 6
        new Rectangle(222, 108, 111, 108), // Frame 7
        new Rectangle(333, 108, 111, 108), // Frame 8
        new Rectangle(444, 108, 111, 108), // Frame 9
        
        // // Row 3
        new Rectangle(0, 216, 111, 108),   // Frame 5
        new Rectangle(111, 216, 111, 108), // Frame 6
       
    };

    @Override
    public int getWidth() {
        return currentFrame != null ? currentFrame.width : 101;
    }

    @Override
    public int getHeight() {
        return currentFrame != null ? currentFrame.height : 108;
    }

    public BabyBossExplosion(int x, int y) {
        initExplosion(x, y);
    }

    private void initExplosion(int x, int y) {
        // Center the explosion on the baby boss position
        this.x = x - 11; // Center 101px explosion on ~79px baby boss
        this.y = y - 14; // Center 108px explosion on ~79px baby boss
        
        setImage(new ImageIcon(IMG_BABY_BOSS_EXPLOSION).getImage());
        currentFrameIndex = 0;
        currentFrame = explosionFrames[0];
        setVisible(true);
        
        // Debug output
        System.out.println("Explosion initialized at: " + this.x + ", " + this.y);
        System.out.println("Total explosion frames: " + explosionFrames.length);
        System.out.println("Animation delay: " + ANIMATION_DELAY + " ticks");
        System.out.println("Expected duration: " + (explosionFrames.length * ANIMATION_DELAY) + " ticks (~" + 
                          (explosionFrames.length * ANIMATION_DELAY / 30.0) + " seconds)");
    }

    @Override
    public Image getImage() {
        // If animation is finished, return null instead of full sprite sheet
        if (image == null || currentFrame == null || !isVisible()) {
            return null;
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
            // Return null instead of full image when there's an error
            System.err.println("Error extracting explosion frame: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void act() {
        animationCounter++;
        
        // Debug current state every few frames
        if (animationCounter % 10 == 0) {
            System.out.println("Explosion frame " + currentFrameIndex + "/" + (explosionFrames.length - 1) + 
                             ", counter: " + animationCounter + ", visible: " + isVisible());
        }
        
        if (animationCounter % ANIMATION_DELAY == 0) {
            currentFrameIndex++;
            if (currentFrameIndex >= explosionFrames.length) {
                System.out.println("Explosion animation finished after " + animationCounter + " ticks");
                setVisible(false); // Animation finished
                currentFrame = null;
            } else {
                currentFrame = explosionFrames[currentFrameIndex];
                System.out.println("Advanced to explosion frame " + currentFrameIndex);
            }
        }
    }

    public boolean isFinished() {
        return !isVisible();
    }

    public void reset(int x, int y) {
        initExplosion(x, y);
        animationCounter = 0;
    }

    public static void triggerAllBabyBossExplosions(boolean babyBossExplosionsTriggered, Boss boss) {
        babyBossExplosionsTriggered = true;
        List<BabyBoss> babies = boss.getBabyBosses();

        for (BabyBoss bb : babies) {
            if (bb.isVisible() && !bb.isExploding()) {
                bb.destroy(); // Start explosion animation
                System.out.println("Triggering baby boss explosion at: " + bb.getX() + ", " + bb.getY());
            }
        }
        System.out.println("All baby boss explosions triggered!");
    }

    public static boolean areAllBabyBossExplosionsComplete(Boss boss) {
        List<BabyBoss> babies = boss.getBabyBosses();

        // If no baby bosses, explosions are complete
        if (babies.isEmpty())
            return true;

        for (BabyBoss bb : babies) {
            // If baby boss is still visible and not exploding, it's still active
            if (bb.isVisible() && !bb.isExploding()) {
                return false;
            }
            // If baby boss is exploding but explosion is still visible
            if (bb.isExploding() && bb.getExplosion() != null && bb.getExplosion().isVisible()) {
                return false;
            }
        }

        System.out.println("All baby boss explosions complete!");
        return true;
    }

    // Helper method to get current frame info for debugging
    public String getDebugInfo() {
        return String.format("Frame %d/%d, Counter: %d, Visible: %b", 
                           currentFrameIndex, explosionFrames.length - 1, 
                           animationCounter, isVisible());
    }
}