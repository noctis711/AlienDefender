package com.nguyen.aliendefender;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Enemy {
    static final float ENEMY_SPEED = 250;
    final Texture enemyTexture;
    AnimatedSprite animatedSprite;
    final ShotManager shotManager;
    float spawnTimeout = 0f;

    public Enemy(Texture enemyTexture, ShotManager shotManager){
        this.enemyTexture = enemyTexture;
        this.shotManager = shotManager;
        spawn();
    }

    private void spawn() {
        Sprite enemySprite = new Sprite(enemyTexture);
        animatedSprite = new AnimatedSprite(enemySprite);
        int xPosition = createRandomPosition();
        animatedSprite.setPosition(xPosition, AlienDefender.SCREEN_HEIGHT - animatedSprite.getHeight());
        animatedSprite.setVelocity(new Vector2(ENEMY_SPEED, 0));
        animatedSprite.setDead(false);
    }

    private int createRandomPosition() {
        Random random = new Random();
        int randomNumber = random.nextInt(AlienDefender.SCREEN_WIDTH - animatedSprite.getWidth());
        return randomNumber + animatedSprite.getWidth() / 2;
    }

    public void draw(SpriteBatch batch) {
        if(!animatedSprite.isDead())
        animatedSprite.draw(batch);
    }

    public void update() {
        if(animatedSprite.isDead()){
            spawnTimeout -= Gdx.graphics.getDeltaTime();
            if(spawnTimeout <= 0){
                spawn();
            }
        }
        else{
            if(shouldChangeDirection()){
                animatedSprite.changeDirection();
            }
            if(shouldFire()){
                shotManager.fireEnemyShot(animatedSprite.getX());//animatedSprite.getX();
            }
            animatedSprite.move();
        }

    }

    private boolean shouldFire() {
        Random random = new Random();
        return random.nextInt(51) == 0;
    }

    private boolean shouldChangeDirection() {
        Random random = new Random();
        return random.nextInt(21) == 0;
    }

    public Rectangle getBoundingBox(){
        return animatedSprite.getBoundingBox();
    }

    public void hit() {
        animatedSprite.setDead(true);
        spawnTimeout = 5f;
    }
}
