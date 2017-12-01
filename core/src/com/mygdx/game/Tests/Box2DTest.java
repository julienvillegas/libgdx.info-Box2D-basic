package com.mygdx.game.Tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class Box2DTest implements ApplicationListener, InputProcessor {
    /** the camera **/
    protected OrthographicCamera camera;

    /** the renderer **/
    protected Box2DDebugRenderer renderer;

    SpriteBatch batch;

    /** our box2D world **/
    protected World world;

    /** ground body to connect the mouse joint to **/
    protected Body groundBody;

    /** our mouse joint **/
    protected MouseJoint mouseJoint = null;

    /** a hit body **/
    protected Body hitBody = null;

    protected abstract void createWorld (World world);

    @Override

    public void render () {
        // update the world with a fixed time step
        world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);

        // clear the screen and setup the projection matrix
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        // render the world using the debug renderer
        renderer.render(world, camera.combined);

        batch.begin();
        batch.end();

    }



    @Override
    public void create () {

        // setup the camera. In Box2D we operate on a
        // meter scale, pixels won't do it. So we use
        // an orthographic camera with a viewport of
        // 48 meters in width and 32 meters in height.
        // We also position the camera so that it
        // looks at (0,16) (that's where the middle of the
        // screen will be located).

        camera = new OrthographicCamera(48, 32);
        camera.position.set(0, 16, 0);


        // create the debug renderer
        renderer = new Box2DDebugRenderer();

        // create the world
        world = new World(new Vector2(0, -10), true);

        // we also need an invisible zero size ground body
        // to which we can connect the mouse joint
        BodyDef bodyDef = new BodyDef();
        groundBody = world.createBody(bodyDef);



        // call abstract method to populate the world

        createWorld(world);

        batch = new SpriteBatch();

    }



    @Override

    public void dispose () {
        renderer.dispose();
        world.dispose();

        renderer = null;
        world = null;
        mouseJoint = null;
        hitBody = null;
    }



    @Override
    public boolean keyDown (int keycode) {
        return false;

    }



    @Override
    public boolean keyTyped (char character) {
        return false;

    }



    @Override
    public boolean keyUp (int keycode) {
        return false;

    }



    /** we instantiate this vector and the callback here so we don't irritate the GC **/

    Vector3 testPoint = new Vector3();
    QueryCallback callback = new QueryCallback() {

        @Override
        public boolean reportFixture (Fixture fixture) {
            // if the hit point is inside the fixture of the body
            // we report it
            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                hitBody = fixture.getBody();
                return false;
            } else
                return true;

        }

    };



    @Override

    public boolean touchDown (int x, int y, int pointer, int button) {
        // translate the mouse coordinates to world coordinates
        camera.unproject(testPoint.set(x, y, 0));
        // ask the world which bodies are within the given
        // bounding box around the mouse pointer
        hitBody = null;
        world.QueryAABB(callback, testPoint.x - 0.0001f, testPoint.y - 0.0001f, testPoint.x + 0.0001f, testPoint.y + 0.0001f);

        if (hitBody == groundBody) hitBody = null;

        // ignore kinematic bodies, they don't work with the mouse joint
        if (hitBody != null && hitBody.getType() == BodyType.KinematicBody) return false;

        // if we hit something we create a new mouse joint
        // and attach it to the hit body.
        if (hitBody != null) {
            MouseJointDef def = new MouseJointDef();
            def.bodyA = groundBody;
            def.bodyB = hitBody;
            def.collideConnected = true;
            def.target.set(testPoint.x, testPoint.y);
            def.maxForce = 1000.0f * hitBody.getMass();

            mouseJoint = (MouseJoint)world.createJoint(def);
            hitBody.setAwake(true);
        }
        return false;
    }



    /** another temporary vector **/
    Vector2 target = new Vector2();



    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        // if a mouse joint exists we simply update
        // the target of the joint based on the new
        // mouse coordinates

        if (mouseJoint != null) {
            camera.unproject(testPoint.set(x, y, 0));
            mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));
        }
        return false;
    }



    @Override

    public boolean touchUp (int x, int y, int pointer, int button) {
        // if a mouse joint exists we simply destroy it
        if (mouseJoint != null) {
            world.destroyJoint(mouseJoint);
            mouseJoint = null;
        }
        return false;
    }



    @Override
    public boolean mouseMoved (int x, int y) {
        return false;
    }



    @Override
    public boolean scrolled (int amount) {
        return false;
    }



    public void pause () {
    }



    public void resume () {
    }



    public void resize (int width, int height) {
    }

}