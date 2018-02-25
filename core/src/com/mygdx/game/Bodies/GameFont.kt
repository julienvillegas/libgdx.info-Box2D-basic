package com.mygdx.game.Bodies

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.mygdx.game.Extra.FontID.Companion.JURA_DEMIBOLD
import com.mygdx.game.Extra.FontID.Companion.JURA_MEDIUM

class GameFont(font: Int, size: Int, color: Color) {

    private var bitmapFont: BitmapFont
    private var labelStyle: Label.LabelStyle

    init {
        val gen = FreeTypeFontGenerator(Gdx.files.internal(getInternalPath(font)))
        val param = FreeTypeFontParameter()
        param.size = size
        param.color = color
        val resBitmapFont = gen.generateFont(param)
        gen.dispose()

        this.bitmapFont = resBitmapFont
        this.labelStyle = Label.LabelStyle(resBitmapFont, color)
    }

    fun getBF(): BitmapFont {
        return this.bitmapFont
    }
    fun getLS(): Label.LabelStyle {
        return this.labelStyle
    }

    private fun getInternalPath(font: Int): String {
        return when (font) {
            JURA_MEDIUM -> "JuraMedium.ttf"
            JURA_DEMIBOLD -> "JuraDemiBold.ttf"
            else -> ""
        }
    }

}