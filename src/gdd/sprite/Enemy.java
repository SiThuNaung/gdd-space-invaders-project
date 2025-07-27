package gdd.sprite;

import static gdd.Global.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

public class Enemy extends Sprite {
    protected String type;
    private int dx = 2;
    private List<Bomb> bombs = new ArrayList<>();
    private int cooldown = 0; // frame-based bomb drop cooldown

    public Enemy(int x, int y) {

        initEnemy(x, y);
    }


    private void initEnemy(int x, int y) {

        this.x = x;
        this.y = y;

        // bomb = new Bomb(x, y);

        var ii = new ImageIcon(IMG_ENEMY);

        // Scale the image to use the global scaling factor
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }


    public void act(int direction) {

        this.x += direction;
    }

    public String getType() {
        return type;
    }

    @Override
    public void act() {

    }


    public void tickCooldown() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    public boolean canDropBomb() {
        return cooldown == 0;
    }

    public void dropBomb() {
        bombs.add(new Bomb(x, y));
        cooldown = 50; // Adjust this for delay between bombs
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void updateBombs() {
        Iterator<Bomb> iter = bombs.iterator();
        while (iter.hasNext()) {
            Bomb b = iter.next();
            if (b.isDestroyed()) {
                iter.remove();
            }
        }
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {
            setDestroyed(false); // Changed from true to false
            this.x = x;
            this.y = y;
            var bombImg = "src/images/bomb.png";
            var ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }

        @Override
        public void act() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'act'");
        }
    }

}