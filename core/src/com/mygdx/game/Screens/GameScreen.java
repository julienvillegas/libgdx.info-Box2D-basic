package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by artum on 12.12.2017.
 */

public class GameScreen implements Screen, InputProcessor {

    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;
    static final float SCALE = 0.005f;

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
    static final int COUNT = 60;

    Body[][] Bodies = new Body[maxwidthofship][maxheightofship];
    String[][] namesofBodies = new String[maxwidthofship][maxheightofship];
    int [][] player1ship = new int[maxwidthofship][maxheightofship];

    int firstplayerturbine1_I = -1;
    int firstplayerturbine1_J = -1;
    int firstplayerturbine2_I = -1;
    int firstplayerturbine2_J = -1;
    int firstplayergun1_I = -1;
    int firstplayergun1_J = -1;
    int firstplayergun2_1_I = -1;
    int firstplayergun2_1_J = -1;
    int firstplayergun2_2_I = -1;
    int firstplayergun2_2_J = -1;



    int [][] player2ship = new int[maxwidthofship][maxheightofship];

    Body[] meteorBodies = new Body[COUNT];
    String[] meteornames = new String[COUNT];
    ArrayList<Body> bullets = new ArrayList<Body>();

    public GameScreen(
            int [][] player1ship
            // int [][] player2ship
            ){
        this.player1ship = player1ship;
        //this.player2ship = player2ship;
        //player1ship[0] = new int[]{0, 0, 0, 0, 0};
        //player1ship[1] = new int[]{0, 6, 1, 6, 0};
        //player1ship[2] = new int[]{0, 7, 1, 7, 0};
        //player1ship[3] = new int[]{0, 5, 4, 5, 0};
        //player1ship[4] = new int[]{0, 0, 0, 0, 0};
        //player1ship[5] = new int[]{0, 0, 0, 0, 0};
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
        generateMeteors();

        debugRenderer = new Box2DDebugRenderer();
    }
    private void addSprites() {
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.getRegions();

        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);

            float width = sprite.getWidth() * SCALE;
            float height = sprite.getHeight() * SCALE;

            sprite.setSize(width, height);
            sprite.setOrigin(0, 0);

            sprites.put(region.name, sprite);
        }
    }

    private void generate() {
        String[] blockNames = new String[]{"steelblock","engine","turbine","halfwoodblock","halfsteelblock","gunone","guntwo", "woodblock"};

        int k=0;
        int f=0;
        for (int j = 0; j < maxheightofship; j++) {
            for (int i = 0; i < maxwidthofship; i++) {
                for (int t = 1; t < 9; t++){
                    if (player1ship[i][j]%10 == t){
                        String name = blockNames[t-1];
                        if (player1ship[i][j]/10 == 1){name+="90";}
                        if (player1ship[i][j]/10 == 2){name+="180";}
                        if (player1ship[i][j]/10 == 3){name+="270";}

                        float x = (float) ((10+i*7)*SCALE/0.02);
                        float y = (float) ((40 -j*7)*SCALE/0.02);

                        //turbine
                        if (player1ship[i][j]%10 == 3){
                            if (k==1){firstplayerturbine2_I = i;firstplayerturbine2_J = j;}
                            if (k==0){firstplayerturbine1_I = i;firstplayerturbine1_J = j;k+=1;}

                            if (player1ship[i][j]/10 == 0){x = (float) ((10+i*7 - 4.5)*SCALE/0.02);}
                            if (player1ship[i][j]/10 == 1){y = (float) ((40-j*7 - 4.5)*SCALE/0.02);}
                        }

                        //gunone
                        if (player1ship[i][j]%10 == 6){
                            firstplayergun1_I = i;
                            firstplayergun1_J = j;

                            if (player1ship[i][j]/10 == 0){y = (float) ((40 -j*7 +1.2)*SCALE/0.02);}
                            if (player1ship[i][j]/10 == 1){x = (float) ((10+i*7+1.2)*SCALE/0.02);}
                            if (player1ship[i][j]/10 == 2){
                                x = (float) ((10+i*7 - 8.2)*SCALE/0.02);
                                y = (float) ((40 -j*7 +1.2)*SCALE/0.02);
                            }
                            if (player1ship[i][j]/10 == 3){
                                x = (float) ((10+i*7 +1.2)*SCALE/0.02);
                                y = (float) ((40 -j*7 -8.2)*SCALE/0.02);
                            }
                        }

                        //guntwo
                        if (player1ship[i][j]%10 == 7){
                            if (f==1){firstplayergun2_2_I = i;firstplayergun2_2_J = j;}
                            if (f==0){firstplayergun2_1_I = i;firstplayergun2_1_J = j;f+=1;}

                            if (player1ship[i][j]/10 == 0){y = (float) ((40 -j*7 +0.1)*SCALE/0.02);}
                            if (player1ship[i][j]/10 == 1){x = (float) ((10+i*7+0.1)*SCALE/0.02);}
                            if (player1ship[i][j]/10 == 2){
                                x = (float) ((10+i*7 - 8.2)*SCALE/0.02);
                                y = (float) ((40 -j*7 +0.1)*SCALE/0.02);
                            }
                            if (player1ship[i][j]/10 == 3){
                                x = (float) ((10+i*7 +0.1)*SCALE/0.02);
                                y = (float) ((40 -j*7 -8.2)*SCALE/0.02);
                            }
                        }

                        namesofBodies[i][j] = name;
                        Bodies[i][j]=createBody(name, x, y, 0);
                    }
                }

            }
        }

        WeldJointDef jointDef = new WeldJointDef();

        for (int j = 0; j < maxheightofship; j++) {
            for (int i = 1; i < maxwidthofship; i++) {
                if (player1ship[i][j]!=0&&player1ship[i-1][j]!=0) {

                    jointDef.initialize(Bodies[i][j],Bodies[i-1][j], new Vector2((float)((10 + 7+7 +3.5)*SCALE/0.02),(float)((40 - 7 -3.5)*SCALE/0.02)));
                    world.createJoint(jointDef);
                }
            }
        }
        for (int i = 0; i < maxwidthofship; i++) {
            for (int j = 1; j < maxheightofship; j++) {
                if (player1ship[i][j] != 0&&player1ship[i][j-1]!=0) {
                    jointDef.initialize(Bodies[i][j],Bodies[i][j-1], new Vector2((float)((10 + 7+7 +3.5)*SCALE/0.02),(float)((40 - 7 -3.5)*SCALE/0.02)));
                    world.createJoint(jointDef);
                    world.createJoint(jointDef);
                }
            }
        }

    }

    private void generateMeteors() {
        String[] meteorNames = new String[]{"meteorthree", "meteorfour", "meteorfive","meteortwo","meteortwo1","meteortwo2","meteortwo3","meteortwo4",
                "meteorone","meteorone1","meteorone2","meteorone3"};

        Random random = new Random();

        for (int i = 0; i < meteorBodies.length; i++) {
            String name = meteorNames[random.nextInt(meteorNames.length)];

            float x = (random.nextFloat() * 150)*(float)( SCALE/0.01);
            float y = (random.nextFloat() * 80)*(float)( SCALE/0.01);
            if ((x<50*SCALE/0.02)&&(y<50*SCALE/0.01)){
                x +=50*(float)(SCALE/0.01);
                y +=50*(float)(SCALE/0.01);
            }
            meteornames[i] = name;
            meteorBodies[i] = createBody(name, x, y, 0);
            meteorBodies[i].setLinearVelocity(new Vector2((random.nextFloat()-0.5f)*20,(random.nextFloat()-0.5f)*20));
        }
    }

    private Body createBody(String name, float x, float y, float rotation) {
        Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
        body.setTransform(x, y, rotation);
        return body;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stepWorld();

        batch.begin();

        drawSprite("background_red_space",0,0,camera.viewportWidth,camera.viewportHeight,0);


        for (int i = 0; i < maxwidthofship; i++) {
            for (int j = 0; j < maxheightofship; j++) {
                if (player1ship[i][j]!=0) {
                    Body body = Bodies[i][j];
                    String name = namesofBodies[i][j];

                    Vector2 position = body.getPosition();
                    float degrees = (float) Math.toDegrees(body.getAngle());
                    drawSprite(name, position.x, position.y, degrees);
                }
            }
        }
        for (int i = 0; i < meteorBodies.length; i++) {
            Body body = meteorBodies[i];
            String name = meteornames[i];

            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSprite(name, position.x, position.y, degrees);
        }
        while (bullets.size()>30){
            world.destroyBody(bullets.get(0));
            bullets.remove(0);
        }
        for (int i = 0; i < bullets.size(); i++) {
            Body body = bullets.get(i);
            String name = "bullet";

            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSprite(name, position.x, position.y, degrees);
        }

        drawSprite("button2",-5,-5,20,20,0);
        drawSprite("button3",-7,8.5f,25,32,0);
        drawSprite("button1",-5, 32,20,20,0);


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
    private void drawSprite(String name, float x, float y,float width, float height, float degrees){
        Sprite sprite = sprites.get(name);
        sprite.setPosition(x, y);
        sprite.setRotation(degrees);
        sprite.setSize(width,height);
        sprite.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        batch.setProjectionMatrix(camera.combined);

        createGround();
    }

    private void createGround() {
        if (upground != null) {world.destroyBody(upground);}
        if (downground != null) {world.destroyBody(downground);}
        if (leftground != null) {world.destroyBody(leftground);}
        if (rightground != null) {world.destroyBody(rightground);}

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

        upground = world.createBody(bodyDef);
        upground.createFixture(fixtureDef);
        upground.setTransform(0, camera.viewportHeight, 0);

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
        textureAtlas.dispose();
        sprites.clear();
        world.dispose();
        debugRenderer.dispose();
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
        //turbine
        if (firstplayerturbine1_I>0) {
            Body body1 = Bodies[firstplayerturbine1_I][firstplayerturbine1_J];
            float cos1 = (float) Math.cos(body1.getAngle());
            float sin1 = (float) Math.sin(body1.getAngle());
            for (int i=1;i<4;i++){
            if(player1ship[firstplayerturbine1_I][firstplayerturbine1_J]/10 == i){
                cos1 = (float) Math.cos(body1.getAngle()+i*Math.PI/2);
                sin1 = (float) Math.sin(body1.getAngle()+i*Math.PI/2);
            }}

            if (screenY < Gdx.graphics.getHeight() / 3) {
                Bodies[firstplayerturbine1_I][firstplayerturbine1_J].applyForceToCenter(20000 * cos1, 20000 * sin1, true);
            }
        }

        if (firstplayerturbine2_I>0) {
            Body body2 = Bodies[firstplayerturbine2_I][firstplayerturbine2_J];
            float cos2 = (float) Math.cos(body2.getAngle());
            float sin2 = (float) Math.sin(body2.getAngle());
            for (int i=1;i<4;i++){
                if(player1ship[firstplayerturbine2_I][firstplayerturbine2_J]/10 == i){
                    cos2 = (float) Math.cos(body2.getAngle()+i*Math.PI/2);
                    sin2 = (float) Math.sin(body2.getAngle()+i*Math.PI/2);
                }}
            if (screenY > Gdx.graphics.getHeight() * 2 / 3) {
                Bodies[firstplayerturbine2_I][firstplayerturbine2_J].applyForceToCenter(20000 * cos2, 20000 * sin2, true);
            }
        }

        //gunone
        if (firstplayergun1_I>0) {
            Body body3 = Bodies[firstplayergun1_I][firstplayergun1_J];
            float alpha = (float) (Math.atan(0.12597403));
            float cos3;float sin3;float cos3alpha;float sin3alpha;
            for (int i = 0; i < 4; i++) {
                if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == i) {
                    cos3 = (float) Math.cos(body3.getAngle() + i * Math.PI/2);
                    sin3 = (float) Math.sin(body3.getAngle() + i * Math.PI/2);

                    if ((screenY > Gdx.graphics.getHeight() / 3) && (screenY < Gdx.graphics.getHeight() * 2 / 3)) {
                        Body bullet = createBody("bullet", 0, 0, body3.getAngle());
                        if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == 0){
                            cos3alpha = (float) Math.cos(body3.getAngle() + alpha);
                            sin3alpha = (float) Math.sin(body3.getAngle() + alpha);
                            bullet.setTransform(body3.getPosition().x + 4f*cos3alpha, body3.getPosition().y + 4f*sin3alpha, body3.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == 1){
                            cos3alpha = (float) Math.cos(body3.getAngle() -alpha +  Math.PI/2);
                            sin3alpha = (float) Math.sin(body3.getAngle() -alpha +  Math.PI/2);
                            bullet.setTransform(body3.getPosition().x + 4f*cos3alpha, body3.getPosition().y + 4f*sin3alpha, body3.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == 2){
                            cos3alpha = (float) Math.cos(body3.getAngle() + Math.PI/2);
                            sin3alpha = (float) Math.sin(body3.getAngle() + Math.PI/2);
                            bullet.setTransform(body3.getPosition().x + 0.5f*cos3alpha, body3.getPosition().y + 0.5f*sin3alpha, body3.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == 3){
                            cos3alpha = (float) Math.cos(body3.getAngle());
                            sin3alpha = (float) Math.sin(body3.getAngle());
                            bullet.setTransform(body3.getPosition().x + 0.5f*cos3alpha, body3.getPosition().y + 0.5f*sin3alpha, body3.getAngle()+(float) Math.PI/2*i);
                        }

                        bullet.setLinearVelocity(new Vector2(body3.getLinearVelocity().x + 10000 * (float) Math.cos(body3.getAngle() + i*Math.PI/2), body3.getLinearVelocity().y + 10000 * (float) Math.sin(body3.getAngle() + i*Math.PI/2)));
                        bullets.add(bullet);
                        Bodies[firstplayergun1_I][firstplayergun1_J].applyForceToCenter(-10000 * cos3, -10000 * sin3, true);
                    }
                }
            }
        }
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
