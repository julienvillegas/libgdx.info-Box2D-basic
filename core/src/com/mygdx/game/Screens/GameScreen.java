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

    private static final float UNIT_SIZE = SCREEN_HEIGHT / 50f;
    private static final float WIDTH_IN_UNITS = SCREEN_WIDTH / UNIT_SIZE;
    private static final float HEIGHT_IN_UNITS = 50f;
    private static final float BUTTON_RADIUS = 6f;

    private static final float ATAN_0195 = (float) Math.atan(0.195);
    private static final float ATAN_01 = (float) Math.atan(0.1);
    private static final float ASIN_0975 = (float) Math.asin(0.975);
    private static final float ASIN_0985 = (float) Math.asin(0.985);

    private static final int BTN_P1_LEFTTURBINE = 0;
    private static final int BTN_P1_GUN = 1;
    private static final int BTN_P1_RIGHTTURBINE = 2;
    private static final int BTN_P2_LEFTTURBINE = 3;
    private static final int BTN_P2_GUN = 4;
    private static final int BTN_P2_RIGHTTURBINE = 5;

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

    private Body[][] p1_bodies = new Body[FIELD_WIDTH][FIELD_HEIGHT];
    private Body[][] p2_bodies = new Body[FIELD_WIDTH][FIELD_HEIGHT];
    private String[][] p1_namesOfBodies = new String[FIELD_WIDTH][FIELD_HEIGHT];
    private String[][] p2_namesOfBodies = new String[FIELD_WIDTH][FIELD_HEIGHT];
    private int [][] p1_ship = new int[FIELD_WIDTH][FIELD_HEIGHT];
    private int [][] p2_ship = new int[FIELD_WIDTH][FIELD_HEIGHT];


    private int p1_turb1_I = -1, p1_turb1_J = -1;
    private int p1_turb2_I = -1, p1_turb2_J = -1;
    private int p1_steelGun_I = -1, p1_steelGun_J = -1;
    private int p1_wGun1_I = -1, p1_wGun1_J = -1;
    private int p1_wGun2_I = -1, p1_wGun2_J = -1;

    private int p2_turb1_I = -1, p2_turb1_J = -1;
    private int p2_turb2_I = -1, p2_turb2_J = -1;
    private int p2_steelGun_I = -1, p2_steelGun_J = -1;
    private int p2_wGun1_I = -1, p2_wGun1_J = -1;
    private int p2_wGun2_I = -1, p2_wGun2_J = -1;


    private ArrayList<Integer> p1_enginesCoords = new ArrayList<Integer>();
    private float p1_turb1power = 0f;
    private float p1_turb2power = 0f;
    private ArrayList<Integer> p2_enginesCoords = new ArrayList<Integer>();
    private float p2_turb1power = 0f;
    private float p2_turb2power = 0f;

    private float p1_gunTimer = 0f;
    private float p2_gunTimer = 0f;



    private Body[] meteorBodies = new Body[COUNT];
    private String[] meteorNames = new String[COUNT];
    private ArrayList<Body> bullets = new ArrayList<Body>();

    GameScreen(
            int[][] player1ship,
            int[][] player2ship
    ){
        this.p1_ship = player1ship;
        this.p2_ship = shipRotate(player2ship);


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
        String[] blockNames = new String[]{"woodblock", "steelblock", "engine", "turbine", "halfwoodblock", "halfsteelblock", "guntwo", "gunone"};

        boolean turbExist = false;
        boolean wGunExist = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if (p1_ship[i][j] != NULL) {
                    int type = p1_ship[i][j] % 10;
                    int facing = p1_ship[i][j] / 10 * 10;

                    String name = blockNames[type];
                    switch (facing) {
                        case UP: name += "90"; break;
                        case LEFT: name += "180"; break;
                        case DOWN: name += "270"; break;
                    }

                    float x = getXOnField(p1_ship[i][j], i, 15);
                    float y = getYOnField(p1_ship[i][j], j, 50);

                    if (type == TURBINE) {
                        if (turbExist) {
                            p1_turb2_I = i; p1_turb2_J = j;
                        }
                        else {
                            p1_turb1_I = i; p1_turb1_J = j;
                            turbExist = true;
                        }
                    }

                    if (type == ENGINE) {
                        p1_enginesCoords.add(i);
                        p1_enginesCoords.add(j);
                    }

                    if (type == STEEL_GUN) {
                        p1_steelGun_I = i;
                        p1_steelGun_J = j;
                    }

                    if (type == WOOD_GUN) {
                        if (wGunExist) {
                            p1_wGun2_I = i; p1_wGun2_J = j;
                        }
                        else {
                            p1_wGun1_I = i; p1_wGun1_J = j;
                            wGunExist = true;
                        }
                    }

                    p1_namesOfBodies[i][j] = name;
                    p1_bodies[i][j] = createBody(name, x, y, 0);
                    p1_bodies[i][j].setBullet(true);
                }
            }
        }

        int xCorner = 355 - FIELD_WIDTH*7 - 15;
        int yCorner = 270 - FIELD_HEIGHT*7 - 50;
        turbExist = false;
        wGunExist = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if (p2_ship[i][j] != NULL) {
                    int type = p2_ship[i][j] % 10;
                    int facing = p2_ship[i][j] / 10 * 10;

                    String name = blockNames[type];
                    switch (facing) {
                        case UP: name += "90"; break;
                        case LEFT: name += "180"; break;
                        case DOWN: name += "270"; break;
                    }

                    float x = getXOnField(p2_ship[i][j], i, xCorner);
                    float y = getYOnField(p2_ship[i][j], j, yCorner);

                    if (type == TURBINE) {
                        if (turbExist) {
                            p2_turb1_I = i; p2_turb1_J = j;
                        }
                        else {
                            p2_turb2_I = i; p2_turb2_J = j;
                            turbExist = true;
                        }
                    }

                    if (type == ENGINE) {
                        p2_enginesCoords.add(i);
                        p2_enginesCoords.add(j);
                    }

                    if (type == STEEL_GUN) {
                        p2_steelGun_I = i;
                        p2_steelGun_J = j;
                    }

                    if (type == WOOD_GUN) {
                        if (wGunExist) {
                            p2_wGun2_I = i; p2_wGun2_J = j;
                        }
                        else {
                            p2_wGun1_I = i; p2_wGun1_J = j;
                            wGunExist = true;
                        }
                    }

                    p2_namesOfBodies[i][j] = name;
                    p2_bodies[i][j] = createBody(name, x, y, 0);
                    p2_bodies[i][j].setBullet(true);
                }
            }
        }


        for (int i = 0; i < p1_enginesCoords.size(); i += 2) {

            int I = p1_enginesCoords.get(i);
            int J = p1_enginesCoords.get(i + 1);

            int turbineNeighbours = 0;                                                                              // Тут хранятся данные о граничащих с двигателем турбинах

            if (Math.abs(p1_turb1_I - I) + Math.abs(p1_turb1_J - J) == 1)
                turbineNeighbours += 1;                                                                             // Если двигатель граничит с первой турбиной, то turbineNeighbours % 2 == 1
            if (Math.abs(p1_turb2_I - I) + Math.abs(p1_turb2_J - J) == 1)
                turbineNeighbours += 2;                                                                             // Если двигатель граничит со второй турбиной, то turbineNeighbours / 2 == 1

            float additionalPower = 54000f / (float) (turbineNeighbours / 2 + turbineNeighbours % 2);               // Добавочный коэффициент силы турбин равен (54000 / КОЛИЧЕСТВО_ТУРБИН)
            if (turbineNeighbours % 2 == 1)
                p1_turb1power += additionalPower;                                                                   // Если двигатель граничит с первой турбиной, то добавляем коэффициент к первой турбине
            if (turbineNeighbours / 2 == 1)
                p1_turb2power += additionalPower;                                                                   // Если двигатель граничит со второй турбиной, то добавляем коэффициент ко второй турбине

        }

        for (int i = 0; i < p2_enginesCoords.size(); i += 2) {

            int I = p2_enginesCoords.get(i);
            int J = p2_enginesCoords.get(i + 1);

            int turbineNeighbours = 0;                                                                              // Тут хранятся данные о граничащих с двигателем турбинах

            if (Math.abs(p2_turb1_I - I) + Math.abs(p2_turb1_J - J) == 1)
                turbineNeighbours += 1;                                                                             // Если двигатель граничит с первой турбиной, то turbineNeighbours % 2 == 1
            if (Math.abs(p2_turb2_I - I) + Math.abs(p2_turb2_J - J) == 1)
                turbineNeighbours += 2;                                                                             // Если двигатель граничит со второй турбиной, то turbineNeighbours / 2 == 1

            float additionalPower = 54000f / (float) (turbineNeighbours / 2 + turbineNeighbours % 2);               // Добавочный коэффициент силы турбин равен (54000 / КОЛИЧЕСТВО_ТУРБИН)
            if (turbineNeighbours % 2 == 1)
                p2_turb1power += additionalPower;                                                                   // Если двигатель граничит с первой турбиной, то добавляем коэффициент к первой турбине
            if (turbineNeighbours / 2 == 1)
                p2_turb2power += additionalPower;                                                                   // Если двигатель граничит со второй турбиной, то добавляем коэффициент ко второй турбине

        }


        WeldJointDef jointDef = new WeldJointDef();

        for (int i = 1; i < FIELD_WIDTH; i++)
            for (int j = 0; j < FIELD_HEIGHT; j++)
                if (p1_ship[i][j] != NULL && p1_ship[i-1][j] != NULL) {
                    if (canBeJoined(p1_ship[i][j], LEFT) && canBeJoined(p1_ship[i-1][j], RIGHT)) {
                        jointDef.initialize(p1_bodies[i][j], p1_bodies[i - 1][j], new Vector2((float) ((15 + 17.5) * SCALE / 0.02), (float) ((50 - 10.5) * SCALE / 0.02)));
                        world.createJoint(jointDef);
                    }
                }

        for (int i = 0; i < FIELD_WIDTH; i++)
            for (int j = 1; j < FIELD_HEIGHT; j++)
                if (p1_ship[i][j] != NULL && p1_ship[i][j-1] != NULL) {
                    if (canBeJoined(p1_ship[i][j], UP) && canBeJoined(p1_ship[i][j-1], DOWN)) {
                        jointDef.initialize(p1_bodies[i][j], p1_bodies[i][j - 1], new Vector2((float) ((15 + 17.5) * SCALE / 0.02), (float) ((50 - 10.5) * SCALE / 0.02)));
                        world.createJoint(jointDef);
                    }
                }

        jointDef = new WeldJointDef();

        for (int i = 1; i < FIELD_WIDTH; i++)
            for (int j = 0; j < FIELD_HEIGHT; j++)
                if (p2_ship[i][j] != NULL && p2_ship[i-1][j] != NULL) {
                    if (canBeJoined(p2_ship[i][j], LEFT) && canBeJoined(p2_ship[i-1][j], RIGHT)) {
                        jointDef.initialize(p2_bodies[i][j], p2_bodies[i - 1][j], new Vector2((float) ((xCorner + 17.5) * SCALE / 0.02), (float) ((yCorner - 10.5) * SCALE / 0.02)));
                        world.createJoint(jointDef);
                    }
                }

        for (int i = 0; i < FIELD_WIDTH; i++)
            for (int j = 1; j < FIELD_HEIGHT; j++)
                if (p2_ship[i][j] != NULL && p2_ship[i][j-1] != NULL) {
                    if (canBeJoined(p2_ship[i][j], UP) && canBeJoined(p2_ship[i][j-1], DOWN)) {
                        jointDef.initialize(p2_bodies[i][j], p2_bodies[i][j - 1], new Vector2((float) ((xCorner + 17.5) * SCALE / 0.02), (float) ((yCorner - 10.5) * SCALE / 0.02)));
                        world.createJoint(jointDef);
                    }
                }
    }

    private void generateMeteors() {
        String[] meteorNames = new String[]{"meteorthree", "meteorfour", "meteorfive","meteortwo","meteortwo1","meteortwo2","meteortwo3","meteortwo4",
                "meteorone","meteorone1","meteorone2","meteorone3"};

        Random random = new Random();

        for (int i = 0; i < meteorBodies.length; i++) {
            String name = meteorNames[random.nextInt(meteorNames.length)];

            float x = (random.nextFloat() * 150) * (float) (SCALE/0.01);
            float y = (random.nextFloat() * 80) * (float) (SCALE/0.01);
            if ((x < 50*SCALE/0.02) && (y < 50*SCALE/0.01)) {
                x += 50 * (float) (SCALE/0.01);
                y += 50 * (float) (SCALE/0.01);
            }
            if ((x > 180*SCALE/0.02) && (y > 120*SCALE/0.01)) {
                x -= 50* (float) (SCALE/0.01);
                y -= 50* (float) (SCALE/0.01);
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
                if (p1_ship[i][j] != NULL) {
                    Body body = p1_bodies[i][j];
                    String name = p1_namesOfBodies[i][j];

                    Vector2 position = body.getPosition();
                    float degrees = (float) Math.toDegrees(body.getAngle());
                    drawSprite(name, position.x, position.y, degrees);
                }
                if (p2_ship[i][j] != NULL) {
                    Body body = p2_bodies[i][j];
                    String name = p2_namesOfBodies[i][j];

                    Vector2 position = body.getPosition();
                    float degrees = (float) Math.toDegrees(body.getAngle());
                    drawSprite(name, position.x, position.y, degrees);
                }
            }
        }


        boolean[] pressedButtons = pressedButtons(readTouchPositions());

        if (pressedButtons[BTN_P1_LEFTTURBINE])
            if (p1_turb1_I != -1)
                workTurbine(1, 1, delta);

        if (pressedButtons[BTN_P1_RIGHTTURBINE])
            if (p1_turb2_I != -1)
                workTurbine(1, 2, delta);

        if (pressedButtons[BTN_P1_GUN]) {
            if (p1_gunTimer >= 0.2f) {
                p1_gunTimer = 0f;
                if (p1_steelGun_I != -1)
                    gunShot(1,1);
                if (p1_wGun1_I != -1)
                    gunShot(1,2);
                if (p1_wGun2_I != -1)
                    gunShot(1, 3);
            }
        }

        if (pressedButtons[BTN_P2_LEFTTURBINE])
            if (p2_turb1_I != -1)
                workTurbine(2, 1, delta);

        if (pressedButtons[BTN_P2_RIGHTTURBINE])
            if (p2_turb2_I != -1)
                workTurbine(2, 2, delta);

        if (pressedButtons[BTN_P2_GUN]) {
            if (p2_gunTimer >= 0.2f) {
                p2_gunTimer = 0f;
                if (p2_steelGun_I != -1)
                    gunShot(2,1);
                if (p2_wGun1_I != -1)
                    gunShot(2,2);
                if (p2_wGun2_I != -1)
                    gunShot(2, 3);
            }
        }


        for (int i = 0; i < meteorBodies.length; i++) {
            Body body = meteorBodies[i];
            String name = meteorNames[i];

            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSprite(name, position.x, position.y, degrees);
        }

        while (bullets.size() > 30) {
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

        drawSprite("buttonturbine",1,1,BUTTON_RADIUS*2,BUTTON_RADIUS*2,0);
        drawSprite("buttonfire",1,HEIGHT_IN_UNITS/2 - BUTTON_RADIUS,BUTTON_RADIUS*2,BUTTON_RADIUS*2,0);
        drawSprite("buttonturbine",1, HEIGHT_IN_UNITS - BUTTON_RADIUS*2 - 1,BUTTON_RADIUS*2,BUTTON_RADIUS*2,0);
        drawSprite("buttonturbine",WIDTH_IN_UNITS - 1,BUTTON_RADIUS*2 + 1,BUTTON_RADIUS*2,BUTTON_RADIUS*2,180);
        drawSprite("buttonfire",WIDTH_IN_UNITS - 1,HEIGHT_IN_UNITS/2 + BUTTON_RADIUS,BUTTON_RADIUS*2,BUTTON_RADIUS*2,180);
        drawSprite("buttonturbine",WIDTH_IN_UNITS - 1, HEIGHT_IN_UNITS - 1,BUTTON_RADIUS*2,BUTTON_RADIUS*2,180);


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

        if (p1_gunTimer < 0.2f)
            p1_gunTimer += delta;
        if (p2_gunTimer < 0.2f)
            p2_gunTimer += delta;
    }

    private void drawSprite(String name, float x, float y, float degrees) {
        Sprite sprite = sprites.get(name);
        sprite.setPosition(x, y);
        sprite.setRotation(degrees);
        sprite.draw(batch);
    }
    private void drawSprite(String name, float x, float y, float width, float height, float degrees){
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
        fixtureDef1.friction = 1;

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



    private static int[][] shipRotate(int[][] ship) {
        int[][] res = new int[ship.length][ship[0].length];
        int ID;

        for (int i = 0; i < ship.length; i++)
            for (int j = 0; j < ship[0].length; j++) {
                res[ship.length - i - 1][ship[0].length - j - 1] = ship[i][j];
                ID = ship[i][j] % 10;
                if ((ID == HALF_WOOD_BLOCK) || (ID == HALF_STEEL_BLOCK) || (ID == TURBINE) || (ID == WOOD_GUN) || (ID == STEEL_GUN)) {
                    switch (ship[i][j] / 10 * 10) {
                        case RIGHT:
                            res[ship.length - i - 1][ship[0].length - j - 1] += LEFT - RIGHT;
                            break;
                        case UP:
                            res[ship.length - i - 1][ship[0].length - j - 1] += DOWN - UP;
                            break;
                        case LEFT:
                            res[ship.length - i - 1][ship[0].length - j - 1] += RIGHT - LEFT;
                            break;
                        case DOWN:
                            res[ship.length - i - 1][ship[0].length - j - 1] += UP - DOWN;
                            break;
                    }
                }
            }

        return res;
    }


    private static float getXOnField(int ID, int i, int xCorner) {
        float x = (xCorner + i*7);
        switch (ID) {
            case TURBINE + RIGHT: {x -= 4.5f; break;}
            case STEEL_GUN + UP: {x += 0.1f; break;}
            case STEEL_GUN + LEFT: {x -= 8.2f; break;}
            case STEEL_GUN + DOWN: {x += 0.1f; break;}
            case WOOD_GUN + UP: {x += 1.2f; break;}
            case WOOD_GUN + LEFT: {x -= 8.2f; break;}
            case WOOD_GUN + DOWN: {x += 1.2f; break;}
        }
        return x * SCALE/0.02f;
    }
    private static float getYOnField(int ID, int j, int yCorner) {
        float y = (yCorner - j*7);
        switch (ID) {
            case TURBINE + UP: {y -= 4.5f; break;}
            case STEEL_GUN + RIGHT: {y += 0.1f; break;}
            case STEEL_GUN + LEFT: {y += 0.1f; break;}
            case STEEL_GUN + DOWN: {y -= 8.2f; break;}
            case WOOD_GUN + RIGHT: {y += 1.2f; break;}
            case WOOD_GUN + LEFT: {y += 1.2f; break;}
            case WOOD_GUN + DOWN: {y -= 8.2f; break;}
        }
        return y * SCALE/0.02f;
    }

    private static boolean canBeJoined(int ID, int side) {
        int item = ID % 10;
        int facing = ID / 10 * 10;
        switch (item) {
            case WOOD_BLOCK: case STEEL_BLOCK: case ENGINE:
                return true;
            case TURBINE:
                return (facing == RIGHT && side != LEFT) ||
                        (facing == UP && side != DOWN) ||
                        (facing == LEFT && side != RIGHT) ||
                        (facing == DOWN && side != UP);
            case WOOD_GUN: case STEEL_GUN:
                return (facing == RIGHT && side == LEFT) ||
                        (facing == UP && side == DOWN) ||
                        (facing == LEFT && side == RIGHT) ||
                        (facing == DOWN && side == UP);
            case HALF_WOOD_BLOCK: case HALF_STEEL_BLOCK:
                return (facing == RIGHT && (side == LEFT || side == DOWN)) ||
                        (facing == UP && (side == DOWN || side == RIGHT)) ||
                        (facing == LEFT && (side == RIGHT || side == UP)) ||
                        (facing == DOWN && (side == UP || side == LEFT));
        }
        return false;
    }


    private static ArrayList<Integer> readTouchPositions() {
        int cnt = 0;
        while (Gdx.input.isTouched(cnt)) cnt++;
        ArrayList<Integer> res = new ArrayList<Integer>();
        for (int i = 0; i < cnt; i++) {
            res.add(Gdx.input.getX(i));
            res.add(SCREEN_HEIGHT - Gdx.input.getY(i));
        }
        return res;
    }

    private static boolean isInCircle(int x, int y, float xCenter, float yCenter, float radius) {
        return (xCenter - x) * (xCenter - x) + (yCenter - y) * (yCenter - y) < radius * radius;
    }

    private static boolean[] pressedButtons(ArrayList<Integer> touchPos) {
        int x, y;
        boolean[] res = new boolean[6];
        for (int i = 0; i < 6; i++)
            res[i] = false;

        for (int i = 0; i < touchPos.size(); i += 2) {
            x = touchPos.get(i);
            y = touchPos.get(i + 1);
            if (isInCircle(x, y, (1 + BUTTON_RADIUS)*UNIT_SIZE, (HEIGHT_IN_UNITS - 1 - BUTTON_RADIUS)*UNIT_SIZE, (BUTTON_RADIUS + 0.8f)*UNIT_SIZE))
                res[BTN_P1_LEFTTURBINE] = true;
            else if (isInCircle(x, y, (1 + BUTTON_RADIUS)*UNIT_SIZE, (HEIGHT_IN_UNITS / 2)*UNIT_SIZE, (BUTTON_RADIUS + 0.8f)*UNIT_SIZE))
                res[BTN_P1_GUN] = true;
            else if (isInCircle(x, y, (1 + BUTTON_RADIUS)*UNIT_SIZE, (1 + BUTTON_RADIUS)*UNIT_SIZE, (BUTTON_RADIUS + 0.8f)*UNIT_SIZE))
                res[BTN_P1_RIGHTTURBINE] = true;
            else if (isInCircle(x, y, (WIDTH_IN_UNITS - 1 - BUTTON_RADIUS)*UNIT_SIZE, (1 + BUTTON_RADIUS)*UNIT_SIZE, (BUTTON_RADIUS + 0.8f)*UNIT_SIZE))
                res[BTN_P2_LEFTTURBINE] = true;
            else if (isInCircle(x, y, (WIDTH_IN_UNITS - 1 - BUTTON_RADIUS)*UNIT_SIZE, (HEIGHT_IN_UNITS / 2)*UNIT_SIZE, (BUTTON_RADIUS + 0.8f)*UNIT_SIZE))
                res[BTN_P2_GUN] = true;
            else if (isInCircle(x, y, (WIDTH_IN_UNITS - 1 - BUTTON_RADIUS)*UNIT_SIZE, (HEIGHT_IN_UNITS - 1 - BUTTON_RADIUS)*UNIT_SIZE, (BUTTON_RADIUS + 0.8f)*UNIT_SIZE))
                res[BTN_P2_RIGHTTURBINE] = true;
        }

        return res;
    }



    private void workTurbine(int player, int turbNum, float delta) {
        int i = 0, j = 0;
        int code = player * 2 + turbNum;
        switch (code) {
            case 3:  //p=1, turb=1
                i = p1_turb1_I; j = p1_turb1_J; break;
            case 4:  //p=1, turb=2
                i = p1_turb2_I; j = p1_turb2_J; break;
            case 5:  //p=2, turb=1
                i = p2_turb1_I; j = p2_turb1_J; break;
            case 6:  //p=2, turb=2
                i = p2_turb2_I; j = p2_turb2_J; break;
        }

        Body body = (player == 1)? p1_bodies[i][j] : p2_bodies[i][j];
        int rotate = (player == 1)? p1_ship[i][j] / 10 : p2_ship[i][j] / 10;
        float cos = (float) Math.cos(body.getAngle() + rotate * Math.PI / 2);
        float sin = (float) Math.sin(body.getAngle() + rotate * Math.PI / 2);
        switch (code) {
            case 3:  //p=1, turb=1
                p1_bodies[i][j].applyForceToCenter(cos * p1_turb1power * delta, sin * p1_turb1power * delta, true);
                break;
            case 4:  //p=1, turb=2
                p1_bodies[i][j].applyForceToCenter(cos * p1_turb2power * delta, sin * p1_turb2power * delta, true);
                break;
            case 5:  //p=2, turb=1
                p2_bodies[i][j].applyForceToCenter(cos * p2_turb1power * delta, sin * p2_turb1power * delta, true);
                break;
            case 6:  //p=2, turb=2
                p2_bodies[i][j].applyForceToCenter(cos * p2_turb2power * delta, sin * p2_turb2power * delta, true);
                break;
        }
    }

    private void gunShot(int player, int gunNum) {
        //gunNum = 1 - стальная пушка; 2 - 1-я деревянная; 3 - 2-я деревянная
        int i = 0, j = 0;
        int code = player * 3 + gunNum;
        switch (code) {
            case 4:   //p=1, gun=steel
                i = p1_steelGun_I; j = p1_steelGun_J; break;
            case 5:   //p=1, gun=wood1
                i = p1_wGun1_I; j = p1_wGun1_J; break;
            case 6:   //p=1, gun=wood2
                i = p1_wGun2_I; j = p1_wGun2_J; break;
            case 7:   //p=2, gun=steel
                i = p2_steelGun_I; j = p2_steelGun_J; break;
            case 8:   //p=2, gun=wood1
                i = p2_wGun1_I; j = p2_wGun1_J; break;
            case 9:   //p=2, gun=wood2
                i = p2_wGun2_I; j = p2_wGun2_J; break;
        }

        Body body = (player == 1)? p1_bodies[i][j] : p2_bodies[i][j];
        int rotate = (player == 1)? p1_ship[i][j] / 10 : p2_ship[i][j] / 10;
        float cos = (float) Math.cos(body.getAngle() + rotate * Math.PI/2);
        float sin = (float) Math.sin(body.getAngle() + rotate * Math.PI/2);
        float cosAlpha, sinAlpha;
        float atan = (gunNum == 1)? ATAN_0195 : ATAN_01;
        float asin = (gunNum == 1)? ASIN_0975 : ASIN_0985;
        float kf2 = (gunNum == 1)? 0.92f: 0.6f;
        float kf3 = (gunNum == 1)? 0.65f: 0.35f;
        int impulse = (gunNum == 1)? 1000 : 500;

        Body bullet = createBody("bullet", 0, 0, body.getAngle());
        if (rotate == 0) {
            cosAlpha = (float) Math.cos(body.getAngle() + atan);
            sinAlpha = (float) Math.sin(body.getAngle() + atan);
            bullet.setTransform(body.getPosition().x + 3.8f * cosAlpha, body.getPosition().y + 3.8f * sinAlpha, body.getAngle() + (float) Math.PI / 2 * rotate);
        }
        else if (rotate == 1) {
            cosAlpha = (float) Math.cos(body.getAngle() + asin);
            sinAlpha = (float) Math.sin(body.getAngle() + asin);
            bullet.setTransform(body.getPosition().x + 3.8f * cosAlpha, body.getPosition().y + 3.8f * sinAlpha, body.getAngle() + (float) Math.PI / 2 * rotate);
        }
        else if (rotate == 2) {
            cosAlpha = (float) Math.cos(body.getAngle() + Math.PI / 2);
            sinAlpha = (float) Math.sin(body.getAngle() + Math.PI / 2);
            bullet.setTransform(body.getPosition().x + kf2 * cosAlpha, body.getPosition().y + kf2 * sinAlpha, body.getAngle() + (float) Math.PI / 2 * rotate);
        }
        else if (rotate == 3) {
            cosAlpha = (float) Math.cos(body.getAngle());
            sinAlpha = (float) Math.sin(body.getAngle());
            bullet.setTransform(body.getPosition().x + kf3 * cosAlpha, body.getPosition().y + kf3 * sinAlpha, body.getAngle() + (float) Math.PI / 2 * rotate);
        }

        bullet.setLinearVelocity(new Vector2(body.getLinearVelocity().x + impulse * (float) Math.cos(body.getAngle() + rotate*Math.PI/2), body.getLinearVelocity().y + impulse * (float) Math.sin(body.getAngle() + rotate * Math.PI/2)));
        bullets.add(bullet);
        if (player == 1)
            p1_bodies[i][j].applyForceToCenter(-impulse * cos, -impulse * sin, true);
        else
            p2_bodies[i][j].applyForceToCenter(-impulse * cos, -impulse * sin, true);
    }

}
