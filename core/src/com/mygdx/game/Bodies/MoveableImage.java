package com.mygdx.game.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Extra.AssemblingScreenCoords;
import com.mygdx.game.Extra.ItemID;

public class MoveableImage extends Image implements ItemID, AssemblingScreenCoords {

    private static final float PIdiv2 = (float) Math.PI / 2;

    public boolean isMoving = false;
    public boolean isTouchable = false;
    private float x;
    private float y;
    private float width;
    private float height;
    private float angle;
    private float startPosX;
    private float startPosY;
    private int XinTable;
    private int YinTable;

    private int itemID;
    private int itemFacing;


    public MoveableImage(float pos_x, float pos_y, float aWidth, float aHeight, float angle, int ID) {
        super(new Texture(getImageName(ID)));
        XinTable = 50;
        YinTable = 50;
        startPosX= pos_x;
        startPosY = pos_y;
        this.x = pos_x;
        this.y = pos_y;
        this.width = aWidth;
        this.height = aHeight;
        this.angle = angle;
        this.itemID = ID;
        switch (ID) {
            case HALF_WOOD_BLOCK: this.itemFacing = FACING_DOWNLEFT; break;
            case HALF_STEEL_BLOCK: this.itemFacing = FACING_DOWNLEFT; break;
            default: this.itemFacing = MISSING;
        }
    }

    public int getXinTable() {
        return XinTable;
    }
    public void setXinTable(int xinTable) {
        XinTable = xinTable;
    }

    public int getYinTable() {
        return YinTable;
    }
    public void setYinTable(int yinTable) {
        YinTable = yinTable;
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

    private float getAngle() {
        return angle;
    }
    private void setAngle(float angle) {
        this.angle = angle;
    }

    public int getItemFacing() {
        return itemFacing;
    }
    public void setItemFacing(int itemFacing) {
        this.itemFacing = itemFacing;
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

    public void returnToStartPos(){
        this.setX(startPosX);
        this.setY(startPosY);
    }

    public boolean isInStartPos(){
        return x == startPosX && y == startPosY;
    }



    private static String getImageName(int ID) {
        switch(ID){
            case EMPTY_CELL: return "gray.png";
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

    public void rotate() {
        if (this.itemID == HALF_WOOD_BLOCK || this.itemID == HALF_STEEL_BLOCK) {
            this.setAngle(this.getAngle() - PIdiv2);
            System.out.println("-");
            switch (this.getItemFacing()) {
                case FACING_DOWNLEFT: {
                    this.setItemFacing(FACING_UPLEFT);
                    this.setY(this.getY() + BLOCK_SIZE);
                    System.out.println("-1");
                    break;
                }
                case FACING_UPLEFT: {
                    this.setItemFacing(FACING_UPRIGHT);
                    this.setX(this.getX() + BLOCK_SIZE);
                    System.out.println("-2");
                    break;
                }
                case FACING_UPRIGHT: {
                    this.setItemFacing(FACING_DOWNRIGHT);
                    this.setY(this.getY() - BLOCK_SIZE);
                    System.out.println("-3");
                    break;
                }
                case FACING_DOWNRIGHT: {
                    this.setItemFacing(FACING_DOWNLEFT);
                    this.setX(this.getX() - BLOCK_SIZE);
                    System.out.println("-4");
                    break;
                }
            }
        }
    }

    public void returnBaseRotation() {
        if (this.itemID == HALF_WOOD_BLOCK || this.itemID == HALF_STEEL_BLOCK) {
            System.out.println("+");
            switch (this.getItemFacing()) {
                case FACING_UPLEFT: {
                    this.setAngle(this.getAngle() + PIdiv2);
                    System.out.println("+1");
                    break;
                }
                case FACING_UPRIGHT: {
                    this.setAngle(this.getAngle() + 2*PIdiv2);
                    System.out.println("+2");
                    break;
                }
                case FACING_DOWNRIGHT: {
                    this.setAngle(this.getAngle() - PIdiv2);
                    System.out.println("+3");
                    break;
                }
            }
            this.setItemFacing(FACING_DOWNLEFT);
        }
    }

}
