package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;

import java.util.ArrayList;

/**
 * Created by artum on 01.12.2017.
 */

public class MyBox2DTest implements ApplicationListener, InputProcessor {
    /** the camera **/
    protected OrthographicCamera camera;

    /** the immediate mode renderer to output our debug drawings **/
    private ShapeRenderer renderer;

    /** box2d debug renderer **/
    private Box2DDebugRenderer debugRenderer;

    private TextureRegion textureRegion;

    SpriteBatch batch;

    /** our box2D world **/
    protected World world;

    /** ground body to connect the mouse joint to **/
    protected Body groundBody;

    /** our boxes **/
    private ArrayList<Body> boxes = new ArrayList<Body>();

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

        // next we setup the immediate mode renderer
        renderer = new ShapeRenderer();

        // next we create the box2d debug renderer
        debugRenderer = new Box2DDebugRenderer();

        // next we create a SpriteBatch a
        batch = new SpriteBatch();

        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("woodenblock.png")));

        // next we create out physics world.
        createPhysicsWorld();

        // register ourselfs as an InputProcessor
        Gdx.input.setInputProcessor(this);
    }

    private void createPhysicsWorld () {
        // we instantiate a new World with a proper gravity vector
        // and tell it to sleep when possible.
        world = new World(new Vector2(0, -10), true);

        float[] vertices = {-0.07421887f, -0.16276085f, -0.12109375f, -0.22786504f, -0.157552f, -0.7122401f, 0.04296875f,
                -0.7122401f, 0.110677004f, -0.6419276f, 0.13151026f, -0.49869835f, 0.08984375f, -0.3190109f};

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

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
        groundBody = world.createBody(groundBodyDef);


        // finally we add a fixture to the body using the polygon
        // defined above. Note that we have to dispose PolygonShapes
        // and CircleShapes once they are no longer used. This is the
        // only time you have to care explicitly for memory management.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundPoly;
        fixtureDef.filter.groupIndex = 0;
        groundBody.createFixture(fixtureDef);
        groundPoly.dispose();

        // We also create a simple ChainShape we put above our
        // ground polygon for extra funkyness.
        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(new Vector2[] {new Vector2(-10, 10), new Vector2(-10, 5), new Vector2(10, 5), new Vector2(10, 11),});
        BodyDef chainBodyDef = new BodyDef();
        chainBodyDef.type = BodyDef.BodyType.StaticBody;
        Body chainBody = world.createBody(chainBodyDef);
        chainBody.createFixture(chainShape, 0);
        chainShape.dispose();

        createBoxes();
    }

    private void createBoxes () {

        // next we create 50 boxes at random locations above the ground

        // body. First we create a nice polygon representing a box 2 meters

        // wide and high.

        PolygonShape boxPoly = new PolygonShape();

        boxPoly.setAsBox(1, 1);



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
            boxes.add(boxBody);
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
        for (int i = 0; i < boxes.size(); i++) {
            Body box = boxes.get(i);
            Vector2 position = box.getPosition(); // that's the box's center position
            float angle = MathUtils.radiansToDegrees * box.getAngle(); // the rotation angle around the center
            batch.draw(textureRegion, position.x - 1, position.y - 1, // the bottom left corner of the box, unrotated
                    1f, 1f, // the rotation center relative to the bottom left corner of the box
                    2, 2, // the width and height of the box
                    1, 1, // the scale on the x- and y-axis
                    angle); // the rotation angle

        }
        batch.end();

        // next we use the debug renderer. Note that we
        // simply apply the camera again and then call
        // the renderer. the camera.apply() call is actually
        // not needed as the opengl matrices are already set
        // by the spritebatch which in turn uses the camera matrices :)
        debugRenderer.render(world, camera.combined);

        // finally we render all contact points
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Point);
        renderer.setColor(0, 1, 0, 1);

        for (int i = 0; i < world.getContactCount(); i++) {
            Contact contact = world.getContactList().get(i);
            // we only render the contact if it actually touches
            if (contact.isTouching()) {
                // get the world manifold from which we get the
                // contact points. A manifold can have 0, 1 or 2
                // contact points.
                WorldManifold manifold = contact.getWorldManifold();
                int numContactPoints = manifold.getNumberOfContactPoints();
                for (int j = 0; j < numContactPoints; j++) {
                    Vector2 point = manifold.getPoints()[j];
                    renderer.point(point.x, point.y, 0);
                }
            }
        }
        renderer.end();
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
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
