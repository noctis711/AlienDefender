package com.nguyen.aliendefender;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class AnimatedSprite {
    static final int FRAMES_COL = 2;
    static final int FRAMES_ROW = 2;
    static final int SHIP_SPEED = 300;

    Sprite sprite;
    Animation animation;
    TextureRegion[] frames;
    TextureRegion currentFrame;
    Vector2 velocity = new Vector2();
    float stateTime;
    boolean isDead = false;

    public AnimatedSprite(Sprite sprite){
        this.sprite = sprite;
        Texture texture = sprite.getTexture();
        TextureRegion[][] temp = TextureRegion.split(texture, (int)getSpriteWidth(), texture.getHeight()/FRAMES_ROW);
        frames = new TextureRegion[FRAMES_COL * FRAMES_ROW];
        int index = 0;
        for(int i = 0; i < FRAMES_ROW; i++){
            for(int j = 0; j < FRAMES_COL;j++){
                frames[index++] = temp[i][j];
            }
        }

        animation = new Animation(0.1f, frames);
        stateTime = 0f;
    }

    public void draw(SpriteBatch spriteBatch){
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame,sprite.getX(),sprite.getY());
    }

    public void setPosition(float x, float y){  //x is center ship, y is bottom
        float widthOffset = sprite.getWidth() / FRAMES_COL;
        sprite.setPosition(x - getSpriteCenter(), y);
    }

    public void moveRight(){
        velocity = new Vector2(SHIP_SPEED, 0);
    }
    public void moveLeft(){
        velocity = new Vector2(-SHIP_SPEED, 0);
    }

    public float getSpriteCenter(){
        return getSpriteWidth()/2;
    }

    public float getSpriteWidth(){
        return sprite.getWidth()/FRAMES_COL;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public int getX(){
        return (int) (sprite.getX() + getSpriteCenter());
    }

    public void move(){
        int xMovement = (int)(velocity.x * Gdx.graphics.getDeltaTime());
        int yMovement = (int)(velocity.y * Gdx.graphics.getDeltaTime());
        sprite.setPosition(sprite.getX() + xMovement, sprite.getY() + yMovement);

        if(sprite.getX() < 0){
            sprite.setX(0);
        }
        if(sprite.getX() + this.getSpriteWidth() > AlienDefender.SCREEN_WIDTH){
            sprite.setX(AlienDefender.SCREEN_WIDTH -this.getSpriteWidth());
        }
    }

    public int getY() {
        return (int)sprite.getY();
    }

    public int getWidth() {
        return (int)getSpriteWidth();
    }

    public int getHeight() {
        return (int)sprite.getHeight() / FRAMES_ROW;
    }

    public void changeDirection() {
        velocity.x = -velocity.x;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(sprite.getX(), sprite.getY(), getWidth(), getHeight());
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }
    public boolean isDead(){
        return isDead;
    }
}
