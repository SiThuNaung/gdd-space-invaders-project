// BabyBoss.java
package gdd.sprite;

import gdd.sprite.Enemy;
import static gdd.Global.*;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import javax.swing.ImageIcon;

public class BabyBoss extends Enemy {
    // Updated rectangle coordinates for the boss sprite sheet (395 × 632)
    private final Rectangle[] frames = new Rectangle[] {
            // Row 1
            new Rectangle(0, 0, 79, 79), // Frame 1
            new Rectangle(79, 0, 79, 79), // Frame 2
            new Rectangle(158, 0, 79, 79), // Frame 3
            new Rectangle(237, 0, 79, 79), // Frame 4
            new Rectangle(316, 0, 79, 79), // Frame 5

            // Row 2
            new Rectangle(0, 79, 79, 79), // Frame 6
            new Rectangle(79, 79, 79, 79), // Frame 7
            new Rectangle(158, 79, 79, 79), // Frame 8
            new Rectangle(237, 79, 79, 79), // Frame 9
            new Rectangle(316, 79, 79, 79), // Frame 10

            // Row 3
            new Rectangle(0, 158, 79, 79), // Frame 11
            new Rectangle(79, 158, 79, 79), // Frame 12
            new Rectangle(158, 158, 79, 79), // Frame 13
            new Rectangle(237, 158, 79, 79), // Frame 14
            new Rectangle(316, 158, 79, 79), // Frame 15

            // Row 4
            new Rectangle(0, 237, 79, 79), // Frame 16
            new Rectangle(79, 237, 79, 79), // Frame 17
            new Rectangle(158, 237, 79, 79), // Frame 18
            new Rectangle(237, 237, 79, 79), // Frame 19
            new Rectangle(316, 237, 79, 79), // Frame 20

            // Row 5
            new Rectangle(0, 316, 79, 79), // Frame 21
            new Rectangle(79, 316, 79, 79), // Frame 22
            new Rectangle(158, 316, 79, 79), // Frame 23
            new Rectangle(237, 316, 79, 79), // Frame 24
            new Rectangle(316, 316, 79, 79), // Frame 25

            // Row 6
            new Rectangle(0, 395, 79, 79), // Frame 26
            new Rectangle(79, 395, 79, 79), // Frame 27
            new Rectangle(158, 395, 79, 79), // Frame 28
            new Rectangle(237, 395, 79, 79), // Frame 29
            new Rectangle(316, 395, 79, 79), // Frame 30

            // Row 7
            new Rectangle(0, 474, 79, 79), // Frame 31
            new Rectangle(79, 474, 79, 79), // Frame 32
            new Rectangle(158, 474, 79, 79), // Frame 33
            new Rectangle(237, 474, 79, 79), // Frame 34
            new Rectangle(316, 474, 79, 79), // Frame 35

            // Row 8 (single frame)
            new Rectangle(0, 553, 79, 79) // Frame 36
    };

    private int currentFrameIndex = 0;
    private int animationCounter = 0;
    private final int ANIMATION_SPEED = 8; // Change frame every 8 game ticks
    private final double SCALE = 1.0; // Scale factor for baby boss (smaller than main boss)

    private double angleToPlayer;
    private double speed = 2;
    private Player targetPlayer;

    // Animation states - adjusted for the new sprite sheet
    private boolean isMoving = false;
    private int idleFrameStart = 0;
    private int idleFrameEnd = 9; // Use first 10 frames for idle
    private int moveFrameStart = 10;
    private int moveFrameEnd = frames.length - 1; // Use remaining frames for movement

    public BabyBoss(int x, int y, Player player) {
        super(x, y);
        this.type = "BabyBoss";
        this.targetPlayer = player;
        this.setImage(new ImageIcon(IMG_BABY_BOSS).getImage());

        // Start with idle animation
        currentFrameIndex = idleFrameStart;
        updateAngleToPlayer();
    }

    private void updateAngleToPlayer() {
        if (targetPlayer != null && targetPlayer.isVisible()) {
            int px = targetPlayer.getX() + targetPlayer.getWidth() / 2;
            int py = targetPlayer.getY() + targetPlayer.getHeight() / 2;
            int myX = this.x + getWidth() / 2;
            int myY = this.y + getHeight() / 2;

            double dx = px - myX;
            double dy = py - myY;
            angleToPlayer = Math.atan2(dy, dx);

            // Determine if boss should be in moving state
            double distance = Math.sqrt(dx * dx + dy * dy);
            isMoving = distance > 5; // If close enough, switch to idle
        }
    }

    private void updateAnimation() {
        animationCounter++;

        if (animationCounter >= ANIMATION_SPEED) {
            animationCounter = 0;

            if (isMoving) {
                // Cycle through movement frames
                currentFrameIndex++;
                if (currentFrameIndex > moveFrameEnd) {
                    currentFrameIndex = moveFrameStart;
                }
            } else {
                // Cycle through idle frames
                currentFrameIndex++;
                if (currentFrameIndex > idleFrameEnd) {
                    currentFrameIndex = idleFrameStart;
                }
            }
        }
    }

    public void chasePlayer(Player player) {
        if (player != null && player.isVisible()) {
            this.targetPlayer = player;
            updateAngleToPlayer();
            act();
        }
    }

    // Replace the explosion-related code in your BabyBoss.java class

    private BabyBossExplosion explosion = null;
    private boolean exploding = false;
    private boolean showingExplosion = false;

    public void destroy() {
        if (!exploding) {
            System.out.println("=== DESTROY CALLED ===");
            exploding = true;
            showingExplosion = true;

            // Store the position before hiding
            int explosionX = this.x;
            int explosionY = this.y;

            // Hide the baby boss sprite immediately
           // setVisible(false);
            System.out.println("Baby boss hidden at position: " + explosionX + ", " + explosionY);

            // Create explosion at the exact position where baby boss was
            explosion = new BabyBossExplosion(explosionX, explosionY);
            System.out.println(
                    "Explosion created and should be visible: " + (explosion != null && explosion.isVisible()));
        }
    }

    public boolean isExploding() {
        boolean result = showingExplosion && explosion != null && explosion.isVisible();
        if (showingExplosion) {
            System.out.println("isExploding check: showingExplosion=" + showingExplosion +
                    ", explosion!=null=" + (explosion != null) +
                    ", explosion.isVisible()=" + (explosion != null ? explosion.isVisible() : "null"));
        }
        return result;
    }

    public boolean isDestroyed() {
        return exploding && !showingExplosion;
    }

    public BabyBossExplosion getExplosion() {
        return explosion;
    }

    @Override
    public void act() {
        if (exploding && explosion != null) {
            explosion.act(); // Update explosion animation
            if (explosion.isFinished()) {
                showingExplosion = false; // Stop showing explosion
                System.out.println("BabyBoss explosion animation finished");
                setVisible(false); // Hide the baby boss after explosion
            }
        } else if (!exploding) {
            // Normal movement and animation code (your existing code)
            if (Math.random() < 0.1) {
                updateAngleToPlayer();
            }

            if (isMoving) {
                this.x += (int) (Math.cos(angleToPlayer) * speed);
                this.y += (int) (Math.sin(angleToPlayer) * speed);
            }

            // Keep baby boss on screen bounds
            if (this.x < 0)
                this.x = 0;
            if (this.x > BOARD_WIDTH - getWidth())
                this.x = BOARD_WIDTH - getWidth();
            if (this.y < 0)
                this.y = 0;
            if (this.y > BOARD_HEIGHT - getHeight())
                this.y = BOARD_HEIGHT - getHeight();

            updateAnimation();
        }
    }

    @Override
    public Image getImage() {
        if (image == null)
            return null;

        BufferedImage bImage = toBufferedImage(image);
        Rectangle r = frames[currentFrameIndex];
        BufferedImage sub;

        try {
            sub = bImage.getSubimage(r.x, r.y, r.width, r.height);
        } catch (RasterFormatException e) {
            // If there's an error, return the full image
            return bImage;
        }

        // Scale the sprite
        int scaledWidth = (int) (r.width * SCALE);
        int scaledHeight = (int) (r.height * SCALE);

        return sub.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    }

    @Override
    public int getWidth() {
        return (int) (frames[currentFrameIndex].width * SCALE);
    }

    @Override
    public int getHeight() {
        return (int) (frames[currentFrameIndex].height * SCALE);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    // Additional methods for animation control
    public void setAnimationSpeed(int speed) {
        // Allow customization of animation speed if needed
        // Lower values = faster animation
    }

    public boolean isMoving() {
        return isMoving;
    }

    public int getCurrentFrame() {
        return currentFrameIndex;
    }
}