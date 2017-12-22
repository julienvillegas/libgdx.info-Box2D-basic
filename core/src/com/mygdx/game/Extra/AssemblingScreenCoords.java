package com.mygdx.game.Extra;

import com.badlogic.gdx.Gdx;

import static com.mygdx.game.Extra.ItemID.NUMBER_OF_ITEMS;

public interface AssemblingScreenCoords {

    // Здесь хранятся координаты для вёрстки окна со сборкой корабля

    int SCREEN_HEIGHT = Gdx.graphics.getHeight();                                       // Высота экрана
    int SCREEN_WIDTH = Gdx.graphics.getWidth();                                         // Ширина экрана
    float SCREEN_HEIGHT_F = Gdx.graphics.getHeight();                                   // То же, но float

    int FIELD_HEIGHT = 6;                                                               // Высота поля сборки (в блоках)
    int FIELD_WIDTH = 8;                                                                // Ширина поля сборки (в блоках)

    float BLOCK_SIZE = Min.min((float) (SCREEN_HEIGHT / (FIELD_HEIGHT + 2)), (float) (SCREEN_WIDTH / (FIELD_WIDTH + 4)), (float) (SCREEN_HEIGHT / (NUMBER_OF_ITEMS + 2)));  // Размер одного блока
    float FIELD_DELTA_Y = (SCREEN_HEIGHT - BLOCK_SIZE * FIELD_HEIGHT) / 2;              // Вертикальный отступ поля сборки
    float FIELD_DELTA_X = (SCREEN_WIDTH - BLOCK_SIZE * FIELD_WIDTH) / 2;                // Горизонтальный отступ поля сборки

}
