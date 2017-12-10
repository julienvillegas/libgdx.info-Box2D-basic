package com.mygdx.game.Tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LibGDXTest extends ApplicationAdapter {
    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;
    static final float SCALE = 0.02f;
    static final int COUNT = 20;

    private int maxwidthofship =6;
    private int maxheightofship =5;


    TextureAtlas textureAtlas;
    SpriteBatch batch;
    final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    OrthographicCamera camera;
    ExtendViewport viewport;

    World world;
    Box2DDebugRenderer debugRenderer;
    PhysicsShapeCache physicsBodies;
    float accumulator = 0;
    Body upground;
    Body downground;
    Body leftground;
    Body rightground;

    ArrayList<Body> Bodies = new ArrayList<Body>();
    ArrayList<String> names = new ArrayList<String>();
    int [][] player1ship = new int[maxwidthofship][maxheightofship];
    int [][] player2ship = new int[maxwidthofship][maxheightofship];


    @Override
    public void create() {
        player1ship[0] = new int[]{0, 0, 0, 0, 0};
        player1ship[1] = new int[]{0, 6, 1, 6, 0};
        player1ship[2] = new int[]{0, 7, 1, 7, 0};
        player1ship[3] = new int[]{0, 5, 4, 5, 0};
        player1ship[4] = new int[]{0, 0, 0, 0, 0};
        player1ship[5] = new int[]{0, 0, 0, 0, 0};
        //player1ship[0] = new int[]{1, 1, 1, 1, 1};
        //player1ship[1] = new int[]{1, 1, 1, 1, 1};
        //player1ship[2] = new int[]{1, 1, 1, 1, 1};
        //player1ship[3] = new int[]{1, 1, 1, 1, 1};
        //player1ship[4] = new int[]{1, 1, 1, 1, 1};
        //player1ship[5] = new int[]{1, 1, 1, 1, 1};

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(50, 50, camera);

        textureAtlas = new TextureAtlas("sprites.txt");
        addSprites();

        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        physicsBodies = new PhysicsShapeCache("physics.xml");
        generate();

        debugRenderer = new Box2DDebugRenderer();
    }

    private void addSprites() {
        Array<AtlasRegion> regions = textureAtlas.getRegions();

        for (AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);

            float width = sprite.getWidth() * SCALE;
            float height = sprite.getHeight() * SCALE;

            sprite.setSize(width, height);
            sprite.setOrigin(0, 0);

            sprites.put(region.name, sprite);
        }
    }

    private void generate() {
        String[] blockNames = new String[]{"steelblock","halfwoodblock","halfsteelblock","gunone","guntwo", "turbine","engine"};

        for (int j = 0; j < maxheightofship; j++) {
            for (int i = 0; i < maxwidthofship; i++) {
                if (player1ship[i][j] == 0) {
                    String name = blockNames[0];

                    float x = (float) (-100);
                    float y = (float) (0);

                    names.add(i+j,name);
                    Bodies.add(i+j,createBody(name, x, y, 0));
                }

                if (player1ship[i][j] == 1) {
                    String name = blockNames[0];

                    float x = (float) (10+i*7);
                    float y = (float) (40 -j*7);

                    names.add(i+j,name);
                    Bodies.add(i+j,createBody(name, x, y, 0));
                }
                if (player1ship[i][j] == 2) {
                    String name = blockNames[1];

                    float x = (float) (10+i*7);
                    float y = (float) (40 -j*7);

                    names.add(i+j,name);
                    Bodies.add(i+j,createBody(name, x, y, 0));
                }
                if (player1ship[i][j] == 3) {
                    String name = blockNames[2];

                    float x = (float) (10+i*7);
                    float y = (float) (40 -j*7);

                    names.add(i+j,name);
                    Bodies.add(i+j,createBody(name, x, y, 0));
                }
                if (player1ship[i][j] == 4) {
                    String name = blockNames[3];

                    float x = (float) (10+i*7);
                    float y = (float) (40 -j*7+1.2);

                    names.add(i+j,name);
                    Bodies.add(i+j,createBody(name, x, y, 0));
                }
                if (player1ship[i][j] == 5) {
                    String name = blockNames[4];

                    float x = (float) (10+i*7);
                    float y = (float) (40 -j*7);

                    names.add(i+j,name);
                    Bodies.add(i+j,createBody(name, x, y, 0));
                }
                if (player1ship[i][j] == 6) {
                    String name = blockNames[5];

                    float x = (float) (10+i*7-4.5);
                    float y = (float) (40 -j*7);

                    names.add(i+j,name);
                    Bodies.add(i+j,createBody(name, x, y, 0));
                }
                if (player1ship[i][j] == 7) {
                    String name = blockNames[6];

                    float x = (float) (10+i*7);
                    float y = (float) (40 -j*7);

                    names.add(i+j,name);
                    Bodies.add(i+j,createBody(name, x, y, 0));
                }
            }
        }
    }

    private Body createBody(String name, float x, float y, float rotation) {
        Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
        body.setTransform(x, y, rotation);

        return body;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        batch.setProjectionMatrix(camera.combined);

        createGround();
    }

    private void createGround() {
        if (upground != null) world.destroyBody(upground);
        if (downground != null) world.destroyBody(upground);
        if (leftground != null) world.destroyBody(upground);
        if (rightground != null) world.destroyBody(upground);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(camera.viewportWidth, 1);
        fixtureDef.shape = shape;

        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(1, camera.viewportHeight);
        fixtureDef1.shape = shape1;

        //upground = world.createBody(bodyDef);
        //upground.createFixture(fixtureDef);
        //upground.setTransform(0, camera.viewportHeight, 0);

        downground = world.createBody(bodyDef);
        downground.createFixture(fixtureDef);
        downground.setTransform(0, 0, 0);

        leftground = world.createBody(bodyDef);
        leftground.createFixture(fixtureDef1);
        leftground.setTransform(0, 0, 0);

        rightground = world.createBody(bodyDef);
        rightground.createFixture(fixtureDef1);
        rightground.setTransform(camera.viewportWidth, 0, 0);

        shape.dispose();
        shape1.dispose();
    }

    @Override
    public void render() {
        //Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stepWorld();

        batch.begin();

        for (int i = 0; i < Bodies.size(); i++) {
            Body body = Bodies.get(i);
            String name = names.get(i);

            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSprite(name, position.x, position.y, degrees);
        }

        batch.end();

        // uncomment to show the polygons
         debugRenderer.render(world, camera.combined);
    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    private void drawSprite(String name, float x, float y, float degrees) {
        Sprite sprite = sprites.get(name);
        sprite.setPosition(x, y);
        sprite.setRotation(degrees);
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        sprites.clear();
        world.dispose();
        debugRenderer.dispose();
    }
}
