package com.mygdx.game.Bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by artum on 30.11.2017.
 */

public class MoveableImage extends Image {
    private Rect rect;
    public boolean isMoving = false;

    public MoveableImage(float pos_x, float pos_y, float aWidth, float aHeight, float angle){
        super(new Texture("woodblock.png"));
        this.setSize(aWidth,aHeight);
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
        this.rotateBy(angle);
        rect  = new Rect(pos_x, pos_y, aWidth, aHeight , angle);
        this.setPosition(pos_x,pos_y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


    }

    public void setRect(float pos_x, float pos_y, float aWidth, float aHeight, float angle){
        rect.setX(pos_x);
        rect.setY(pos_y);
        rect.setWidth(aWidth);
        rect.setHeight(aHeight);
        rect.setAngle(angle);
    }

    public Rect getRect(){
        return rect;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setRotation(rect.getAngle()*  MathUtils.radiansToDegrees);

        this.setPosition(rect.getX()-this.getWidth()/2,rect.getY()-this.getHeight()/2);


    }

    public boolean contains(float x, float y){
        return (x>rect.getX() && x<(rect.getX()+rect.getWidth())) && (y>rect.getY() && y<(rect.getY()+rect.getHeight()));
    }


}
