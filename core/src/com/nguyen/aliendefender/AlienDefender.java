package com.nguyen.aliendefender;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;

public class AlienDefender extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Sprite playerSprite;
	AnimatedSprite playerAnimated;
	OrthographicCamera camera;
	FitViewport viewport;
	Vector2 velocity = new Vector2();
	ShotManager shotManager;
	List<Enemy> enemyList = new ArrayList<Enemy>();
	Enemy boss;
	CollisionManager collisionManager;
	static boolean isGameOver = false;
	static final int SCREEN_HEIGHT = 720;
	static final int SCREEN_WIDTH = 1280;
	Music gameTheme;
	static int score;
	static int lives;
	static String playerScore;
	static String playerLives;
	BitmapFont bitmapFont;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		viewport = new FitViewport(1280, 720, camera);
		viewport.apply();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.update();
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("background.jpg"));
		Texture player = new Texture(Gdx.files.internal("xwingAnimated.png"));
		playerSprite = new Sprite(player);
		playerAnimated = new AnimatedSprite(playerSprite);
		playerAnimated.setPosition(300, 0);

		Texture shotTexture = new Texture(Gdx.files.internal("laserRedAnimated.png"));
		Texture enemyShotTexture = new Texture(Gdx.files.internal("laserGreenAnimated.png"));
		shotManager = new ShotManager(shotTexture, enemyShotTexture);

		gameTheme = Gdx.audio.newMusic(Gdx.files.internal("theme.mp3"));
		gameTheme.setVolume(.25f);
		gameTheme.setLooping(true);
		gameTheme.play();

		addEnemies();
		collisionManager = new CollisionManager(playerAnimated,enemyList, shotManager);
		score = 0;
		lives = 3;
		playerScore = "SCORE: 0";
		playerLives = "LIVES : "+Integer.toString(lives);
		bitmapFont = new BitmapFont();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render() {
		if (Gdx.input.isKeyPressed(Input.Keys.BACK) || (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))){
			Gdx.app.exit();
		}

		setStage();
		userInput();

		if(!isGameOver) {
			playerAnimated.move();
			updateEnemies();
			shotManager.update();

			collisionManager.handleCollisions();
		}

		if(playerAnimated.isDead()){
			isGameOver = true;
		}
	}
	public void userInput(){
		if (Gdx.input.isTouched()){

			if(isGameOver){
				resetGame();
			}

			int xPos = Gdx.input.getX();
			if(xPos > playerAnimated.getX()){
				playerAnimated.moveRight();
			}
			else{
				playerAnimated.moveLeft();
			}
			shotManager.firePlayerShot(playerAnimated.getX());
		}
	}
	public void addEnemies(){
		Texture enemyTexture = new Texture((Gdx.files.internal("greenAIAnimated.png")));
		for(int i = 0; i < 3; i++){
			enemyList.add(new Enemy(enemyTexture, shotManager));
		}
	}
	public void setStage(){
		camera.update();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//batch.draw(background, 0, 0, 1280, 720);
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(isGameOver){
			BitmapFont font = new BitmapFont();
			font.getData().setScale(5);
			font.draw(batch, "GAME OVER!", 400, 400);
			font.draw(batch, "Click to restart. Back, ESC to exit.", 100, 320);
		}

		playerAnimated.draw(batch);
		drawEnemies();
		//boss.draw(batch);  boss battle!
		shotManager.draw(batch);
		BitmapFont font2 = new BitmapFont();
		font2.getData().setScale(2);
		font2.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font2.draw(batch, playerScore, 25, 100);
		font2.draw(batch, playerLives, 25, 50);
		batch.end();
	}
	public void resetGame(){//reset score, lives, shots, enemies
		lives = 3;
		score = 0;
		playerScore = "SCORE: 0";
		playerLives = "LIVES : "+Integer.toString(lives);
		Texture enemyTexture = new Texture((Gdx.files.internal("greenAIAnimated.png")));
		for(int i = 0; i < enemyList.size();i++ ) {
			enemyList.set(i, new Enemy(enemyTexture, shotManager));
		}
		shotManager.reset();
		playerAnimated.setDead(false);
		isGameOver = false;
	}
	public void drawEnemies(){
		Iterator<Enemy> i = enemyList.iterator();
		while(i.hasNext()){
			i.next().draw(batch);
		}
	}
	public void updateEnemies(){
		Iterator<Enemy> i = enemyList.iterator();
		while(i.hasNext()){
			i.next().update();
		}
	}


}
