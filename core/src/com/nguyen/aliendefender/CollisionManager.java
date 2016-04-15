package com.nguyen.aliendefender;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollisionManager {
    final AnimatedSprite playerAnimated;
    final ShotManager shotManager;
    Sound explosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
    List<Enemy> enemyList = new ArrayList<Enemy>();

    public CollisionManager(AnimatedSprite playerAnimated, List<Enemy> enemyList, ShotManager shotManager){
        this.playerAnimated = playerAnimated;
        this.enemyList = enemyList;
        this.shotManager = shotManager;
    }

    public void handleCollisions() {
        handleEnemyShot();
        handlePlayerShot();
    }

    private void handlePlayerShot() {
        if(shotManager.enemyShotTouches(playerAnimated.getBoundingBox())){
            explosion.play();
            AlienDefender.lives--;
            AlienDefender.playerLives = "LIVES : "+Integer.toString(AlienDefender.lives);
            if(AlienDefender.lives < 1){
                playerAnimated.setDead(true);
            }
        }
    }

    private void handleEnemyShot() {
        Iterator<Enemy> i = enemyList.iterator();
        while(i.hasNext()){
            Enemy temp = i.next();
            if(shotManager.playerShotTouches(temp.getBoundingBox())){
                temp.hit();
                AlienDefender.score+=100;
                AlienDefender.playerScore = "SCORE: " + AlienDefender.score;
            }
        }

    }
}
