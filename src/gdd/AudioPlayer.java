// Java program to play an Audio
// file using Clip Object
package gdd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.*;

import static gdd.Global.*;

public class AudioPlayer {
    private Clip titleClip;
    private Clip scene2Clip;

    private Clip healthPowerUpClip;
    private Clip normalPowerUpClip;
    private Clip warpSoundClip;
    private Clip playerHitSoundClip;
    private Clip shieldGuardSoundClip;

    // Track current positions for pause/resume functionality
    private Long titleCurrentFrame = 0L;
    private Long scene2CurrentFrame = 0L;

    // Track playback status
    private String titleStatus = "stopped";
    private String scene2Status = "stopped";

    // Store file paths for reset functionality
    private String titleMusicPath;
    private String scene2MusicPath;

    private int laserClipIndex = 0;
    private int explosionClipIndex = 0;

    private final int clipPoolSize = 12;
    private ArrayList<Clip> explosionClips;
    private ArrayList<Clip> laserClips;

    private ExecutorService audioExecutor;

    public AudioPlayer() {
        audioExecutor = Executors.newSingleThreadExecutor();
        initSounds();
    }

    private void initSounds() {
        // Store paths for later use
        titleMusicPath = TITLE_MUSIC;
        scene2MusicPath = SCENE2_MUSIC;

        File titleMusicFile = new File(titleMusicPath);
        File scene2MusicFile = new File(scene2MusicPath);

        explosionClips = new ArrayList<>();
        laserClips = new ArrayList<>();

        try {
            AudioInputStream titleAudioStream = AudioSystem.getAudioInputStream(titleMusicFile);
            titleClip = AudioSystem.getClip();
            titleClip.open(titleAudioStream);
            setVolume(titleClip, 0.75f);

            AudioInputStream scene2AudioStream = AudioSystem.getAudioInputStream(scene2MusicFile);
            scene2Clip = AudioSystem.getClip();
            scene2Clip.open(scene2AudioStream);
            setVolume(scene2Clip, 0.75f);


            AudioInputStream healthPowerUpStream = AudioSystem.getAudioInputStream(new File(HEALTH_POWERUP));
            healthPowerUpClip = AudioSystem.getClip();
            healthPowerUpClip.open(healthPowerUpStream);
            setVolume(healthPowerUpClip, 0.75f);

            AudioInputStream normalPowerUpStream = AudioSystem.getAudioInputStream(new File(NORMAL_POWERUP));
            normalPowerUpClip = AudioSystem.getClip();
            normalPowerUpClip.open(normalPowerUpStream);
            setVolume(normalPowerUpClip, 0.75f);

            AudioInputStream warpSoundStream = AudioSystem.getAudioInputStream(new File(WARP_SOUND));
            warpSoundClip = AudioSystem.getClip();
            warpSoundClip.open(warpSoundStream);

            AudioInputStream playerHitSoundStream = AudioSystem.getAudioInputStream(new File(PLAYER_HIT_SOUND));
            playerHitSoundClip = AudioSystem.getClip();
            playerHitSoundClip.open(playerHitSoundStream);

            AudioInputStream shieldGuardSoundStream = AudioSystem.getAudioInputStream(new File(SHIELD_GUARD_SOUND));
            shieldGuardSoundClip = AudioSystem.getClip();
            shieldGuardSoundClip.open(shieldGuardSoundStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        loadClipPools();
    }

    public void loadClipPools() {
        for (int i = 0; i < clipPoolSize; i++) {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(EXPLOSION_SOUND));
                Clip explosionClip = AudioSystem.getClip();
                explosionClip.open(stream);
                setVolume(explosionClip, 0.75f);
                explosionClips.add(explosionClip);

                AudioInputStream laserStream = AudioSystem.getAudioInputStream(new File(LASER_SOUND));
                Clip laserClip = AudioSystem.getClip();
                laserClip.open(laserStream);
                setVolume(laserClip, 0.8f);
                laserClips.add(laserClip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Enhanced title music methods with pause/resume support
    public void playTitleMusic() {
        if (titleClip != null) {
            titleClip.setFramePosition(0);
            titleClip.loop(Clip.LOOP_CONTINUOUSLY);
            titleStatus = "playing";
        }
    }

    public void pauseTitleMusic() {
        if (titleClip != null && titleStatus.equals("playing")) {
            titleCurrentFrame = titleClip.getMicrosecondPosition();
            titleClip.stop();
            titleStatus = "paused";
        }
    }

    public void resumeTitleMusic() {
        if (titleClip != null && titleStatus.equals("paused")) {
            try {
                titleClip.close();
                resetTitleAudioStream();
                titleClip.setMicrosecondPosition(titleCurrentFrame);
                playTitleMusic();
            } catch (Exception e) {
                System.err.println("Error resuming title music: " + e.getMessage());
            }
        }
    }

    public void restartTitleMusic() {
        if (titleClip != null) {
            try {
                titleClip.stop();
                titleClip.close();
                resetTitleAudioStream();
                titleCurrentFrame = 0L;
                titleClip.setMicrosecondPosition(0);
                playTitleMusic();
            } catch (Exception e) {
                System.err.println("Error restarting title music: " + e.getMessage());
            }
        }
    }

    public void stopTitleMusic() {
        if (titleClip != null) {
            titleCurrentFrame = 0L;
            titleClip.stop();
            titleClip.close();
            titleStatus = "stopped";
        }
    }

    // Enhanced scene2 music methods with pause/resume support
    public void playScene2Music() {
        if (scene2Clip != null) {
            scene2Clip.loop(Clip.LOOP_CONTINUOUSLY);
            scene2Status = "playing";
        }
    }

    public void pauseScene2Music() {
        if (scene2Clip != null && scene2Status.equals("playing")) {
            scene2CurrentFrame = scene2Clip.getMicrosecondPosition();
            scene2Clip.stop();
            scene2Status = "paused";
        }
    }

    public void resumeScene2Music() {
        if (scene2Clip != null && scene2Status.equals("paused")) {
            try {
                scene2Clip.close();
                resetScene2AudioStream();
                scene2Clip.setMicrosecondPosition(scene2CurrentFrame);
                playScene2Music();
            } catch (Exception e) {
                System.err.println("Error resuming scene2 music: " + e.getMessage());
            }
        }
    }

    public void restartScene2Music() {
        if (scene2Clip != null) {
            try {
                scene2Clip.stop();
                scene2Clip.close();
                resetScene2AudioStream();
                scene2CurrentFrame = 0L;
                scene2Clip.setMicrosecondPosition(0);
                playScene2Music();
            } catch (Exception e) {
                System.err.println("Error restarting scene2 music: " + e.getMessage());
            }
        }
    }

    public void playHealthPowerUp() {
        if (healthPowerUpClip != null) {
            healthPowerUpClip.setFramePosition(0);
            healthPowerUpClip.start();
        }
    }

    public void playNormalPowerUp() {
        if (normalPowerUpClip != null) {
            normalPowerUpClip.setFramePosition(0);
            normalPowerUpClip.start();
        }
    }

    public void playWarpSound() {
        if (warpSoundClip != null) {
            warpSoundClip.setFramePosition(0);
            warpSoundClip.start();
        }
    }

    public void playPlayerHitSound() {
        if (playerHitSoundClip != null) {
            playerHitSoundClip.setFramePosition(0);
            playerHitSoundClip.start();
        }
    }

    public void playShieldGuardSound() {
        if (shieldGuardSoundClip != null) {
            shieldGuardSoundClip.setFramePosition(0);
            shieldGuardSoundClip.start();
        }
    }

    public void stopScene2Music() {
        if (scene2Clip != null) {
            scene2CurrentFrame = 0L;
            scene2Clip.stop();
            scene2Clip.close();
            scene2Status = "stopped";
        }
    }

    // Jump to specific time methods
    public void jumpTitleMusic(long microseconds) {
        if (titleClip != null && microseconds > 0 && microseconds < titleClip.getMicrosecondLength()) {
            try {
                titleClip.stop();
                titleClip.close();
                resetTitleAudioStream();
                titleCurrentFrame = microseconds;
                titleClip.setMicrosecondPosition(microseconds);
                playTitleMusic();
            } catch (Exception e) {
                System.err.println("Error jumping title music: " + e.getMessage());
            }
        }
    }

    public void jumpScene2Music(long microseconds) {
        if (scene2Clip != null && microseconds > 0 && microseconds < scene2Clip.getMicrosecondLength()) {
            try {
                scene2Clip.stop();
                scene2Clip.close();
                resetScene2AudioStream();
                scene2CurrentFrame = microseconds;
                scene2Clip.setMicrosecondPosition(microseconds);
                playScene2Music();
            } catch (Exception e) {
                System.err.println("Error jumping scene2 music: " + e.getMessage());
            }
        }
    }

    // Helper methods to reset audio streams
    private void resetTitleAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(titleMusicPath).getAbsoluteFile());
        titleClip.open(audioInputStream);
        titleClip.loop(Clip.LOOP_CONTINUOUSLY);
        setVolume(titleClip, 0.75f);
    }

    private void resetScene2AudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(scene2MusicPath).getAbsoluteFile());
        scene2Clip.open(audioInputStream);
        scene2Clip.loop(Clip.LOOP_CONTINUOUSLY);
        setVolume(scene2Clip, 0.75f);
    }

    // Getters for music length and current position
    public long getTitleMusicLength() {
        return titleClip != null ? titleClip.getMicrosecondLength() : 0;
    }

    public long getScene2MusicLength() {
        return scene2Clip != null ? scene2Clip.getMicrosecondLength() : 0;
    }

    public long getTitleCurrentPosition() {
        return titleClip != null ? titleClip.getMicrosecondPosition() : 0;
    }

    public long getScene2CurrentPosition() {
        return scene2Clip != null ? scene2Clip.getMicrosecondPosition() : 0;
    }

    public String getTitleStatus() {
        return titleStatus;
    }

    public String getScene2Status() {
        return scene2Status;
    }

    // Original sound effect methods remain unchanged
    public void playLaser() {
        audioExecutor.submit(() -> {
            while (laserClips.get(laserClipIndex).isRunning()) {
                laserClipIndex = (laserClipIndex + 1) % clipPoolSize;
            }

            laserClips.get(laserClipIndex).setFramePosition(0);
            laserClips.get(laserClipIndex).start();
        });
    }

    public void playExplosion() {
        audioExecutor.submit(() -> {
            while (explosionClips.get(explosionClipIndex).isRunning()) {
                explosionClipIndex = (explosionClipIndex + 1) % clipPoolSize;
            }

            explosionClips.get(explosionClipIndex).setFramePosition(0);
            explosionClips.get(explosionClipIndex).start();
        });
    }

    public void dispose() {
        // Close music clips
        if (titleClip != null) {
            titleClip.close();
        }
        if (scene2Clip != null) {
            scene2Clip.close();
        }

        // Close all explosion clips
        for (Clip clip : explosionClips) {
            if (clip != null) {
                clip.close();
            }
        }

        // Close all laser clips
        for (Clip clip : laserClips) {
            if (clip != null) {
                clip.close();
            }
        }

        audioExecutor.shutdown();
    }

    private void setVolume(Clip clip, float volume) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                // Convert percentage (0.0-1.0) to decibel range
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();

                gainControl.setValue(gain);
            } catch (Exception e) {
                System.err.println("Could not set volume: " + e.getMessage());
            }
        }
    }
}