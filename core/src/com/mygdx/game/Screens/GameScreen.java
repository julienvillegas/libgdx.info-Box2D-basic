package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Bodies.Platform;
import com.mygdx.game.Bodies.Block;

/**
 * Created by julienvillegas on 17/01/2017.
 */
public class GameScreen implements Screen {

    private Stage stage;
    private Game game;
    private World world = new World(new Vector2(0, 0), true);
    private Box2DDebugRenderer debugRenderer;
    private Block block1;
    private Block block2;

    static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private OrthographicCamera cam;
    private SpriteBatch batch;

    private Sprite mapSprite;


    public GameScreen(Game aGame) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        mapSprite = new Sprite(new Texture(Gdx.files.internal("woodenblock.png")));
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT*(h/w));

        cam = new OrthographicCamera(100, 100*(h/w));

        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        batch = new SpriteBatch();



        game = aGame;
        ScreenViewport screenViewport = new ScreenViewport();
        screenViewport.setWorldSize(100,100);
        stage = new Stage(screenViewport);
        Gdx.input.setInputProcessor(stage);
        debugRenderer = new Box2DDebugRenderer();


        block1 = new Block(world,10,10,10,10,30);
        stage.addActor(block1);
        //block2 = new Block(world,Gdx.graphics.getWidth()/4+block1.getWidth(),Gdx.graphics.getHeight(),Gdx.graphics.getHeight()/20,Gdx.graphics.getHeight()/20, 30);
        //stage.addActor(block2);

        stage.addActor(new Platform(world,0,Gdx.graphics.getHeight()/3,Gdx.graphics.getWidth()*2/3,Gdx.graphics.getHeight()/10,-30));



       // block1.AddDWeldJoint(block2);
    }


    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");

    }

    @Override
    public void render(float delta) {
        //jave 8
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        mapSprite.draw(batch);
        batch.end();
        stage.act();
        stage.draw();
        //debugRenderer.render(world, stage.getCamera().combined);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }




    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = 100f;
        cam.viewportHeight = 100f * height/width;
        cam.update();
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
        mapSprite.getTexture().dispose();
        batch.dispose();
    }


}