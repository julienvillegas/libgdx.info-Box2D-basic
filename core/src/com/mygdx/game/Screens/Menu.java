package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Bodies.MoveableImage;

public class Menu implements Screen, InputProcessor {

    private Stage stage;
    private Game game;
    private MoveableImage image;
    private float oldX=0,oldY=0;
    public Menu(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);
        image = new MoveableImage(Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, 0);
        stage.addActor(image);


    }

        @Override
        public boolean keyDown (int keycode) {

            return true;
        }

        @Override
        public boolean keyUp (int keycode) {

            return true;
        }

        @Override
        public boolean keyTyped (char character) {

            return true;
        }

        @Override
        public boolean touchDown (int x, int y, int pointer, int button) {
            if (image.contains(x,y)){
                image.isMoving = true;

            }
            oldX=x;
            oldY=y;

            return true;
        }

        @Override
        public boolean touchUp (int x, int y, int pointer, int button) {
            image.isMoving = false;

            return true;
        }

        @Override
        public boolean touchDragged (int x, int y, int pointer) {
        if (image.isMoving == true) {
            image.setX(image.getX() + x - oldX);
            image.setY(image.getY() - y + oldY);
            oldX = x;
            oldY = y;
        }
            return true;
        }

        @Override
        public boolean mouseMoved (int x, int y) {

            return true;
        }

        @Override
        public boolean scrolled (int amount) {
            return true;
        }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
