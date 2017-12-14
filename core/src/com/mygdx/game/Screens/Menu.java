package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Bodies.MoveableImage;
import com.mygdx.game.Extra.AssemblingScreenCoords;
import com.mygdx.game.Extra.ItemID;

import java.util.ArrayList;

public class Menu implements Screen, InputProcessor, ItemID, AssemblingScreenCoords {

    private Stage stage;
    private float oldX = 0, oldY = 0;

    private int[][] blockArr = new int[FIELD_HEIGHT][FIELD_WIDTH];
    private ArrayList<MoveableImage[]> blocks = new ArrayList<MoveableImage[]>();
    private MoveableImage[][] cells = new MoveableImage[FIELD_HEIGHT][FIELD_WIDTH];
    private Label[] labels = new Label[NUMBER_OF_ITEMS];

    private int[] inventory = setPrimaryInventory();                                                // Инвентарь игрока

    public Menu() {
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
                blocks.get(i)[blocks.get(i).length - j - 1] = new MoveableImage(getPosX(i), getPosY(i), BLOCK_SIZE, BLOCK_SIZE, 0, getImageName(i));
            blocks.get(i)[0].isTouchable = true;
            stage.addActor(blocks.get(i)[0]);
        }

        BitmapFont itemsCountBF = new BitmapFont();
        itemsCountBF.getData().scale(2);
        Label.LabelStyle itemsCountLS = new Label.LabelStyle(itemsCountBF, Color.BLACK);

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(countToString(blocks.get(i).length), itemsCountLS);
            labels[i].setX((i < NUMBER_OF_ITEMS/2)? BLOCK_SIZE/5 : SCREEN_WIDTH - BLOCK_SIZE*2/5);
            labels[i].setY(getPosY(i) + BLOCK_SIZE/5);
            stage.addActor(labels[i]);
        }
    }



    private float getPosX(int i) {
        return (i < NUMBER_OF_ITEMS/2)? BLOCK_SIZE/2 : SCREEN_WIDTH - BLOCK_SIZE*3/2;
    }

    private float getPosY(int i) {
        return (i < NUMBER_OF_ITEMS/2)? BLOCK_SIZE*i + BLOCK_SIZE/2 : BLOCK_SIZE*i + BLOCK_SIZE/2 - BLOCK_SIZE*NUMBER_OF_ITEMS/2;
    }


    private String countToString(int N) {
        return (N == 0)? "" : "x" + N;
    }               // Преобразует число N в строку "xN"

    private String add1ToString(String N) {
        if (N.equals(""))
            return "x1";
        else {
            int num = Integer.parseInt(N.substring(1)) + 1;
            return "x" + num;
        }
    }             // Прибавляет 1 к числу в строковом виде
    private String subtract1FromString(String N) {
        if (N.equals("") || N.equals("x1"))
            return "";
        else {
            int num = Integer.parseInt(N.substring(1)) - 1;
            return "x" + num;
        }
    }      // Отнимает 1 от числа в строковом виде


    private String getImageName(int i) {
        switch(i){
            case WOOD_BLOCK: return "woodblock.png";
            case STEEL_BLOCK: return "steelblock.png";
            case ENGINE: return "engine.png";
            case TURBINE: return "turbine.png";
            case HALF_WOOD_BLOCK: return "halfwoodblock.png";
            case HALF_STEEL_BLOCK: return "halfsteelblock.png";
            case GUN_1: return "gun_1.png";
            case GUN_2: return "gun_2.png";
        }
        return "";
    }

    private int[] setPrimaryInventory() {
        int[] inventory = new int[8];
        inventory[WOOD_BLOCK] = 8;
        inventory[STEEL_BLOCK] = 4;
        inventory[ENGINE] = 2;
        inventory[TURBINE] = 2;
        inventory[HALF_WOOD_BLOCK] = 4;
        inventory[HALF_STEEL_BLOCK] = 2;
        inventory[GUN_1] = 1;
        inventory[GUN_2] = 2;
        return inventory;
    }               // Задаёт изначальное количество предметов для расстановки


    public boolean setEndPosition(MoveableImage image, int x, int y) {
        if (((x > FIELD_DELTA_X) && (x < SCREEN_WIDTH - FIELD_DELTA_X)) &&
            ((y > FIELD_DELTA_Y) && (y < SCREEN_HEIGHT - FIELD_DELTA_Y))) {
            for (int i = 0; i < FIELD_HEIGHT; i++) {
                for (int j = 0; j < FIELD_WIDTH; j++) {
                    if (cells[i][j].contains(x,y)) {
                        if (blockArr[i][j] == 0) {
                            blockArr[i][j] = image.getNumber();
                            image.setX(cells[i][j].getX());
                            image.setY(cells[i][j].getY());
                            image.setXinTable(i);
                            image.setYinTable(j);
                            return true;

                        }
                        else {
                            image.returnToStartPos();
                            return false;
                        }

                    }
                }
            }
        }
        else {
            image.returnToStartPos();
            return false;
        }
        return true;
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
                    if (blocks.get(i)[j].isTouchable) {
                        blocks.get(i)[j].isMoving = true;

                        if (blocks.get(i)[j].getXinTable() != 50) {
                            blockArr[blocks.get(i)[j].getXinTable()][blocks.get(i)[j].getYinTable()] = 0;
                            blocks.get(i)[j].setXinTable(50);
                            blocks.get(i)[j].setYinTable(50);
                        } else {
                            labels[i].setText(subtract1FromString(labels[i].getText().toString()));
                        }
                    }
                }
            }
        }
        oldX = x;
        oldY = y;
        return true;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                if (blocks.get(i)[j].isMoving) {
                    if (setEndPosition(blocks.get(i)[j], x, y)) {
                        if (j < blocks.get(i).length - 1) {
                            if (blocks.get(i)[j + 1].isInStartPos()) {
                                blocks.get(i)[j + 1].isTouchable = true;
                                stage.addActor(blocks.get(i)[j + 1]);
                            }
                        }
                    } else {
                        labels[i].setText(add1ToString(labels[i].getText().toString()));
                        for (int h = j + 1; h < blocks.get(i).length; h++) {
                            if (blocks.get(i)[h].isInStartPos()) {
                                blocks.get(i)[h].isTouchable = false;
                                blocks.get(i)[h].remove();
                            }
                        }
                    }
                }
                blocks.get(i)[j].isMoving = false;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                if (blocks.get(i)[j].isMoving) {
                    blocks.get(i)[j].setX(blocks.get(i)[j].getX() + x - oldX);
                    blocks.get(i)[j].setY(blocks.get(i)[j].getY() - y + oldY);
                }
            }
        }
        oldX = x;
        oldY = y;
        return true;
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
        Gdx.input.setInputProcessor(this);
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
