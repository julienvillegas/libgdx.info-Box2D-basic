package com.mygdx.game.Extra;

import com.badlogic.gdx.Gdx;

public interface AssemblingScreenCoords {

    // Здесь хранятся координаты для вёрстки окна со сборкой корабля

    int SCREEN_HEIGHT = Gdx.graphics.getHeight();                                       // Высота экрана
    int SCREEN_WIDTH = Gdx.graphics.getWidth();                                         // Ширина экрана

    int FIELD_HEIGHT = 5;                                                               // Высота поля сборки (в блоках)
    int FIELD_WIDTH = 6;                                                                // Ширина поля сборки (в блоках)

    float BLOCK_SIZE = SCREEN_HEIGHT / (FIELD_HEIGHT + 2);                              // Размер одного блока
    float FIELD_DELTA_Y = (SCREEN_HEIGHT - BLOCK_SIZE * FIELD_HEIGHT) / 2;              // Вертикальный отступ поля сборки
    float FIELD_DELTA_X = (SCREEN_WIDTH - BLOCK_SIZE * FIELD_WIDTH) / 2;                // Горизонтальный отступ поля сборки


}
