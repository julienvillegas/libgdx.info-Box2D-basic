package com.mygdx.game.Extra;

public interface ItemID {

    // Здесь хранятся ID составных частей корабля

    int NUMBER_OF_ITEMS = 9;                        // Количество видов предметов

    int WOOD_BLOCK = 0;
    int STEEL_BLOCK = 1;
    int ENGINE = 2;
    int TURBINE = 3;
    int HALF_WOOD_BLOCK = 4;
    int HALF_STEEL_BLOCK = 5;
    int STEEL_GUN = 6;
    int WOOD_GUN = 7;
    int EYE = 8;
    int EYE2 = 9;

    int RIGHT = 0;
    int UP = 10;
    int LEFT = 20;
    int DOWN = 30;

    int NULL = 1000;

    // Здесь хранятся максимальные ХП для каждого из блоков

    float HP_WOOD_BLOCK = 2f;
    float HP_STEEL_BLOCK = 4f;
    float HP_ENGINE = 3f;
    float HP_TURBINE = 3f;
    float HP_HALF_WOOD_BLOCK = 1f;
    float HP_HALF_STEEL_BLOCK = 2f;
    float HP_STEEL_GUN = 5f;
    float HP_WOOD_GUN = 3f;
    float HP_EYE = 1f;
    float HP_EYE2 = 1f;

    // Здесь хранятся максимальное количество блоков каждого из видов в окне сборки

    int WOOD_BLOCK_MAXCNT = 8;
    int STEEL_BLOCK_MAXCNT = 4;
    int ENGINE_MAXCNT = 3;
    int TURBINE_MAXCNT = 2;
    int HALF_WOOD_BLOCK_MAXCNT = 4;
    int HALF_STEEL_BLOCK_MAXCNT = 2;
    int STEEL_GUN_MAXCNT = 1;
    int WOOD_GUN_MAXCNT = 2;
    int EYE_MAXCNT = 1;
    int EYE2_MAXCNT = 1;


    int[] ITEMS_MAX_CNT = {WOOD_BLOCK_MAXCNT, STEEL_BLOCK_MAXCNT, ENGINE_MAXCNT,
            TURBINE_MAXCNT, HALF_WOOD_BLOCK_MAXCNT, HALF_STEEL_BLOCK_MAXCNT,
            STEEL_GUN_MAXCNT, WOOD_GUN_MAXCNT, EYE_MAXCNT};

}
