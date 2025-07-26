package gdd.sprite;

import static gdd.Global.*;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Boss extends Enemy {

    private final Rectangle[] bossFrames = new Rectangle[]{
            // Row 1
            new Rectangle(0, 0, 84, 84),      // Frame 1
            new Rectangle(84, 0, 84, 84),     // Frame 2
            new Rectangle(168, 0, 84, 84),    // Frame 3
            new Rectangle(252, 0, 84, 84),    // Frame 4
            new Rectangle(336, 0, 84, 84),    // Frame 5

            // Row 2
            new Rectangle(0, 84, 84, 84),     // Frame 6
            new Rectangle(84, 84, 84, 84),    // Frame 7
            new Rectangle(168, 84, 84, 84),   // Frame 8
            new Rectangle(252, 84, 84, 84),   // Frame 9
            new Rectangle(336, 84, 84, 84),   // Frame 10

            // Row 3
            new Rectangle(0, 168, 84, 84),    // Frame 11
            new Rectangle(84, 168, 84, 84),   // Frame 12
            new Rectangle(168, 168, 84, 84),  // Frame 13
            new Rectangle(252, 168, 84, 84),  // Frame 14
            new Rectangle(336, 168, 84, 84),  // Frame 15

            // Row 4
            new Rectangle(0, 252, 84, 84),    // Frame 16
            new Rectangle(84, 252, 84, 84),   // Frame 17
            new Rectangle(168, 252, 84, 84),  // Frame 18
            new Rectangle(252, 252, 84, 84),  // Frame 19
            new Rectangle(336, 252, 84, 84),  // Frame 20

            // Row 5
            new Rectangle(0, 336, 84, 84),    // Frame 21
            new Rectangle(84, 336, 84, 84),   // Frame 22
            new Rectangle(168, 336, 84, 84),  // Frame 23
            new Rectangle(252, 336, 84, 84),  // Frame 24
            new Rectangle(336, 336, 84, 84),  // Frame 25

            // Row 6
            new Rectangle(0, 420, 84, 84),    // Frame 26
            new Rectangle(84, 420, 84, 84),   // Frame 27
            new Rectangle(168, 420, 84, 84),  // Frame 28
            new Rectangle(252, 420, 84, 84),  // Frame 29
            new Rectangle(336, 420, 84, 84),  // Frame 30

            // Row 7 (2 frames only)
            new Rectangle(0, 504, 84, 84),    // Frame 31
            new Rectangle(84, 504, 84, 84)    // Frame 32
    };

    private int currentFrameIndex = 0;
    private int animationCounter = 0;
    private final int ANIMATION_DELAY = 6;
    private final double SCALE = 3;

    private Rectangle currentFrame;

    private List<BabyBoss> babyBosses = new ArrayList<>();
    private int waveCooldown = 0;
    private int waveCount = 0;
    private final int MAX_WAVES = 5;
    private final int WAVE_INTERVAL = 300; // ~10s at 30fps (30 * 10 = 300)
    private boolean firstWaveSpawned = false; // Track if first wave has been spawned

    private Player player;
    
    // Boss health and explosion system
    private int hitCount = 0;
    private final int MAX_HITS = 30;
    private BossExplosion explosion = null;
    private boolean isExploding = false;
    private boolean isDead = false;

    public Boss(int x, int y, Player player) {
        super(x, y);
        this.type = "Boss";
        this.player = player;
        initBoss(x, y);
    }

    private void initBoss(int x, int y) {
        this.x = x;
        this.y = y;
        setImage(new ImageIcon(IMG_BOSS).getImage());
        currentFrame = bossFrames[0];
    }

    @Override
    public void act() {
        // If boss is dead, don't do anything
        if (isDead) return;
        
        // Handle explosion animation
        if (isExploding) {
            if (explosion != null) {
                explosion.update();
                if (explosion.isAnimationComplete()) {
                    isDead = true;
                    this.visible = false; // Hide the boss sprite
                    System.out.println("Boss defeated!");
                }
            }
            return; // Don't do normal boss behavior during explosion
        }

        // Normal boss behavior
        // Animate the boss
        animationCounter++;
        if (animationCounter >= ANIMATION_DELAY) {
            currentFrameIndex = (currentFrameIndex + 1) % bossFrames.length;
            currentFrame = bossFrames[currentFrameIndex];
            animationCounter = 0;
        }

        // Handle baby boss waves
        if (waveCount < MAX_WAVES) {
            // Spawn first wave immediately
            if (!firstWaveSpawned) {
                spawnBabyBosses();
                firstWaveSpawned = true;
                waveCount++;
                waveCooldown = 0; // Reset cooldown for next wave
                System.out.println("Wave " + waveCount + " spawned immediately! (" + babyBosses.size() + " baby bosses)");
            } else {
                // For subsequent waves, wait for the interval
                waveCooldown++;
                if (waveCooldown >= WAVE_INTERVAL) {
                    spawnBabyBosses();
                    waveCooldown = 0;
                    waveCount++;
                    System.out.println("Wave " + waveCount + " spawned after 10s wait! (" + babyBosses.size() + " baby bosses)");
                }
            }
        }

        // Update all baby bosses
        Iterator<BabyBoss> iterator = babyBosses.iterator();
        while (iterator.hasNext()) {
            BabyBoss bb = iterator.next();
            if (bb.isVisible()) {
                bb.chasePlayer(player);
            } else {
                iterator.remove(); // Remove dead baby bosses
            }
        }
    }
    
    // Method to handle boss getting hit
    public void takeDamage() {
        if (isExploding || isDead) return;
        
        hitCount++;
        System.out.println("Boss hit! " + hitCount + "/" + MAX_HITS);
        
        if (hitCount >= MAX_HITS) {
            startExplosion();
        }
    }
    
    private void startExplosion() {
        isExploding = true;
        explosion = new BossExplosion(this.x, this.y);
        System.out.println("Boss explosion started!");
    }

    public List<BabyBoss> getBabyBosses() {
        return babyBosses;
    }

    private void spawnBabyBosses() {
        // Clear previous wave (optional - you can remove this if you want accumulated waves)
        babyBosses.clear();

        int baseX = this.x + this.getWidth() / 2;
        int baseY = this.y + this.getHeight();

        int spacing = 50; // Increased spacing for better visibility

        // U-shaped formation with 7 baby bosses
        int[] offsetsX = {-3, -2, -1, 0, 1, 2, 3};
        int[] offsetsY = {50, 30, 15, 0, 15, 30, 50}; // U shape - outer ones lower

        for (int i = 0; i < 5; i++) {
            int bx = baseX + offsetsX[i] * spacing;
            int by = baseY + offsetsY[i];

            // Ensure baby bosses spawn within screen bounds
            bx = Math.max(25, Math.min(BOARD_WIDTH - 75, bx));
            by = Math.max(0, Math.min(BOARD_HEIGHT - 100, by));

            BabyBoss baby = new BabyBoss(bx, by, player);
            babyBosses.add(baby);
        }
    }

    @Override
    public Image getImage() {
        // If exploding, return explosion image
        if (isExploding && explosion != null) {
            return explosion.getImage();
        }
        
        // Normal boss image
        if (image == null || currentFrame == null) return null;

        BufferedImage bImage = toBufferedImage(image);
        Rectangle r = currentFrame;

        try {
            BufferedImage sub = bImage.getSubimage(r.x, r.y, r.width, r.height);
            int scaledW = (int) (r.width * SCALE);
            int scaledH = (int) (r.height * SCALE);
            return sub.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
        } catch (RasterFormatException e) {
            return bImage;
        }
    }

    @Override
    public int getWidth() {
        if (isExploding && explosion != null) {
            return explosion.getWidth();
        }
        return currentFrame != null ? (int)(currentFrame.width * SCALE) : super.getWidth();
    }

    @Override
    public int getHeight() {
        if (isExploding && explosion != null) {
            return explosion.getHeight();
        }
        return currentFrame != null ? (int)(currentFrame.height * SCALE) : super.getHeight();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    // Method to get current wave information (for debugging/UI)
    public int getCurrentWave() {
        return waveCount;
    }

    public int getMaxWaves() {
        return MAX_WAVES;
    }

    public boolean isWaveActive() {
        return !babyBosses.isEmpty();
    }

    // Method to manually trigger next wave (for testing)
    public void forceNextWave() {
        if (waveCount < MAX_WAVES) {
            spawnBabyBosses();
            waveCount++;
            waveCooldown = 0;
        }
    }

    // Method to get time until next wave (for UI display)
    public int getTimeUntilNextWave() {
        if (waveCount >= MAX_WAVES) return 0;
        if (!firstWaveSpawned) return 0; // First wave spawns immediately
        return Math.max(0, WAVE_INTERVAL - waveCooldown);
    }
    
    // Getters for boss state
    public int getHitCount() { return hitCount; }
    public int getMaxHits() { return MAX_HITS; }
    public boolean isExploding() { return isExploding; }
    public boolean isDead() { return isDead; }
    public float getHealthPercentage() { 
        return (float)(MAX_HITS - hitCount) / MAX_HITS; 
    }

    // Method to reset boss state (for game restart)
    public void reset() {
        babyBosses.clear();
        waveCount = 0;
        waveCooldown = 0;
        firstWaveSpawned = false;
        currentFrameIndex = 0;
        animationCounter = 0;
        currentFrame = bossFrames[0];
        hitCount = 0;
        isExploding = false;
        isDead = false;
        explosion = null;
        this.visible = true;
    }
}