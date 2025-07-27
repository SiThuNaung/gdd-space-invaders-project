package gdd;

import gdd.scene.Scene1;
//import gdd.scene.Scene2;
import gdd.scene.TitleScene;
import javax.swing.JFrame;

public class Game extends JFrame  {

    TitleScene titleScene;
    Scene1 scene1;
//    Scene2 scene2;

    public Game() {
        titleScene = new TitleScene(this);
        scene1 = new Scene1(this);
//        scene2 = new Scene2();
        initUI();
         loadTitle();
//        loadScene2();
    }

    private void initUI() {
        setTitle("Space Invaders");
        setSize(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void loadTitle() {
        getContentPane().removeAll();
        add(titleScene);
        titleScene.start();
        revalidate();
        repaint();
    }

    public void loadScene2() {
        getContentPane().removeAll();
        add(scene1); //add(scene1)
        titleScene.stop();
        scene1.start();
        scene1.requestFocusInWindow(); //scene1.start();
        revalidate();
        repaint();
    }

    public TitleScene getTitleScene() {
        return titleScene;
    }

    public Scene1 getScene1() {
        return scene1;
    }
}