package com.example.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;

	Texture background;

	Texture[] bird;
	int birdStateFlag;
	float flyHeight;
	float fallingSpeed = 0;
	int gameStateFlag = 0;

	Texture topTube;
	Texture bottomTube;
	int spaceBetweenTubes = 500;
	Random random;

	int tubeSpeed = 5;
	int tubesNumber = 5;
	float tubeX[] = new float[tubesNumber];
	float tubeShift[] = new float[tubesNumber];
	float distanceBetweenTubes;

	Circle birdCircle;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;
//	ShapeRenderer shapeRenderer;

	int gameScore = 0;
	int passedTubeIndex = 0;
	BitmapFont scoreFont;

	Texture gameOver;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");

		birdCircle = new Circle();
//		shapeRenderer = new ShapeRenderer();
		topTubeRectangle = new Rectangle[tubesNumber];
		bottomTubeRectangle = new Rectangle[tubesNumber];

		bird = new Texture[2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");
		flyHeight = Gdx.graphics.getHeight()/2 -  bird[0].getHeight()/2;

		topTube = new Texture("top_tube.png");
		bottomTube = new Texture("bottom_tube.png");
		random = new Random();

		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.CYAN);
		scoreFont.getData().setScale(10);

		gameOver = new Texture("game_over.png");

		distanceBetweenTubes = Gdx.graphics.getWidth()/2;
		for (int i = 0; i < tubesNumber; i++){
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + i * distanceBetweenTubes;
			tubeShift[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - spaceBetweenTubes - 200);

			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();
		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (Gdx.input.justTouched()){
			gameStateFlag = 1;
		}

		if (gameStateFlag == 1){

			Gdx.app.log("Game score", String.valueOf(gameScore));

			if (tubeX[passedTubeIndex] < Gdx.graphics.getWidth()){
				gameScore++;

				if (passedTubeIndex < tubesNumber - 1){
					passedTubeIndex++;
				} else {
					passedTubeIndex = 0;
				}
			}

			if (Gdx.input.justTouched()){
				fallingSpeed = -30;
			}
			if (flyHeight > 0){
				fallingSpeed++;
				flyHeight -= fallingSpeed;
			} else {
				gameStateFlag = 2;
			}

		} else if (gameStateFlag == 0) {
			if (Gdx.input.justTouched()){
				gameStateFlag = 1;
			}
		} else if(gameStateFlag == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2,
					Gdx.graphics.getHeight() - gameOver.getHeight()/2);
		}

		for (int i = 0; i < tubesNumber; i++){

			if(tubeX[i] < -topTube.getWidth()){
				tubeX[i] = tubesNumber * distanceBetweenTubes;
			} else {
				tubeX[i] -= tubeSpeed;
			}

			tubeX[i] -= tubeSpeed;

			batch.draw(topTube, tubeX[i],
					Gdx.graphics.getHeight()/2 + spaceBetweenTubes/2 + tubeShift[i]);
			batch.draw(bottomTube, tubeX[i],
					Gdx.graphics.getHeight()/2 - spaceBetweenTubes/2 - bottomTube.getHeight() + tubeShift[i]);

			topTubeRectangle[i] = new Rectangle(tubeX[i],
					Gdx.graphics.getHeight()/2 + spaceBetweenTubes/2 + tubeShift[i], topTube.getWidth(), topTube.getHeight());

			bottomTubeRectangle[i] = new Rectangle( tubeX[i],
					Gdx.graphics.getHeight()/2 - spaceBetweenTubes/2 - bottomTube.getHeight() + tubeShift[i],
					bottomTube.getWidth(), bottomTube.getHeight());

		}



		if (birdStateFlag == 0){
			birdStateFlag = 1;
		} else {
			birdStateFlag = 0;
		}

		batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth()/2 - bird[birdStateFlag].getWidth()/2,
				flyHeight);

		scoreFont.draw(batch, String.valueOf(gameScore), 50, 150);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2, flyHeight + bird[birdStateFlag].getHeight()/2, bird[birdStateFlag].getWidth()/2);
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.CYAN);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);


		for (int i = 0; i < tubesNumber; i++){
//			shapeRenderer.rect(tubeX[i],
//					Gdx.graphics.getHeight()/2 + spaceBetweenTubes/2 + tubeShift[i], topTube.getWidth(), topTube.getHeight());
//			shapeRenderer.rect( tubeX[i],
//					Gdx.graphics.getHeight()/2 - spaceBetweenTubes/2 - bottomTube.getHeight() + tubeShift[i],
//					bottomTube.getWidth(), bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRectangle[i]) ||
					Intersector.overlaps(birdCircle, bottomTubeRectangle[i])){

				Gdx.app.log("Intersect", "Bump!");

				gameStateFlag = 2;
			}

		}

//		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}
