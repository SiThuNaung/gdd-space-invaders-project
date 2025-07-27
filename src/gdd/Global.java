package gdd;

public class Global {
    private Global() {
        // Prevent instantiation
    }

    public static final int SCALE_FACTOR = 3; // Scaling factor for sprites

    public static final int BOARD_WIDTH = 716; // Doubled from 358
    public static final int BOARD_HEIGHT = 700; // Doubled from 350
    public static final int BORDER_RIGHT = 60; // Doubled from 30
    public static final int BORDER_LEFT = 10; // Doubled from 5

    public static final int GROUND = 580; // Doubled from 290
    public static final int BOMB_HEIGHT = 10; // Doubled from 5

    public static final int ALIEN_HEIGHT = 24; // Doubled from 12
    public static final int ALIEN_WIDTH = 24; // Doubled from 12
    public static final int ALIEN_INIT_X = 300; // Doubled from 150
    public static final int ALIEN_INIT_Y = 10; // Doubled from 5
    public static final int ALIEN_GAP = 30; // Gap between aliens

    public static final int GO_DOWN = 30; // Doubled from 15
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    public static final int CHANCE = 5;
    public static final int DELAY = 17;
    public static final int PLAYER_WIDTH = 30; // Doubled from 15
    public static final int PLAYER_HEIGHT = 20; // Doubled from 10

    // Images
    public static final String IMG_ENEMY = "src/images/alien.png";
    public static final String IMG_PLAYER = "src/images/player.png";
    public static final String IMG_SPACE_SHIP = "src/images/space_ship.png";
    public static final String IMG_BOOSTER = "src/images/boosters.png";
    public static final String IMG_BOOSTER_LEFT = "src/images/boosters_left.png";
    public static final String IMG_BOOSTER_RIGHT = "src/images/boosters_right.png";
    public static final String IMG_SHOT = "src/images/space_ship_shot.png";
    public static final String IMG_EXPLOSION = "src/images/explosion.png";
    public static final String IMG_SPACESHIP_EXPLOSION = "src/images/space_ship_explosion.png";
    public static final String IMG_TITLE = "src/images/title.png";
    public static final String IMG_ALIEN_UFO = "src/images/alien_ufo.png";
    public static final String  IMG_POWERUP_SPEEDUP = "src/images/powerup-s.png";
    public static final String IMG_POWERUP_HEART = "src/images/life_powerup.png";
    public static final String IMG_POWERUP_SHIELD = "src/images/shield.png";
    public static final String IMG_POWERUP_AMMO = "src/images/ammo_powerup.png";
    public static final String IMG_SPEED_ICON = "src/images/speed_boost_icon.png";
    public static final String IMG_AMMO_ICON = "src/images/ammo_info_icon.png";
    public static final String IMG_HEALTH_ICON = "src/images/health_info_icon.png";
    public static final String IMG_FLYING_ALIEN = "src/images/flying_alien.png";
    public static final String IMG_FLYING_ALIEN_EXPLOSION = "src/images/flying_alien_explosion.png";
    public static final String IMG_BOSS = "src/images/boss_3.png";
    public static final String IMG_BABY_BOSS = "src/images/boss_2.png";
    public static final String IMG_BABY_BOSS_EXPLOSION = "src/images/baby_boss_explosion.png";
    public static final String IMG_BOSS_EXPLOSION = "src/images/boss_explosion.png";

    // Stage timings in frame
    public static final int STAGE_1_END = 18000;   // 5 minutes (halfway)
    public static final int STAGE_2_END = 36000;   // 10 minutes (end)

    // Constants for fading
    public static final int FADE_DURATION = 180; // 3 seconds at 60fps
    public static final int FADE_IN_FRAMES = 60;  // 1 second fade in
    public static final int FADE_OUT_FRAMES = 60; // 1 second fade out
    public static final int FADE_HOLD_FRAMES = 60; // 1 second hold at full opacity

    // Audio
    public static final String TITLE_MUSIC = "src/audio/title.wav";
    public static final String SCENE2_MUSIC = "src/audio/scene1.wav";
    public static final String EXPLOSION_SOUND = "src/audio/invaderKilled.wav";
    public static final String LASER_SOUND = "src/audio/shoot.wav";
    public static final String HEALTH_POWERUP = "src/audio/health_powerup.wav";
    public static final String NORMAL_POWERUP = "src/audio/normal_powerup.wav";
    public static final String WARP_SOUND = "src/audio/warp_sound.wav";
    public static final String PLAYER_HIT_SOUND = "src/audio/player_hit_sound.wav";
    public static final String SHIELD_GUARD_SOUND = "src/audio/shield_guard_sound.wav";
}
