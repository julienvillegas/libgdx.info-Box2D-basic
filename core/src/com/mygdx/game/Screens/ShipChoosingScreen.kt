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
import com.mygdx.game.Extra.AssemblingScreenCoords.*
import com.mygdx.game.Extra.ItemID
import com.mygdx.game.Extra.ItemID.NULL
import com.mygdx.game.Extra.ItemID.NUMBER_OF_ITEMS
import com.mygdx.game.MyGdxGame

class ShipChoosingScreen(private val game: Game) : Screen {

    private val stage: Stage = Stage(ScreenViewport())

    var p1_ship = setPrimaryShip()
    var p2_ship = setPrimaryShip()
    var p1_inventory = setPrimaryInventory()
    var p2_inventory = setPrimaryInventory()


    fun getMe() = this

    init {

        val p1shipbutton = TextButton("Create 1st ship", MyGdxGame.skin)
        val p2shipbutton = TextButton("Create 2nd ship", MyGdxGame.skin)
        val playButton = TextButton("Start!", MyGdxGame.skin)

        playButton.width = (SCREEN_WIDTH / 2).toFloat()

        p1shipbutton.setPosition( p1shipbutton.width / 4, AssemblingScreenCoords.BLOCK_SIZE / 2)
        p2shipbutton.setPosition(AssemblingScreenCoords.SCREEN_WIDTH - p2shipbutton.width * 5 / 4, AssemblingScreenCoords.BLOCK_SIZE / 2)
        playButton.setPosition(SCREEN_WIDTH / 2 - playButton.width / 2, SCREEN_HEIGHT / 2 - playButton.height / 2)


        p1shipbutton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = Menu(game, getMe(), 1, p1_ship, p1_inventory)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        p2shipbutton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = Menu(game,getMe(), 2, p2_ship, p2_inventory)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        playButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = GameScreen(p1_ship, p2_ship)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })


        stage.addActor(p1shipbutton)
        stage.addActor(p2shipbutton)
        stage.addActor(playButton)

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



    fun TextButton.contains( x : Float, y : Float ) = ( x > this.x ) && ( x < this.x + this.width ) && (y < SCREEN_HEIGHT - this.y) &&
            (y > SCREEN_HEIGHT - this.y - this.height)


    private fun setPrimaryShip(): Array<IntArray> {                                                 // Задаёт изначальный вариант корабля (т.е. пустое поле)
        val resShip = Array(FIELD_WIDTH) { IntArray(FIELD_HEIGHT) }
        for (i in 0 until FIELD_WIDTH) {
            for (j in 0 until FIELD_HEIGHT) {
                resShip[i][j] = NULL
            }
        }
        return resShip
    }                                             // Задаёт изначальный корабль (т.е. пустое поле)
    private fun setPrimaryInventory(): IntArray {
        val inventory = IntArray(NUMBER_OF_ITEMS)
        for (i in 0 until ItemID.NUMBER_OF_ITEMS)
            inventory[i] = ItemID.ITEMS_MAX_CNT[i]
        return inventory
    }                                               // Задаёт изначальное количество предметов для расстановки

    fun setShipData(ship: Array<IntArray>, player: Int, inventory: IntArray) {
        when (player) {
            1 -> {
                p1_ship = ship
                p1_inventory = inventory
            }
            2 -> {
                p2_ship = ship
                p2_inventory = inventory
            }
        }
    }

}