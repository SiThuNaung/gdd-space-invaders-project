package gdd.scene;

import gdd.sprite.Player;
import gdd.sprite.Boss;
import gdd.sprite.BabyBoss;
import gdd.sprite.BabyBossExplosion;
import gdd.sprite.Shot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static gdd.Global.*;

public class Scene2 extends JPanel implements ActionListener {

    private Player player;
    private Boss boss;
    private Timer timer;
    private int frame = 0;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean babyBossExplosionsTriggered = false;

    private final int BLOCKHEIGHT = 50;
    private final int BLOCKWIDTH = 50;

    public Scene2() {
        setFocusable(true);
        setDoubleBuffered(true);

        player = new Player();
        boss = new Boss((BOARD_WIDTH - 150) / 2, 60, player); // Centered

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver && !gameWon) {
                    player.keyPressed(e);
                }
                // Press R to restart game
                if (e.getKeyCode() == KeyEvent.VK_R && (gameOver || gameWon)) {
                    restartGame();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!gameOver && !gameWon) {
                    player.keyReleased(e);
                }
            }
        });

        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        if (gameWon) {
            drawVictory(g);
        } else if (gameOver) {
            drawGameOver(g);
        } else {
            drawMap(g);

            // Draw player
            if (player.isVisible()) {
                g.drawImage(player.getImage(), player.getX(), player.getY(), this);
            }

            // Draw boss (including explosion animation)
            if (boss.isVisible()) {
                g.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
            }

            // Draw baby bosses (show them even when boss is exploding so we can see their
            // explosions)
            for (int i = 0; i < boss.getBabyBosses().size(); i++) {
                BabyBoss bb = boss.getBabyBosses().get(i);

                // Draw normal baby boss if visible and not exploding
                if (bb.isVisible() && !bb.isExploding()) {
                    Image bbImage = bb.getImage();
                    if (bbImage != null) {
                        g.drawImage(bbImage, bb.getX(), bb.getY(), this);
                    }
                }
            }

            // Draw baby boss explosions (always show explosions)
            for (int i = 0; i < boss.getBabyBosses().size(); i++) {
                BabyBoss bb = boss.getBabyBosses().get(i);

                // Draw explosion if present
                if (bb.isExploding() && bb.getExplosion() != null) {
                    BabyBossExplosion explosion = bb.getExplosion();
                    if (explosion.isVisible()) {
                        Image explosionImage = explosion.getImage();
                        if (explosionImage != null) {
                            g.drawImage(explosionImage, explosion.getX(), explosion.getY(), this);
                        }
                    }
                }
            }

            // Draw player shots only if boss isn't exploding
            if (!boss.isExploding()) {
                List<Shot> playerShots = player.getShots();
                for (Shot shot : playerShots) {
                    if (shot.isVisible()) {
                        g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
                    }
                }
            }

            // Draw UI information
            drawUI(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawUI(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        // Wave information
        String waveInfo = "Wave: " + boss.getCurrentWave() + "/" + boss.getMaxWaves();
        g.drawString(waveInfo, 10, 25);

        // Baby boss count
        String babyCount = "Baby Bosses: " + boss.getBabyBosses().size();
        g.drawString(babyCount, 10, 45);

        // Boss health information
        String healthInfo = "Boss Health: " + (boss.getMaxHits() - boss.getHitCount()) + "/" + boss.getMaxHits();
        g.drawString(healthInfo, 10, 65);

        // Boss health bar
        int barWidth = 200;
        int barHeight = 10;
        int barX = 10;
        int barY = 75;

        // Background (red)
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);

        // Health (green)
        g.setColor(Color.GREEN);
        int healthWidth = (int) (barWidth * boss.getHealthPercentage());
        g.fillRect(barX, barY, healthWidth, barHeight);

        // Border
        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, barHeight);

        // Time until next wave (more accurate)
        if (boss.getCurrentWave() < boss.getMaxWaves() && !boss.isExploding()) {
            int timeLeft = boss.getTimeUntilNextWave() / 30; // Convert frames to seconds
            if (boss.getCurrentWave() == 0) {
                g.drawString("First wave spawning now!", 10, 105);
            } else {
                String nextWave = "Next wave in: " + timeLeft + "s";
                g.drawString(nextWave, 10, 105);
            }
        } else if (boss.isExploding()) {
            g.setColor(Color.YELLOW);
            g.drawString("BOSS EXPLODING!", 10, 105);
        } else {
            g.drawString("All waves spawned!", 10, 105);
        }

        // Controls information
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Controls: Arrow Keys = Move, SPACE = Shoot", 10, BOARD_HEIGHT - 20);
    }

    private void drawVictory(Graphics g) {
        // Draw victory screen (same format as game over)
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String victoryText = "YOU WIN!";
        FontMetrics fm = g.getFontMetrics();
        int x = (BOARD_WIDTH - fm.stringWidth(victoryText)) / 2;
        int y = BOARD_HEIGHT / 2;
        g.drawString(victoryText, x, y);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String restartText = "Press R to Play Again";
        fm = g.getFontMetrics();
        x = (BOARD_WIDTH - fm.stringWidth(restartText)) / 2;
        y = y + 60;
        g.drawString(restartText, x, y);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String gameOverText = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        int x = (BOARD_WIDTH - fm.stringWidth(gameOverText)) / 2;
        int y = BOARD_HEIGHT / 2;
        g.drawString(gameOverText, x, y);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String restartText = "Press R to Restart";
        fm = g.getFontMetrics();
        x = (BOARD_WIDTH - fm.stringWidth(restartText)) / 2;
        y = y + 60;
        g.drawString(restartText, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver || gameWon)
            return;

        frame++;

        // Update player
        if (player.isVisible()) {
            player.act();
        }

        // Update boss
        if (boss.isVisible()) {
            boss.act();
        }

        // Update baby bosses and their explosions
        List<BabyBoss> babies = boss.getBabyBosses();
        for (BabyBoss bb : babies) {
            bb.act(); // Always update baby bosses so explosions animate
        }

        // Check if boss just started exploding and trigger baby boss explosions
        if (boss.isExploding() && !boss.isDead() && !babyBossExplosionsTriggered) {
            triggerAllBabyBossExplosions();
        }

        // In actionPerformed()
        if (boss.isDead() && !gameWon) {
            if (areAllBabyBossExplosionsComplete()) {
                gameWon = true;
                clearAllSprites();
                System.out.println("Victory! Boss and all baby bosses defeated!");
                repaint(); // Force immediate repaint
                return; // Skip the rest of the frame processing
            }
        }

        // Handle baby boss collisions with player (only if boss not exploding)
        if (!boss.isExploding()) {
            for (BabyBoss bb : babies) {
                if (bb.isVisible() && player.isVisible()) {
                    // Check collision using bounds
                    if (bb.getBounds().intersects(player.getBounds())) {
                        player.setVisible(false);
                        gameOver = true;
                        System.out.println("Player hit by baby boss! Game Over!");
                        break;
                    }
                }
            }

            // Collision with boss
            if (boss.isVisible() && player.isVisible()) {
                if (boss.getBounds().intersects(player.getBounds())) {
                    player.setVisible(false);
                    gameOver = true;
                    System.out.println("Player hit by boss! Game Over!");
                }
            }

            // Handle shot collisions (only if boss not exploding)
            for (Shot shot : player.getShots()) {
                if (!shot.isVisible())
                    continue;

                // Check bullet-boss collisions
                if (shot.getBounds().intersects(boss.getBounds()) && !boss.isDead()) {
                    boss.takeDamage();
                    shot.setVisible(false); // Remove the bullet
                }

                // Check bullet-baby boss collisions
                for (BabyBoss bb : babies) {
                    if (bb.isVisible() && !bb.isExploding() && shot.getBounds().intersects(bb.getBounds())) {
                        // Hit! Remove shot and start explosion
                        shot.setVisible(false);
                        System.out.println("=== COLLISION DETECTED ===");
                        System.out.println("Shot hit baby boss at: " + bb.getX() + ", " + bb.getY());
                        bb.destroy(); // This will start the explosion animation
                        System.out.println("Baby boss destroy() called - should be exploding now: " + bb.isExploding());
                        break; // Exit inner loop since shot is destroyed
                    }
                }
            }
        }

        repaint();
    }

    private void clearAllSprites() {
        // Clear all baby bosses
        boss.getBabyBosses().clear();

        // Clear all player shots
        player.getShots().clear();

        // Make sure all sprites are hidden
        System.out.println("All sprites cleared for victory screen!");
    }

    private void triggerAllBabyBossExplosions() {
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

    private boolean areAllBabyBossExplosionsComplete() {
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

    private void restartGame() {
        gameOver = false;
        gameWon = false; // Reset victory state
        frame = 0;
        babyBossExplosionsTriggered = false; // Reset explosion trigger

        // Reset player (using the reset method)
        player.reset();

        // Reset boss (using the reset method)
        boss.reset();

        System.out.println("Game restarted!");
    }

    // Add getBounds method to Player class if not present
    public Rectangle getPlayerBounds() {
        return new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }

    // STARFIELD MAP (unchanged)
    private final int[][] MAP = {
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
    };

    private void drawMap(Graphics g) {
        int scrollOffset = frame % BLOCKHEIGHT;
        int baseRow = frame / BLOCKHEIGHT;
        int rowsNeeded = (BOARD_HEIGHT / BLOCKHEIGHT) + 2;

        for (int screenRow = 0; screenRow < rowsNeeded; screenRow++) {
            int mapRow = (baseRow + screenRow) % MAP.length;
            int y = BOARD_HEIGHT - ((screenRow * BLOCKHEIGHT) - scrollOffset);

            if (y > BOARD_HEIGHT || y < -BLOCKHEIGHT)
                continue;

            for (int col = 0; col < MAP[mapRow].length; col++) {
                if (MAP[mapRow][col] == 1) {
                    int x = col * BLOCKWIDTH;
                    drawStarCluster(g, x, y, BLOCKWIDTH, BLOCKHEIGHT);
                }
            }
        }
    }

    private void drawStarCluster(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.WHITE);
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g.fillOval(centerX - 2, centerY - 2, 4, 4);

        g.fillOval(centerX - 15, centerY - 10, 2, 2);
        g.fillOval(centerX + 12, centerY - 8, 2, 2);
        g.fillOval(centerX - 8, centerY + 12, 2, 2);
        g.fillOval(centerX + 10, centerY + 15, 2, 2);

        g.fillOval(centerX - 20, centerY + 5, 1, 1);
        g.fillOval(centerX + 18, centerY - 15, 1, 1);
        g.fillOval(centerX - 5, centerY - 18, 1, 1);
        g.fillOval(centerX + 8, centerY + 20, 1, 1);
    }
}