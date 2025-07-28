package gdd.sprite;

import static gdd.Global.*;

import javax.swing.ImageIcon;

import gdd.AudioPlayer;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class BossExplosion {
    private final Rectangle[] bossExplosionFrames = new Rectangle[]{
        // Row 1
        new Rectangle(2, 6, 85, 84),      // Frame 1
        new Rectangle(93, 7, 85, 83),     // Frame 2
        new Rectangle(185, 7, 86, 83),    // Frame 3
        new Rectangle(276, 7, 85, 83),    // Frame 4
        new Rectangle(366, 7, 88, 83),    // Frame 5

        // Row 2
        new Rectangle(2, 99, 88, 82),     // Frame 6
        new Rectangle(94, 98, 87, 83),    // Frame 7
        new Rectangle(185, 99, 86, 82),   // Frame 8
        new Rectangle(275, 98, 87, 83),   // Frame 9
        new Rectangle(367, 98, 86, 83),   // Frame 10

        // Row 3
        new Rectangle(2, 190, 86, 82),    // Frame 11
        new Rectangle(94, 189, 86, 83),   // Frame 12
        new Rectangle(184, 189, 87, 83),  // Frame 13
        new Rectangle(276, 189, 87, 83),  // Frame 14
        new Rectangle(367, 189, 89, 83),  // Frame 15

        // Row 4
        new Rectangle(2, 280, 88, 83),    // Frame 16
        new Rectangle(94, 280, 85, 83),   // Frame 17
        new Rectangle(184, 279, 85, 84),  // Frame 18
    };

    private int x, y;
    private Image image;
    private int currentFrameIndex = 0;
    private int animationCounter = 0;
    private final int ANIMATION_DELAY = 3; // Faster animation for explosion
    private final double SCALE = 2.0;
    private boolean animationComplete = false;
    private boolean visible = true;
    private AudioPlayer audioPlayer;

    private void initAudio() {
        try {
            audioPlayer = new AudioPlayer();
            audioPlayer.playBossScream();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }
    
    public BossExplosion(int x, int y) {
        initAudio();
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(IMG_BOSS_EXPLOSION).getImage(); // You'll need to define this in Global
    }
    
    public void update() {
        if (animationComplete) return;
        
        animationCounter++;
        if (animationCounter >= ANIMATION_DELAY) {
            currentFrameIndex++;
            animationCounter = 0;
            
            if (currentFrameIndex >= bossExplosionFrames.length) {
                animationComplete = true;
                visible = false;
            }
        }
    }
    
    public Image getImage() {
        if (image == null || animationComplete) return null;
        
        BufferedImage bImage = toBufferedImage(image);
        Rectangle currentFrame = bossExplosionFrames[currentFrameIndex];
        
        try {
            BufferedImage sub = bImage.getSubimage(
                currentFrame.x, currentFrame.y, 
                currentFrame.width, currentFrame.height
            );
            int scaledW = (int) (currentFrame.width * SCALE);
            int scaledH = (int) (currentFrame.height * SCALE);
            return sub.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            return bImage;
        }
    }
    
    // Helper method to convert Image to BufferedImage
    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        
        BufferedImage bimage = new BufferedImage(
            img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB
        );
        
        java.awt.Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        
        return bimage;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isVisible() { return visible; }
    public boolean isAnimationComplete() { return animationComplete; }
    
    public int getWidth() {
        if (animationComplete) return 0;
        Rectangle currentFrame = bossExplosionFrames[currentFrameIndex];
        return (int)(currentFrame.width * SCALE);
    }
    
    public int getHeight() {
        if (animationComplete) return 0;
        Rectangle currentFrame = bossExplosionFrames[currentFrameIndex];
        return (int)(currentFrame.height * SCALE);
    }
}