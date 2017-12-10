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
    private MoveableImage[] woodblocks = new MoveableImage[numwoodblocks];
    private int numsteelblocks = 4;
    private MoveableImage[] steelblocks = new MoveableImage[numsteelblocks];
    private int numengines = 2;
    private MoveableImage[] engines = new MoveableImage[numengines];
    private int numturbines = 2;
    private MoveableImage[] turbines = new MoveableImage[numturbines];
    private int numhalfwoodblocks = 4;
    private MoveableImage[] halfwoodblocks = new MoveableImage[numhalfwoodblocks];
    private int numhalfsteelblocks = 2;
    private MoveableImage[] halfsteelblocks = new MoveableImage[numhalfsteelblocks];
    private int numgun1s = 1;
    private MoveableImage[] gun1s = new MoveableImage[numgun1s];
    private int numgun2s = 2;
    private MoveableImage[] gun2s = new MoveableImage[numgun2s];

    private Label woodblocklabel;
    private Label steelblocklabel;
    private Label enginelabel;
    private Label turbinelabel;
    private Label halfwoodblocklabel;
    private Label halfsteelblocklabel;
    private Label gun1label;
    private Label gun2label;

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


        for (int i=0;i<numwoodblocks;i++){
            woodblocks[numwoodblocks-i-1]= new MoveableImage(blockSize/2, blockSize/2, blockSize, blockSize,0,"woodblock.png");
            stage.addActor(woodblocks[numwoodblocks-i-1]);
        }
        woodblocks[0].isTouchable = true;

        BitmapFont font = new BitmapFont();
         woodblocklabel = new Label(String.valueOf(numwoodblocks), new Label.LabelStyle(font, Color.BLACK));
        woodblocklabel.setX(blockSize*1/5);
        woodblocklabel.setY(blockSize*1/5+blockSize*1/2);
        stage.addActor(woodblocklabel);

        for (int i=0;i<numsteelblocks;i++){
            steelblocks[numsteelblocks-i-1]= new MoveableImage(blockSize/2, blockSize*3/2, blockSize, blockSize,0,"steelblock.png");
            stage.addActor(steelblocks[numsteelblocks-i-1]);
        }
        steelblocks[0].isTouchable = true;

         steelblocklabel = new Label(String.valueOf(numsteelblocks), new Label.LabelStyle(font, Color.BLACK));
        steelblocklabel.setX(blockSize*1/5);
        steelblocklabel.setY(blockSize*1/5+blockSize*3/2);
        stage.addActor(steelblocklabel);


        for (int i=0;i<numengines;i++){
            engines[numengines-i-1]= new MoveableImage(blockSize/2, blockSize*5/2, blockSize, blockSize,0,"engine.png");
            stage.addActor(engines[numengines-i-1]);
        }
        engines[0].isTouchable = true;

        enginelabel = new Label(String.valueOf(numengines), new Label.LabelStyle(font, Color.BLACK));
        enginelabel.setX(blockSize*1/5);
        enginelabel.setY(blockSize*1/5+blockSize*5/2);
        stage.addActor(enginelabel);

        for (int i=0;i<numturbines;i++){
            turbines[numturbines-i-1]= new MoveableImage(blockSize/2, blockSize*7/2, blockSize, blockSize,0,"turbine.png");
            stage.addActor(turbines[numturbines-i-1]);
        }
        turbines[0].isTouchable = true;

        turbinelabel = new Label(String.valueOf(numturbines), new Label.LabelStyle(font, Color.BLACK));
        turbinelabel.setX(blockSize*1/5);
        turbinelabel.setY(blockSize*1/5+blockSize*7/2);
        stage.addActor(turbinelabel);

        for (int i=0;i<numhalfwoodblocks;i++){
            halfwoodblocks[numhalfwoodblocks-i-1]= new MoveableImage(Gdx.graphics.getWidth()-blockSize*3/2, blockSize/2, blockSize, blockSize,0,"halfwoodblock.png");
            stage.addActor(halfwoodblocks[numhalfwoodblocks-i-1]);
        }
        halfwoodblocks[0].isTouchable = true;

        halfwoodblocklabel = new Label(String.valueOf(numhalfwoodblocks), new Label.LabelStyle(font, Color.BLACK));
        halfwoodblocklabel.setX(Gdx.graphics.getWidth()-blockSize*2/5);
        halfwoodblocklabel.setY(blockSize*1/5+blockSize*1/2);
        stage.addActor(halfwoodblocklabel);

        for (int i=0;i<numhalfsteelblocks;i++){
            halfsteelblocks[numhalfsteelblocks-i-1]= new MoveableImage(Gdx.graphics.getWidth()-blockSize*3/2, blockSize*3/2, blockSize, blockSize,0,"halfsteelblock.png");
            stage.addActor(halfsteelblocks[numhalfsteelblocks-i-1]);
        }
        halfsteelblocks[0].isTouchable = true;

        halfsteelblocklabel = new Label(String.valueOf(numhalfsteelblocks), new Label.LabelStyle(font, Color.BLACK));
        halfsteelblocklabel.setX(Gdx.graphics.getWidth()-blockSize*2/5);
        halfsteelblocklabel.setY(blockSize*1/5+blockSize*3/2);
        stage.addActor(halfsteelblocklabel);

        for (int i=0;i<numgun1s;i++){
            gun1s[numgun1s-i-1]= new MoveableImage(Gdx.graphics.getWidth()-blockSize*3/2, blockSize*5/2, blockSize, blockSize,0,"gun_1.png");
            stage.addActor(gun1s[numgun1s-i-1]);
        }
        gun1s[0].isTouchable = true;

        gun1label = new Label(String.valueOf(numgun1s), new Label.LabelStyle(font, Color.BLACK));
        gun1label.setX(Gdx.graphics.getWidth()-blockSize*2/5);
        gun1label.setY(blockSize*1/5+blockSize*5/2);
        stage.addActor(gun1label);

        for (int i=0;i<numgun2s;i++){
           gun2s[numgun2s-i-1]= new MoveableImage(Gdx.graphics.getWidth()-blockSize*3/2, blockSize*7/2, blockSize, blockSize,0,"gun_2.png");
            stage.addActor(gun2s[numgun2s-i-1]);
        }
        gun2s[0].isTouchable = true;

        gun2label = new Label(String.valueOf(numgun2s), new Label.LabelStyle(font, Color.BLACK));
        gun2label.setX(Gdx.graphics.getWidth()-blockSize*2/5);
        gun2label.setY(blockSize*1/5+blockSize*7/2);
        stage.addActor(gun2label);

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
        for (int i=0;i<numwoodblocks;i++) {
            if (woodblocks[i].contains(x, y)) {
                if (woodblocks[i].isTouchable == true) {
                    woodblocks[i].isMoving = true;

                    if (woodblocks[i].getXinTable() != 50) {
                        blockarr[woodblocks[i].getXinTable()][woodblocks[i].getYinTable()] = 0;
                        woodblocks[i].setXinTable(50);
                        woodblocks[i].setYinTable(50);
                    }
                    else {
                        woodblocklabel.setText(String.valueOf(Integer.parseInt(woodblocklabel.getText().toString())-1));
                    }
                }

            }
        }
            for (int i=0;i<numsteelblocks;i++) {
                if (steelblocks[i].contains(x, y)) {
                    if (steelblocks[i].isTouchable == true) {
                        steelblocks[i].isMoving = true;
                        if (steelblocks[i].getXinTable() != 50) {
                            blockarr[steelblocks[i].getXinTable()][steelblocks[i].getYinTable()] = 0;
                            steelblocks[i].setXinTable(50);
                            steelblocks[i].setYinTable(50);
                        }
                        else {
                            steelblocklabel.setText(String.valueOf(Integer.parseInt(steelblocklabel.getText().toString())-1));
                        }
                    }

                }
            }
            for (int i=0;i<numengines;i++) {
                if (engines[i].contains(x, y)) {
                    if (engines[i].isTouchable == true) {
                        engines[i].isMoving = true;
                        if (engines[i].getXinTable() != 50) {
                            blockarr[engines[i].getXinTable()][engines[i].getYinTable()] = 0;
                            engines[i].setXinTable(50);
                            engines[i].setYinTable(50);
                        }
                        else {
                            enginelabel.setText(String.valueOf(Integer.parseInt(enginelabel.getText().toString())-1));
                        }
                    }

                }
            }
            for (int i=0;i<numturbines;i++) {
                if (turbines[i].contains(x, y)) {
                    if (turbines[i].isTouchable == true) {
                        turbines[i].isMoving = true;
                        if (turbines[i].getXinTable() != 50) {
                            blockarr[turbines[i].getXinTable()][turbines[i].getYinTable()] = 0;
                            turbines[i].setXinTable(50);
                            turbines[i].setYinTable(50);
                        }
                        else {
                            turbinelabel.setText(String.valueOf(Integer.parseInt(turbinelabel.getText().toString())-1));
                        }
                    }

                }
            }
            for (int i=0;i<numgun2s;i++) {
                if (gun2s[i].contains(x, y)) {
                    if (gun2s[i].isTouchable == true) {
                        gun2s[i].isMoving = true;
                        if (gun2s[i].getXinTable() != 50) {
                            blockarr[gun2s[i].getXinTable()][gun2s[i].getYinTable()] = 0;
                            gun2s[i].setXinTable(50);
                            gun2s[i].setYinTable(50);
                        }
                        else {
                            gun2label.setText(String.valueOf(Integer.parseInt(gun2label.getText().toString())-1));
                        }
                    }

                }
            }
            for (int i=0;i<numgun1s;i++) {
                if (gun1s[i].contains(x, y)) {
                    if (gun1s[i].isTouchable == true) {
                        gun1s[i].isMoving = true;
                        if (gun1s[i].getXinTable() != 50) {
                            blockarr[gun1s[i].getXinTable()][gun1s[i].getYinTable()] = 0;
                            gun1s[i].setXinTable(50);
                            gun1s[i].setYinTable(50);
                        }
                        else {
                            gun1label.setText(String.valueOf(Integer.parseInt(gun1label.getText().toString())-1));
                        }
                    }

                }
            }
            for (int i=0;i<numhalfwoodblocks;i++) {
                if (halfwoodblocks[i].contains(x, y)) {
                    if (halfwoodblocks[i].isTouchable == true) {
                        halfwoodblocks[i].isMoving = true;
                        if (halfwoodblocks[i].getXinTable() != 50) {
                            blockarr[halfwoodblocks[i].getXinTable()][halfwoodblocks[i].getYinTable()] = 0;
                            halfwoodblocks[i].setXinTable(50);
                            halfwoodblocks[i].setYinTable(50);
                        }
                        else {
                            halfwoodblocklabel.setText(String.valueOf(Integer.parseInt(halfwoodblocklabel.getText().toString())-1));
                        }
                    }

                }
            }
            for (int i=0;i<numhalfsteelblocks;i++) {
                if (halfsteelblocks[i].contains(x, y)) {
                    if (halfsteelblocks[i].isTouchable == true) {
                        halfsteelblocks[i].isMoving = true;
                        if (halfsteelblocks[i].getXinTable() != 50) {
                            blockarr[halfsteelblocks[i].getXinTable()][halfsteelblocks[i].getYinTable()] = 0;
                            halfsteelblocks[i].setXinTable(50);
                            halfsteelblocks[i].setYinTable(50);
                        }
                        else {
                            halfsteelblocklabel.setText(String.valueOf(Integer.parseInt(halfsteelblocklabel.getText().toString())-1));
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
            for (int i=0;i<numwoodblocks;i++){
                if (woodblocks[i].isMoving == true){
                    if (setEndPosition(woodblocks[i],x,y)== true){
                        if (i<numwoodblocks-1){
                            for (int j=i+1;j<numwoodblocks;j++) {
                                if (woodblocks[j].isInStartPos() == true) {
                                    woodblocks[j].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        woodblocklabel.setText(String.valueOf(Integer.parseInt(woodblocklabel.getText().toString())+1));
                        for (int j=0;j<i;j++){
                            if (woodblocks[j].isInStartPos() == true) {
                                woodblocks[i].isTouchable = false;
                            }
                        }
                        for (int j=i+1;j<numwoodblocks;j++){
                                if (woodblocks[j].isInStartPos() == true){
                                    woodblocks[j].isTouchable = false;
                                }
                        }

                    }
              }
                woodblocks[i].isMoving = false;
            }
            for (int i=0;i<numsteelblocks;i++){
                if (steelblocks[i].isMoving == true){
                    if (setEndPosition(steelblocks[i],x,y)== true){
                        if (i<numsteelblocks-1){
                            for (int j=i+1;j<numsteelblocks;j++) {
                                if (steelblocks[j].isInStartPos() == true) {
                                    steelblocks[j].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        steelblocklabel.setText(String.valueOf(Integer.parseInt(steelblocklabel.getText().toString())+1));
                        for (int j=0;j<i;j++){
                            if (steelblocks[j].isInStartPos() == true) {
                                steelblocks[i].isTouchable = false;
                            }
                        }
                        for (int j=i+1;j<numsteelblocks;j++){
                            if (steelblocks[j].isInStartPos() == true){
                                steelblocks[j].isTouchable = false;
                            }
                        }

                    }
                }
                steelblocks[i].isMoving = false;
            }
            for (int i=0;i<numengines;i++){
                if (engines[i].isMoving == true){
                    if (setEndPosition(engines[i],x,y)== true){
                        if (i<numengines-1){
                            for (int j=i+1;j<numengines;j++) {
                                if (engines[j].isInStartPos() == true) {
                                    engines[j].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        enginelabel.setText(String.valueOf(Integer.parseInt(enginelabel.getText().toString())+1));
                        for (int j=0;j<i;j++){
                            if (engines[j].isInStartPos() == true) {
                                engines[i].isTouchable = false;
                            }
                        }
                        for (int j=i+1;j<numengines;j++){
                            if (engines[j].isInStartPos() == true){
                                engines[j].isTouchable = false;
                            }
                        }

                    }
                }
                engines[i].isMoving = false;
            }
            for (int i=0;i<numturbines;i++){
                if (turbines[i].isMoving == true){
                    if (setEndPosition(turbines[i],x,y)== true){
                        if (i<numturbines-1){
                            for (int j=i+1;j<numturbines;j++) {
                                if (turbines[j].isInStartPos() == true) {
                                    turbines[j].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        turbinelabel.setText(String.valueOf(Integer.parseInt(turbinelabel.getText().toString())+1));
                        for (int j=0;j<i;j++){
                            if (turbines[j].isInStartPos() == true) {
                                turbines[i].isTouchable = false;
                            }
                        }
                        for (int j=i+1;j<numturbines;j++){
                            if (turbines[j].isInStartPos() == true){
                                turbines[j].isTouchable = false;
                            }
                        }

                    }
                }
                turbines[i].isMoving = false;
            }
            for (int i=0;i<numhalfwoodblocks;i++){
                if (halfwoodblocks[i].isMoving == true){
                    if (setEndPosition(halfwoodblocks[i],x,y)== true){
                        if (i<numhalfwoodblocks-1){
                            for (int j=i+1;j<numhalfwoodblocks;j++) {
                                if (halfwoodblocks[j].isInStartPos() == true) {
                                    halfwoodblocks[j].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        halfwoodblocklabel.setText(String.valueOf(Integer.parseInt(halfwoodblocklabel.getText().toString())+1));
                        for (int j=0;j<i;j++){
                            if (halfwoodblocks[j].isInStartPos() == true) {
                                halfwoodblocks[i].isTouchable = false;
                            }
                        }
                        for (int j=i+1;j<numhalfwoodblocks;j++){
                            if (halfwoodblocks[j].isInStartPos() == true){
                                halfwoodblocks[j].isTouchable = false;
                            }
                        }

                    }
                }
                halfwoodblocks[i].isMoving = false;
            }
            for (int i=0;i<numhalfsteelblocks;i++){
                if (halfsteelblocks[i].isMoving == true){
                    if (setEndPosition(halfsteelblocks[i],x,y)== true){
                        if (i<numwoodblocks-1){
                            for (int j=i+1;j<numhalfsteelblocks;j++) {
                                if (halfsteelblocks[j].isInStartPos() == true) {
                                    halfsteelblocks[j].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        halfsteelblocklabel.setText(String.valueOf(Integer.parseInt(halfsteelblocklabel.getText().toString())+1));
                        for (int j=0;j<i;j++){
                            if (halfsteelblocks[j].isInStartPos() == true) {
                                halfsteelblocks[i].isTouchable = false;
                            }
                        }
                        for (int j=i+1;j<numhalfsteelblocks;j++){
                            if (halfsteelblocks[j].isInStartPos() == true){
                                halfsteelblocks[j].isTouchable = false;
                            }
                        }

                    }
                }
                halfsteelblocks[i].isMoving = false;
            }
            for (int i=0;i<numgun1s;i++){
                if (gun1s[i].isMoving == true){
                    if (setEndPosition(gun1s[i],x,y)== true){
                        if (i<numgun1s-1){
                            for (int j=i+1;j<numgun1s;j++) {
                                if (gun1s[j].isInStartPos() == true) {
                                    gun1s[j].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        gun1label.setText(String.valueOf(Integer.parseInt(gun1label.getText().toString())+1));
                        for (int j=0;j<i;j++){
                            if (gun1s[j].isInStartPos() == true) {
                                gun1s[i].isTouchable = false;
                            }
                        }
                        for (int j=i+1;j<numgun1s;j++){
                            if (gun1s[j].isInStartPos() == true){
                                gun1s[j].isTouchable = false;
                            }
                        }

                    }
                }
                gun1s[i].isMoving = false;
            }
            for (int i=0;i<numgun2s;i++){
                if (gun2s[i].isMoving == true){
                    if (setEndPosition(gun2s[i],x,y)== true){
                        if (i<numgun2s-1){
                            for (int j=i+1;j<numgun2s;j++) {
                                if (gun2s[j].isInStartPos() == true) {
                                    gun2s[j].isTouchable = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        gun2label.setText(String.valueOf(Integer.parseInt(gun2label.getText().toString())+1));
                        for (int j=0;j<i;j++){
                            if (gun2s[j].isInStartPos() == true) {
                                gun2s[i].isTouchable = false;
                            }
                        }
                        for (int j=i+1;j<numgun2s;j++){
                            if (gun2s[j].isInStartPos() == true){
                                gun2s[j].isTouchable = false;
                            }
                        }

                    }
                }
                gun2s[i].isMoving = false;
            }
            return true;
        }

        @Override
        public boolean touchDragged (int x, int y, int pointer) {
            for (int i=0;i<numwoodblocks;i++) {
                if (woodblocks[i].isMoving == true) {
                    woodblocks[i].setX(woodblocks[i].getX() + x - oldX);
                    woodblocks[i].setY(woodblocks[i].getY() - y + oldY);
                }
            }
            for (int i=0;i<numsteelblocks;i++) {
                if (steelblocks[i].isMoving == true) {
                    steelblocks[i].setX(steelblocks[i].getX() + x - oldX);
                    steelblocks[i].setY(steelblocks[i].getY() - y + oldY);
                }
            }
            for (int i=0;i<numengines;i++) {
                if (engines[i].isMoving == true) {
                    engines[i].setX(engines[i].getX() + x - oldX);
                    engines[i].setY(engines[i].getY() - y + oldY);
                }
            }
            for (int i=0;i<numturbines;i++) {
                if (turbines[i].isMoving == true) {
                    turbines[i].setX(turbines[i].getX() + x - oldX);
                    turbines[i].setY(turbines[i].getY() - y + oldY);
                }
            }
            for (int i=0;i<numhalfwoodblocks;i++) {
                if (halfwoodblocks[i].isMoving == true) {
                    halfwoodblocks[i].setX(halfwoodblocks[i].getX() + x - oldX);
                    halfwoodblocks[i].setY(halfwoodblocks[i].getY() - y + oldY);
                }
            }
            for (int i=0;i<numhalfsteelblocks;i++) {
                if (halfsteelblocks[i].isMoving == true) {
                    halfsteelblocks[i].setX(halfsteelblocks[i].getX() + x - oldX);
                    halfsteelblocks[i].setY(halfsteelblocks[i].getY() - y + oldY);
                }
            }
            for (int i=0;i<numgun1s;i++) {
                if (gun1s[i].isMoving == true) {
                    gun1s[i].setX(gun1s[i].getX() + x - oldX);
                    gun1s[i].setY(gun1s[i].getY() - y + oldY);
                }
            }
            for (int i=0;i<numgun2s;i++) {
                if (gun2s[i].isMoving == true) {
                    gun2s[i].setX(gun2s[i].getX() + x - oldX);
                    gun2s[i].setY(gun2s[i].getY() - y + oldY);
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
