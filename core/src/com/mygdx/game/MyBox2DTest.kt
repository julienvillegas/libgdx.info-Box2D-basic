package com.mygdx.game

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World


import java.util.ArrayList


class MyBox2DTest : ApplicationListener, InputProcessor {
    // the camera
    private var camera: OrthographicCamera? = null

    // box2d debug renderer
    private var debugRenderer: Box2DDebugRenderer? = null

    //pictures
    private var woodblock_picture: TextureRegion? = null
    private var steelblock_picture: TextureRegion? = null
    private var halfwoodblock_picture: TextureRegion? = null
    private var halfsteelblock_picture: TextureRegion? = null
    private var engine_picture: TextureRegion? = null
    private var gun_1_picture: TextureRegion? = null
    private var gun_2_picture: TextureRegion? = null
    private var turbine_picture: TextureRegion? = null
    private var meteor_picture: TextureRegion? = null
    private var meteor_2_picture: TextureRegion? = null
    private var background_picture: TextureRegion? = null

    private var batch: SpriteBatch? = null

    //box2D world
    private var world: World? = null

    //bodies
    private val woodblocks = ArrayList<Body>()
    private val steelblocks = ArrayList<Body>()
    private var meteor: Body? = null
    private var halfwoodblockbody: Body? = null
    private var halfsteelblockbody: Body? = null


    override fun create() {
        // setup the camera. In Box2D we operate on a
        // meter scale, pixels won't do it. So we use
        // an orthographic camera with a viewport of
        // 48 meters in width and 32 meters in height.
        // We also position the camera so that it
        // looks at (0,16) (that's where the middle of the
        // screen will be located).
        camera = OrthographicCamera(48f, 32f)
        camera!!.position.set(0f, 16f, 0f)

        // next we create the box2d debug renderer
        debugRenderer = Box2DDebugRenderer()

        // next we create a SpriteBatch a
        batch = SpriteBatch()

        woodblock_picture = TextureRegion(Texture(Gdx.files.internal("woodblock.png")))
        steelblock_picture = TextureRegion(Texture(Gdx.files.internal("steelblock.png")))
        halfwoodblock_picture = TextureRegion(Texture(Gdx.files.internal("halfwoodblock.png")))
        halfsteelblock_picture = TextureRegion(Texture(Gdx.files.internal("halfsteelblock.png")))
        engine_picture = TextureRegion(Texture(Gdx.files.internal("engine.png")))
        gun_1_picture = TextureRegion(Texture(Gdx.files.internal("gun_1.png")))
        gun_2_picture = TextureRegion(Texture(Gdx.files.internal("gun_2.png")))
        turbine_picture = TextureRegion(Texture(Gdx.files.internal("turbine.png")))
        meteor_picture = TextureRegion(Texture(Gdx.files.internal("meteor.png")))
        meteor_2_picture = TextureRegion(Texture(Gdx.files.internal("meteor_2.png")))
        background_picture = TextureRegion(Texture(Gdx.files.internal("background_red_space.png")))

        // next we create out physics world.
        createPhysicsWorld()

        // register ourselfs as an InputProcessor
        Gdx.input.inputProcessor = this
    }

    private fun createPhysicsWorld() {
        // we instantiate a new World with a proper gravity vector
        // and tell it to sleep when possible.
        world = World(Vector2(0f, -10f), true)

            // next we create a static ground platform. This platform
            // is not moveable and will not react to any influences from
            // outside. It will however influence other bodies. First we
            // create a PolygonShape that holds the form of the platform.
            // it will be 100 meters wide and 2 meters high, centered
            // around the origin
            val groundPoly = PolygonShape()
            groundPoly.setAsBox(50f, 1f)

            // next we create the body for the ground platform. It's
            // simply a static body.
            val groundBodyDef = BodyDef()
            groundBodyDef.type = BodyDef.BodyType.StaticBody
            /* ground body to connect the mouse joint to */
            val groundBody = world!!.createBody(groundBodyDef)

            // finally we add a fixture to the body using the polygon
            // defined above. Note that we have to dispose PolygonShapes
            // and CircleShapes once they are no longer used. This is the
            // only time you have to care explicitly for memory management.
            val fixtureDef = FixtureDef()
            fixtureDef.shape = groundPoly
            fixtureDef.filter.groupIndex = 0
            groundBody.createFixture(fixtureDef)
            groundPoly.dispose()


            val shape = CircleShape()
            shape.radius = 2f

            val fd = FixtureDef()
            fd.shape = shape
            fd.density = 1.0f


            val bd = BodyDef()
            bd.type = BodyDef.BodyType.DynamicBody
            bd.position.set(0f, 30f)

            meteor = world!!.createBody(bd)
            meteor!!.createFixture(fd)

            shape.dispose()


            val vertices = arrayOfNulls<Vector2>(3)
            vertices[0] = Vector2(0f, 0f)
            vertices[1] = Vector2(0f, 4f)
            vertices[2] = Vector2(4f, 0f)


            val shape1 = PolygonShape()
            shape1.set(vertices)

            val fd1 = FixtureDef()
            fd1.shape = shape1
            fd1.density = 1.0f

            val bd1 = BodyDef()
            bd1.type = BodyDef.BodyType.DynamicBody
            bd1.position.set(-10f, 60f)
            halfwoodblockbody = world!!.createBody(bd1)
            halfwoodblockbody!!.createFixture(fd1)

            shape1.dispose()


            val vertices1 = arrayOfNulls<Vector2>(3)
            vertices1[0] = Vector2(0f, 0f)
            vertices1[1] = Vector2(0f, -4f)
            vertices1[2] = Vector2(4f, 0f)


            val shape2 = PolygonShape()
            shape2.set(vertices1)

            val fd2 = FixtureDef()
            fd2.shape = shape2
            fd2.density = 1.0f

            val bd2 = BodyDef()
            bd2.type = BodyDef.BodyType.DynamicBody
            bd2.position.set(-20f, 60f)
            halfsteelblockbody = world!!.createBody(bd2)
            halfsteelblockbody!!.createFixture(fd2)

            shape2.dispose()


        createBoxes()
    }

    private fun createBoxes() {
        // next we create 50 boxes at random locations above the ground
        // body. First we create a nice polygon representing a box 2 meters
        // wide and high.
        val boxPoly = PolygonShape()
        boxPoly.setAsBox(2f, 2f)

        // next we create the 50 box bodies using the PolygonShape we just
        // defined. This process is similar to the one we used for the ground
        // body. Note that we reuse the polygon for each body fixture.
        for (i in 0..19) {
            // Create the BodyDef, set a random position above the
            // ground and create a new body
            val boxBodyDef = BodyDef()
            boxBodyDef.type = BodyDef.BodyType.DynamicBody
            boxBodyDef.position.x = -24 + (Math.random() * 48).toFloat()
            boxBodyDef.position.y = 10 + (Math.random() * 100).toFloat()
            val boxBody = world!!.createBody(boxBodyDef)
            boxBody.createFixture(boxPoly, 1f)

            // add the box to our list of boxes
            woodblocks.add(boxBody)
        }
        // we are done, all that's left is disposing the boxPoly
        boxPoly.dispose()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun render() {
        // first we update the world. For simplicity
        // we use the delta time provided by the Graphics
        // instance. Normally you'll want to fix the time
        // step.

        world!!.step(Gdx.graphics.deltaTime, 8, 3)


        // next we clear the color buffer and set the camera
        // matrices
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera!!.update()

        // next we render each box via the SpriteBatch.
        // for this we have to set the projection matrix of the
        // spritebatch to the camera's combined matrix. This will
        // make the spritebatch work in world coordinates
        batch!!.projectionMatrix.set(camera!!.combined)

        batch!!.begin()
        batch!!.draw(background_picture!!, -24f, 0f, 48f, 32f)
        for (i in 0 .. woodblocks.size / 2-1) {
            val box = woodblocks[i]
            val position = box.position // that's the box's center position
            val angle = MathUtils.radiansToDegrees * box.angle // the rotation angle around the center
            batch!!.draw(woodblock_picture!!, position.x - 2, position.y - 2, // the bottom left corner of the box, unrotated
                    2f, 2f, // the rotation center relative to the bottom left corner of the box
                    4f, 4f, // the width and height of the box
                    1.1f, 1.1f, // the scale on the x- and y-axis
                    angle) // the rotation angle

        }
        for (i in woodblocks.size / 2 .. woodblocks.size-1) {
            val box = woodblocks[i]
            val position = box.position // that's the box's center position
            val angle = MathUtils.radiansToDegrees * box.angle // the rotation angle around the center
            batch!!.draw(steelblock_picture!!, position.x - 2, position.y - 2, // the bottom left corner of the box, unrotated
                    2f, 2f, // the rotation center relative to the bottom left corner of the box
                    4f, 4f, // the width and height of the box
                    1.1f, 1.1f, // the scale on the x- and y-axis
                    angle) // the rotation angle

        }

            val position = meteor!!.position
            val angle = MathUtils.radiansToDegrees * meteor!!.angle
            batch!!.draw(meteor_2_picture!!, position.x - 2, position.y - 2, // the bottom left corner of the box, unrotated
                    2f, 2f, // the rotation center relative to the bottom left corner of the box
                    4f, 4f, // the width and height of the box
                    1.0f, 1.0f, // the scale on the x- and y-axis
                    angle)


            val position1 = halfwoodblockbody!!.position
            val angle1 = MathUtils.radiansToDegrees * halfwoodblockbody!!.angle
            batch!!.draw(halfwoodblock_picture!!, position1.x, position1.y, // the bottom left corner of the box, unrotated
                    0f, 0f, // the rotation center relative to the bottom left corner of the box
                    4f, 4f, // the width and height of the box
                    1.0f, 1.0f, // the scale on the x- and y-axis
                    angle1)

        // {
        //     Vector2 position = halfsteelblockbody.getPosition();
        //     float angle = MathUtils.radiansToDegrees * halfsteelblockbody.getAngle();
        //     batch.draw(halfsteelblock_picture, position.x, position.y , // the bottom left corner of the box, unrotated
        //             0, 0, // the rotation center relative to the bottom left corner of the box
        //             4, 4, // the width and height of the box
        //             1.0f, 1.0f, // the scale on the x- and y-axis
        //             angle);
        // }
        batch!!.end()

        debugRenderer!!.render(world, camera!!.combined)

    }


    override fun pause() {

    }

    override fun resume() {

    }

    override fun dispose() {
        debugRenderer!!.dispose()
        world!!.dispose()

        world = null
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }
}
