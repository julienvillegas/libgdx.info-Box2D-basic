package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Bodies.MoveableImage;
import com.mygdx.game.Bodies.Rect;
import com.mygdx.game.MyGdxGame;

/**
 * Created by julienvillegas on 17/01/2017.
 */
public class Menu implements Screen {

    private Stage stage;
    private Game game;
    private MoveableImage image;

    public Menu(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(new MyInputProcessor());

        TextButton playButton = new TextButton("Start!", MyGdxGame.skin);
        playButton.setWidth(Gdx.graphics.getWidth()/2);
        playButton.setPosition(Gdx.graphics.getWidth()/2-playButton.getWidth()/2,Gdx.graphics.getHeight()/2-playButton.getHeight()/2);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);

        image = new MoveableImage(0, Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, 0);
        stage.addActor(image);


    }
    public class MyInputProcessor implements InputProcessor {
        @Override
        public boolean keyDown (int keycode) {
            System.out.println("abc");
            return true;
        }

        @Override
        public boolean keyUp (int keycode) {
            System.out.println("abc");
            return true;
        }

        @Override
        public boolean keyTyped (char character) {
            System.out.println("abc");
            return true;
        }

        @Override
        public boolean touchDown (int x, int y, int pointer, int button) {
            System.out.println("abc");
            return true;
        }

        @Override
        public boolean touchUp (int x, int y, int pointer, int button) {
            System.out.println("abc");
            return true;
        }

        @Override
        public boolean touchDragged (int x, int y, int pointer) {
            image.setRect(x,y,image.getRect().getWidth(), image.getRect().getHeight(),0);
            System.out.println("abc");
            return true;
        }

        @Override
        public boolean mouseMoved (int x, int y) {
            System.out.println("abc");
            return true;
        }

        @Override
        public boolean scrolled (int amount) {
            System.out.println("abc");
            return true;
        }
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
