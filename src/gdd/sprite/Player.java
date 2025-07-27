package gdd.sprite;

import static gdd.Global.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

public class Player extends Sprite {

    // Position and Scaling
    private static final int START_X = 270;
    private static final int START_Y = 540;
    private int currentSpeed = 3;
    private int lives = 3;
    private int speedStage = 1;
    private int bulletStage = 1;
    private boolean shieldActive = false;
    private boolean isOnCoolDown = false;

    public boolean isOnCoolDown() {
        return isOnCoolDown;
    }

    public void setOnCoolDown(boolean onCoolDown) {
        isOnCoolDown = onCoolDown;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
    private static final double LOCAL_SCALE = 3.0;
    private static final int FRAME_SIZE = 16;

    // Movement
    private int dx = 0, dy = 0;
    private int speed = 5;

    // Sprite Dimensions
    private final int width = (int) (FRAME_SIZE * LOCAL_SCALE);
    private final int height = (int) (FRAME_SIZE * LOCAL_SCALE);

    // Booster animation
    private int animationTick = 0;
    private static final int ANIMATION_SPEED = 10;
    private boolean boosterFrameToggle = false;

    // State flags
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    // Shooting
    private List<Shot> shots = new ArrayList<>();
    private int shotCooldown = 0;
    private static final int SHOT_DELAY = 15; // Frames between shots

    // Sprites
    private BufferedImage shipSheet;
    private BufferedImage boosterIdle;
    private BufferedImage boosterLeft;
    private BufferedImage boosterRight;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        shipSheet = toBufferedImage(new ImageIcon(IMG_SPACE_SHIP).getImage());
        boosterIdle = toBufferedImage(new ImageIcon(IMG_BOOSTER).getImage());
        boosterLeft = toBufferedImage(new ImageIcon(IMG_BOOSTER_LEFT).getImage());
        boosterRight = toBufferedImage(new ImageIcon(IMG_BOOSTER_RIGHT).getImage());

        setX(START_X);
        setY(START_Y);
    }

    public BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage bi) return bi;

        BufferedImage bimage = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = bimage.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return bimage;
    }

    @Override
    public Image getImage() {
        // Increase frame height to leave room for booster flames
        int extendedHeight = height + height / 2;
        BufferedImage frame = new BufferedImage(width, extendedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = frame.createGraphics();

        // --- Draw Ship ---
        int frameX = leftPressed ? 0 : rightPressed ? 32 : 16;
        BufferedImage ship = shipSheet.getSubimage(frameX, 0, FRAME_SIZE, FRAME_SIZE);
        Image scaledShip = ship.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        g.drawImage(scaledShip, 0, 0, null);

        // --- Draw Anime Booster ---
        BufferedImage booster;
        if (leftPressed) {
            int frameW = boosterLeft.getWidth() / 2;
            int frameH = boosterLeft.getHeight();
            int sx = boosterFrameToggle ? frameW : 0;
            booster = boosterLeft.getSubimage(sx, 0, frameW, frameH);
        } else if (rightPressed) {
            int frameW = boosterRight.getWidth() / 2;
            int frameH = boosterRight.getHeight();
            int sx = boosterFrameToggle ? frameW : 0;
            booster = boosterRight.getSubimage(sx, 0, frameW, frameH);
        } else {
            int frameW = boosterIdle.getWidth() / 2;
            int frameH = boosterIdle.getHeight();
            int sx = boosterFrameToggle ? frameW : 0;
            booster = boosterIdle.getSubimage(sx, 0, frameW, frameH);
        }

        int boosterHeight = height / 2;
        int boosterWidth = (int) (width * 0.4);
        int boosterX = (width - boosterWidth) / 2;
        int boosterY = height - boosterHeight / 4; // Adjust flame origin lower
        Image scaledBooster = booster.getScaledInstance(boosterWidth, boosterHeight, Image.SCALE_SMOOTH);
        g.drawImage(scaledBooster, boosterX, boosterY, null);

        g.dispose();
        return frame;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public void setShieldActive(boolean shieldActive) {
        this.shieldActive = shieldActive;
    }

    public int getSpeedStage() {
        return speedStage;
    }

    public void setSpeedStage(int speedStage) {
        this.speedStage = speedStage;
    }

    public int getBulletStage() {
        return bulletStage;
    }

    public void setBulletStage(int bulletStage) {
        this.bulletStage = bulletStage;
    }

    public void act() {
        // Handle movement
        x += dx;
        y += dy;

        int frameHeight = height + height / 2;

        x = Math.max(0, Math.min(BOARD_WIDTH - width, x));
        y = Math.max(0, Math.min(BOARD_HEIGHT - frameHeight, y));

        // Handle animation
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            boosterFrameToggle = !boosterFrameToggle;
            animationTick = 0;
        }

        // Handle shot cooldown
        if (shotCooldown > 0) {
            shotCooldown--;
        }

        // Update shots
        updateShots();
    }

    private void updateShots() {
        Iterator<Shot> iterator = shots.iterator();
        while (iterator.hasNext()) {
            Shot shot = iterator.next();
            shot.act();
            if (!shot.isVisible()) {
                iterator.remove();
            }
        }
    }

    public void shoot() {
        if (shotCooldown <= 0) {
            // Create shot using your existing Shot class constructor
            Shot newShot = new Shot(x, y, width);
            shots.add(newShot);
            shotCooldown = SHOT_DELAY;
            System.out.println("Player shot fired! Total shots: " + shots.size());
        } else {
            System.out.println("Shot on cooldown: " + shotCooldown);
        }
    }

    public List<Shot> getShots() {
        return shots;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -speed;
            leftPressed = true;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = speed;
            rightPressed = true;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -speed;
            upPressed = true;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = speed;
            downPressed = true;
        }

        if (key == KeyEvent.VK_SPACE) {
            shoot();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
            dx = rightPressed ? speed : 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
            dx = leftPressed ? -speed : 0;
        }

        if (key == KeyEvent.VK_UP) {
            upPressed = false;
            dy = downPressed ? speed : 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            downPressed = false;
            dy = upPressed ? -speed : 0;
        }
    }

    // Optional Getter/Setter for speed if needed
    public int getSpeed() {
        return speed;
    }

    public int setSpeed(int speed) {
        this.speed = Math.max(1, speed);
        return this.speed;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public Rectangle getBounds() {
        int frameHeight = height + height / 2; // Match the extended height from getImage()
        return new Rectangle(x, y, width, frameHeight);
    }

    // Reset method for game restart
    public void reset() {
        shots.clear();
        shotCooldown = 0;
        setX(START_X);
        setY(START_Y);
        dx = 0;
        dy = 0;
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        setVisible(true);
    }
}