package com.mygdx.game.Bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.security.cert.TrustAnchor;

/**
 * Created by julienvillegas on 31/01/2017.
 */

public class Block extends Image  {

    private Body body;
    private World world;

    public Block(World aWorld, float pos_x, float pos_y,float aWidth, float aHeight, float angle){
        super(new Texture("woodenblock.png"));
        this.setSize(aWidth,aHeight);
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
        this.rotateBy(angle);
        this.setPosition(pos_x,pos_y);

        world = aWorld;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos_x, pos_y);
        bodyDef.linearDamping = 0f;
        bodyDef.angularDamping = 0f;
        bodyDef.gravityScale = 1000f;

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions as our sprite
        shape.setAsBox(this.getWidth()/2, this.getHeight()/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others we will see shortly
        // If you are wondering, density and area are used to calculate over all mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);

    }

    public void AddDWeldJoint(Block block){
        WeldJointDef jointDef = new WeldJointDef ();
        jointDef.initialize(this.body, block.body, new Vector2(0,0) );
        world.createJoint(jointDef); // Returns subclass Joint.
    }


    public void ApplyForseToCenter(float a, float b) {
        this.body.applyForceToCenter(a, b, true);
    }


    public void SetLinearVelocity(float a, float b){
        this.body.setLinearVelocity(a,b);

    }

    public void SetAngularVelocity(float a){
        this.body.setAngularVelocity(a);

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setRotation(body.getAngle()*  MathUtils.radiansToDegrees);

        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);


    }




}
