package com.mygdx.game.Screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.Bodies.MoveableImage
import com.mygdx.game.Extra.AssemblingScreenCoords
import com.mygdx.game.Extra.ItemID
import com.mygdx.game.MyGdxGame

import java.util.ArrayList

class Menu internal constructor(private val game: Game) : Screen, InputProcessor, ItemID, AssemblingScreenCoords {

    private val stage: Stage

    private var oldX = 0f
    private var oldY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private var lastX = 0f
    private var lastY = 0f
    private var lastXInTable = 0
    private var lastYInTable = 0
    private var delta_flag = ItemID.NULL

    private val blockArr = Array(AssemblingScreenCoords.FIELD_WIDTH) { IntArray(AssemblingScreenCoords.FIELD_HEIGHT) }
    private val blocks = ArrayList<Array<MoveableImage?>>()
    private val cells = Array<Array<MoveableImage?>>(AssemblingScreenCoords.FIELD_WIDTH) { arrayOfNulls(AssemblingScreenCoords.FIELD_HEIGHT) }
    private val invCells = ArrayList<MoveableImage>()
    private val occupiedCells = Array(AssemblingScreenCoords.FIELD_WIDTH) { BooleanArray(AssemblingScreenCoords.FIELD_HEIGHT) }
    private val labels = arrayOfNulls<Label>(ItemID.NUMBER_OF_ITEMS)

    private var currI = ItemID.NULL
    private var currJ = ItemID.NULL                                                         // Предмет, который мы сейчас перетаскиваем
    private var isImageDragging = false                                                        // Перетаскиваем ли мы какой-нибудь из предметов

    private val inventory = setPrimaryInventory()                                                // Инвентарь игрока


    init {
        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = this

        for (i in 0 until AssemblingScreenCoords.FIELD_WIDTH) {
            for (j in 0 until AssemblingScreenCoords.FIELD_HEIGHT) {
                cells[i][j] = MoveableImage(AssemblingScreenCoords.FIELD_DELTA_X + AssemblingScreenCoords.BLOCK_SIZE * i, AssemblingScreenCoords.FIELD_DELTA_Y + AssemblingScreenCoords.BLOCK_SIZE * j, AssemblingScreenCoords.BLOCK_SIZE, AssemblingScreenCoords.BLOCK_SIZE, 0, "gray.png")
                stage.addActor(cells[i][j])
                blockArr[i][j] = ItemID.NULL
                occupiedCells[i][j] = false
            }
        }

        for (itemsCount in inventory)
            blocks.add(arrayOfNulls(itemsCount))

        for (i in blocks.indices) {
            if (i == ItemID.TURBINE || i == ItemID.STEEL_GUN || i == ItemID.WOOD_GUN) {
                invCells.add(MoveableImage(getPosX(i, "invCell_left"), getPosY(i, "invCell"), AssemblingScreenCoords.BLOCK_SIZE, AssemblingScreenCoords.BLOCK_SIZE, 0, "gray.png"))
                invCells.add(MoveableImage(getPosX(i, "invCell_right"), getPosY(i, "invCell"), AssemblingScreenCoords.BLOCK_SIZE, AssemblingScreenCoords.BLOCK_SIZE, 0, "gray.png"))
                stage.addActor(invCells[invCells.size - 2])
                stage.addActor(invCells[invCells.size - 1])
            } else {
                invCells.add(MoveableImage(getPosX(i, "invCell_mid"), getPosY(i, "invCell"), AssemblingScreenCoords.BLOCK_SIZE, AssemblingScreenCoords.BLOCK_SIZE, 0, "gray.png"))
                stage.addActor(invCells[invCells.size - 1])
            }

            for (j in 0 until blocks[i].size) {
                blocks[i][blocks[i].size - j - 1] = MoveableImage(getPosX(i, "item"), getPosY(i, "item"), getWidth(i), getHeight(i), 0, getImageName(i))
            }
            blocks[i][0]!!.isTouchable = true
            stage.addActor(blocks[i][0])
        }

        val itemsCountBF = BitmapFont()
        itemsCountBF.data.scale(2f)
        val itemsCountLS = Label.LabelStyle(itemsCountBF, Color.BLACK)

        for (i in labels.indices) {
            labels[i] = Label("x" + blocks[i].size, itemsCountLS)
            labels[i]!!.setX(getPosX(i, "label"))
            labels[i]!!.setY(getPosY(i, "label"))
            stage.addActor(labels[i])
        }

        val button = TextButton("Start!", MyGdxGame.skin)
        button.width = (AssemblingScreenCoords.SCREEN_WIDTH / 4).toFloat()
        button.setPosition(AssemblingScreenCoords.SCREEN_WIDTH - button.width * 3 / 2, AssemblingScreenCoords.BLOCK_SIZE / 2)
        button.addListener(object : InputListener() {

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                val arr = Array(AssemblingScreenCoords.FIELD_WIDTH) { IntArray(AssemblingScreenCoords.FIELD_HEIGHT) }
                for (i in 0 until AssemblingScreenCoords.FIELD_WIDTH)
                    for (j in 0 until AssemblingScreenCoords.FIELD_HEIGHT)
                        arr[i][j] = blockArr[i][AssemblingScreenCoords.FIELD_HEIGHT - 1 - j]
                game.screen = GameScreen(arr, arr)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        stage.addActor(button)
    }


    private fun getPosX(i: Int, typeOfObj: String): Float {
        if (typeOfObj == "item") {
            return if (i < ItemID.NUMBER_OF_ITEMS / 2)
                if (i == ItemID.TURBINE) AssemblingScreenCoords.BLOCK_SIZE * 5 / 2 - getWidth(i) else AssemblingScreenCoords.BLOCK_SIZE
            else if (i < ItemID.NUMBER_OF_ITEMS - 1)
                if (i == ItemID.STEEL_GUN || i == ItemID.WOOD_GUN) AssemblingScreenCoords.SCREEN_WIDTH - AssemblingScreenCoords.BLOCK_SIZE * 5 / 2 else AssemblingScreenCoords.SCREEN_WIDTH - AssemblingScreenCoords.BLOCK_SIZE * 2
            else
                AssemblingScreenCoords.SCREEN_WIDTH / 2 - AssemblingScreenCoords.BLOCK_SIZE / 2
        }

        if (typeOfObj == "invCell_mid")
            return if (i < ItemID.NUMBER_OF_ITEMS - 1)
                if (i < ItemID.NUMBER_OF_ITEMS / 2) AssemblingScreenCoords.BLOCK_SIZE else AssemblingScreenCoords.SCREEN_WIDTH - AssemblingScreenCoords.BLOCK_SIZE * 2
            else
                AssemblingScreenCoords.SCREEN_WIDTH / 2 - AssemblingScreenCoords.BLOCK_SIZE / 2

        if (typeOfObj == "invCell_left")
            return if (i < ItemID.NUMBER_OF_ITEMS / 2) AssemblingScreenCoords.BLOCK_SIZE / 2 else AssemblingScreenCoords.SCREEN_WIDTH - AssemblingScreenCoords.BLOCK_SIZE * 5 / 2

        if (typeOfObj == "invCell_right")
            return if (i < ItemID.NUMBER_OF_ITEMS / 2) AssemblingScreenCoords.BLOCK_SIZE * 3 / 2 else AssemblingScreenCoords.SCREEN_WIDTH - AssemblingScreenCoords.BLOCK_SIZE * 3 / 2

        return if (typeOfObj == "label") {
            if (i < ItemID.NUMBER_OF_ITEMS / 2)
                if (i == ItemID.TURBINE) getPosX(i, "invCell_right") + AssemblingScreenCoords.BLOCK_SIZE * 4 / 3 else getPosX(i, "invCell_mid") + AssemblingScreenCoords.BLOCK_SIZE * 4 / 3
            else
                if (i == ItemID.STEEL_GUN || i == ItemID.WOOD_GUN) getPosX(i, "invCell_left") - AssemblingScreenCoords.BLOCK_SIZE * 11 / 15 else getPosX(i, "invCell_mid") - AssemblingScreenCoords.BLOCK_SIZE * 11 / 15
        } else 0f

    }

    private fun getPosY(i: Int, typeOfObj: String): Float {
        if (typeOfObj == "item")
            return if (i < ItemID.NUMBER_OF_ITEMS - 1)
                if (i < ItemID.NUMBER_OF_ITEMS / 2) AssemblingScreenCoords.BLOCK_SIZE * (i + 2).toFloat() * 3f / 2 - getHeight(i) / 2 else AssemblingScreenCoords.BLOCK_SIZE * (i - 2).toFloat() * 3f / 2 - getHeight(i) / 2
            else
                AssemblingScreenCoords.SCREEN_HEIGHT - getHeight(i) * 3 / 2

        if (typeOfObj == "invCell")
            return if (i < ItemID.NUMBER_OF_ITEMS - 1)
                if (i < ItemID.NUMBER_OF_ITEMS / 2) AssemblingScreenCoords.BLOCK_SIZE * (i + 2).toFloat() * 3f / 2 - AssemblingScreenCoords.BLOCK_SIZE / 2 else AssemblingScreenCoords.BLOCK_SIZE * (i - 2).toFloat() * 3f / 2 - AssemblingScreenCoords.BLOCK_SIZE / 2
            else
                AssemblingScreenCoords.SCREEN_HEIGHT - getHeight(i) * 3 / 2

        return if (typeOfObj == "label") getPosY(i, "invCell") + AssemblingScreenCoords.BLOCK_SIZE / 5 else 0f

    }

    private fun getWidth(i: Int): Float {
        when (i) {
            ItemID.STEEL_GUN -> return 765 * AssemblingScreenCoords.BLOCK_SIZE / 345
            ItemID.WOOD_GUN -> return 770 * AssemblingScreenCoords.BLOCK_SIZE / 345
            ItemID.TURBINE -> return 565 * AssemblingScreenCoords.BLOCK_SIZE / 345
        }
        return AssemblingScreenCoords.BLOCK_SIZE
    }

    private fun getHeight(i: Int): Float {
        when (i) {
            ItemID.STEEL_GUN -> return 315 * AssemblingScreenCoords.BLOCK_SIZE / 345
            ItemID.WOOD_GUN -> return 194 * AssemblingScreenCoords.BLOCK_SIZE / 345
        }
        return AssemblingScreenCoords.BLOCK_SIZE
    }


    private fun plus1ToLabel(index: Int) {
        labels[index]!!.setText("x" + (Integer.parseInt(labels[index]!!.getText().toString().substring(1)) + 1))
    }

    private fun minus1FromLabel(index: Int) {
        labels[index]!!.setText("x" + (Integer.parseInt(labels[index]!!.getText().toString().substring(1)) - 1))
    }


    private fun getImageName(i: Int): String {
        when (i) {
            ItemID.WOOD_BLOCK -> return "woodblock.png"
            ItemID.STEEL_BLOCK -> return "steelblock.png"
            ItemID.ENGINE -> return "engine.png"
            ItemID.TURBINE -> return "turbine.png"
            ItemID.HALF_WOOD_BLOCK -> return "halfwoodblock.png"
            ItemID.HALF_STEEL_BLOCK -> return "halfsteelblock.png"
            ItemID.STEEL_GUN -> return "gun_2.png"
            ItemID.WOOD_GUN -> return "gun_1.png"
            ItemID.EYE -> return "eye.png"
        }
        return ""
    }

    private fun setPrimaryInventory(): IntArray {
        val inventory = IntArray(9)
        inventory[ItemID.WOOD_BLOCK] = 8
        inventory[ItemID.STEEL_BLOCK] = 4
        inventory[ItemID.ENGINE] = 3
        inventory[ItemID.TURBINE] = 2
        inventory[ItemID.HALF_WOOD_BLOCK] = 4
        inventory[ItemID.HALF_STEEL_BLOCK] = 2
        inventory[ItemID.STEEL_GUN] = 1
        inventory[ItemID.WOOD_GUN] = 2
        inventory[ItemID.EYE] = 1
        return inventory
    }                                                       // Задаёт изначальное количество предметов для расстановки

    private fun setCoordsFromCell(x: Float, y: Float, relativeX: Float, relativeY: Float) {
        var imgCenterX = AssemblingScreenCoords.BLOCK_SIZE / 2
        var imgCenterY = AssemblingScreenCoords.BLOCK_SIZE / 2
        val ID = blocks[currI][currJ]!!.getNumber() % 10
        val facing = blocks[currI][currJ]!!.getNumber() / 10 * 10

        if ((ID == ItemID.STEEL_GUN || ID == ItemID.WOOD_GUN) && facing == ItemID.RIGHT && relativeX > AssemblingScreenCoords.BLOCK_SIZE || ID == ItemID.TURBINE && facing == ItemID.LEFT && relativeX > getWidth(ItemID.TURBINE)) {
            imgCenterX -= AssemblingScreenCoords.BLOCK_SIZE
            delta_flag = ItemID.LEFT
        }
        if (((ID == ItemID.STEEL_GUN || ID == ItemID.WOOD_GUN) && facing == ItemID.UP || ID == ItemID.TURBINE && facing == ItemID.DOWN) && relativeY > AssemblingScreenCoords.BLOCK_SIZE) {
            imgCenterY -= AssemblingScreenCoords.BLOCK_SIZE
            delta_flag = ItemID.DOWN
        }
        if ((ID == ItemID.STEEL_GUN || ID == ItemID.WOOD_GUN) && facing == ItemID.LEFT && relativeX < 0 || ID == ItemID.TURBINE && facing == ItemID.RIGHT && relativeX < getWidth(ItemID.TURBINE) - AssemblingScreenCoords.BLOCK_SIZE) {
            imgCenterX += AssemblingScreenCoords.BLOCK_SIZE
            delta_flag = ItemID.RIGHT
        }
        if (((ID == ItemID.STEEL_GUN || ID == ItemID.WOOD_GUN) && facing == ItemID.DOWN || ID == ItemID.TURBINE && facing == ItemID.UP) && relativeY < 0) {
            imgCenterY += AssemblingScreenCoords.BLOCK_SIZE
            delta_flag = ItemID.UP
        }

        blocks[currI][currJ]!!.setXY(x + imgCenterX - blocks[currI][currJ]!!.originX, y + imgCenterY - blocks[currI][currJ]!!.originY)
    }


    private fun initCurrentImage(i: Int, j: Int) {
        isImageDragging = true
        currI = i
        currJ = j
    }                                               // Запоминает предмет, которое мы сейчас перетаскиваем

    private fun resetCurrentImage() {
        isImageDragging = false
        currI = ItemID.NULL
        currJ = ItemID.NULL
    }                                                          // Сбрасывает перетаскиваемый предмет


    private fun setEndPosition(x: Int, y: Int): Boolean {
        val relativeX = x - blocks[currI][currJ]!!.x
        val relativeY = AssemblingScreenCoords.SCREEN_HEIGHT.toFloat() - y.toFloat() - blocks[currI][currJ]!!.y
        var i = Math.floor(((x - AssemblingScreenCoords.FIELD_DELTA_X) / AssemblingScreenCoords.BLOCK_SIZE).toDouble()).toInt()
        var j = AssemblingScreenCoords.FIELD_HEIGHT - 1 - Math.floor(((y - AssemblingScreenCoords.FIELD_DELTA_Y) / AssemblingScreenCoords.BLOCK_SIZE).toDouble()).toInt()

        if (i >= 0 && i < AssemblingScreenCoords.FIELD_WIDTH && j >= 0 && j < AssemblingScreenCoords.FIELD_HEIGHT) {
            if (blockArr[i][j] == ItemID.NULL) {
                setCoordsFromCell(cells[i][j]!!.x, cells[i][j]!!.y, relativeX, relativeY)
                when (delta_flag) {
                    ItemID.RIGHT -> i++
                    ItemID.UP -> j++
                    ItemID.LEFT -> i--
                    ItemID.DOWN -> j--
                }
                delta_flag = ItemID.NULL
                if (i != -1 && i != AssemblingScreenCoords.FIELD_WIDTH && j != -1 && j != AssemblingScreenCoords.FIELD_HEIGHT) {
                    if (!isImgOutOfBounds(i, j)) {
                        if (!occupiedCells[i][j] && !isNearCellOccupied(i, j)) {
                            setImageOnTable(i, j)
                            return true
                        } else {
                            returnImageBack()
                            return false
                        }
                    } else {
                        returnImageBack()
                        return false
                    }
                } else {
                    returnImageBack()
                    return false
                }
            } else {
                returnImageBack()
                return false
            }
        } else {
            blocks[currI][currJ]!!.angle = 0
            blocks[currI][currJ]!!.returnToStartPos()
            return false
        }
    }

    private fun safeRotate() {
        val x = blocks[currI][currJ]!!.xinTable
        val y = blocks[currI][currJ]!!.yinTable
        do
            blocks[currI][currJ]!!.flip90()
        while (isImgOutOfBounds(x, y) || isNearCellOccupied(x, y))
    }

    private fun isImgOutOfBounds(x: Int, y: Int): Boolean {
        val ID = blocks[currI][currJ]!!.getNumber() % 10
        val facing = blocks[currI][currJ]!!.getNumber() / 10 * 10
        return ((ID == ItemID.STEEL_GUN || ID == ItemID.WOOD_GUN) && facing == ItemID.LEFT || ID == ItemID.TURBINE && facing == ItemID.RIGHT) && x == 0 ||
                ((ID == ItemID.STEEL_GUN || ID == ItemID.WOOD_GUN) && facing == ItemID.RIGHT || ID == ItemID.TURBINE && facing == ItemID.LEFT) && x == AssemblingScreenCoords.FIELD_WIDTH - 1 ||
                ((ID == ItemID.STEEL_GUN || ID == ItemID.WOOD_GUN) && facing == ItemID.DOWN || ID == ItemID.TURBINE && facing == ItemID.UP) && y == 0 ||
                ((ID == ItemID.STEEL_GUN || ID == ItemID.WOOD_GUN) && facing == ItemID.UP || ID == ItemID.TURBINE && facing == ItemID.DOWN) && y == AssemblingScreenCoords.FIELD_HEIGHT - 1
    }

    private fun isNearCellOccupied(x: Int, y: Int): Boolean {
        val ID = blocks[currI][currJ]!!.getNumber()
        when (ID) {
            ItemID.TURBINE + ItemID.LEFT, ItemID.STEEL_GUN + ItemID.RIGHT, ItemID.WOOD_GUN + ItemID.RIGHT -> return occupiedCells[x + 1][y]
            ItemID.TURBINE + ItemID.DOWN, ItemID.STEEL_GUN + ItemID.UP, ItemID.WOOD_GUN + ItemID.UP -> return occupiedCells[x][y + 1]
            ItemID.TURBINE + ItemID.RIGHT, ItemID.STEEL_GUN + ItemID.LEFT, ItemID.WOOD_GUN + ItemID.LEFT -> return occupiedCells[x - 1][y]
            ItemID.TURBINE + ItemID.UP, ItemID.STEEL_GUN + ItemID.DOWN, ItemID.WOOD_GUN + ItemID.DOWN -> return occupiedCells[x][y - 1]
        }
        return false
    }


    private fun setImageOnTable(i: Int, j: Int) {
        val ID = blocks[currI][currJ]!!.getNumber()

        blockArr[i][j] = ID
        blocks[currI][currJ]!!.setXYinTable(i, j)
        occupiedCells[i][j] = true
        when (ID) {
            ItemID.TURBINE + ItemID.LEFT, ItemID.STEEL_GUN + ItemID.RIGHT, ItemID.WOOD_GUN + ItemID.RIGHT -> occupiedCells[i + 1][j] = true
            ItemID.TURBINE + ItemID.DOWN, ItemID.STEEL_GUN + ItemID.UP, ItemID.WOOD_GUN + ItemID.UP -> occupiedCells[i][j + 1] = true
            ItemID.TURBINE + ItemID.RIGHT, ItemID.STEEL_GUN + ItemID.LEFT, ItemID.WOOD_GUN + ItemID.LEFT -> occupiedCells[i - 1][j] = true
            ItemID.TURBINE + ItemID.UP, ItemID.STEEL_GUN + ItemID.DOWN, ItemID.WOOD_GUN + ItemID.DOWN -> occupiedCells[i][j - 1] = true
        }
    }

    private fun removeImageFromTable() {
        val i = blocks[currI][currJ]!!.xinTable
        val j = blocks[currI][currJ]!!.yinTable
        val ID = blocks[currI][currJ]!!.getNumber()

        blockArr[i][j] = ItemID.NULL
        blocks[currI][currJ]!!.setXYinTable(ItemID.NULL, ItemID.NULL)
        occupiedCells[i][j] = false
        when (ID) {
            ItemID.TURBINE + ItemID.LEFT, ItemID.STEEL_GUN + ItemID.RIGHT, ItemID.WOOD_GUN + ItemID.RIGHT -> occupiedCells[i + 1][j] = false
            ItemID.TURBINE + ItemID.DOWN, ItemID.STEEL_GUN + ItemID.UP, ItemID.WOOD_GUN + ItemID.UP -> occupiedCells[i][j + 1] = false
            ItemID.TURBINE + ItemID.RIGHT, ItemID.STEEL_GUN + ItemID.LEFT, ItemID.WOOD_GUN + ItemID.LEFT -> occupiedCells[i - 1][j] = false
            ItemID.TURBINE + ItemID.UP, ItemID.STEEL_GUN + ItemID.DOWN, ItemID.WOOD_GUN + ItemID.DOWN -> occupiedCells[i][j - 1] = false
        }
    }

    private fun returnImageBack() {
        blocks[currI][currJ]!!.returnToStartPos(lastX, lastY)
        if (lastXInTable != ItemID.NULL)
            setImageOnTable(lastXInTable, lastYInTable)
    }


    override fun keyDown(keycode: Int): Boolean {
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return true
    }

    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        for (i in blocks.indices)
            for (j in 0 until blocks[i].size)
                if (blocks[i][j]!!.contains(x.toFloat(), y.toFloat()) && blocks[i][j]!!.isTouchable && !isImageDragging) {
                    initCurrentImage(i, j)
                    blocks[currI][currJ]!!.isMoving = true
                    blocks[currI][currJ]!!.isAlreadyMoved = false
                    lastX = blocks[currI][currJ]!!.x
                    lastY = blocks[currI][currJ]!!.y
                    if (blocks[currI][currJ]!!.xinTable != ItemID.NULL) {
                        lastXInTable = blocks[currI][currJ]!!.xinTable
                        lastYInTable = blocks[currI][currJ]!!.yinTable
                        removeImageFromTable()
                    } else {
                        lastXInTable = ItemID.NULL
                        lastYInTable = ItemID.NULL
                        if (blocks[currI][currJ]!!.isInStartPos)
                            minus1FromLabel(i)
                    }
                }
        oldX = x.toFloat()
        oldY = y.toFloat()
        currentX = x.toFloat()
        currentY = y.toFloat()
        return false
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        if (isImageDragging) {
            if (!blocks[currI][currJ]!!.isAlreadyMoved) {
                blocks[currI][currJ]!!.setXY(lastX, lastY)
                if (blocks[currI][currJ]!!.isInStartPos)
                    plus1ToLabel(currI)
                else {
                    val ID = blocks[currI][currJ]!!.getNumber()
                    blockArr[lastXInTable][lastYInTable] = ID
                    blocks[currI][currJ]!!.setXYinTable(lastXInTable, lastYInTable)
                    when (ID % 10) {
                        ItemID.TURBINE,
                        ItemID.WOOD_GUN,
                        ItemID.STEEL_GUN,
                        ItemID.HALF_WOOD_BLOCK,
                        ItemID.HALF_STEEL_BLOCK -> safeRotate()
                    }
                    setImageOnTable(lastXInTable, lastYInTable)
                }
            } else if (setEndPosition(x, y)) {
                if (currJ < blocks[currI].size - 1) {
                    for (h in currJ + 1 until blocks[currI].size) {
                        if (blocks[currI][h]!!.isInStartPos) {
                            blocks[currI][h]!!.isTouchable = true
                            stage.addActor(blocks[currI][h])
                            break
                        }
                    }
                }
            } else {
                if (blocks[currI][currJ]!!.isInStartPos) {
                    plus1ToLabel(currI)
                    for (h in 0 until blocks[currI].size) {
                        if (h != currJ && blocks[currI][h]!!.isInStartPos) {
                            blocks[currI][h]!!.isTouchable = false
                            blocks[currI][h]!!.remove()
                        }
                    }
                }
            }
            blocks[currI][currJ]!!.isMoving = false
            resetCurrentImage()
        }
        return false
    }

    override fun touchDragged(x: Int, y: Int, pointer: Int): Boolean {
        if (isImageDragging) {
            if (Math.abs(x - currentX) > AssemblingScreenCoords.BLOCK_SIZE / 8 || Math.abs(y - currentY) > AssemblingScreenCoords.BLOCK_SIZE / 8)
                blocks[currI][currJ]!!.isAlreadyMoved = true
            blocks[currI][currJ]!!.setXY(blocks[currI][currJ]!!.x + x - oldX, blocks[currI][currJ]!!.y - y + oldY)
            oldX = x.toFloat()
            oldY = y.toFloat()
        }
        return false
    }

    override fun mouseMoved(x: Int, y: Int): Boolean {
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        return true
    }


    override fun show() {
        val m = InputMultiplexer()
        m.addProcessor(this)
        m.addProcessor(stage)
        Gdx.input.inputProcessor = m
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        stage.dispose()
    }

}
