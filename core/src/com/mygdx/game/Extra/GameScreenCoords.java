package com.mygdx.game.Extra;

import static com.mygdx.game.Extra.AssemblingScreenCoords.SCREEN_HEIGHT;
import static com.mygdx.game.Extra.AssemblingScreenCoords.SCREEN_WIDTH;

public interface GameScreenCoords  {

    // Базовые константы

    float SCALE = 0.005f;                                      // Константа, переводящая GDX-овские размеры объектов в игровые
    float HEIGHT_IN_UNITS = 80f;                               // Высота экрана в клетках
    float UNIT_SIZE = SCREEN_HEIGHT / HEIGHT_IN_UNITS;         // Размер 1 клетки при отрисовке текстур
    float WIDTH_IN_UNITS = SCREEN_WIDTH / UNIT_SIZE;           // Ширина экрана в клетках
    float BUTTON_RADIUS = 9.6f;                                  // Размер кнопки в клетках



    // Пропорции всплывающих окон

    float POPUP_SCREEN_PROP = 768f / 530f;

    float POPUP_BUTTON_RADIUS_PROP = 445f / 1661f / 2f;
    float POPUP_BUTTON1_DELTA_X_PROP = 272f / 1661f;
    float POPUP_BUTTON2_DELTA_X_PROP = 771f / 1661f;



    // Размеры и координаты всплывающих окон

    float PAUSE_BTN_SIZE = WIDTH_IN_UNITS * 0.06f;
    float PAUSE_BTN_DELTA_X = (WIDTH_IN_UNITS - PAUSE_BTN_SIZE) / 2;
    float PAUSE_BTN_DELTA_Y = HEIGHT_IN_UNITS - PAUSE_BTN_SIZE;

    float PAUSE_SCREEN_WIDTH = Math.min(WIDTH_IN_UNITS * 0.6f,
            HEIGHT_IN_UNITS * 0.6f * POPUP_SCREEN_PROP);
    float PAUSE_SCREEN_HEIGHT = Math.min(HEIGHT_IN_UNITS * 0.6f,
            WIDTH_IN_UNITS * 0.6f / POPUP_SCREEN_PROP);
    float PAUSE_SCREEN_DELTA_X = (WIDTH_IN_UNITS - PAUSE_SCREEN_WIDTH) / 2f;
    float PAUSE_SCREEN_DELTA_Y = (HEIGHT_IN_UNITS - PAUSE_SCREEN_HEIGHT) / 2f;

    float PLAYER1WON_SCREEN_WIDTH = Math.min(WIDTH_IN_UNITS * 0.6f,
            HEIGHT_IN_UNITS * 0.6f * POPUP_SCREEN_PROP);
    float PLAYER1WON_SCREEN_HEIGHT = Math.min(HEIGHT_IN_UNITS * 0.6f,
            WIDTH_IN_UNITS * 0.6f / POPUP_SCREEN_PROP);
    float PLAYER1WON_SCREEN_DELTA_X = (WIDTH_IN_UNITS - PAUSE_SCREEN_WIDTH) / 2f;
    float PLAYER1WON_SCREEN_DELTA_Y = (HEIGHT_IN_UNITS - PAUSE_SCREEN_HEIGHT) / 2f;

    float PLAYER2WON_SCREEN_WIDTH = Math.min(WIDTH_IN_UNITS * 0.6f,
            HEIGHT_IN_UNITS * 0.6f * POPUP_SCREEN_PROP);
    float PLAYER2WON_SCREEN_HEIGHT = Math.min(HEIGHT_IN_UNITS * 0.6f,
            WIDTH_IN_UNITS * 0.6f / POPUP_SCREEN_PROP);
    float PLAYER2WON_SCREEN_DELTA_X = (WIDTH_IN_UNITS - PAUSE_SCREEN_WIDTH) / 2f;
    float PLAYER2WON_SCREEN_DELTA_Y = (HEIGHT_IN_UNITS - PAUSE_SCREEN_HEIGHT) / 2f;

    float TIMEOUT_SCREEN_WIDTH = Math.min(WIDTH_IN_UNITS * 0.6f,
            HEIGHT_IN_UNITS * 0.6f * POPUP_SCREEN_PROP);
    float TIMEOUT_SCREEN_HEIGHT = Math.min(HEIGHT_IN_UNITS * 0.6f,
            WIDTH_IN_UNITS * 0.6f / POPUP_SCREEN_PROP);
    float TIMEOUT_SCREEN_DELTA_X = (WIDTH_IN_UNITS - PAUSE_SCREEN_WIDTH) / 2f;
    float TIMEOUT_SCREEN_DELTA_Y = (HEIGHT_IN_UNITS - PAUSE_SCREEN_HEIGHT) / 2f;


    float POPUP_BTN_RADIUS = PAUSE_SCREEN_WIDTH * POPUP_BUTTON_RADIUS_PROP;
    float POPUP_BTN1_X = PAUSE_SCREEN_DELTA_X + PAUSE_SCREEN_WIDTH * POPUP_BUTTON1_DELTA_X_PROP + POPUP_BTN_RADIUS;
    float POPUP_BTN2_X = PAUSE_SCREEN_DELTA_X + PAUSE_SCREEN_WIDTH * POPUP_BUTTON2_DELTA_X_PROP + POPUP_BTN_RADIUS;
    float POPUP_BTN_Y = PAUSE_BTN_DELTA_Y - 2*POPUP_BTN_RADIUS;

}
