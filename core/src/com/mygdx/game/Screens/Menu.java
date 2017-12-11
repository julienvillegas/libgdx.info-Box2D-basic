package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Bodies.MoveableImage;

import java.util.ArrayList;

public class Menu implements Screen, InputProcessor {

    private Stage stage;
    private Game game;
    private float oldX=0,oldY=0;
    private int blockarrHeight = 5;
    private int blockarrWidth = 6;
    private float blockSize= Gdx.graphics.getHeight()/(blockarrHeight+2);
    private float deltaXForBlocks = Gdx.graphics.getWidth()/2-blockarrWidth*blockSize/2;
    private float deltaYForBlocks = Gdx.graphics.getHeight()/2-blockarrHeight*blockSize/2;
    private int[][] blockarr = new int[blockarrHeight][blockarrWidth];

    private int numwoodblocks = 8;

    private int numsteelblocks = 4;

    private int numengines = 2;

    private int numturbines = 2;

    private int numhalfwoodblocks = 4;

    private int numhalfsteelblocks = 2;

    private int numgun1s = 1;

    private int numgun2s = 2;


    ArrayList<MoveableImage[]> blocks = new ArrayList<MoveableImage[]>();
    private Label[] labels = new Label[8];

    private MoveableImage[][] emptyblocks = new MoveableImage[blockarrHeight][blockarrWidth];
    public Menu(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);
        for (int i=0;i<blockarrHeight;i++){
            for (int j=0;j<blockarrWidth;j++){
                emptyblocks[i][j]=new MoveableImage(deltaXForBlocks+blockSize*j,deltaYForBlocks+blockSize*i,blockSize,blockSize,0,"gray.png");
                stage.addActor(emptyblocks[i][j]);
            }
        }

        blocks.add(new MoveableImage[numwoodblocks]);
        blocks.add(new MoveableImage[numsteelblocks]);
        blocks.add(new MoveableImage[numengines]);
        blocks.add(new MoveableImage[numturbines]);
        blocks.add(new MoveableImage[numhalfwoodblocks]);
        blocks.add(new MoveableImage[numhalfsteelblocks]);
        blocks.add(new MoveableImage[numgun1s]);
        blocks.add(new MoveableImage[numgun2s]);

        for (int i=0;i<blocks.size();i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                blocks.get(i)[blocks.get(i).length - j - 1] = new MoveableImage(getPosX(i), getPosY(i), getWidth(i),getHeight(i), 0, getImageName(i));
                stage.addActor(blocks.get(i)[blocks.get(i).length - j - 1]);
            }
            blocks.get(i)[0].isTouchable = true;
        }

        BitmapFont font = new BitmapFont();

        for (int i=0;i<labels.length;i++){
            labels[i] = new Label(String.valueOf(blocks.get(i).length), new Label.LabelStyle(font, Color.BLACK));
            if (i<4) {
                labels[i].setX(blockSize*1/5);
            }else {
                labels[i].setX(Gdx.graphics.getWidth()-blockSize*2/5);
            }
            labels[i].setY(getPosY(i)+blockSize*1/5);
            stage.addActor(labels[i]);
        }
    }

    private float getPosX(int i) {
        if (i<4){
            return blockSize/2;
        }
        else {
            return Gdx.graphics.getWidth()-blockSize*3/2;
        }
    }
    private float getPosY(int i) {
        if (i<4) {
            return blockSize * ((2 * i + 1) / 2);
        }
        else {
            return blockSize * ((2 * (i-4) + 1) / 2);
        }
    }
    private float getWidth(int i) {
        return blockSize;
    }
    private float getHeight(int i) {
        return blockSize;
    }
    private String getImageName(int i) {
        switch(i){
            case 0: return "woodblock.png";
            case 1: return "steelblock.png";
            case 2: return "engine.png";
            case 3: return "turbine.png";
            case 4: return "halfwoodblock.png";
            case 5: return "halfsteelblock.png";
            case 6: return "gun_1.png";
            case 7: return "gun_2.png";
        }
        return "";
    }


    public boolean setEndPosition(MoveableImage image, int x,int y){
        if (((x>deltaXForBlocks) && (x<deltaXForBlocks+blockSize*blockarrWidth)) &&
        ((y>deltaYForBlocks) && (y<deltaYForBlocks+blockSize*blockarrHeight))){
            for (int i=0;i<blockarrHeight;i++){
                for (int j=0;j<blockarrWidth;j++){
                    if (emptyblocks[i][j].contains(x,y)){
                        if (blockarr[i][j]==0) {
                            blockarr[i][j] = image.getNumber();
                            image.setX(emptyblocks[i][j].getX());
                            image.setY(emptyblocks[i][j].getY());
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
        for (int i=0;i<blocks.size();i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                if (blocks.get(i)[j].contains(x, y)) {
                    if (blocks.get(i)[j].isTouchable == true) {
                        blocks.get(i)[j].isMoving = true;

                        if (blocks.get(i)[j].getXinTable() != 50) {
                            blockarr[blocks.get(i)[j].getXinTable()][blocks.get(i)[j].getYinTable()] = 0;
                            blocks.get(i)[j].setXinTable(50);
                            blocks.get(i)[j].setYinTable(50);
                        } else {
                            labels[i].setText(String.valueOf(Integer.parseInt(labels[i].getText().toString()) - 1));
                        }
                    }

                }
            }
        }
            oldX=x;
            oldY=y;

            return true;
        }

        @Override
        public boolean touchUp (int x, int y, int pointer, int button) {
        for (int i=0;i<blocks.size();i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                if (blocks.get(i)[j].isMoving == true) {
                    if (setEndPosition(blocks.get(i)[j], x, y) == true) {
                        if (j < blocks.get(i).length - 1) {
                            for (int h = j + 1; h < blocks.get(i).length; h++) {
                                if (blocks.get(i)[h].isInStartPos() == true) {
                                    blocks.get(i)[h].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        labels[i].setText(String.valueOf(Integer.parseInt(labels[i].getText().toString()) + 1));
                        for (int h = 0; h < j; j++) {
                            if (blocks.get(i)[h].isInStartPos() == true) {
                                blocks.get(i)[j].isTouchable = false;
                            }
                        }
                        for (int h = j + 1; h < blocks.get(i).length; h++) {
                            if (blocks.get(i)[h].isInStartPos() == true) {
                                blocks.get(i)[h].isTouchable = false;
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
        for (int i=0;i<blocks.size();i++) {
            for (int j = 0; j < blocks.get(i).length; j++) {
                if (blocks.get(i)[j].isMoving == true) {
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
