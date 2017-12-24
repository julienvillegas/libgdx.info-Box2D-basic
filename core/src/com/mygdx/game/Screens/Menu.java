package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Bodies.MoveableImage;
import com.mygdx.game.Extra.AssemblingScreenCoords;
import com.mygdx.game.Extra.ItemID;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;

public class Menu implements Screen, InputProcessor, ItemID, AssemblingScreenCoords {

    private Stage stage;
    private Game game;
    private float oldX = 0, oldY = 0;
    private float currentX=0, currentY=0;

    private int[][] blockArr = new int[FIELD_HEIGHT][FIELD_WIDTH];
    private ArrayList<MoveableImage[]> blocks = new ArrayList<MoveableImage[]>();
    private MoveableImage[][] cells = new MoveableImage[FIELD_HEIGHT][FIELD_WIDTH];
    private Label[] labels = new Label[NUMBER_OF_ITEMS];

    private int[] inventory = setPrimaryInventory();                                                // Инвентарь игрока

    public Menu(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);

        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                cells[i][j] = new MoveableImage(FIELD_DELTA_X + BLOCK_SIZE*j, FIELD_DELTA_Y + BLOCK_SIZE*i, BLOCK_SIZE, BLOCK_SIZE,0,"gray.png");
                stage.addActor(cells[i][j]);
            }
        }

        for (int itemsCount : inventory)
            blocks.add(new MoveableImage[itemsCount]);

        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < blocks.get(i).length; j++)
                blocks.get(i)[blocks.get(i).length - j - 1] = new MoveableImage(getPosX(i), getPosY(i), getWidth(i), getHeight(i), 0, getImageName(i));
            blocks.get(i)[0].setTouchable(true);
            stage.addActor(blocks.get(i)[0]);
        }

        BitmapFont itemsCountBF = new BitmapFont();
        itemsCountBF.getData().scale(2);
        Label.LabelStyle itemsCountLS = new Label.LabelStyle(itemsCountBF, Color.BLACK);

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(countToString(blocks.get(i).length), itemsCountLS);
            labels[i].setX((i < NUMBER_OF_ITEMS/2)? BLOCK_SIZE/5: SCREEN_WIDTH - BLOCK_SIZE*2/5);
            labels[i].setY(getPosY(i) + BLOCK_SIZE/5);
            stage.addActor(labels[i]);
        }

        TextButton button = new TextButton("Start!", MyGdxGame.skin);
        button.setWidth(SCREEN_WIDTH/4);
        button.setPosition(SCREEN_WIDTH - button.getWidth()*3/2, BLOCK_SIZE/2);
        button.addListener(new InputListener(){

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                int[][] arr = new int[FIELD_WIDTH][FIELD_HEIGHT];
                for (int i=0;i<FIELD_WIDTH;i++){
                    for (int j=0;j<FIELD_HEIGHT;j++){
                        arr[i][j] = blockArr[FIELD_HEIGHT - j- 1][i];
                    }
                }
                game.setScreen(new GameScreen(arr,arr));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(button);


    }



    private float getPosX(int i) {
        return (i < NUMBER_OF_ITEMS/2)? BLOCK_SIZE/2 : SCREEN_WIDTH - BLOCK_SIZE/2-getWidth(i);
    }

    private float getPosY(int i) {
        return (i < NUMBER_OF_ITEMS/2)? BLOCK_SIZE*(i+2)*3/2-getHeight(i)/2: BLOCK_SIZE*(i-2)*3/2-getHeight(i)/2;
    }

    private float getWidth(int i) {
        switch(i){
            case WOOD_GUN: return 770*BLOCK_SIZE/345;
            case STEEL_GUN: return 765*BLOCK_SIZE/345;
            case TURBINE: return 565*BLOCK_SIZE/345;
        }
        return BLOCK_SIZE;
    }

    private float getHeight(int i) {
        switch(i){
            case WOOD_GUN: return 194*BLOCK_SIZE/345;
            case STEEL_GUN: return 315*BLOCK_SIZE/345;
        }
        return BLOCK_SIZE;
    }


    private String countToString(int N) {
        return "x" + N;
    }               // Преобразует число N в строку "xN"

    private String add1ToString(String N) {
            int num = Integer.parseInt(N.substring(1)) + 1;
            return "x" + num;
    }             // Прибавляет 1 к числу в строковом виде
    private String subtract1FromString(String N) {
            int num = Integer.parseInt(N.substring(1)) - 1;
            return "x" + num;
    }      // Отнимает 1 от числа в строковом виде


    private String getImageName(int i) {
        switch(i){
            case WOOD_BLOCK: return "woodblock.png";
            case STEEL_BLOCK: return "steelblock.png";
            case ENGINE: return "engine.png";
            case TURBINE: return "turbine.png";
            case HALF_WOOD_BLOCK: return "halfwoodblock.png";
            case HALF_STEEL_BLOCK: return "halfsteelblock.png";
            case WOOD_GUN: return "gun_1.png";
            case STEEL_GUN: return "gun_2.png";
        }
        return "";
    }

    private int[] setPrimaryInventory() {
        int[] inventory = new int[8];
        inventory[WOOD_BLOCK] = 8;
        inventory[STEEL_BLOCK] = 4;
        inventory[ENGINE] = 3;
        inventory[TURBINE] = 2;
        inventory[HALF_WOOD_BLOCK] = 4;
        inventory[HALF_STEEL_BLOCK] = 2;
        inventory[WOOD_GUN] = 1;
        inventory[STEEL_GUN] = 2;
        return inventory;
    }               // Задаёт изначальное количество предметов для расстановки

    private void setCoordsFromCell(MoveableImage image,float x, float y){
        image.setX(x + BLOCK_SIZE/2 - image.getOriginX());
        image.setY(y + BLOCK_SIZE/2 - image.getOriginY());
    }



    private boolean setEndPosition(MoveableImage image, int x, int y) {
        int iCoord = (int) Math.floor((x - FIELD_DELTA_X) / BLOCK_SIZE);
        int jCoord = (int) Math.floor((y - FIELD_DELTA_Y) / BLOCK_SIZE);
        int i = FIELD_HEIGHT - jCoord - 1;
        int j = iCoord;
        if ((i >= 0) && (i < FIELD_HEIGHT) && (j >= 0) && (j < FIELD_WIDTH) && (blockArr[i][j] == 0)) {
            blockArr[i][j] = image.getNumber();
            setCoordsFromCell(image, cells[i][j].getX(), cells[i][j].getY());
            image.setXinTable(i);
            image.setYinTable(j);
            return true;
        } else {
            image.setAngle(0);
            image.returnToStartPos();
            return false;
        }
    }

    @Override
    public boolean keyDown (int keycode) {
            return true;
        }

    @Override
    public boolean keyUp (int keycode) {
            return true;
        }

    @Override
    public boolean keyTyped (char character) {
            return true;
        }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                if (blocks.get(i)[j].contains(x, y)) {
                    if (blocks.get(i)[j].isTouchable()) {
                        blocks.get(i)[j].setMoving(true);
                        blocks.get(i)[j].setAlreadyMoved(false);

                        if (blocks.get(i)[j].getXinTable() != NULL) {
                            blockArr[blocks.get(i)[j].getXinTable()][blocks.get(i)[j].getYinTable()] = 0;
                            blocks.get(i)[j].setXinTable(NULL);
                            blocks.get(i)[j].setYinTable(NULL);
                        } else {
                            labels[i].setText(subtract1FromString(labels[i].getText().toString()));
                        }
                    }
                }
            }
        }
        oldX = x;
        oldY = y;
        currentX = x;
        currentY = y;
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                if (blocks.get(i)[j].isMoving()) {
                    if (!blocks.get(i)[j].isAlreadyMoved()){
                        blocks.get(i)[j].flip90();
                    }
                    if (setEndPosition(blocks.get(i)[j], x, y)) {
                        if (j < blocks.get(i).length - 1) {
                            for (int h=j+1;h<blocks.get(i).length;h++){
                                if (blocks.get(i)[h].isInStartPos()) {
                                    blocks.get(i)[h].setTouchable(true);
                                    stage.addActor(blocks.get(i)[h]);
                                    break;
                                }
                            }
                        }
                    } else {
                        labels[i].setText(add1ToString(labels[i].getText().toString()));
                        for (int h = j + 1; h < blocks.get(i).length; h++) {
                            if (blocks.get(i)[h].isInStartPos()) {
                                blocks.get(i)[h].setTouchable(false);
                                blocks.get(i)[h].remove();
                            }
                        }
                        for (int h = 0; h < j; h++) {
                            if (blocks.get(i)[h].isInStartPos()) {
                                blocks.get(i)[j].setTouchable(false);
                                blocks.get(i)[j].remove();
                                break;
                            }
                        }
                    }
                }
                blocks.get(i)[j].setMoving(false);
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                if (blocks.get(i)[j].isMoving()) {
                    if ((Math.abs(x-currentX)>BLOCK_SIZE/8)||(Math.abs(y-currentY)>BLOCK_SIZE/8)) {
                        blocks.get(i)[j].setAlreadyMoved(true);
                    }
                    blocks.get(i)[j].setX(blocks.get(i)[j].getX() + x - oldX);
                    blocks.get(i)[j].setY(blocks.get(i)[j].getY() - y + oldY);
                }
            }
        }
        oldX = x;
        oldY = y;
        return false;
    }

    @Override
    public boolean mouseMoved (int x, int y) {
            return true;
    }

    @Override
    public boolean scrolled (int amount) {
            return true;
        }



    @Override
    public void show() {
        InputMultiplexer m = new InputMultiplexer();
        m.addProcessor(this);
        m.addProcessor(stage);
        Gdx.input.setInputProcessor(m);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
    }
}
