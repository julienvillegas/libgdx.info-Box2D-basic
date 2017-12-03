package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


import java.util.ArrayList;


public class MyBox2DTest implements ApplicationListener, InputProcessor {
    // the camera
    private OrthographicCamera camera;

    // box2d debug renderer
    private Box2DDebugRenderer debugRenderer;

    //pictures
    private TextureRegion woodblock_picture;
    private TextureRegion steelblock_picture;
    private TextureRegion halfwoodblock_picture;
    private TextureRegion halfsteelblock_picture;
    private TextureRegion engine_picture;
    private TextureRegion gun_1_picture;
    private TextureRegion gun_2_picture;
    private TextureRegion turbine_picture;
    private TextureRegion meteor_picture;
    private TextureRegion meteor_2_picture;
    private TextureRegion background_picture;

    private SpriteBatch batch;

    //box2D world
    private World world;

    //bodies
    private ArrayList<Body> woodblocks = new ArrayList<Body>();
    private ArrayList<Body> steelblocks = new ArrayList<Body>();
    private Body meteor;
    private Body halfwoodblockbody;
    private Body halfsteelblockbody;




    @Override
    public void create() {
        // setup the camera. In Box2D we operate on a
        // meter scale, pixels won't do it. So we use
        // an orthographic camera with a viewport of
        // 48 meters in width and 32 meters in height.
        // We also position the camera so that it
        // looks at (0,16) (that's where the middle of the
        // screen will be located).
        camera = new OrthographicCamera(48, 32);
        camera.position.set(0, 16, 0);

        // next we create the box2d debug renderer
        debugRenderer = new Box2DDebugRenderer();

        // next we create a SpriteBatch a
        batch = new SpriteBatch();

        woodblock_picture = new TextureRegion(new Texture(Gdx.files.internal("woodblock.png")));
        steelblock_picture = new TextureRegion(new Texture(Gdx.files.internal("steelblock.png")));
        halfwoodblock_picture = new TextureRegion(new Texture(Gdx.files.internal("halfwoodblock.png")));
        halfsteelblock_picture = new TextureRegion(new Texture(Gdx.files.internal("halfsteelblock.png")));
        engine_picture = new TextureRegion(new Texture(Gdx.files.internal("engine.png")));
        gun_1_picture = new TextureRegion(new Texture(Gdx.files.internal("gun_1.png")));
        gun_2_picture = new TextureRegion(new Texture(Gdx.files.internal("gun_2.png")));
        turbine_picture = new TextureRegion(new Texture(Gdx.files.internal("turbine.png")));
        meteor_picture = new TextureRegion(new Texture(Gdx.files.internal("meteor.png")));
        meteor_2_picture = new TextureRegion(new Texture(Gdx.files.internal("meteor_2.png")));
        background_picture = new TextureRegion(new Texture(Gdx.files.internal("background_red_space.png")));

        // next we create out physics world.
        createPhysicsWorld();

        // register ourselfs as an InputProcessor
        Gdx.input.setInputProcessor(this);
    }

    private void createPhysicsWorld () {
        // we instantiate a new World with a proper gravity vector
        // and tell it to sleep when possible.
        world = new World(new Vector2(0, -10), true);
        {
            // next we create a static ground platform. This platform
            // is not moveable and will not react to any influences from
            // outside. It will however influence other bodies. First we
            // create a PolygonShape that holds the form of the platform.
            // it will be 100 meters wide and 2 meters high, centered
            // around the origin
            PolygonShape groundPoly = new PolygonShape();
            groundPoly.setAsBox(50, 1);

            // next we create the body for the ground platform. It's
            // simply a static body.
            BodyDef groundBodyDef = new BodyDef();
            groundBodyDef.type = BodyDef.BodyType.StaticBody;
        /* ground body to connect the mouse joint to */
            Body groundBody = world.createBody(groundBodyDef);

            // finally we add a fixture to the body using the polygon
            // defined above. Note that we have to dispose PolygonShapes
            // and CircleShapes once they are no longer used. This is the
            // only time you have to care explicitly for memory management.
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = groundPoly;
            fixtureDef.filter.groupIndex = 0;
            groundBody.createFixture(fixtureDef);
            groundPoly.dispose();
        }
        {
            CircleShape shape = new CircleShape();
            shape.setRadius(2f);

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1.0f;


            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.DynamicBody;
            bd.position.set(0, 30);

            meteor = world.createBody(bd);
            meteor.createFixture(fd);

            shape.dispose();
        }
        {
            Vector2[] vertices = new Vector2[3];
            vertices[0] = new Vector2(0, 0);
            vertices[1] = new Vector2(0, 4);
            vertices[2] = new Vector2(4, 0);


            PolygonShape shape = new PolygonShape();
            shape.set(vertices);

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1.0f;

            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.DynamicBody;
            bd.position.set(-10, 60);
            halfwoodblockbody = world.createBody(bd);
            halfwoodblockbody.createFixture(fd);

            shape.dispose();
        }
        {
            Vector2[] vertices = new Vector2[3];
            vertices[0] = new Vector2(0, 0);
            vertices[1] = new Vector2(0, -4);
            vertices[2] = new Vector2(4, 0);


            PolygonShape shape = new PolygonShape();
            shape.set(vertices);

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1.0f;

            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.DynamicBody;
            bd.position.set(-20, 60);
            halfsteelblockbody = world.createBody(bd);
            halfsteelblockbody.createFixture(fd);

            shape.dispose();
        }

        createBoxes();
    }

    private void createBoxes () {
        // next we create 50 boxes at random locations above the ground
        // body. First we create a nice polygon representing a box 2 meters
        // wide and high.
        PolygonShape boxPoly = new PolygonShape();
        boxPoly.setAsBox(2, 2);

        // next we create the 50 box bodies using the PolygonShape we just
        // defined. This process is similar to the one we used for the ground
        // body. Note that we reuse the polygon for each body fixture.
        for (int i = 0; i < 20; i++) {
            // Create the BodyDef, set a random position above the
            // ground and create a new body
            BodyDef boxBodyDef = new BodyDef();
            boxBodyDef.type = BodyDef.BodyType.DynamicBody;
            boxBodyDef.position.x = -24 + (float)(Math.random() * 48);
            boxBodyDef.position.y = 10 + (float)(Math.random() * 100);
            Body boxBody = world.createBody(boxBodyDef);
            boxBody.createFixture(boxPoly, 1);

            // add the box to our list of boxes
            woodblocks.add(boxBody);
        }
        // we are done, all that's left is disposing the boxPoly
        boxPoly.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        // first we update the world. For simplicity
        // we use the delta time provided by the Graphics
        // instance. Normally you'll want to fix the time
        // step.

        world.step(Gdx.graphics.getDeltaTime(), 8, 3);


        // next we clear the color buffer and set the camera
        // matrices
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // next we render each box via the SpriteBatch.
        // for this we have to set the projection matrix of the
        // spritebatch to the camera's combined matrix. This will
        // make the spritebatch work in world coordinates
        batch.getProjectionMatrix().set(camera.combined);

        batch.begin();
        batch.draw(background_picture,-24,0,48,32);
        for (int i = 0; i <woodblocks.size()/2; i++) {
            Body box = woodblocks.get(i);
            Vector2 position = box.getPosition(); // that's the box's center position
            float angle = MathUtils.radiansToDegrees * box.getAngle(); // the rotation angle around the center
            batch.draw(woodblock_picture, position.x -2, position.y - 2, // the bottom left corner of the box, unrotated
                    2f, 2f, // the rotation center relative to the bottom left corner of the box
                    4, 4, // the width and height of the box
                    1.1f, 1.1f, // the scale on the x- and y-axis
                    angle); // the rotation angle

        }
        for (int i = woodblocks.size()/2; i <woodblocks.size(); i++) {
            Body box = woodblocks.get(i);
            Vector2 position = box.getPosition(); // that's the box's center position
            float angle = MathUtils.radiansToDegrees * box.getAngle(); // the rotation angle around the center
            batch.draw(steelblock_picture, position.x -2, position.y - 2, // the bottom left corner of the box, unrotated
                    2f, 2f, // the rotation center relative to the bottom left corner of the box
                    4, 4, // the width and height of the box
                    1.1f, 1.1f, // the scale on the x- and y-axis
                    angle); // the rotation angle

        }
        {
            Vector2 position = meteor.getPosition();
            float angle = MathUtils.radiansToDegrees * meteor.getAngle();
            batch.draw(meteor_2_picture, position.x - 2, position.y - 2, // the bottom left corner of the box, unrotated
                    2f, 2f, // the rotation center relative to the bottom left corner of the box
                    4, 4, // the width and height of the box
                    1.0f, 1.0f, // the scale on the x- and y-axis
                    angle);
        }
        {
            Vector2 position = halfwoodblockbody.getPosition();
            float angle = MathUtils.radiansToDegrees * halfwoodblockbody.getAngle();
            batch.draw(halfwoodblock_picture, position.x, position.y , // the bottom left corner of the box, unrotated
                    0, 0, // the rotation center relative to the bottom left corner of the box
                    4, 4, // the width and height of the box
                    1.0f, 1.0f, // the scale on the x- and y-axis
                    angle);
        }
       // {
       //     Vector2 position = halfsteelblockbody.getPosition();
       //     float angle = MathUtils.radiansToDegrees * halfsteelblockbody.getAngle();
       //     batch.draw(halfsteelblock_picture, position.x, position.y , // the bottom left corner of the box, unrotated
       //             0, 0, // the rotation center relative to the bottom left corner of the box
       //             4, 4, // the width and height of the box
       //             1.0f, 1.0f, // the scale on the x- and y-axis
       //             angle);
       // }
        batch.end();

        debugRenderer.render(world, camera.combined);

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        world.dispose();

        world = null;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
