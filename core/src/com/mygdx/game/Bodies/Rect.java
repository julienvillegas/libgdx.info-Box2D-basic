package com.mygdx.game.Bodies;

/**
 * Created by artum on 30.11.2017.
 */

public class Rect {
    private float angle;
    private float x;
    private float y;
    private float width;
    private float height;
    Rect(float pos_x, float pos_y, float width, float height,float angle){
        x=pos_x;
        y=pos_y;
        this.width=width;
        this.height=height;
        this.angle = angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getAngle() {
        return angle;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
