package com.mygdx.game.Extra;

public interface ItemID {

    // Здесь хранятся ID составных частей корабля

    int NUMBER_OF_ITEMS = 8;                        // Количество видов предметов

    int MISSING = -1;                       // Отсутсвие текстуры
    int WOOD_BLOCK = 0;
    int STEEL_BLOCK = 1;
    int ENGINE = 2;
    int TURBINE = 3;
    int HALF_WOOD_BLOCK = 4;
    int HALF_STEEL_BLOCK = 5;
    int GUN_1 = 6;
    int GUN_2 = 7;
    int EMPTY_CELL = 8;

    int FACING_DOWNLEFT = 0;                // Объект расположен углом вниз-влево
    int FACING_UPLEFT = 1;                  // - вверх-влево
    int FACING_UPRIGHT = 2;                 // - вверх-вправо
    int FACING_DOWNRIGHT = 3;               // - вниз-вправо

}
