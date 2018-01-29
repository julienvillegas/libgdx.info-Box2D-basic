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

}
