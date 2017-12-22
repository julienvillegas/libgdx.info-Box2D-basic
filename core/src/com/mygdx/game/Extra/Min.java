package com.mygdx.game.Extra;

abstract class Min {

    static float min(float a, float b, float c) {
        return min(min(a, b), c);
    }

    static float min(float a, float b) {
        return (a > b)? b : a;
    }

}
