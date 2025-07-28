package gdd.scene;

import gdd.*;

import static gdd.Global.*;

import gdd.powerup.*;
import gdd.sprite.AlienUFO;
import gdd.ProceduralStarField;
import gdd.SpawnDetails;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.sprite.Alien1;
import java.awt.*;
import gdd.SpawnDetails;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.sprite.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;

public class Scene1 extends JPanel {
    //resetting music
    private boolean win= false;
    private boolean lose= false;


    private int frame = 0;
    private List<PowerUp> powerups;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private List<Shot> shots;
    private Player player;
    // private Shot shot;

    // boss stuff
    private PlayerExplosion playerExplosion;
    private boolean babyBossExplosionsTriggered = false;

    final int BLOCKHEIGHT = 50;
    final int BLOCKWIDTH = 50;

    final int BLOCKS_TO_DRAW = BOARD_HEIGHT / BLOCKHEIGHT;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private final Random randomizer = new Random();

    private Timer timer;
    private final Game game;

    private int score = 0;
    private Shield currentShield;
    private boolean isOnCooldown = false;

    private int currentRow = -1;
    // TODO load this map from a file
    private int mapOffset = 0;
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
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }
    };

    private String fadeMessage = "";
    private int fadeAlpha = 0;
    private int fadeTimer = 0;
    private boolean isFading = false;

    // Stage tracking
    private int currentStage = 1;
    private boolean stage2MessageShown = false;
    private boolean stage3MessageShown = false;

    private ProceduralStarField starField;

    private HashMap<Integer, SpawnDetails> spawnMap = new HashMap<>();
    private AudioPlayer audioPlayer;

    private Boss boss;

    public Scene1(Game game) {
        this.game = game;
         initBoard();
        // gameInit();
        loadSpawnDetails();
    }

    private void initAudio() {
        try {
            audioPlayer = new AudioPlayer();
            audioPlayer.playScene2Music();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void loadSpawnDetails() {
        try {
            // Load from external file
            spawnMap = SpawnDetailsLoader.loadFromCSV("src/gdd/spawns.csv");
            System.out.println("Loaded " + spawnMap.size() + " spawn entries from CSV");
        } catch (IOException e) {
            System.err.println("Error loading spawn data from CSV: " + e.getMessage());
        }
        // spawnMap.put(100, new SpawnDetails(SpawnType.LIFE, 200, 100, 1, 0));
        // spawnMap.put(100, new SpawnDetails(SpawnType.SHIELD, 200, 100, 1, 0));
        //spawnMap.put(100, new SpawnDetails(SpawnType.SHIELD, 200, 100, 1, 0));
        // spawnMap.put(2000, new SpawnDetails(SpawnType.SHIELD, 200, 100, 1, 0));
        // spawnMap.put(3000, new SpawnDetails(SpawnType.SHIELD, 200, 100, 1, 0));
        // spawnMap.put(100, new SpawnDetails(SpawnType.AMMO_UPGRADE, 200, 100, 1, 0));
        // spawnMap.put(300, new SpawnDetails(SpawnType.AMMO_UPGRADE, 200, 100, 1, 0));
        // spawnMap.put(200, new SpawnDetails(SpawnType.AMMO_UPGRADE, 200, 100, 1, 0));
        // spawnMap.put(400, new SpawnDetails(SpawnType.AMMO_UPGRADE, 200, 100, 1, 0));
        // spawnMap.put(400, new SpawnDetails(SpawnType.SPEED_BOOST, 200, 100, 1, 0));
        // spawnMap.put(800, new SpawnDetails(SpawnType.AMMO_UPGRADE, 200, 100, 1, 0));
        // spawnMap.put(900, new SpawnDetails(SpawnType.SPEED_BOOST, 200, 100, 1, 0));
        // spawnMap.put(1200, new SpawnDetails(SpawnType.AMMO_UPGRADE, 200, 100, 1, 0));
        // spawnMap.put(1400, new SpawnDetails(SpawnType.SPEED_BOOST, 200, 100, 1, 0));
        // spawnMap.put(1600, new SpawnDetails(SpawnType.FLYING_ALIEN, 200, 100, 1, 0));
        //spawnMap.put(200, new SpawnDetails(SpawnType.BOSS, 270, 0, 1, 0));
    }

    // function to spawn anything from spawn map
    private void spawnEntity() {
        // Check enemy spawn
        SpawnDetails sd = spawnMap.get(frame);
        if (sd != null) {
            for (int i = 0; i < sd.getCount(); i++) {
                int xPosition = sd.getX() + (sd.getSpacing() * i);

                // Create a new enemy based on the spawn details
                switch (sd.getType()) {
                    case ALIEN_UFO: // rename this type to "AlienUFO" in your spawnMap too for clarity
                        Enemy ufo = new AlienUFO(xPosition, sd.getY());
                        enemies.add(ufo);
                        System.out.println("Entity Spawned at frame: " + frame);
                        break;
                    // Add more cases for different enemy types if needed
                    case FLYING_ALIEN:
                        Enemy flyingAlien = new FlyingAlien(xPosition, sd.getY(), player);
                        enemies.add(flyingAlien);
                        break;
                    case BOSS:
                        boss = new Boss(xPosition, sd.getY(), player);
                        enemies.add(boss);
                        break;
                    case SPEED_BOOST:
                        // Handle speed up item spawn
                        PowerUp speedUp = new SpeedUp(xPosition, sd.getY());
                        powerups.add(speedUp);
                        System.out.println("Entity Spawned at frame: " + frame);
                        break;
                    case LIFE:
                        PowerUp life = new Life(xPosition, sd.getY());
                        powerups.add(life);
                        break;
                    case SHIELD:
                        Shield shield = new Shield(xPosition, sd.getY());
                        currentShield = shield;
                        powerups.add(shield);
                        break;
                    case AMMO_UPGRADE:
                        AmmoUpgrade ammoUpgrade = new AmmoUpgrade(xPosition, sd.getY());
                        powerups.add(ammoUpgrade);
                        break;
                    default:
                        System.out.println("Unknown enemy type: " + sd.getType());
                        break;
                }
            }
        }
    }

    private void initBoard() {
        starField = new ProceduralStarField(BOARD_WIDTH, BOARD_HEIGHT, System.currentTimeMillis());
    }


    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.black);

        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();

        gameInit();
        initAudio();
    }

    public void stop() {
        timer.stop();
        try {
            if (audioPlayer != null) {
                audioPlayer.stopScene2Music();
                audioPlayer.stopBossSceneMusic();
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void gameInit() {

        enemies = new ArrayList<>();
        powerups = new ArrayList<>();
        explosions = new ArrayList<>();
        shots = new ArrayList<>();

        // for (int i = 0; i < 4; i++) {
        // for (int j = 0; j < 6; j++) {
        // var enemy = new Enemy(ALIEN_INIT_X + (ALIEN_WIDTH + ALIEN_GAP) * j,
        // ALIEN_INIT_Y + (ALIEN_HEIGHT + ALIEN_GAP) * i);
        // enemies.add(enemy);
        // }
        // }
        player = new Player();
        // shot = new Shot();
    }

    private void drawGameInfo(Graphics g) {
        drawScore(g);
        drawLife(g);
        drawSpeedInfo(g);
        drawAmmoInfo(g);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 24)); // Use monospaced for equal character width

        // Format score with leading zeros (8 digits)
        String formattedScore = String.format("%08d", score);

        // Draw score at top-left
        g.drawString(formattedScore, 20, 40);
    }

    private void drawAmmoInfo(Graphics g) {
        // the text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 24)); // Use monospaced for equal character width
        String text = player.getBulletStage() + "x";

        // Get font metrics to calculate text width for right alignment
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        // Draw in top right corner (10 pixels from right edge, 15 pixels from top)
        g.drawString(text, d.width - textWidth - 330, 40);

        // the image
        ImageIcon ii = new ImageIcon(IMG_AMMO_ICON);
        g.drawImage(ii.getImage(), d.width - 405, 10, this);
    }

    private void drawSpeedInfo(Graphics g) {
        // the text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 24)); // Use monospaced for equal character width
        String text = player.getSpeedStage() + "x";

        // Get font metrics to calculate text width for right alignment
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        // Draw in top right corner (10 pixels from right edge, 15 pixels from top)
        g.drawString(text, d.width - textWidth - 230, 40);

        // the image
        ImageIcon ii = new ImageIcon(IMG_SPEED_ICON);
        g.drawImage(ii.getImage(), d.width - 305, 10, this);

    }

    private void drawLife(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 24)); // Use monospaced for equal character width
        String livesText = ": " + player.getLives();

        // Get font metrics to calculate text width for right alignment
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(livesText);

        // Draw in top right corner (10 pixels from right edge, 15 pixels from top)
        g.drawString(livesText, d.width - textWidth - 40, 40);

        // the image
        ImageIcon ii = new ImageIcon(IMG_HEALTH_ICON);
        g.drawImage(ii.getImage(), d.width - 130, 10, this);
    }

    private void drawStarCluster(Graphics g, int x, int y, int width, int height) {
        // Set star color to white
        g.setColor(Color.WHITE);

        // Draw multiple stars in a cluster pattern
        // Main star (larger)
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g.fillOval(centerX - 2, centerY - 2, 4, 4);

        // Smaller surrounding stars
        g.fillOval(centerX - 15, centerY - 10, 2, 2);
        g.fillOval(centerX + 12, centerY - 8, 2, 2);
        g.fillOval(centerX - 8, centerY + 12, 2, 2);
        g.fillOval(centerX + 10, centerY + 15, 2, 2);

        // Tiny stars for more detail
        g.fillOval(centerX - 20, centerY + 5, 1, 1);
        g.fillOval(centerX + 18, centerY - 15, 1, 1);
        g.fillOval(centerX - 5, centerY - 18, 1, 1);
        g.fillOval(centerX + 8, centerY + 20, 1, 1);
    }

    private void drawAliens(Graphics g) {
        for (Enemy enemy : enemies) {
            if (enemy instanceof Boss) {
                Boss.drawBossStuff(g, boss, player, this);
            } else {
                if (enemy.isVisible()) {

                    g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
                }

                if (enemy.isDying()) {

                    enemy.die();
                }
            }
        }
    }

    private void drawPowreUps(Graphics g) {

        for (PowerUp p : powerups) {

            if (p.isVisible()) {

                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }

            if (p.isDying()) {

                p.die();
            }
        }
    }

    private void drawPlayer(Graphics2D g2d) {

        if (player.isVisible()) {
            if (player.isOnCoolDown()) {
                float opacity = 0.5f; // Example: 50% transparent
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
            } else {
                g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
            }
        }

        if (player.isDying()) {

            player.die();
            Timer inGameTimer = new Timer(1000, e -> inGame = false);
            inGameTimer.setRepeats(false);
            inGameTimer.start();
        }
    }

    private void drawShot(Graphics g) {

        for (Shot shot : shots) {

            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    private void drawBombing(Graphics g) {
        for (Enemy enemy : enemies) {
            for (Enemy.Bomb bomb : enemy.getBombs()) {
                if (enemy instanceof AlienUFO ufo) {
                    if (!bomb.isDestroyed()) {
                        g.drawImage(bomb.getImage(), bomb.getX(), bomb.getY(), this);
                    }
                }
            }
        }
    }

    private void drawStars(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        // Use async update for better performance
        starField.updateAsync(1.5f, frame);
        starField.draw(g2d);

        // Add effects less frequently
        if (frame % 3 == 0) { // Every 3rd frame
            starField.addRandomEffects(g2d, frame);
        }
    }



    private void drawExplosions(Graphics g) {

        // List<Explosion> toRemove = new ArrayList<>();

        for (Explosion explosion : explosions) {

            if (explosion.isVisible()) {
                // explosion.act();
                g.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), this);
                // explosion.visibleCountDown();
                // if (!explosion.isVisible()) {
                // toRemove.add(explosion);
                // }
            }
        }

        // explosions.removeAll(toRemove);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.white);
        g.drawString("FRAME: " + frame, 10, 10);

        g.setColor(Color.green);

        if (inGame) {

            if (!isFading) {
                drawStars(g2d);
            }
            drawAliens(g);
            drawPowreUps(g);
            drawExplosions(g);
            drawBombing(g);
            drawPlayer(g2d);
            Shield.drawActiveShield(player, g2d);
            drawShot(g);
            drawGameInfo(g);
            drawFadeMessage(g);
            drawPlayerExplosion(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            saveScore(score);

            if (boss != null&&boss.isDead()) {
                drawVictory(g);
            } else {
                drawGameOver(g);
            }

            // if (boss.isDead()) {
            // drawVictory(g);
            // } else {
            // drawGameOver(g);
            // }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    // private void gameOver(Graphics g) {
    //
    // g.setColor(Color.black);
    // g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
    //
    // g.setColor(new Color(0, 32, 48));
    // g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
    // g.setColor(Color.white);
    // g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
    //
    // var small = new Font("Helvetica", Font.BOLD, 14);
    // var fontMetrics = this.getFontMetrics(small);
    //
    // g.setColor(Color.white);
    // g.setFont(small);
    // g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
    // BOARD_WIDTH / 2);
    // }

    private void handleShooting() {
        audioPlayer.playLaser();
        if (player.getBulletStage() == 1) {
            // Single shot - straight up
            shots.add(new Shot(player.getX() , player.getY(), 0, -3));

        } else if (player.getBulletStage() == 2) {
            // Double shot - slight spread
            shots.add(new Shot(player.getX() - 10, player.getY(), -1, -3));
            shots.add(new Shot(player.getX()  + 10, player.getY(), 1, -3));

        } else if (player.getBulletStage() == 3) {
            // Triple shot - center straight, sides angled
            shots.add(new Shot(player.getX()  - 20, player.getY(), -2, -3));
            shots.add(new Shot(player.getX() , player.getY(), 0, -3));
            shots.add(new Shot(player.getX()  + 20, player.getY(), 2, -3));

        } else if (player.getBulletStage() == 4) {
            // Quad shot - outer ones scatter more, inner ones slight scatter
            shots.add(new Shot(player.getX() - 30, player.getY(), -3, -3)); // Far left
            shots.add(new Shot(player.getX()  - 10, player.getY(), -1, -3)); // Inner left
            shots.add(new Shot(player.getX()  + 10, player.getY(), 1, -3)); // Inner right
            shots.add(new Shot(player.getX()  + 30, player.getY(), 3, -3)); // Far right
        }
    }

    private void update() {
        if (!isFading) {
            spawnEntity();
        }

        if (frame % 60 == 0) {
            score++;
        }

        // for stage transitions
        checkStageTransitions();
        updateFadeMessage();

        // player
        player.act();

        // player explosion
        if (player.isDying()) {
            playerExplosion.act();
        }

        // Power-ups
        for (PowerUp powerup : powerups) {
            if (powerup.isVisible()) {
                powerup.act();
                if (powerup.collidesWith(player)) {
                    if (powerup instanceof Life) {
                        audioPlayer.playHealthPowerUp();
                    } else {
                        audioPlayer.playNormalPowerUp();
                    }
                    powerup.upgrade(player);
                }
            }
        }

        // Enemies
        // using iterator so that you can remove elements within a list whilst looping
        // over them :)
        Iterator<Enemy> iterator = enemies.iterator();

        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();

            if (enemy.isVisible() && !(enemy instanceof Boss)) {
                int y = enemy.getY();

                if (y >= BOARD_HEIGHT) {
                    iterator.remove();
                } else {
                    enemy.act(direction); // only act if not removed
                }
            }
        }
        // explosion

        List<Explosion> explosionsToRemove = new ArrayList<>();
        for (Explosion explosion : explosions) {
            explosion.act(); // Advance animation frame
            if (!explosion.isVisible()) {
                explosionsToRemove.add(explosion);
            }
        }
        explosions.removeAll(explosionsToRemove);

        // shot
        List<Shot> shotsToRemove = new ArrayList<>();
        for (Shot shot : shots) {
            shot.act();
            if (shot.isVisible()) {
                int shotX = shot.getX();
                int shotY = shot.getY();

                for (Enemy enemy : enemies) {
                    // Collision detection: shot and enemy
                    int enemyX = enemy.getX();
                    int enemyY = enemy.getY();

                    int enemyWidth = enemy.getWidth();
                    int enemyHeight = enemy.getHeight();

                    if (enemy.isVisible() && shot.isVisible()
                            && shotX >= (enemyX)
                            && shotX <= (enemyX + enemyWidth)
                            && shotY >= (enemyY)
                            && shotY <= (enemyY + enemyHeight)) {
                        if(boss!=null&&!(boss.isExploding())){
                            score += 5;
                        }
                        if (boss==null){
                            score += 5;

                        }
                        //score += 5;
                        if((enemy instanceof FlyingAlien)){
                            audioPlayer.playFlyingAlienExplosion();
                        }
                        if((enemy instanceof AlienUFO)){
                            audioPlayer.playExplosion();
                        }
                        // if((enemy instanceof BabyBoss)){
                        //     audioPlayer.playBabyBossExplosion();
                        // }
                        //audioPlayer.playExplosion();
                        if (!(enemy instanceof Boss)) {
                            explosions.add(new Explosion(enemy.getX(), enemy.getY(), enemy.getType()));
                        }
                        if(boss!=null && !(boss.isExploding())) {
                            if(enemy instanceof Boss){
                                boss.takeDamage();
                            }
                            
                            
                        }
                        
                        deaths++;
                        enemy.setDying(true);
                        shot.die();
                        System.out.println("Shot removed");
                        shotsToRemove.add(shot);
                    }
                }

                int y = shot.getY();
                y -= 4;
                // y -= 20;

                if (y < 13) {
                    shot.die();
                    System.out.println("Shot removed");
                    shotsToRemove.add(shot);
                } else {
                    shot.setY(y);
                }
            }
        }

        // System.out.println("Shots to remove: " + shotsToRemove);
        //shots.removeAll(shotsToRemove);
        // enemies
        // for (Enemy enemy : enemies) {
        // int x = enemy.getX();
        // if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
        // direction = -1;
        // for (Enemy e2 : enemies) {
        // e2.setY(e2.getY() + GO_DOWN);
        // }
        // }
        // if (x <= BORDER_LEFT && direction != 1) {
        // direction = 1;
        // for (Enemy e : enemies) {
        // e.setY(e.getY() + GO_DOWN);
        // }
        // }
        // }
        // for (Enemy enemy : enemies) {
        // if (enemy.isVisible()) {
        // int y = enemy.getY();
        // if (y > GROUND - ALIEN_HEIGHT) {
        // inGame = false;
        // message = "Invasion!";
        // }
        // enemy.act(direction);
        // }
        // }
        // bombs - collision detection
        // Bomb is with enemy, so it loops over enemies

        for (Enemy enemy : enemies) {
            // ufo collide
            if (enemy instanceof AlienUFO) {
                if (enemy.isVisible() && player.isVisible()) {
                    if (enemy.getBounds().intersects(player.getBounds())) {
                        handleCollision(player.getX(), player.getY());
                        System.out.println("Player hit by alienufo! Game Over!");
                    }
                }

                // Cooldown logic
                enemy.tickCooldown();

                // Random chance to drop a bomb
                if (enemy.isVisible() && enemy.canDropBomb() && randomizer.nextInt(120) == 0) {
                    enemy.dropBomb();
                }

                // Handle all bombs from this enemy
                for (Enemy.Bomb bomb : enemy.getBombs()) {
                    if (!bomb.isDestroyed()) {

                        // Move bomb downward
                        bomb.setY(bomb.getY() + 4);

                        // If hits the ground, destroy it
                        if (bomb.getY() >= GROUND - BOMB_HEIGHT) {
                            bomb.setDestroyed(true);
                        }

                        // Collision with player
                        int bombX = bomb.getX();
                        int bombY = bomb.getY();
                        int playerX = player.getX();
                        int playerY = player.getY();

                        if (player.isVisible()
                                && bombX >= playerX && bombX <= (playerX + PLAYER_WIDTH)
                                && bombY >= playerY && bombY <= (playerY + PLAYER_HEIGHT)) {

                            handleCollision(playerX, playerY);
                            bomb.setDestroyed(true);
                        }
                    }
                }

                // Clean up destroyed bombs
                enemy.updateBombs();
            }
            // collide with flying alien
            if (enemy instanceof FlyingAlien) {
                if (enemy.isVisible() && player.isVisible()) {
                    if (enemy.getBounds().intersects(player.getBounds())) {
                        handleCollision(player.getX(), player.getY());
                        System.out.println("Player hit by flyingalien! Game Over!");
                    }
                }
            }
        }

        // Update boss
        if (boss != null) {
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
                BabyBossExplosion.triggerAllBabyBossExplosions(babyBossExplosionsTriggered, boss);
            }

            if (boss.isDead() && inGame) {
                if (BabyBossExplosion.areAllBabyBossExplosionsComplete(boss)) {
                    inGame = false;
                    System.out.println("THIS IS RUNNING");
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
                            handleCollision(player.getX(), player.getY());
                            System.out.println("Player hit by baby boss! Game Over!");
                            break;
                        }
                    }
                }

                // Collision with boss
                if (boss.isVisible() && player.isVisible()) {
                    if (boss.getBounds().intersects(player.getBounds())) {
                        handleCollision(player.getX(), player.getY());
                        System.out.println("Player hit by boss! Game Over!");
                    }
                }

                // Handle shot collisions (only if boss not exploding)
                for (Shot shot : shots) {
                    if (!shot.isVisible())
                        continue;

                    // Check bullet-boss collisions
                    // if (shot.getBounds().intersects(boss.getBounds()) && !boss.isDead()) {
                    //     boss.takeDamage();
                    //     shot.setVisible(false); // Remove the bullet
                    // }

                    // Check bullet-baby boss collisions
                    for (BabyBoss bb : babies) {
                        if (bb.isVisible() && !bb.isExploding() && shot.getBounds().intersects(bb.getBounds())) {
                            // Hit! Remove shot and start explosion
                            audioPlayer.playBabyBossExplosion();
                            shotsToRemove.add(shot);
                            shot.setVisible(false);

                            System.out.println("=== COLLISION DETECTED ===");
                            System.out.println("Shot hit baby boss at: " + bb.getX() + ", " + bb.getY());
                            bb.destroy(); // This will start the explosion animation
                            System.out.println(
                                    "Baby boss destroy() called - should be exploding now: " + bb.isExploding());
                            break; // Exit inner loop since shot is destroyed
                        }
                    }
                }
            }
        }
        shots.removeAll(shotsToRemove);
       //System.out.println(player.isShieldActive());
    }

    private void handleCollision(int playerX, int playerY) {
        player.setOnCoolDown(true);
        if (!player.isShieldActive()) {
            player.setLives(player.getLives() - 1);
            if (player.getLives() != 0) {
                audioPlayer.playPlayerHitSound();
            } else {
                audioPlayer.playSpaceshipExplosion();;
                // Audio here for player death
            }
        } else {
            //player.setShieldActive(false);
            currentShield.disposeShieldTimer();
            player.setShieldActive(false);
            audioPlayer.playShieldGuardSound();
        }

        // cool down period for player
        new Timer(1000, e -> {
            if (player.isShieldActive()) {
                //player.setShieldActive(false);
            }
            player.setOnCoolDown(false);
        }).start();

        if (player.getLives() == 0) {
            playerExplosion = new PlayerExplosion(playerX, playerY);
            player.setDying(true);
        }
    }

    private void clearAllSprites() {
        // Clear all baby bosses
        boss.getBabyBosses().clear();

        // Clear all player shots
        player.getShots().clear();

        // Make sure all sprites are hidden
        System.out.println("All sprites cleared for victory screen!");
    }

    private void drawVictory(Graphics g) {
        // Draw victory screen (same format as game over) 
        audioPlayer.stopScene2Music();
        audioPlayer.stopBossSceneMusic();
        audioPlayer.playWinning();
        win=true;
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
        g.drawString("High Score: " + loadScore(), x + 55, y + 90);
    }

    private void drawGameOver(Graphics g) {
        audioPlayer.stopScene2Music();
        audioPlayer.stopBossSceneMusic();
        audioPlayer.playGameOver();
        lose=true;
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
        // g.drawString("Press ESC to Exit", x, y + 90);
        g.drawString("High Score: " + loadScore(), x + 55, y + 90);
    }

    private void resetGame() {
        
        initAudio();
        frame = 0;
        babyBossExplosionsTriggered = false;

        // Reset player (using the reset method)
        player.reset();
        // Additional player variable resets (if not handled in player.reset())
        player.setLives(3);
        player.setSpeedStage(1);
        player.setBulletStage(1);
        player.setShieldActive(false);
        player.setOnCoolDown(false);
        player.setX(270); // START_X
        player.setY(540); // START_Y

        // Reset boss (using the reset method)
        if (boss != null) {
            boss.reset();
            // Additional boss variable resets (if not handled in boss.reset())
            boss.setCurrentFrameIndex(0);
            boss.setAnimationCounter(0);
            boss.clearBabyBosses(); // Clear the babyBosses list
            boss.setWaveCooldown(0);
            boss.setWaveCount(0);
            boss.setFirstWaveSpawned(false);
            boss.setHitCount(0);
            boss.setExplosion(null);
            boss.setIsExploding(false);
            boss.setIsDead(false);
        }

        // Clear all game object lists
        shots.clear();
        enemies.clear();
        explosions.clear();
        powerups.clear();

        // Reset game state variables
        score = 0;

        // Reset stage tracking
        currentStage = 1;
        stage2MessageShown = false;
        stage3MessageShown = false;

        // Reset fade/message system
        fadeTimer = 0;
        isFading = false;
        message = "Game Over"; // Reset to default message

        // Reset shield system
        currentShield = null;
        isOnCooldown = false;

        // Reset player explosion if it exists
        if (playerExplosion != null) {
            playerExplosion.setClipNo(0);
            playerExplosion.setAnimationCounter(0);
            playerExplosion = null;
        }

        // Restart the game timer if it was stopped
        if (timer != null && !timer.isRunning()) {
            timer.start();
        }

    }

    private void restartGame() {
        resetGame();
        inGame = true;
    }

    // Method to trigger a fade message
    private void showFadeMessage(String message) {
        fadeMessage = message;
        fadeAlpha = 0;
        fadeTimer = 0;
        isFading = true;

        // Initialize action lines
        showActionLines = true;
        actionLineTimer = 0;
        actionLines.clear();
    }

    private void checkStageTransitions() {
        // Stage 2 transition
        if (frame >= STAGE_1_END && !stage2MessageShown) {
            enemies.clear();
            currentStage = 2;
            showFadeMessage("Your enemies grow stronger...");
            audioPlayer.playWarpSound();
            stage2MessageShown = true;
        }

        // Stage 3 transition
        if (frame >= STAGE_2_END && !stage3MessageShown) {
            enemies.clear();
            currentStage = 3;
            showFadeMessage("Meet the big boss...");
            audioPlayer.playWarpSound();
            stage3MessageShown = true;
            audioPlayer.stopScene2Music();
            audioPlayer.playBossSceneMusic();

        }
    }

    // Add this to your update() method to handle fading
    private void updateFadeMessage() {
        if (isFading) {
            fadeTimer++;

            if (fadeTimer <= FADE_IN_FRAMES) {
                // Fade in
                fadeAlpha = (int) (255 * ((double) fadeTimer / FADE_IN_FRAMES));
            } else if (fadeTimer <= FADE_IN_FRAMES + FADE_HOLD_FRAMES) {
                // Hold at full opacity
                fadeAlpha = 255;
            } else if (fadeTimer <= FADE_IN_FRAMES + FADE_HOLD_FRAMES + FADE_OUT_FRAMES) {
                // Fade out
                int fadeOutProgress = fadeTimer - FADE_IN_FRAMES - FADE_HOLD_FRAMES;
                fadeAlpha = 255 - (int) (255 * ((double) fadeOutProgress / FADE_OUT_FRAMES));
            } else {
                // Fade complete
                isFading = false;
                fadeAlpha = 0;
                fadeMessage = "";
                showActionLines = false; // Stop action lines when fade is complete
            }
        }

        // Handle action lines
        if (showActionLines) {
            actionLineTimer++;

            // Start showing action lines after a delay
            if (actionLineTimer >= ACTION_LINE_DELAY) {
                // Generate new action lines periodically
                if (actionLineTimer % 3 == 0) { // Every 3 frames
                    for (int i = 0; i < 5; i++) {
                        int x = randomizer.nextInt(BOARD_WIDTH);
                        int y = -randomizer.nextInt(100); // Start above screen
                        int length = 20 + randomizer.nextInt(40);
                        int speed = 8 + randomizer.nextInt(12);
                        actionLines.add(new ActionLine(x, y, length, speed));
                    }
                }
            }

            // Update existing action lines
            List<ActionLine> linesToRemove = new ArrayList<>();
            for (ActionLine line : actionLines) {
                line.update();
                if (!line.isVisible()) {
                    linesToRemove.add(line);
                }
            }
            actionLines.removeAll(linesToRemove);

            // Stop action lines after duration
            if (actionLineTimer >= ACTION_LINE_DURATION) {
                showActionLines = false;
                actionLines.clear();
            }
        }
    }

    private void drawFadeMessage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw action lines first (behind the message)
        if (showActionLines && actionLineTimer >= ACTION_LINE_DELAY) {
            for (ActionLine line : actionLines) {
                line.draw(g2d);
            }
        }

        if (isFading && fadeAlpha > 0) {
            // Set up font and get metrics
            Font messageFont = new Font("Arial", Font.BOLD, 36);
            FontMetrics fm = g2d.getFontMetrics(messageFont);

            // Calculate position to center the text
            int textWidth = fm.stringWidth(fadeMessage);
            int textHeight = fm.getHeight();
            int x = (BOARD_WIDTH - textWidth) / 2;
            int y = (BOARD_HEIGHT - textHeight) / 2;

            // Create semi-transparent background
            g2d.setColor(new Color(0, 0, 0, Math.min(fadeAlpha, 150))); // Dark background
            g2d.fillRoundRect(x - 20, y - textHeight + 10, textWidth + 40, textHeight + 20, 10, 10);

            // Draw the text with fade effect
            g2d.setColor(new Color(255, 255, 255, fadeAlpha)); // White text
            g2d.setFont(messageFont);
            g2d.drawString(fadeMessage, x, y);

            // Optional: Add a subtle glow effect
            g2d.setColor(new Color(255, 255, 0, fadeAlpha / 3)); // Yellow glow
            g2d.drawString(fadeMessage, x - 1, y - 1);
            g2d.drawString(fadeMessage, x + 1, y + 1);
        }
    }

    // Add these fields to your Scene1 class
    private boolean showActionLines = false;
    private int actionLineTimer = 0;
    private final int ACTION_LINE_DURATION = 180; // 3 seconds at 60 FPS
    private final int ACTION_LINE_DELAY = 30; // Start action lines after 0.5 seconds of fade message
    private List<ActionLine> actionLines = new ArrayList<>();

    // ActionLine helper class - add this inside Scene1 or as a separate class
    private class ActionLine {
        private int x, y;
        private int length;
        private int speed;
        private Color color;
        private int alpha;

        public ActionLine(int x, int y, int length, int speed) {
            this.x = x;
            this.y = y;
            this.length = length;
            this.speed = speed;
            this.alpha = 255;
            // Vary the color slightly for more dynamic effect
            int variation = randomizer.nextInt(50);
            this.color = new Color(200 + variation, 200 + variation, 255);
        }

        public void update() {
            y += speed;
            // Fade out as they move
            alpha = Math.max(0, alpha - 3);
        }

        public void draw(Graphics2D g2d) {
            if (alpha > 0) {
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x, y, x, y + length);
            }
        }

        public boolean isVisible() {
            return y < BOARD_HEIGHT + length && alpha > 0;
        }
        // bombs
        // for (Enemy enemy : enemies) {
        // if (enemy instanceof AlienUFO ufo) {
        //
        // // Cooldown logic
        // enemy.tickCooldown();
        //
        // // Random chance to drop a bomb
        // if (enemy.isVisible() && enemy.canDropBomb() && randomizer.nextInt(120) == 0)
        // {
        // enemy.dropBomb();
        // }
        //
        // // Handle all bombs from this enemy
        // for (Enemy.Bomb bomb : enemy.getBombs()) {
        // if (!bomb.isDestroyed()) {
        //
        // // Move bomb downward
        // bomb.setY(bomb.getY() + 4);
        //
        // // If hits the ground, destroy it
        // if (bomb.getY() >= GROUND - BOMB_HEIGHT) {
        // bomb.setDestroyed(true);
        // }
        //
        // // Collision with player
        // int bombX = bomb.getX();
        // int bombY = bomb.getY();
        // int playerX = player.getX();
        // int playerY = player.getY();
        //
        // if (player.isVisible()
        // && bombX >= playerX && bombX <= (playerX + PLAYER_WIDTH)
        // && bombY >= playerY && bombY <= (playerY + PLAYER_HEIGHT)) {
        //
        // // explosions.add(new Explosion(playerX, playerY));
        //
        // playerExplosion = new PlayerExplosion(playerX, playerY);
        // player.setDying(true);
        // bomb.setDestroyed(true);
        // }
        // }
        // }
        //
        // // Clean up destroyed bombs
        // enemy.updateBombs();
        // }
        // }

    }

    private void drawPlayerExplosion(Graphics g) {
        if (playerExplosion != null) {
            g.drawImage(playerExplosion.getImage(), playerExplosion.getX(), playerExplosion.getY(), this);
        }
    }

    private void doGameCycle() {
        if (!isFading) {
            frame++;
        }
        update();
        repaint();
    }

    private void saveScore(int score) {
        if (score > loadScore()) {
            String json = "{ \"highScore\": " + score + " }";
            try (FileWriter file = new FileWriter("score.json")) {
                file.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // String json = "{ \"highScore\": " + score + " }";
        // try (FileWriter file = new FileWriter("score.json")) {
        //     file.write(json);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

    }

    public int loadScore() {
        File file = new File("score.json");
        if (!file.exists()) {
            System.out.println("score.json not found.");
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                // very simple manual parsing
                line = line.replaceAll("[^0-9]", ""); // removes non-digits
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // System.out.println("Scene2.keyPressed: " + e.getKeyCode());

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();
            int w = player.getWidth();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE && inGame) {
                System.out.println("Shots: " + shots.size());
                if (shots.size() < 4 * player.getBulletStage()) {
                    // Create a new shot and add it to the list
                    handleShooting();
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_R && (!inGame)) {
                if (win=true){
                    audioPlayer.stopWinning();
                    win=false;
                }
                if (lose=true){
                    audioPlayer.stopGameOver();
                    lose=false;
                }
                restartGame();
            }

            // if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // resetGame();
            // stop();
            // game.loadTitle();
            // SwingUtilities.invokeLater(() -> {
            // game.getTitleScene().requestFocusInWindow();
            // });
            // }
        }
    }
}
