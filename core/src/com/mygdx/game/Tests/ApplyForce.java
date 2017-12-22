package com.mygdx.game.Tests;

import com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.Body;

import com.badlogic.gdx.physics.box2d.BodyDef;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import com.badlogic.gdx.physics.box2d.EdgeShape;

import com.badlogic.gdx.physics.box2d.FixtureDef;

import com.badlogic.gdx.physics.box2d.PolygonShape;

import com.badlogic.gdx.physics.box2d.Transform;

import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.mygdx.game.Tests.Box2DTest;


public class ApplyForce extends Box2DTest {

    Body m_body;



    @Override
    protected void createWorld (World world) {
        world.setGravity(new Vector2(0, 0));



        float k_restitution = 0.4f;
        Body ground;
        {

            BodyDef bd = new BodyDef();
            bd.position.set(0, 20);
            ground = world.createBody(bd);

            EdgeShape shape = new EdgeShape();
            FixtureDef sd = new FixtureDef();

            sd.shape = shape;
            sd.density = 0;
            sd.restitution = k_restitution;

            shape.set(new Vector2(-20, -20), new Vector2(-20, 20));
            ground.createFixture(sd);

            shape.set(new Vector2(20, -20), new Vector2(20, 20));
            ground.createFixture(sd);

            shape.set(new Vector2(-20, 20), new Vector2(20, 20));
            ground.createFixture(sd);

            shape.set(new Vector2(-20, -20), new Vector2(20, -20));
            ground.createFixture(sd);

            shape.dispose();

        }



        {

            Transform xf1 = new Transform(new Vector2(), 0.3524f * (float)Math.PI);
            xf1.setPosition(xf1.mul(new Vector2(1, 0)));



            Vector2[] vertices = new Vector2[3];
            vertices[0] = xf1.mul(new Vector2(-1, 0));
            vertices[1] = xf1.mul(new Vector2(1, 0));
            vertices[2] = xf1.mul(new Vector2(0, 0.5f));

            PolygonShape poly1 = new PolygonShape();
            poly1.set(vertices);

            FixtureDef sd1 = new FixtureDef();
            sd1.shape = poly1;
            sd1.density = 4.0f;

            Transform xf2 = new Transform(new Vector2(), -0.3524f * (float)Math.PI);
            xf2.setPosition(xf2.mul(new Vector2(-1, 0)));

            vertices[0] = xf2.mul(new Vector2(-1, 0));
            vertices[1] = xf2.mul(new Vector2(1, 0));
            vertices[2] = xf2.mul(new Vector2(0, 0.5f));



            PolygonShape poly2 = new PolygonShape();

            poly2.set(vertices);



            FixtureDef sd2 = new FixtureDef();

            sd2.shape = poly2;

            sd2.density = 2.0f;



            BodyDef bd = new BodyDef();

            bd.type = BodyType.DynamicBody;

            bd.angularDamping = 5.0f;

            bd.linearDamping = 0.1f;



            bd.position.set(0, 2);

            bd.angle = (float)Math.PI;

            bd.allowSleep = false;

            m_body = world.createBody(bd);

            m_body.createFixture(sd1);

            m_body.createFixture(sd2);

            poly1.dispose();

            poly2.dispose();

        }



        {

            PolygonShape shape = new PolygonShape();

            shape.setAsBox(0.5f, 0.5f);



            FixtureDef fd = new FixtureDef();

            fd.shape = shape;

            fd.density = 1.0f;

            fd.friction = 0.3f;



            for (int i = 0; i < 10; i++) {

                BodyDef bd = new BodyDef();

                bd.type = BodyType.DynamicBody;



                bd.position.set(0, 5 + 1.54f * i);

                Body body = world.createBody(bd);



                body.createFixture(fd);



                float gravity = 10.0f;

                float I = body.getInertia();

                float mass = body.getMass();



                float radius = (float)Math.sqrt(2 * I / mass);



                FrictionJointDef jd = new FrictionJointDef();

                jd.localAnchorA.set(0, 0);

                jd.localAnchorB.set(0, 0);

                jd.bodyA = ground;

                jd.bodyB = body;

                jd.collideConnected = true;

                jd.maxForce = mass * gravity;

                jd.maxTorque = mass * radius * gravity;



                world.createJoint(jd);

            }



            shape.dispose();

        }

    }



    private final Vector2 tmp = new Vector2();



    public boolean keyDown (int keyCode) {

        if (keyCode == Keys.W) {

            Vector2 f = m_body.getWorldVector(tmp.set(0, -200));

            Vector2 p = m_body.getWorldPoint(tmp.set(0, 2));

            m_body.applyForce(f, p, true);

        }

        if (keyCode == Keys.A) m_body.applyTorque(50, true);

        if (keyCode == Keys.D) m_body.applyTorque(-50, true);



        return false;

    }
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


}