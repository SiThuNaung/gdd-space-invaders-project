package gdd.sprite;

import static gdd.Global.*;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class PlayerExplosion extends Sprite {

    private final Rectangle[] explosionFrames = new Rectangle[]{
            new Rectangle(166, 93, 34, 34),
            new Rectangle(121, 94, 32, 33),
            new Rectangle(216, 96, 30, 28),
            new Rectangle(254, 96, 28, 28),
            new Rectangle(79, 99, 30, 27),
            new Rectangle(44, 102, 26, 23),
            new Rectangle(19, 106, 13, 15),
            new Rectangle(17, 107, 11, 11),
            new Rectangle(165, 123, 1, 1)
    };

    private int clipNo = 0;
    private int animationCounter = 0;
    private final int ANIMATION_DELAY = 5; // adjust speed if needed
    private ImageIcon sheetIcon;

    public PlayerExplosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.visible = true;
        sheetIcon = new ImageIcon(IMG_SPACESHIP_EXPLOSION);
    }

    @Override
    public void act() {
        animationCounter++;
        if (animationCounter % ANIMATION_DELAY == 0) {
            clipNo++;
            if (clipNo >= explosionFrames.length) {
                visible = false;
            }
        }
    }

    @Override
    public Image getImage() {
        if (clipNo >= explosionFrames.length) {
            return null;
        }
        BufferedImage bImage = toBufferedImage(sheetIcon.getImage());
        Rectangle frame = explosionFrames[clipNo];
        return bImage.getSubimage(frame.x, frame.y, frame.width, frame.height);
    }
}
