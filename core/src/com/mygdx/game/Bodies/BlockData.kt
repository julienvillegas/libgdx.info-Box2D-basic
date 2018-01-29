package com.mygdx.game.Bodies

import com.mygdx.game.Extra.ItemID.*

class BlockData(var type: Int) {

    var hp: Float = 0f

    var isBulletActivated: Boolean = false
    var turbinePower: Float = 0f
    var engineLabels = ArrayList<Int>()

    init {
        this.hp = when (type) {
            -2 -> -2f
            -1 -> -1f
            WOOD_BLOCK -> HP_WOOD_BLOCK
            STEEL_BLOCK -> HP_STEEL_BLOCK
            ENGINE -> HP_ENGINE
            TURBINE -> HP_TURBINE
            HALF_WOOD_BLOCK -> HP_HALF_WOOD_BLOCK
            HALF_STEEL_BLOCK -> HP_HALF_STEEL_BLOCK
            STEEL_GUN -> HP_STEEL_GUN
            WOOD_GUN -> HP_WOOD_GUN
            EYE -> HP_EYE
            else -> 0f
        }
        this.isBulletActivated = false
        this.turbinePower = 0f
        this.engineLabels = ArrayList()
    }

}
