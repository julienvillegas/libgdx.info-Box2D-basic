package com.mygdx.game.Bodies;

import static com.mygdx.game.Extra.ItemID.ENGINE;
import static com.mygdx.game.Extra.ItemID.EYE;
import static com.mygdx.game.Extra.ItemID.HALF_STEEL_BLOCK;
import static com.mygdx.game.Extra.ItemID.HALF_WOOD_BLOCK;
import static com.mygdx.game.Extra.ItemID.HP;
import static com.mygdx.game.Extra.ItemID.STEEL_BLOCK;
import static com.mygdx.game.Extra.ItemID.STEEL_GUN;
import static com.mygdx.game.Extra.ItemID.TURBINE;
import static com.mygdx.game.Extra.ItemID.WOOD_BLOCK;
import static com.mygdx.game.Extra.ItemID.WOOD_GUN;

public class MyBlock {
    float hp = 999f;
    int type;

    public MyBlock(int type) {
        this.type = type;
        switch (type){
            case -2: this.hp = -2; break;
            case -1: this.hp = -1; break;
            case WOOD_BLOCK: this.hp = HP; break;
            case STEEL_BLOCK: this.hp = HP*2; break;
            case ENGINE: this.hp = HP*3/2; break;
            case TURBINE: this.hp = HP*3/2; break;
            case HALF_WOOD_BLOCK: this.hp = HP/2; break;
            case HALF_STEEL_BLOCK: this.hp = HP; break;
            case STEEL_GUN: this.hp = HP*2+1; break;
            case WOOD_GUN: this.hp = HP+1; break;
            case  EYE: this.hp = HP/2;break;
        }
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
