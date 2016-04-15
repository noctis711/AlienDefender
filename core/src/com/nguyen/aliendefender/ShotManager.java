package com.nguyen.aliendefender;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShotManager {
    static final int SHOT_Y_OFFSET = 40;//33
    static final int SHOT_SPEED = 300;
    static final float MINIMUM_TIME_BETWEEN_SHOTS = .4f;
    static final float ENEMY_SHOT_Y_OFFSET =650;
    float timeSinceLastShot = 0;
    Sound laser = Gdx.audio.newSound(Gdx.files.internal("laser.wav"));
    final Texture shotTexture;
    final Texture enemyShotTexture;
    List<AnimatedSprite> shots = new ArrayList<AnimatedSprite>();
    List<AnimatedSprite> enemyShots = new ArrayList<AnimatedSprite>();

    public ShotManager(Texture shotTexture, Texture enemyShotTexture) {
        this.shotTexture = shotTexture;
        this.enemyShotTexture = enemyShotTexture;
    }

    public void firePlayerShot(int playerXLocation) {
        if(canFireShot()){
            Sprite newShot = new Sprite(shotTexture);
            AnimatedSprite newShotAnimated = new AnimatedSprite(newShot);
            newShotAnimated.setPosition(playerXLocation, SHOT_Y_OFFSET);
            newShotAnimated.setVelocity(new Vector2(0, SHOT_SPEED));
            shots.add(newShotAnimated);
            timeSinceLastShot = 0f;
            laser.play();
        }
    }

    public void reset(){
        shots = new ArrayList<AnimatedSprite>();
        enemyShots = new ArrayList<AnimatedSprite>();
    }

    private boolean canFireShot() {
        return timeSinceLastShot > MINIMUM_TIME_BETWEEN_SHOTS;
    }

    public void update() {
        Iterator<AnimatedSprite> i = shots.iterator();
        while(i.hasNext()){
            AnimatedSprite shot = i.next();
            shot.move();
            if(shot.getY() > AlienDefender.SCREEN_HEIGHT){
                i.remove();
            }
        }
        for(AnimatedSprite shot : shots){
            shot.move();
        }

        Iterator<AnimatedSprite> j = enemyShots.iterator();
        while(j.hasNext()){
            AnimatedSprite shot = j.next();
            shot.move();
            if(shot.getY() < 0){
                j.remove();
            }
        }
        for(AnimatedSprite shot : shots){
            shot.move();
        }

        timeSinceLastShot += Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        for(AnimatedSprite shot: shots){
            shot.draw(batch);
        }

        for(AnimatedSprite shot: enemyShots){
            shot.draw(batch);
        }
    }

    public void fireEnemyShot(int enemyXLocation){
        Sprite newShot = new Sprite(enemyShotTexture);
        AnimatedSprite newShotAnimated = new AnimatedSprite(newShot);
        newShotAnimated.setPosition(enemyXLocation, ENEMY_SHOT_Y_OFFSET);
        newShotAnimated.setVelocity(new Vector2(0, -SHOT_SPEED));
        enemyShots.add(newShotAnimated);
    }

    public boolean playerShotTouches(Rectangle boundingBox) {
        return shotTouches(shots, boundingBox);
    }

    public boolean enemyShotTouches(Rectangle boundingBox) {
        return shotTouches(enemyShots, boundingBox);//change enemyShots to shots
    }

    public boolean shotTouches(List<AnimatedSprite> shots, Rectangle boundingBox){
        Rectangle intersection = new Rectangle();
        Iterator<AnimatedSprite> i = shots.iterator();
        while(i.hasNext()){
            AnimatedSprite shot = i.next();
            if(Intersector.intersectRectangles(shot.getBoundingBox(), boundingBox, intersection)){
                i.remove();
                return true;
            }
        }
        return false;
    }

}
