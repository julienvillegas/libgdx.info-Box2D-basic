package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.mygdx.game.Bodies.BlockData;
import com.mygdx.game.Bodies.GameFont;
import com.mygdx.game.Extra.AssemblingScreenCoords;
import com.mygdx.game.Extra.FontID;
import com.mygdx.game.Extra.GameScreenCoords;
import com.mygdx.game.Extra.ItemID;
import com.mygdx.game.Extra.MathConsts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class GameScreen implements Screen, InputProcessor, ItemID, AssemblingScreenCoords, GameScreenCoords, MathConsts {



    public enum State {
        PAUSE,
        RUN,
        END
    }

    private State state = State.RUN;



    private static final float STEP_TIME = 1f / 60f;                                // Базовый интервал времени между кадрами (в сек)
    private static final int VELOCITY_ITERATIONS = 6;                               // ???
    private static final int POSITION_ITERATIONS = 2;                               // ???
    private static final float GUNSHOT_DELTA = 0.2f;                                // Интервал времени между выстрелами пушек при стрельбе очередью (в сек)
    private static final float MAX_RENDER_DELTA = 0.15f;                            // Максимальный интервал внутриигрового времени (в сек). То есть, если по каким-то причинам кадры в игре меняются через время, бОльшее, чем 0.15 сек, то игра считает, что прошло ровно 0.15 сек
    private static final float MAX_BULLET_LIFETIME = 7f;                            // Время жизни пули (в сек)
    private static final float MAX_GAMEPLAY_TIME = 180f;                            // Максимальное время игры (в сек)

    private static final int METEORS_COUNT = 30;                                    // Количество метеоритов на карте

    private static final float ENGINE_POWER = 70000f;                               // Мощность одного двигателя


    private static final int BTN_P1_LEFTTURBINE = 0;                                //
    private static final int BTN_P1_GUN = 1;                                        //
    private static final int BTN_P1_RIGHTTURBINE = 2;                               //      ID всех игровых кнопок (по 3 кнопки на каждого
    private static final int BTN_P2_LEFTTURBINE = 3;                                //                      из 2 игроков)
    private static final int BTN_P2_GUN = 4;                                        //
    private static final int BTN_P2_RIGHTTURBINE = 5;                               //

    private static final int bulletType = -1;



    private TextureAtlas textureAtlas;
    private TextureAtlas textureAtlas2;
    private SpriteBatch batch;
    private final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();


    private OrthographicCamera camera;
    private ExtendViewport viewport;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private PhysicsShapeCache physicsBodies;
    private float accumulator = 0f;
    private float gameplayTimer = 0f;                                                                                       // Время с начала запуска игры (в сек)

    private Body[] gameFieldBounds = new Body[4];                                                                           // Границы карты
    private int[][] SPAWN_CORNERS = {{7, 7*FIELD_HEIGHT},
            {(int) (WIDTH_IN_UNITS/SCALE*0.02f) - (FIELD_WIDTH+1)*7, (int) (HEIGHT_IN_UNITS/SCALE*0.02f)- 7*2}};            // Массив 2х2, хранит координаты спавна у первого корабля ([0][]) и второго корабля ([1][])

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
    private boolean p1_won = false;
    private boolean p2_won = false;


    private ArrayList<Integer> p1_enginesCoords = new ArrayList<Integer>();
    private float p1_turb1power = 0f;
    private float p1_turb2power = 0f;
    private ArrayList<Integer> p2_enginesCoords = new ArrayList<Integer>();
    private float p2_turb1power = 0f;
    private float p2_turb2power = 0f;

    private float p1_gunTimer = 0f;
    private float p2_gunTimer = 0f;


    private Game game;
    private ShipChoosingScreen shipChoosingScreen;

    private GameFont timerFont = new GameFont(FontID.Companion.getJURA_MEDIUM(), 4, new Color(0xffcccccc));


    private Body[] meteorBodies = new Body[METEORS_COUNT];
    private Body[] obstacleBodies = new Body[3];
    private String[] meteorNames = new String[METEORS_COUNT];
    private String[] obstacleNames = new String[3];
    private ArrayList<Body> bullets = new ArrayList<Body>();
    private ArrayList<Body> bullets2 = new ArrayList<Body>();

    GameScreen(
            ShipChoosingScreen shipChoosingScreen,
            Game game,
            int[][] player1ship,
            int[][] player2ship
    ){
        this.game = game;
        this.shipChoosingScreen = shipChoosingScreen;
        this.p1_ship = player1ship;
        this.p2_ship = player2ship;


        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(80, 80, camera);


        textureAtlas = new TextureAtlas("sprite.txt");
        //textureAtlas2 = new TextureAtlas("sprites2.txt");
        addSprites();

        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                BlockData dataA = (BlockData) bodyA.getUserData();
                BlockData dataB = (BlockData) bodyB.getUserData();
                if ((dataA.getType() >= 0) && (dataB.getType() == bulletType) && (!dataB.isBulletActivated())) {
                    float x = bodyA.getLinearVelocity().x;
                    float y = bodyB.getLinearVelocity().y;
                    if (x*x + y*y > 14*14) {
                        BD_getDamage(bodyA);
                        BD_activateBullet(bodyB);
                    }
                }
                if ((dataB.getType() >= 0) && (dataA.getType() == bulletType)&& (!dataA.isBulletActivated())) {
                    float x = bodyA.getLinearVelocity().x;
                    float y = bodyB.getLinearVelocity().y;
                    if (x*x + y*y > 14*14) {
                        BD_getDamage(bodyB);
                        BD_activateBullet(bodyA);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
        physicsBodies = new PhysicsShapeCache("physics.xml");

        generate();
        generateMeteors();
        generateObstacles();

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
        /*regions = textureAtlas2.getRegions();
        for (TextureAtlas.AtlasRegion region : regions) {
            Sprite sprite = textureAtlas2.createSprite(region.name);

            float width = sprite.getWidth() * SCALE;
            float height = sprite.getHeight() * SCALE;

            sprite.setSize(width, height);
            sprite.setOrigin(0, 0);

            sprites.put(region.name, sprite);
        }*/
    }

    private void generate() {
        String[] blockNames = new String[]{"woodblock", "steelblock", "engine", "turbine", "halfwoodblock", "halfsteelblock", "guntwo", "gunone","hero1","hero2"};

        boolean turbExist = false;
        boolean wGunExist = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if (p1_ship[i][j] != NULL) {
                    int type = p1_ship[i][j] % 10;
                    BlockData block = new BlockData(type);
                    int facing = p1_ship[i][j] / 10 * 10;

                    String name = blockNames[type];
                    switch (facing) {
                        case UP: name += "90"; break;
                        case LEFT: name += "180"; break;
                        case DOWN: name += "270"; break;
                    }

                    float x = getXOnField(p1_ship[i][j], i, SPAWN_CORNERS[0][0]);
                    float y = getYOnField(p1_ship[i][j], j, SPAWN_CORNERS[0][1]);

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
                    Gdx.app.log("Block","Block Hp: " +block.getHp() + "Block Type: " + block.getType());
                    p1_bodies[i][j].setUserData(block);
                }
            }
        }

        turbExist = false;
        wGunExist = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if (p2_ship[i][j] != NULL) {
                    int type = p2_ship[i][j] % 10;
                    BlockData block = new BlockData(type);
                    int facing = p2_ship[i][j] / 10 * 10;
                    if (type == EYE){
                        type = EYE2;
                    }
                    String name = blockNames[type];
                    switch (facing) {
                        case UP: name += "90"; break;
                        case LEFT: name += "180"; break;
                        case DOWN: name += "270"; break;
                    }

                    float x = getXOnField(p2_ship[i][j], i, SPAWN_CORNERS[1][0]);
                    float y = getYOnField(p2_ship[i][j], j, SPAWN_CORNERS[1][1]);
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
                    Gdx.app.log("Block","Block Hp: " +block.getHp() + "Block Type: " + block.getType());
                    p2_bodies[i][j].setUserData(block);
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

            float additionalPower = ENGINE_POWER / (float) (turbineNeighbours / 2 + turbineNeighbours % 2);       // Добавочный коэффициент силы турбин равен (T_M_P / КОЛИЧЕСТВО_ТУРБИН)
            if (turbineNeighbours % 2 == 1) {
                p1_turb1power += additionalPower;                                                                   // Если двигатель граничит с первой турбиной, то добавляем коэффициент к первой турбине
                BD_addEngineLabel(p1_bodies[I][J], 1);
            }
            if (turbineNeighbours / 2 == 1) {
                p1_turb2power += additionalPower;                                                                   // Если двигатель граничит со второй турбиной, то добавляем коэффициент ко второй турбине
                BD_addEngineLabel(p1_bodies[I][J], 2);
            }
        }

        for (int i = 0; i < p2_enginesCoords.size(); i += 2) {

            int I = p2_enginesCoords.get(i);
            int J = p2_enginesCoords.get(i + 1);

            int turbineNeighbours = 0;                                                                              // Тут хранятся данные о граничащих с двигателем турбинах

            if (Math.abs(p2_turb1_I - I) + Math.abs(p2_turb1_J - J) == 1)
                turbineNeighbours += 1;                                                                             // Если двигатель граничит с первой турбиной, то turbineNeighbours % 2 == 1
            if (Math.abs(p2_turb2_I - I) + Math.abs(p2_turb2_J - J) == 1)
                turbineNeighbours += 2;                                                                             // Если двигатель граничит со второй турбиной, то turbineNeighbours / 2 == 1

            float additionalPower = ENGINE_POWER / (float) (turbineNeighbours / 2 + turbineNeighbours % 2);       // Добавочный коэффициент силы турбин равен (T_M_P / КОЛИЧЕСТВО_ТУРБИН)
            if (turbineNeighbours % 2 == 1) {
                p2_turb1power += additionalPower;                                                                   // Если двигатель граничит с первой турбиной, то добавляем коэффициент к первой турбине
                BD_addEngineLabel(p2_bodies[I][J], 1);
            }
            if (turbineNeighbours / 2 == 1) {
                p2_turb2power += additionalPower;                                                                   // Если двигатель граничит со второй турбиной, то добавляем коэффициент ко второй турбине
                BD_addEngineLabel(p2_bodies[I][J], 2);
            }
        }


        WeldJointDef jointDef = new WeldJointDef();
        jointDef.frequencyHz = 0f;
        jointDef.dampingRatio = 0f;

        for (int i = 0; i < FIELD_WIDTH; i++)
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if (i != 0) {
                    if (p1_ship[i][j] != NULL && p1_ship[i - 1][j] != NULL)
                        if (canBeJoined(p1_ship[i][j], LEFT) && canBeJoined(p1_ship[i - 1][j], RIGHT)) {
                            jointDef.initialize(p1_bodies[i][j], p1_bodies[i - 1][j], new Vector2((float) ((15 + 17.5) * SCALE / 0.02), (float) ((50 - 10.5) * SCALE / 0.02)));
                            world.createJoint(jointDef);
                        }
                    if (p2_ship[i][j] != NULL && p2_ship[i - 1][j] != NULL)
                        if (canBeJoined(p2_ship[i][j], LEFT) && canBeJoined(p2_ship[i - 1][j], RIGHT)) {
                            jointDef.initialize(p2_bodies[i][j], p2_bodies[i - 1][j], new Vector2((float) ((SPAWN_CORNERS[1][0] + 17.5) * SCALE / 0.02), (float) ((SPAWN_CORNERS[1][1] - 10.5) * SCALE / 0.02)));
                            world.createJoint(jointDef);
                        }
                }
                if (j != 0) {
                    if (p1_ship[i][j] != NULL && p1_ship[i][j - 1] != NULL)
                        if (canBeJoined(p1_ship[i][j], UP) && canBeJoined(p1_ship[i][j - 1], DOWN)) {
                            jointDef.initialize(p1_bodies[i][j], p1_bodies[i][j - 1], new Vector2((float) ((15 + 17.5) * SCALE / 0.02), (float) ((50 - 10.5) * SCALE / 0.02)));
                            world.createJoint(jointDef);
                        }
                    if (p2_ship[i][j] != NULL && p2_ship[i][j - 1] != NULL)
                        if (canBeJoined(p2_ship[i][j], UP) && canBeJoined(p2_ship[i][j - 1], DOWN)) {
                            jointDef.initialize(p2_bodies[i][j], p2_bodies[i][j - 1], new Vector2((float) ((SPAWN_CORNERS[1][0] + 17.5) * SCALE / 0.02), (float) ((SPAWN_CORNERS[1][1] - 10.5) * SCALE / 0.02)));
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
            float[][] dangerRects = {{getXOnField(WOOD_BLOCK, -1, SPAWN_CORNERS[0][0]), getXOnField(WOOD_BLOCK, FIELD_WIDTH, SPAWN_CORNERS[0][0]), getYOnField(WOOD_BLOCK, FIELD_HEIGHT, SPAWN_CORNERS[0][1]), getYOnField(WOOD_BLOCK, -1, SPAWN_CORNERS[0][1])},
                    {getXOnField(WOOD_BLOCK, -1, SPAWN_CORNERS[1][0]), getXOnField(WOOD_BLOCK, FIELD_WIDTH, SPAWN_CORNERS[1][0]), getYOnField(WOOD_BLOCK, FIELD_HEIGHT, SPAWN_CORNERS[1][1]), getYOnField(WOOD_BLOCK, -1, SPAWN_CORNERS[1][1])}};
                    // Массив 2х4 вида {{minX, maxX, minY, maxY},{minX, maxX, minY, maxY}}; представляет из себя две зоны, где спавнятся корабли и где метеориты быть не должны

            int x, y;
            do {
                x = (int) (random.nextFloat() * (WIDTH_IN_UNITS / SCALE * 0.02 - 1) * SCALE / 0.02);
                y = (int) (random.nextFloat() * (HEIGHT_IN_UNITS / SCALE * 0.02 - 1) * SCALE / 0.02);
            } while (isInRect(x, y, dangerRects[0][0], dangerRects[0][1], dangerRects[0][2], dangerRects[0][3])
                    || isInRect(x, y, dangerRects[1][0], dangerRects[1][1], dangerRects[1][2], dangerRects[1][3]));

                this.meteorNames[i] = name;
                int scale = Math.abs((int)(random.nextFloat()*2+1));
                BlockData block = new BlockData(-scale-100);
                meteorBodies[i] = createBodyWH(name, x, y, 0,scale);
                meteorBodies[i].setUserData(block);
                meteorBodies[i].setLinearVelocity(new Vector2((random.nextFloat()-0.5f)*20,(random.nextFloat()-0.5f)*20));
            }

        }
    private void generateObstacles(){
       String[] obstacleNames = new String[]{"obstacle1","obstacle2","obstacle3"};
       for (int i=0;i<obstacleNames.length;i++){
           String name  = obstacleNames[i];
           this.obstacleNames[i] = name;
           int scale = 4;
           BlockData block = new BlockData(-scale-100);
           int x=0,y=0;
           if (i == 1){
               y=(int)(HEIGHT_IN_UNITS*0.85);
               x = (int)(HEIGHT_IN_UNITS*0.55);
           }
           if (i == 2){
               y=0;
               x = (int)(HEIGHT_IN_UNITS*0.4);
           }
           if (i ==0){
               y=(int)(HEIGHT_IN_UNITS*0.3);
               x = (int)((1 -0.088)*WIDTH_IN_UNITS);
           }
           obstacleBodies[i] = createBodyWH(name,x,y,0,scale);
           obstacleBodies[i].setBullet(true);
           obstacleBodies[i].setUserData(block);
           obstacleBodies[i].setAngularVelocity(10f);
       }
    }


    private Body createBody(String name, float x, float y, float rotation) {
        Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
        body.setTransform(x, y, rotation);
        return body;
    }
    private Body createBodyWH(String name, float x, float y, float rotation, float proportion) {
        Body body = physicsBodies.createBody(name, world, SCALE*proportion, SCALE*proportion);
        body.setTransform(x, y, rotation);
        return body;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();


        if (delta > MAX_RENDER_DELTA)
            delta = MAX_RENDER_DELTA;

        if (state == State.RUN)
            stepWorld(delta);




        drawSprite("background", 0, 0, camera.viewportWidth, camera.viewportHeight, 0);

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_HEIGHT; j++) {
                if (p1_bodies[i][j] != null) {
                    if (p1_bodies[i][j].getUserData() != null) {
                        BlockData block = (BlockData) p1_bodies[i][j].getUserData();
                        if (block.getHp() <= 0) {

                            for (JointEdge joint : p1_bodies[i][j].getJointList())
                                world.destroyJoint(joint.joint);

                            if (p1_ship[i][j] == EYE){
                                p2_won = true;
                                state = State.END;}

                            if (p1_ship[i][j] == ENGINE) {
                                ArrayList<Integer> labels = ((BlockData) p1_bodies[i][j].getUserData()).getEngineLabels();
                                for (int label : labels)
                                    switch (label) {
                                        case 1:
                                            p1_turb1power -= ENGINE_POWER / (float) (labels.size());
                                            break;
                                        case 2:
                                            p1_turb2power -= ENGINE_POWER / (float) (labels.size());
                                            break;
                                    }
                            }

                            if ((i == p1_turb1_I) && (j == p1_turb1_J)) {
                                p1_turb1_I = -1;
                                p1_turb1_J = -1;
                            }
                            if ((i == p1_turb2_I) && (j == p1_turb2_J)) {
                                p1_turb2_I = -1;
                                p1_turb2_J = -1;
                            }
                            if ((i == p1_steelGun_I) && (j == p1_steelGun_J)) {
                                p1_steelGun_I = -1;
                                p1_steelGun_J = -1;
                            }
                            if ((i == p1_wGun1_I) && (j == p1_wGun1_J)) {
                                p1_wGun1_I = -1;
                                p1_wGun1_J = -1;
                            }
                            if ((i == p1_wGun2_I) && (j == p1_wGun2_J)) {
                                p1_wGun2_I = -1;
                                p1_wGun2_J = -1;
                            }

                            world.destroyBody(p1_bodies[i][j]);
                            p1_bodies[i][j] = null;

                        } else {
                            Body body = p1_bodies[i][j];
                            String name = p1_namesOfBodies[i][j];


                            Vector2 position = body.getPosition();
                            float degrees = (float) Math.toDegrees(body.getAngle());
                            drawSprite(name, position.x, position.y, degrees);
                        }
                    }
                }
                if (p2_bodies[i][j] != null) {
                    if (p2_bodies[i][j].getUserData() != null) {
                        BlockData block = (BlockData) p2_bodies[i][j].getUserData();
                        if (block.getHp() <= 0) {

                            for (JointEdge joint : p2_bodies[i][j].getJointList())
                                world.destroyJoint(joint.joint);

                            if (p2_ship[i][j] == EYE){
                                p1_won = true;
                                state = State.END;}

                            if (p2_ship[i][j] == ENGINE) {
                                ArrayList<Integer> labels = ((BlockData) p2_bodies[i][j].getUserData()).getEngineLabels();
                                for (int label : labels)
                                    switch (label) {
                                        case 1:
                                            p2_turb1power -= ENGINE_POWER / (float) (labels.size());
                                            break;
                                        case 2:
                                            p2_turb2power -= ENGINE_POWER / (float) (labels.size());
                                            break;
                                    }
                            }
                            if ((i == p2_turb1_I) && (j == p2_turb1_J)) {
                                p2_turb1_I = -1;
                                p2_turb1_J = -1;
                            }
                            if ((i == p2_turb2_I) && (j == p2_turb2_J)) {
                                p2_turb2_I = -1;
                                p2_turb2_J = -1;
                            }
                            if ((i == p2_steelGun_I) && (j == p2_steelGun_J)) {
                                p2_steelGun_I = -1;
                                p2_steelGun_J = -1;
                            }
                            if ((i == p2_wGun1_I) && (j == p2_wGun1_J)) {
                                p2_wGun1_I = -1;
                                p2_wGun1_J = -1;
                            }
                            if ((i == p2_wGun2_I) && (j == p2_wGun2_J)) {
                                p2_wGun2_I = -1;
                                p2_wGun2_J = -1;
                            }

                            world.destroyBody(p2_bodies[i][j]);
                            p2_bodies[i][j] = null;
                        } else {
                            Body body = p2_bodies[i][j];
                            String name = p2_namesOfBodies[i][j];


                            Vector2 position = body.getPosition();
                            float degrees = (float) Math.toDegrees(body.getAngle());
                            drawSprite(name, position.x, position.y, degrees);
                        }
                    }
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
                    gunShot(1, 1);
                if (p1_wGun1_I != -1)
                    gunShot(1, 2);
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
                    gunShot(2, 1);
                if (p2_wGun1_I != -1)
                    gunShot(2, 2);
                if (p2_wGun2_I != -1)
                    gunShot(2, 3);
            }
        }


        for (int i = 0; i < meteorBodies.length; i++) {
            Body body = meteorBodies[i];
            String name = meteorNames[i];
            BlockData block  =(BlockData) body.getUserData();
            int scale = -block.getType() - 100;
            //Gdx.app.log("scale", scale+ "");
            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSpriteWH(name, position.x, position.y, degrees, scale);
        }

        for (int i = 0; i < obstacleBodies.length; i++) {
            Body body = obstacleBodies[i];
            String name = obstacleNames[i];
            BlockData block  =(BlockData) body.getUserData();
            int scale = -block.getType() - 100;
            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSpriteWH(name, position.x, position.y, degrees, scale);
        }

        while (bullets.size() > 30) {
            world.destroyBody(bullets.get(0));
            bullets.remove(0);
        }
        while (bullets2.size() > 30) {
            world.destroyBody(bullets2.get(0));
            bullets2.remove(0);
        }


        for (int i = 0; i < bullets.size(); i++) {

            Body body = bullets.get(i);
            String name = "bullet0";
            BlockData block = (BlockData) body.getUserData();
            if (block.isBulletActivated()) {
                name = "bullet";
            }
            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSprite(name, position.x, position.y, degrees);
            bullets.get(i).setUserData(block);
        }
        for (int i = 0; i < bullets2.size(); i++) {
            Body body = bullets2.get(i);
            String name = "bullet2";
            BlockData block = (BlockData) body.getUserData();
            if (block.isBulletActivated()) {
                name = "bullet";
            }
            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSprite(name, position.x, position.y, degrees);
            bullets2.get(i).setUserData(block);
        }

        if (p1_turb2_I != -1)
            if (p1_turb2power > 10)
                drawSprite("buttonturbine", 1, 1, BUTTON_RADIUS * 2, BUTTON_RADIUS * 2, 0);

        if ((p1_steelGun_I != -1) || (p1_wGun1_I != -1) || (p1_wGun2_I != -1))
            drawSprite("buttonfire", 1, HEIGHT_IN_UNITS / 2 - BUTTON_RADIUS, BUTTON_RADIUS * 2, BUTTON_RADIUS * 2, 0);

        if (p1_turb1_I != -1)
            if (p1_turb1power > 10)
                drawSprite("buttonturbine", 1, HEIGHT_IN_UNITS - BUTTON_RADIUS * 2 - 1, BUTTON_RADIUS * 2, BUTTON_RADIUS * 2, 0);

        if (p2_turb1_I != -1)
            if (p2_turb1power > 10)
                drawSprite("buttonturbine", WIDTH_IN_UNITS - 1, BUTTON_RADIUS * 2 + 1, BUTTON_RADIUS * 2, BUTTON_RADIUS * 2, 180);

        if ((p2_steelGun_I != -1) || (p2_wGun1_I != -1) || (p2_wGun2_I != -1))
            drawSprite("buttonfire", WIDTH_IN_UNITS - 1, HEIGHT_IN_UNITS / 2 + BUTTON_RADIUS, BUTTON_RADIUS * 2, BUTTON_RADIUS * 2, 180);

        if (p2_turb2_I != -1)
            if (p2_turb2power > 10)
                drawSprite("buttonturbine", WIDTH_IN_UNITS - 1, HEIGHT_IN_UNITS - 1, BUTTON_RADIUS * 2, BUTTON_RADIUS * 2, 180);



        if (gameplayTimer > MAX_GAMEPLAY_TIME) {
            state = State.END;
        } else {
            String time = getStringTimer(gameplayTimer);
            timerFont.getBF().draw(batch, time, WIDTH_IN_UNITS * 0.45f, HEIGHT_IN_UNITS - WIDTH_IN_UNITS * 0.08f);
        }

        switch (this.state) {
            case PAUSE:
                drawSprite("stop", PAUSE_BTN_DELTA_X, PAUSE_BTN_DELTA_Y, PAUSE_BTN_SIZE, PAUSE_BTN_SIZE, 0);
                drawSprite("pausescreen", PAUSE_SCREEN_DELTA_X, PAUSE_SCREEN_DELTA_Y, PAUSE_SCREEN_WIDTH, PAUSE_SCREEN_HEIGHT, 0);
                break;
            case END:
                if (p2_won){
                    drawSprite("player2won", PLAYER2WON_SCREEN_DELTA_X, PLAYER2WON_SCREEN_DELTA_Y, PLAYER2WON_SCREEN_WIDTH, PLAYER2WON_SCREEN_HEIGHT, 0);
                } else {
                    if (p1_won) {
                        drawSprite("player1won", PLAYER1WON_SCREEN_DELTA_X, PLAYER1WON_SCREEN_DELTA_Y, PLAYER1WON_SCREEN_WIDTH, PLAYER1WON_SCREEN_HEIGHT, 0);
                    } else {
                        drawSprite("timeisover", TIMEOUT_SCREEN_DELTA_X, TIMEOUT_SCREEN_DELTA_Y, TIMEOUT_SCREEN_WIDTH, TIMEOUT_SCREEN_HEIGHT, 0);
                    }
                }
                break;
            case RUN:
                drawSprite("pause", PAUSE_BTN_DELTA_X, PAUSE_BTN_DELTA_Y, PAUSE_BTN_SIZE, PAUSE_BTN_SIZE, 0);
                break;
        }

        //debugRenderer.render(world, camera.combined);
        batch.end();

    }

    private void stepWorld(float delta) {
        accumulator += delta;
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }

        if (gameplayTimer < MAX_GAMEPLAY_TIME)
            gameplayTimer += delta;

        if (p1_gunTimer < GUNSHOT_DELTA)
            p1_gunTimer += delta;
        if (p2_gunTimer < GUNSHOT_DELTA)
            p2_gunTimer += delta;

        for (int i = 0; i < bullets.size(); i++) {
            synchronized (bullets.get(i)) {
                BD_addLifetime(bullets.get(i), delta);
                if (((BlockData) bullets.get(i).getUserData()).getBulletLifetime() > MAX_BULLET_LIFETIME) {
                    world.destroyBody(bullets.get(i));
                    bullets.remove(i);
                }
            }
        }

        for (int i = 0; i < bullets2.size(); i++) {
            synchronized (bullets2.get(i)) {
                BD_addLifetime(bullets2.get(i), delta);
                if (((BlockData) bullets2.get(i).getUserData()).getBulletLifetime() > MAX_BULLET_LIFETIME) {
                    world.destroyBody(bullets2.get(i));
                    bullets2.remove(i);
                }
            }
        }
    }

    private void drawSprite(String name, float x, float y, float degrees) {
        Sprite sprite = sprites.get(name);
        sprite.setPosition(x, y);
        sprite.setRotation(degrees);
        sprite.draw(batch);
    }
    private void drawSpriteWH(String name, float x, float y, float degrees, float proportion) {
        Sprite sprite = sprites.get(name);
        sprite.setPosition(x, y);
        sprite.setRotation(degrees);
        sprite.setScale(proportion);
        sprite.draw(batch);
    }
    private void drawSprite(String name, float x, float y, float width, float height, float degrees) {
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
        fixtureDef.friction = 0.5f;
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.friction = 0.5f;
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.friction = 0.5f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(camera.viewportWidth, 1);
        fixtureDef.shape = shape;

        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(1, camera.viewportHeight);
        fixtureDef1.shape = shape1;

        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(camera.viewportWidth/4, camera.viewportHeight/4);
        fixtureDef2.shape = shape2;


        for (int i = 0; i < 4; i++) {
                gameFieldBounds[i] = world.createBody(bodyDef);
                BlockData block = new BlockData(-2);
                gameFieldBounds[i].setUserData(block);
                gameFieldBounds[i].createFixture((i < 2)? fixtureDef : fixtureDef1);
                gameFieldBounds[i].setTransform((i == 3)? camera.viewportWidth : 0, (i == 0)? camera.viewportHeight : 0, 0);
        }




        shape.dispose();
        shape1.dispose();
    }

    @Override
    public void pause() {
        this.state = State.PAUSE;
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
        switch (this.state) {
            case PAUSE:
                if (isInCircle(screenX, screenY, POPUP_BTN1_X * UNIT_SIZE, POPUP_BTN_Y * UNIT_SIZE, POPUP_BTN_RADIUS * UNIT_SIZE))
                    game.setScreen(shipChoosingScreen);
                Gdx.app.log("Button", POPUP_BTN_RADIUS*UNIT_SIZE + " ");
                if (isInCircle(screenX, screenY, POPUP_BTN2_X * UNIT_SIZE, POPUP_BTN_Y * UNIT_SIZE, POPUP_BTN_RADIUS * UNIT_SIZE))
                    this.state = State.RUN;
                if (isInRect(screenX, screenY, PAUSE_BTN_DELTA_X * UNIT_SIZE, (PAUSE_BTN_DELTA_X + PAUSE_BTN_SIZE) * UNIT_SIZE, 0, PAUSE_BTN_SIZE * UNIT_SIZE))
                    this.state = State.RUN;
                break;

            case END:
                if (isInCircle(screenX, screenY, POPUP_BTN1_X * UNIT_SIZE, POPUP_BTN_Y * UNIT_SIZE, POPUP_BTN_RADIUS * UNIT_SIZE))
                    game.setScreen(shipChoosingScreen);
                if (isInCircle(screenX, screenY, POPUP_BTN2_X * UNIT_SIZE, POPUP_BTN_Y * UNIT_SIZE, POPUP_BTN_RADIUS * UNIT_SIZE))
                    game.setScreen(new GameScreen(shipChoosingScreen,game,p1_ship,p2_ship));
                break;

            case RUN:
                if (isInRect(screenX, screenY, PAUSE_BTN_DELTA_X * UNIT_SIZE, (PAUSE_BTN_DELTA_X + PAUSE_BTN_SIZE) * UNIT_SIZE, 0, PAUSE_BTN_SIZE * UNIT_SIZE))
                    this.state = State.PAUSE;
                break;

        }
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


    /*
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
    }*/

    /*private static int[][] shipFlipHorizontal(int[][] ship) {
        int[][] res = new int[ship.length][ship[0].length];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                res[i][j] = ship[i][FIELD_HEIGHT - 1 - j];
            }
        }
        return res;
    }*/


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
            case WOOD_BLOCK: case STEEL_BLOCK: case ENGINE: case EYE:
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
    private static boolean isInRect(int x, int y, float minX, float maxX, float minY, float maxY) {
        return (x > minX) && (x < maxX) && (y > minY) && (y < maxY);
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
        float atan = (gunNum == 1)? ARCTG_0195 : ARCTG_01;
        float asin = (gunNum == 1)? ARCSIN_0975 : ARCSIN_0985;
        float kf2 = (gunNum == 1)? 0.93f: 0.61f;
        float kf3 = (gunNum == 1)? 0.65f: 0.35f;
        int impulse = (gunNum == 1)? (int)(150) : (int)(100);
        Body bullet;
         if (gunNum == 1){bullet = createBody("bullet2", 0, 0, body.getAngle());}
        else { bullet = createBody("bullet", 0, 0, body.getAngle());}
        if (rotate == 0) {
            cosAlpha = (float) Math.cos(body.getAngle() + atan);
            sinAlpha = (float) Math.sin(body.getAngle() + atan);
            bullet.setTransform(body.getPosition().x + 4.0f*SCALE/0.005f * cosAlpha, body.getPosition().y + 4.0f *SCALE/0.005f* sinAlpha, body.getAngle() + (float) Math.PI / 2 * rotate);
        }
        else if (rotate == 1) {
            cosAlpha = (float) Math.cos(body.getAngle() + asin);
            sinAlpha = (float) Math.sin(body.getAngle() + asin);
            bullet.setTransform(body.getPosition().x + 4.0f*SCALE/0.005f * cosAlpha, body.getPosition().y + 4.0f*SCALE/0.005f * sinAlpha, body.getAngle() + (float) Math.PI / 2 * rotate);
        }
        else if (rotate == 2) {
            cosAlpha = (float) Math.cos(body.getAngle() + Math.PI / 2);
            sinAlpha = (float) Math.sin(body.getAngle() + Math.PI / 2);
            float cosAlpha1 = (float) Math.cos(body.getAngle());
            float sinAlpha1 = (float) Math.sin(body.getAngle());
            bullet.setTransform(body.getPosition().x + kf2*SCALE/0.005f * cosAlpha -0.2f*SCALE/0.005f*cosAlpha1, body.getPosition().y + kf2*SCALE/0.005f * sinAlpha-0.2f*SCALE/0.005f*sinAlpha1, body.getAngle() + (float) Math.PI / 2 * rotate);
        }
        else if (rotate == 3) {
            cosAlpha = (float) Math.cos(body.getAngle());
            sinAlpha = (float) Math.sin(body.getAngle());
            float cosAlpha1 = (float) Math.cos(body.getAngle() + Math.PI / 2);
            float sinAlpha1 = (float) Math.sin(body.getAngle() + Math.PI / 2);
            bullet.setTransform(body.getPosition().x + kf3 *SCALE/0.005f* cosAlpha -0.2f*SCALE/0.005f*cosAlpha1, body.getPosition().y + kf3*SCALE/0.005f * sinAlpha-0.2f*SCALE/0.005f*sinAlpha1, body.getAngle() + (float) Math.PI / 2 * rotate);
        }

        bullet.setLinearVelocity(new Vector2(body.getLinearVelocity().x + impulse * (float) Math.cos(body.getAngle() + rotate*Math.PI/2), body.getLinearVelocity().y + impulse * (float) Math.sin(body.getAngle() + rotate * Math.PI/2)));
        BlockData block = new BlockData(bulletType);
        //Gdx.app.log("Bullet","Bullet Hp: " +block.getHp() + "Bullet Type: " + block.getType());
        bullet.setUserData(block);
        if (gunNum == 1)
            bullets2.add(bullet);
        else
            bullets.add(bullet);
        if (player == 1) {
            p1_bodies[i][j].applyForceToCenter(-impulse * cos, -impulse * sin, true);
            BlockData bodyblock = (BlockData) p1_bodies[i][j].getUserData();
            bodyblock.setHp(bodyblock.getHp());
            p1_bodies[i][j].setUserData(bodyblock);
        }
        else {
            p2_bodies[i][j].applyForceToCenter(-impulse * cos, -impulse * sin, true);
            BlockData bodyblock = (BlockData) p2_bodies[i][j].getUserData();
            bodyblock.setHp(bodyblock.getHp());
            p2_bodies[i][j].setUserData(bodyblock);
        }
    }



    private void BD_activateBullet(Body bullet) {
        BlockData data = (BlockData) bullet.getUserData();
        data.setBulletActivated(true);
        bullet.setUserData(data);
    }

    private void BD_getDamage(Body body) {
        BlockData data = (BlockData) body.getUserData();
        data.setHp(data.getHp() - 1);
        body.setUserData(data);
    }

    private void BD_addEngineLabel(Body engine, int newLabel) {
        BlockData data = (BlockData) engine.getUserData();
        ArrayList<Integer> labels = data.getEngineLabels();
        labels.add(newLabel);
        data.setEngineLabels(labels);
        engine.setUserData(data);
    }

    private void BD_addLifetime(Body bullet, float delta) {
        BlockData data = (BlockData) bullet.getUserData();
        data.setBulletLifetime(data.getBulletLifetime() + delta);
        bullet.setUserData(data);
    }


    private String getStringTimer(float timerInFloat) {
        int min = (int) (timerInFloat / 60);
        int sec = (int) (timerInFloat % 60);
        return (sec < 10)? min + ":0" + sec : min + ":" + sec;
    }

}