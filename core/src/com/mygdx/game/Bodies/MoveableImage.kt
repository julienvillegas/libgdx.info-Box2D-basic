package com.mygdx.game.Bodies

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.mygdx.game.Extra.AssemblingScreenCoords
import com.mygdx.game.Extra.ItemID


class MoveableImage(private val startPosX: Float, private val startPosY: Float, private var width: Float, private var height: Float, var angle: Int, texture: String) : Image(Texture(texture)), ItemID, AssemblingScreenCoords {
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
        xinTable = 50
        yinTable = 50
        this.x = startPosX
        this.y = startPosY
        if (number != ItemID.TURBINE) {
            this.setOrigin(AssemblingScreenCoords.BLOCK_SIZE / 2, height / 2)
        } else {
            this.setOrigin(width - AssemblingScreenCoords.BLOCK_SIZE / 2, height / 2)
        }
    }

    override fun isTouchable(): Boolean {
        return isTouchable
    }

    fun setTouchable(touchable: Boolean) {
        isTouchable = touchable
    }

    fun getString(texture: String): Int {
        if (texture == "woodblock.png") {
            return 8
        }
        if (texture == "steelblock.png") {
            return ItemID.STEEL_BLOCK
        }
        if (texture == "engine.png") {
            return ItemID.ENGINE
        }
        if (texture == "turbine.png") {
            return ItemID.TURBINE
        }
        if (texture == "halfwoodblock.png") {
            return ItemID.HALF_WOOD_BLOCK
        }
        if (texture == "halfsteelblock.png") {
            return ItemID.HALF_STEEL_BLOCK
        }
        if (texture == "gun_1.png") {
            return ItemID.GUN_1
        }
        return if (texture == "gun_2.png") {
            ItemID.GUN_2
        } else
            ItemID.NULL

    }

    fun getNumber(): Int {
        when (angle) {
            0 -> return number
            90 -> return number + ItemID.UP
            180 -> return number + ItemID.LEFT
            270 -> return number + ItemID.DOWN
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

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
    }

    override fun act(delta: Float) {
        super.act(delta)
        this.rotation = angle.toFloat()
        this.setPosition(x, y)
    }

    fun contains(x: Float, y: Float): Boolean {
        when (angle) {
            0 -> return x > this.x && x < this.x + this.width && y < AssemblingScreenCoords.SCREEN_HEIGHT - this.y && y > AssemblingScreenCoords.SCREEN_HEIGHT.toFloat() - this.y - this.height
            90 -> return x > this.x + originX - this.height + originY && x < this.x + originX + originY && y < AssemblingScreenCoords.SCREEN_HEIGHT.toFloat() - this.y - originY + originX && y > AssemblingScreenCoords.SCREEN_HEIGHT.toFloat() - this.y - originY + originX - this.width
            180 -> return x > this.x + 2 * originX - this.width && x < this.x + 2 * originX && y < AssemblingScreenCoords.SCREEN_HEIGHT - this.y && y > AssemblingScreenCoords.SCREEN_HEIGHT.toFloat() - this.y - this.height
            270 -> return x > this.x + originX - originY && x < this.x + originX + this.height - originY && y < AssemblingScreenCoords.SCREEN_HEIGHT.toFloat() - this.y - originY - originX + this.width && y > AssemblingScreenCoords.SCREEN_HEIGHT.toFloat() - this.y - originY - originX
        }
        return false
    }

    fun returnToStartPos() {
        this.setX(startPosX)
        this.setY(startPosY)
    }

    fun flip90() {
        angle += 90
        if (angle == 360) {
            angle = 0
        }
    }


}
