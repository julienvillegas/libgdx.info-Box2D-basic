package com.mygdx.game.Screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.Extra.AssemblingScreenCoords
import com.mygdx.game.Extra.AssemblingScreenCoords.SCREEN_HEIGHT
import com.mygdx.game.Extra.AssemblingScreenCoords.SCREEN_WIDTH
import com.mygdx.game.MyGdxGame

class ShipChoosingScreen(private val game: Game) : Screen {

    private val stage: Stage
    var p1_ship = Array(AssemblingScreenCoords.FIELD_WIDTH) { IntArray(AssemblingScreenCoords.FIELD_HEIGHT) }
    var p2_ship = Array(AssemblingScreenCoords.FIELD_WIDTH) { IntArray(AssemblingScreenCoords.FIELD_HEIGHT) }
    fun TextButton.contains( x : Float, y : Float ) = ( x > this.x ) && ( x < this.x + this.width ) && (y < SCREEN_HEIGHT - this.y) &&
            (y > SCREEN_HEIGHT - this.y - this.height)

    fun setArray( arr : Array<IntArray> , i : Int  ){
        if (i == 1)
            p1_ship = arr
        else
            p2_ship = arr
    }

    fun getMe() = this

    init {
        stage = Stage(ScreenViewport())

        val p1shipbutton = TextButton("Create your ship",MyGdxGame.skin)
        p1shipbutton.setPosition( p1shipbutton.width / 4, AssemblingScreenCoords.BLOCK_SIZE / 2)
        p1shipbutton.addListener(object : InputListener() {

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = Menu(game, getMe(), 1)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })


        val p2shipbutton = TextButton("Create your ship",MyGdxGame.skin)
        p2shipbutton.setPosition(AssemblingScreenCoords.SCREEN_WIDTH - p2shipbutton.width * 5 / 4, AssemblingScreenCoords.BLOCK_SIZE / 2)

        p2shipbutton.addListener(object : InputListener() {

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                    game.screen = Menu(game,getMe(), 2)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        val playButton = TextButton("Start!", MyGdxGame.skin)
        playButton.width = (SCREEN_WIDTH / 2).toFloat()
        playButton.setPosition(SCREEN_WIDTH / 2 - playButton.width / 2, SCREEN_HEIGHT / 2 - playButton.height / 2)
        playButton.addListener(object : InputListener() {

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = GameScreen(p1_ship,p2_ship)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })
        stage.addActor(playButton)
        stage.addActor(p1shipbutton)
        stage.addActor(p2shipbutton)


    }




    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
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
        stage.dispose()
    }
}