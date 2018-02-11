package com.mygdx.game.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.mygdx.game.Extra.AssemblingScreenCoords.SCREEN_HEIGHT
import com.mygdx.game.Extra.AssemblingScreenCoords.SCREEN_WIDTH
import com.mygdx.game.MyGdxGame

class Test : Screen {
    private var stage: Stage? = null
    private var container: Table

    init {
        stage = Stage()
        val skin = MyGdxGame.skin
        Gdx.input.inputProcessor = stage

        // Gdx.graphics.setVSync(false);

         container = Table()

        stage!!.addActor(container.apply {
            setFillParent(true)
            background = TextureRegionDrawable(TextureRegion(Texture("background.png")))

            })
            val table = Table()

            for (i in 0..99) {
                table.row()
                val button1 = TextButton(i.toString() + "one", skin)
                val button2 = TextButton(i.toString() + "two", skin)
                val button3 = TextButton(i.toString() + "three", skin)
                table.add(button1).width(300.toFloat())
                table.add(button2).width(300.toFloat())
                table.add(button3).width(300.toFloat())
            }
            val scroll = ScrollPane(table, skin)
            scroll.setFadeScrollBars(true)
            container.add(scroll).height(SCREEN_HEIGHT/2.toFloat()).width(SCREEN_WIDTH/2.toFloat())
    }

    fun ScrollPane.hit(x: Float, y: Float): ScrollPane? {
        return if (x > 0 && x < width && y > 0 && y < height) this else null
    }




    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage!!.act()
        stage!!.draw()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        stage!!.dispose()
    }
}