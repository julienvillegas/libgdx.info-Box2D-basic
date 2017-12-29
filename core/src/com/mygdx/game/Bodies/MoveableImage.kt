package com.mygdx.game.Bodies

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.mygdx.game.Extra.ItemID.*
import com.mygdx.game.Extra.AssemblingScreenCoords.*


class MoveableImage(private val startPosX: Float, private val startPosY: Float, private var width: Float, private var height: Float, var angle: Int, texture: String) : Image(Texture(texture)) {

    var isMoving = false
    private var isTouchable = false
    var isAlreadyMoved = false
    private var x: Float = 0.toFloat()
    private var y: Float = 0.toFloat()
    private val number: Int
    var xinTable: Int = 0
    var yinTable: Int = 0


    val isInStartPos: Boolean
        get() = x == startPosX && y == startPosY

    init {
        number = getString(texture)
        xinTable = NULL
        yinTable = NULL
        this.x = startPosX
        this.y = startPosY
        if (number != TURBINE)
            this.setOrigin(BLOCK_SIZE / 2, height / 2)
        else
            this.setOrigin(width - BLOCK_SIZE / 2, height / 2)
    }

    override fun isTouchable(): Boolean {
        return isTouchable
    }
    fun setTouchable(touchable: Boolean) {
        isTouchable = touchable
    }

    private fun getString(texture: String): Int {
        return when (texture) {
            "woodblock.png" -> WOOD_BLOCK
            "steelblock.png" -> STEEL_BLOCK
            "engine.png" -> ENGINE
            "turbine.png" -> TURBINE
            "halfwoodblock.png" -> HALF_WOOD_BLOCK
            "halfsteelblock.png" -> HALF_STEEL_BLOCK
            "gun_1.png" -> WOOD_GUN
            "gun_2.png" -> STEEL_GUN
            else -> NULL
        }
    }

    fun getNumber(): Int {
        when (angle) {
            0 -> return number
            90 -> return number + UP
            180 -> return number + LEFT
            270 -> return number + DOWN
        }
        return number
    }

    override fun getX(): Float {
        return x
    }
    override fun setX(x: Float) {
        this.x = x
    }

    override fun getY(): Float {
        return y
    }
    override fun setY(y: Float) {
        this.y = y
    }

    fun setXY(x: Float, y: Float) {
        this.setX(x)
        this.setY(y)
    }
    fun setXYinTable(x: Int, y: Int) {
        this.xinTable = x
        this.yinTable = y
    }

    override fun getWidth(): Float {
        return width
    }
    override fun setWidth(width: Float) {
        this.width = width
    }

    override fun getHeight(): Float {
        return height
    }
    override fun setHeight(height: Float) {
        this.height = height
    }

    override fun act(delta: Float) {
        super.act(delta)
        this.rotation = angle.toFloat()
        this.setPosition(x, y)
    }

    fun contains(x: Float, y: Float): Boolean {
        when (angle) {
            0 -> return (x > this.x) &&
                    (x < this.x + this.width) &&
                    (y < SCREEN_HEIGHT - this.y) &&
                    (y > SCREEN_HEIGHT_F - this.y - this.height)
            90 -> return (x > this.x + originX - this.height + originY) &&
                    (x < this.x + originX + originY) &&
                    (y < SCREEN_HEIGHT_F - this.y - originY + originX) &&
                    (y > SCREEN_HEIGHT_F - this.y - originY + originX - this.width)
            180 -> return (x > this.x + 2 * originX - this.width) &&
                    (x < this.x + 2 * originX) &&
                    (y < SCREEN_HEIGHT - this.y) &&
                    (y > SCREEN_HEIGHT_F - this.y - this.height)
            270 -> return (x > this.x + originX - originY) &&
                    (x < this.x + originX + this.height - originY) &&
                    (y < SCREEN_HEIGHT_F - this.y - originY - originX + this.width) &&
                    (y > SCREEN_HEIGHT_F - this.y - originY - originX)
        }
        return false
    }

    fun returnToStartPos(currentX: Float, currentY: Float) {
        this.setX(currentX)
        this.setY(currentY)
    }
    fun returnToStartPos() {
        this.setX(startPosX)
        this.setY(startPosY)
    }

    fun flip90() {
        angle += 90
        if (angle == 360) angle = 0
    }


}
