package com.mygdx.game.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class MoveableImage extends Image {
    public boolean isMoving = false;
    private float x;
    private float y;
    private float width;
    private float height;
    private float angle;

    public MoveableImage(float pos_x, float pos_y, float aWidth, float aHeight, float angle) {
        super(new Texture("woodblock.png"));
        this.x = pos_x;
        this.y = pos_y;
        this.width = aWidth;
        this.height = aHeight;
        this.angle = angle;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


    }




    @Override
    public void act(float delta) {
        super.act(delta);
        this.setRotation(angle*  MathUtils.radiansToDegrees);
        this.setPosition(x,y);
    }

    public boolean contains(float x, float y){

        return (x>this.x && x<(this.x+this.width)) && (y< Gdx.graphics.getHeight()-this.y && y>Gdx.graphics.getHeight()-this.y-this.height);
    }



}
