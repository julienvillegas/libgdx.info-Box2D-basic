package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Bodies.MoveableImage;

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
    private int numBlocks = 2;
    private MoveableImage[] blocks  = new MoveableImage[numBlocks];
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

        blocks[0] = new MoveableImage(blockSize/2, blockSize/2,blockSize,blockSize,0,"woodblock.png");
        blocks[1] = new MoveableImage(blockSize/2, blockSize*5/2,blockSize,blockSize,0,"steelblock.png");

        for (int i=0;i<numBlocks;i++){
            stage.addActor(blocks[i]);
        }


    }


    public void setEndPosition(MoveableImage image, int x,int y){
        if (((x>deltaXForBlocks) && (x<deltaXForBlocks+blockSize*blockarrWidth)) &&
        ((y>deltaYForBlocks) && (y<deltaYForBlocks+blockSize*blockarrHeight))){
            for (int i=0;i<blockarrHeight;i++){
                for (int j=0;j<blockarrWidth;j++){
                    if (emptyblocks[i][j].contains(x,y)){
                        if (blockarr[i][j]==0) {
                            blockarr[i][j] = image.getNumber();
                            image.setX(emptyblocks[i][j].getX());
                            image.setY(emptyblocks[i][j].getY());
                        }
                        else {
                            image.returnToStartPos();
                        }
                    }
                }
            }
        }
        else {
            image.returnToStartPos();
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
        for (int i=0;i<numBlocks;i++) {
            if (blocks[i].contains(x, y)) {
               blocks[i].isMoving = true;

            }
        }
            oldX=x;
            oldY=y;

            return true;
        }

        @Override
        public boolean touchUp (int x, int y, int pointer, int button) {
        for (int i=0;i<numBlocks;i++){
            if (blocks[i].isMoving == true){
                setEndPosition(blocks[i],x,y);
            }
            blocks[i].isMoving = false;
        }
            return true;
        }

        @Override
        public boolean touchDragged (int x, int y, int pointer) {
            for (int i=0;i<numBlocks;i++) {
                if (blocks[i].isMoving == true) {
                    blocks[i].setX(blocks[i].getX() + x - oldX);
                    blocks[i].setY(blocks[i].getY() - y + oldY);
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
