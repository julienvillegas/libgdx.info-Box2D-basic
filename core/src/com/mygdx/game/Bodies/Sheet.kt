package com.mygdx.game.Bodies

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.mygdx.game.Extra.AssemblingScreenCoords
import com.mygdx.game.Extra.ItemID
import com.mygdx.game.MyGdxGame


class Sheet(name : String, player : Int): Table(){
    private val ship = Array(AssemblingScreenCoords.FIELD_WIDTH) { IntArray(AssemblingScreenCoords.FIELD_HEIGHT) }
    private val inventory = IntArray(ItemID.NUMBER_OF_ITEMS)
    private var SheetName = ""
    private var player : Int = 0
    init{
        val stageLayout = Table()
        add(stageLayout.apply {
            debugAll()
            defaults().width(100.toFloat())
            setFillParent(true)
            for (j in 0 until AssemblingScreenCoords.FIELD_HEIGHT){
                row()
                for (i in 0 until AssemblingScreenCoords.FIELD_WIDTH){
                    if (ship[i][j]==1000){
                        add(Image(Texture(getString(ship[i][j]))))
                    } else add().fill()
                    }

            }
        }).width(100.toFloat()).pad(10.toFloat())
        row()
        add(Label(SheetName, MyGdxGame.skin, "big-black")).width(100.toFloat())

    }
    private fun getString(texture: Int): String {
        return when (texture) {
              ItemID.WOOD_BLOCK -> "woodblock.png"
              ItemID.STEEL_BLOCK -> "steelblock.png"
              ItemID.ENGINE -> "engine.png"
              ItemID.TURBINE -> "turbine.png"
              ItemID.HALF_WOOD_BLOCK -> "halfwoodblock.png"
              ItemID.HALF_STEEL_BLOCK -> "halfsteelblock.png"
              ItemID.STEEL_GUN -> "gun_2.png"
              ItemID.WOOD_GUN -> "gun_1.png"
              ItemID.EYE -> "eye${player}.png"
            else -> ""
        }
    }

    private fun getShip(name: String):Array<IntArray>{
        return when (SheetName){
            "balcon" -> arrayOf(intArrayOf(1000,1000,1000,1000,1000,1000),
                    intArrayOf(1000,1000,1000,1000,1000,1000),
                    intArrayOf(1000,1000,3,2,3,1000),
                    intArrayOf(1000,1000,2,8,2,1000),
                    intArrayOf(1000,1000,1000,6,1000,1000),
                    intArrayOf(1000,1000,1000,1000,1000,1000),
                    intArrayOf(1000,1000,1000,1000,1000,1000),
                    intArrayOf(1000,1000,1000,1000,1000,1000))
            else -> arrayOf(IntArray(0))
        }
    }



}