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
import com.mygdx.game.Extra.AssemblingScreenCoords;
import com.mygdx.game.Extra.ItemID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class GameScreen implements Screen, InputProcessor, ItemID, AssemblingScreenCoords {

    private static final float STEP_TIME = 1f / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float SCALE = 0.005f;
    private static final int COUNT = 60;

    private TextureAtlas textureAtlas;
    private TextureAtlas textureAtlas2;
    private SpriteBatch batch;
    private final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();


    private OrthographicCamera camera;
    private ExtendViewport viewport;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private PhysicsShapeCache physicsBodies;
    private float accumulator = 0;

    private Body[] gameFieldBounds = new Body[4];                           // Границы карты

    private Body[][] Bodies1 = new Body[FIELD_WIDTH][FIELD_HEIGHT];
    private Body[][] Bodies2 = new Body[FIELD_WIDTH][FIELD_HEIGHT];
    private String[][] namesOfBodies1 = new String[FIELD_WIDTH][FIELD_HEIGHT];
    private String[][] namesOfBodies2 = new String[FIELD_WIDTH][FIELD_HEIGHT];
    private int [][] player1ship = new int[FIELD_WIDTH][FIELD_HEIGHT];
    private int [][] player2ship = new int[FIELD_WIDTH][FIELD_HEIGHT];

    private int firstplayerturbine1_I = -1;
    private int firstplayerturbine1_J = -1;
    private int firstplayerturbine2_I = -1;
    private int firstplayerturbine2_J = -1;
    private int firstplayergun1_I = -1;
    private int firstplayergun1_J = -1;
    private int firstplayergun2_1_I = -1;
    private int firstplayergun2_1_J = -1;
    private int firstplayergun2_2_I = -1;
    private int firstplayergun2_2_J = -1;


    private ArrayList<Integer> p1_enginesCoords = new ArrayList<Integer>();
    private float p1_turbine1power = 0f;
    private float p1_turbine2power = 0f;


    private int secondplayerturbine1_I = -1;
    private int secondplayerturbine1_J = -1;
    private int secondplayerturbine2_I = -1;
    private int secondplayerturbine2_J = -1;
    private int secondplayergun1_I = -1;
    private int secondplayergun1_J = -1;
    private int secondplayergun2_1_I = -1;
    private int secondplayergun2_1_J = -1;
    private int secondplayergun2_2_I = -1;
    private int secondplayergun2_2_J = -1;


    private Body[] meteorBodies = new Body[COUNT];
    private String[] meteorNames = new String[COUNT];
    private ArrayList<Body> bullets = new ArrayList<Body>();

    public GameScreen(
            int [][] player1ship,
            int [][] player2ship
            ){
        this.player1ship = player1ship;
        this.player2ship = player2ship;


        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(50, 50, camera);

        textureAtlas = new TextureAtlas("sprites.txt");
        textureAtlas2 = new TextureAtlas("sprites2.txt");
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
        regions = textureAtlas2.getRegions();
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
        for (int j = 0; j < FIELD_HEIGHT; j++) {
            for (int i = 0; i < FIELD_WIDTH; i++) {
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

                        //engine
                        if (player1ship[i][j] % 10 == ENGINE) {
                            p1_enginesCoords.add(i);
                            p1_enginesCoords.add(j);
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

                        namesOfBodies1[i][j] = name;
                        Bodies1[i][j]=createBody(name, x, y, 0);
                        Bodies1[i][j].setBullet(true);
                    }
                }

            }
        }

        for (int i = 0; i < p1_enginesCoords.size(); i += 2) {

            int I = p1_enginesCoords.get(i);
            int J = p1_enginesCoords.get(i + 1);

            int turbineNeighbours = 0;                                                                          // Тут хранятся данные о граничащих с двигателем турбинах

            if (Math.abs(firstplayerturbine1_I - I) + Math.abs(firstplayerturbine1_J - J) == 1)
                turbineNeighbours += 1;                                                                         // Если двигатель граничит с первой турбиной, то turbineNeighbours % 2 == 1
            if (Math.abs(firstplayerturbine2_I - I) + Math.abs(firstplayerturbine2_J - J) == 1)
                turbineNeighbours += 2;                                                                         // Если двигатель граничит со второй турбиной, то turbineNeighbours / 2 == 1

            float additionalPower = 1f / (float) (turbineNeighbours / 2 + turbineNeighbours % 2);     // Добавочный коэффициент силы турбин равен (1 / КОЛИЧЕСТВО_ТУРБИН)
            if (turbineNeighbours % 2 == 1)
                p1_turbine1power += additionalPower;                                                            // Если двигатель граничит с первой турбиной, то добавляем коэффициент к первой турбине
            if (turbineNeighbours / 2 == 1)
                p1_turbine2power += additionalPower;                                                            // Если двигатель граничит со второй турбиной, то добавляем коэффициент ко второй турбине

        }

        WeldJointDef jointDef = new WeldJointDef();

        for (int j = 0; j < FIELD_HEIGHT; j++) {
            for (int i = 1; i < FIELD_WIDTH; i++) {
                if (player1ship[i][j]!=0&&player1ship[i-1][j]!=0) {
                    jointDef.initialize(Bodies1[i][j], Bodies1[i-1][j], new Vector2((float)((10 + 7+7 +3.5)*SCALE/0.02),(float)((40 - 7 -3.5)*SCALE/0.02)));
                    world.createJoint(jointDef);
                }
            }
        }
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 1; j < FIELD_HEIGHT; j++) {
                if (player1ship[i][j] != 0&&player1ship[i][j-1]!=0) {
                    jointDef.initialize(Bodies1[i][j], Bodies1[i][j-1], new Vector2((float)((10 + 7+7 +3.5)*SCALE/0.02),(float)((40 - 7 -3.5)*SCALE/0.02)));
                    world.createJoint(jointDef);
                }
            }
        }

        k=0;
        f=0;
        for (int j = 0; j < FIELD_HEIGHT; j++) {
            for (int i = 0; i < FIELD_WIDTH; i++) {
                for (int t = 1; t < 9; t++){
                    if (player2ship[i][j]%10 == t){
                        String name = blockNames[t-1];
                        if (player2ship[i][j]/10 == 1){name+="90";}
                        if (player2ship[i][j]/10 == 2){name+="180";}
                        if (player2ship[i][j]/10 == 3){name+="270";}

                        float x = (float) ((210+i*7)*SCALE/0.02);
                        float y = (float) ((190 -j*7)*SCALE/0.02);

                        //turbine
                        if (player2ship[i][j]%10 == 3){
                            if (k==1){secondplayerturbine2_I = i;secondplayerturbine2_J = j;}
                            if (k==0){secondplayerturbine1_I = i;secondplayerturbine1_J = j;k+=1;}

                            if (player2ship[i][j]/10 == 0){x = (float) ((210+i*7 - 4.5)*SCALE/0.02);}
                            if (player2ship[i][j]/10 == 1){y = (float) ((190-j*7 - 4.5)*SCALE/0.02);}
                        }

                        //gunone
                        if (player2ship[i][j]%10 == 6){
                            secondplayergun1_I = i;
                            secondplayergun1_J = j;

                            if (player2ship[i][j]/10 == 0){y = (float) ((190 -j*7 +1.2)*SCALE/0.02);}
                            if (player2ship[i][j]/10 == 1){x = (float) ((210+i*7+1.2)*SCALE/0.02);}
                            if (player2ship[i][j]/10 == 2){
                                x = (float) ((210+i*7 - 8.2)*SCALE/0.02);
                                y = (float) ((190 -j*7 +1.2)*SCALE/0.02);
                            }
                            if (player2ship[i][j]/10 == 3){
                                x = (float) ((210+i*7 +1.2)*SCALE/0.02);
                                y = (float) ((190 -j*7 -8.2)*SCALE/0.02);
                            }
                        }

                        //guntwo
                        if (player2ship[i][j]%10 == 7){
                            if (f==1){secondplayergun2_2_I = i;secondplayergun2_2_J = j;}
                            if (f==0){secondplayergun2_1_I = i;secondplayergun2_1_J = j;f+=1;}

                            if (player2ship[i][j]/10 == 0){y = (float) ((190 -j*7 +0.1)*SCALE/0.02);}
                            if (player2ship[i][j]/10 == 1){x = (float) ((210+i*7+0.1)*SCALE/0.02);}
                            if (player2ship[i][j]/10 == 2){
                                x = (float) ((210+i*7 - 8.2)*SCALE/0.02);
                                y = (float) ((190 -j*7 +0.1)*SCALE/0.02);
                            }
                            if (player2ship[i][j]/10 == 3){
                                x = (float) ((210+i*7 +0.1)*SCALE/0.02);
                                y = (float) ((190 -j*7 -8.2)*SCALE/0.02);
                            }
                        }

                        namesOfBodies2[i][j] = name;
                        Bodies2[i][j]=createBody(name, x, y, 0);
                        Bodies2[i][j].setBullet(true);
                    }
                }

            }
        }

        jointDef = new WeldJointDef();

        for (int j = 0; j < FIELD_HEIGHT; j++) {
            for (int i = 1; i < FIELD_WIDTH; i++) {
                if (player2ship[i][j]!=0&&player2ship[i-1][j]!=0) {
                    jointDef.initialize(Bodies2[i][j], Bodies2[i-1][j], new Vector2((float)((150+ 7+7 +3.5)*SCALE/0.02),(float)((120 - 7 -3.5)*SCALE/0.02)));
                    world.createJoint(jointDef);
                }
            }
        }
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 1; j < FIELD_HEIGHT; j++) {
                if (player2ship[i][j] != 0&&player2ship[i][j-1]!=0) {
                    jointDef.initialize(Bodies2[i][j], Bodies2[i][j-1], new Vector2((float)((150 + 7+7 +3.5)*SCALE/0.02),(float)((120- 7 -3.5)*SCALE/0.02)));
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
            if ((x>180*SCALE/0.02)&&(y>120*SCALE/0.01)){
                x -=50*(float)(SCALE/0.01);
                y -=50*(float)(SCALE/0.01);
            }
            this.meteorNames[i] = name;
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


        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if (player1ship[i][j]!=0) {
                    Body body = Bodies1[i][j];
                    String name = namesOfBodies1[i][j];

                    Vector2 position = body.getPosition();
                    float degrees = (float) Math.toDegrees(body.getAngle());
                    drawSprite(name, position.x, position.y, degrees);
                }
            }
        }
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if (player2ship[i][j]!=0) {
                    Body body = Bodies2[i][j];
                    String name = namesOfBodies2[i][j];

                    Vector2 position = body.getPosition();
                    float degrees = (float) Math.toDegrees(body.getAngle());
                    drawSprite(name, position.x, position.y, degrees);
                }
            }
        }
        for (int i = 0; i < meteorBodies.length; i++) {
            Body body = meteorBodies[i];
            String name = meteorNames[i];

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

        drawSprite("buttonturbine",1,1,10,10,0);
        drawSprite("buttonfire",1,20,10,10,0);
        drawSprite("buttonturbine",1, 39,10,10,0);
        drawSprite("buttonturbine",66,11,10,10,180);
        drawSprite("buttonfire",66,31,10,10,180);
        drawSprite("buttonturbine",66, 49,10,10,180);


        batch.end();

        // uncomment to show the polygons
        // debugRenderer.render(world, camera.combined);

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
        for (Body ground : gameFieldBounds)
            if (ground != null)
                world.destroyBody(ground);

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

        for (int i = 0; i < 4; i++) {
            gameFieldBounds[i] = world.createBody(bodyDef);
            gameFieldBounds[i].createFixture((i < 2)? fixtureDef : fixtureDef1);
            gameFieldBounds[i].setTransform((i == 3)? camera.viewportWidth : 0, (i == 0)? camera.viewportHeight : 0, 0);
        }

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
    public boolean touchDown(int x, int y, int pointer, int button) {
        int h = Gdx.graphics.getHeight()/50;
        //turbine
        if (firstplayerturbine1_I>-1) {
            Body body1 = Bodies1[firstplayerturbine1_I][firstplayerturbine1_J];
            float cos1 = (float) Math.cos(body1.getAngle());
            float sin1 = (float) Math.sin(body1.getAngle());
            for (int i=1;i<4;i++){
            if(player1ship[firstplayerturbine1_I][firstplayerturbine1_J]/10 == i){
                cos1 = (float) Math.cos(body1.getAngle()+i*Math.PI/2);
                sin1 = (float) Math.sin(body1.getAngle()+i*Math.PI/2);
            }}

            if ((x-6*h)*(x-6*h)+(y-6*h)*(y-6*h)<=25*h*h) {
                Bodies[firstplayerturbine1_I][firstplayerturbine1_J].applyForceToCenter(15000 * cos1 * p1_turbine1power, 15000 * sin1 * p1_turbine1power, true);

            }
        }

        if (firstplayerturbine2_I>-1) {
            Body body2 = Bodies1[firstplayerturbine2_I][firstplayerturbine2_J];
            float cos2 = (float) Math.cos(body2.getAngle());
            float sin2 = (float) Math.sin(body2.getAngle());
            for (int i=1;i<4;i++){
                if(player1ship[firstplayerturbine2_I][firstplayerturbine2_J]/10 == i){
                    cos2 = (float) Math.cos(body2.getAngle()+i*Math.PI/2);
                    sin2 = (float) Math.sin(body2.getAngle()+i*Math.PI/2);
                }}
            if ((x-6*h)*(x-6*h)+(y-44*h)*(y-44*h)<=25*h*h) {
                Bodies[firstplayerturbine2_I][firstplayerturbine2_J].applyForceToCenter(15000 * cos2 * p1_turbine2power, 15000 * sin2 * p1_turbine2power, true);

            }
        }

        //gunone
        if (firstplayergun1_I>-1) {
            Body body3 = Bodies1[firstplayergun1_I][firstplayergun1_J];
            float cos3;float sin3;float cos3alpha;float sin3alpha;
            for (int i = 0; i < 4; i++) {
                if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == i) {
                    cos3 = (float) Math.cos(body3.getAngle() + i * Math.PI/2);
                    sin3 = (float) Math.sin(body3.getAngle() + i * Math.PI/2);

                    if ((x-6*h)*(x-6*h)+(y-25*h)*(y-25*h)<=25*h*h) {
                        Body bullet = createBody("bullet", 0, 0, body3.getAngle());
                        if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == 0){
                            float alpha = (float) (Math.atan(0.1));
                            cos3alpha = (float) Math.cos(body3.getAngle() + alpha);
                            sin3alpha = (float) Math.sin(body3.getAngle() + alpha);
                            bullet.setTransform(body3.getPosition().x + 3.8f*cos3alpha, body3.getPosition().y + 3.8f*sin3alpha, body3.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == 1){
                            cos3alpha = (float) Math.cos(body3.getAngle() +Math.asin(0.985));
                            sin3alpha = (float) Math.sin(body3.getAngle() +Math.asin(0.985));
                            bullet.setTransform(body3.getPosition().x + 3.8f*cos3alpha, body3.getPosition().y + 3.8f*sin3alpha, body3.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == 2){
                            cos3alpha = (float) Math.cos(body3.getAngle() + Math.PI/2);
                            sin3alpha = (float) Math.sin(body3.getAngle() + Math.PI/2);
                            bullet.setTransform(body3.getPosition().x + 0.6f*cos3alpha, body3.getPosition().y + 0.6f*sin3alpha, body3.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun1_I][firstplayergun1_J] / 10 == 3){
                            cos3alpha = (float) Math.cos(body3.getAngle());
                            sin3alpha = (float) Math.sin(body3.getAngle());
                            bullet.setTransform(body3.getPosition().x + 0.35f*cos3alpha, body3.getPosition().y + 0.35f*sin3alpha, body3.getAngle()+(float) Math.PI/2*i);
                        }

                        bullet.setLinearVelocity(new Vector2(body3.getLinearVelocity().x + 1000 * (float) Math.cos(body3.getAngle() + i*Math.PI/2), body3.getLinearVelocity().y + 1000 * (float) Math.sin(body3.getAngle() + i*Math.PI/2)));
                        bullets.add(bullet);
                        Bodies1[firstplayergun1_I][firstplayergun1_J].applyForceToCenter(-1000 * cos3, -1000 * sin3, true);
                    }
                }
            }
        }

        //guntwo1
        if (firstplayergun2_1_I>-1) {
            Body body4 = Bodies1[firstplayergun2_1_I][firstplayergun2_1_J];
            float cos3;float sin3;float cos3alpha;float sin3alpha;
            for (int i = 0; i < 4; i++) {
                if (player1ship[firstplayergun2_1_I][firstplayergun2_1_J] / 10 == i) {
                    cos3 = (float) Math.cos(body4.getAngle() + i * Math.PI/2);
                    sin3 = (float) Math.sin(body4.getAngle() + i * Math.PI/2);

                    if ((x-6*h)*(x-6*h)+(y-25*h)*(y-25*h)<=25*h*h) {
                        Body bullet = createBody("bullet", 0, 0, body4.getAngle());
                        if (player1ship[firstplayergun2_1_I][firstplayergun2_1_J] / 10 == 0){
                            float alpha = (float) (Math.atan(0.195));
                            cos3alpha = (float) Math.cos(body4.getAngle() + alpha);
                            sin3alpha = (float) Math.sin(body4.getAngle() + alpha);
                            bullet.setTransform(body4.getPosition().x + 3.8f*cos3alpha, body4.getPosition().y + 3.8f*sin3alpha, body4.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun2_1_I][firstplayergun2_1_J] / 10 == 1){
                            cos3alpha = (float) Math.cos(body4.getAngle() +Math.asin(0.975));
                            sin3alpha = (float) Math.sin(body4.getAngle() +Math.asin(0.975));
                            bullet.setTransform(body4.getPosition().x + 3.8f*cos3alpha, body4.getPosition().y + 3.8f*sin3alpha, body4.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun2_1_I][firstplayergun2_1_J] / 10 == 2){
                            cos3alpha = (float) Math.cos(body4.getAngle() + Math.PI/2);
                            sin3alpha = (float) Math.sin(body4.getAngle() + Math.PI/2);
                            bullet.setTransform(body4.getPosition().x + 0.92f*cos3alpha, body4.getPosition().y + 0.92f*sin3alpha, body4.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun2_1_I][firstplayergun2_1_J] / 10 == 3){
                            cos3alpha = (float) Math.cos(body4.getAngle());
                            sin3alpha = (float) Math.sin(body4.getAngle());
                            bullet.setTransform(body4.getPosition().x + 0.65f*cos3alpha, body4.getPosition().y + 0.65f*sin3alpha, body4.getAngle()+(float) Math.PI/2*i);
                        }

                        bullet.setLinearVelocity(new Vector2(body4.getLinearVelocity().x + 500 * (float) Math.cos(body4.getAngle() + i*Math.PI/2), body4.getLinearVelocity().y + 500 * (float) Math.sin(body4.getAngle() + i*Math.PI/2)));
                        bullets.add(bullet);
                        Bodies1[firstplayergun2_1_I][firstplayergun2_1_J].applyForceToCenter(-500 * cos3, -500 * sin3, true);
                    }
                }
            }
        }
        //guntwo2
        if (firstplayergun2_2_I>-1) {
            Body body4 = Bodies1[firstplayergun2_2_I][firstplayergun2_2_J];
            float cos3;float sin3;float cos3alpha;float sin3alpha;
            for (int i = 0; i < 4; i++) {
                if (player1ship[firstplayergun2_2_I][firstplayergun2_2_J] / 10 == i) {
                    cos3 = (float) Math.cos(body4.getAngle() + i * Math.PI/2);
                    sin3 = (float) Math.sin(body4.getAngle() + i * Math.PI/2);

                    if ((x-6*h)*(x-6*h)+(y-25*h)*(y-25*h)<=25*h*h) {
                        Body bullet = createBody("bullet", 0, 0, body4.getAngle());
                        if (player1ship[firstplayergun2_2_I][firstplayergun2_2_J] / 10 == 0){
                            float alpha = (float) (Math.atan(0.195));
                            cos3alpha = (float) Math.cos(body4.getAngle() + alpha);
                            sin3alpha = (float) Math.sin(body4.getAngle() + alpha);
                            bullet.setTransform(body4.getPosition().x + 3.8f*cos3alpha, body4.getPosition().y + 3.8f*sin3alpha, body4.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun2_2_I][firstplayergun2_2_J] / 10 == 1){
                            cos3alpha = (float) Math.cos(body4.getAngle() +Math.asin(0.975));
                            sin3alpha = (float) Math.sin(body4.getAngle() +Math.asin(0.975));
                            bullet.setTransform(body4.getPosition().x + 3.8f*cos3alpha, body4.getPosition().y + 3.8f*sin3alpha, body4.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun2_2_I][firstplayergun2_2_J] / 10 == 2){
                            cos3alpha = (float) Math.cos(body4.getAngle() + Math.PI/2);
                            sin3alpha = (float) Math.sin(body4.getAngle() + Math.PI/2);
                            bullet.setTransform(body4.getPosition().x + 0.92f*cos3alpha, body4.getPosition().y + 0.92f*sin3alpha, body4.getAngle()+(float) Math.PI/2*i);
                        }
                        if (player1ship[firstplayergun2_2_I][firstplayergun2_2_J] / 10 == 3){
                            cos3alpha = (float) Math.cos(body4.getAngle());
                            sin3alpha = (float) Math.sin(body4.getAngle());
                            bullet.setTransform(body4.getPosition().x + 0.65f*cos3alpha, body4.getPosition().y + 0.65f*sin3alpha, body4.getAngle()+(float) Math.PI/2*i);
                        }

                        bullet.setLinearVelocity(new Vector2(body4.getLinearVelocity().x + 500 * (float) Math.cos(body4.getAngle() + i*Math.PI/2), body4.getLinearVelocity().y + 500 * (float) Math.sin(body4.getAngle() + i*Math.PI/2)));
                        bullets.add(bullet);
                        Bodies1[firstplayergun2_2_I][firstplayergun2_2_J].applyForceToCenter(-500 * cos3, -500 * sin3, true);
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
