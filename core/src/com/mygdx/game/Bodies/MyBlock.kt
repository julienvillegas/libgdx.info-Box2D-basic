package com.mygdx.game.Bodies

import com.mygdx.game.Extra.ItemID.ENGINE
import com.mygdx.game.Extra.ItemID.EYE
import com.mygdx.game.Extra.ItemID.HALF_STEEL_BLOCK
import com.mygdx.game.Extra.ItemID.HALF_WOOD_BLOCK
import com.mygdx.game.Extra.ItemID.HP
import com.mygdx.game.Extra.ItemID.STEEL_BLOCK
import com.mygdx.game.Extra.ItemID.STEEL_GUN
import com.mygdx.game.Extra.ItemID.TURBINE
import com.mygdx.game.Extra.ItemID.WOOD_BLOCK
import com.mygdx.game.Extra.ItemID.WOOD_GUN

class MyBlock(var type: Int) {
    var hp = 999f
    var isBulletisActivated = false

    init {
        when (type) {
            -2 -> this.hp = -2f
            -1 -> this.hp = -1f
            WOOD_BLOCK -> this.hp = HP.toFloat()
            STEEL_BLOCK -> this.hp = (HP * 2).toFloat()
            ENGINE -> this.hp = (HP * 3 / 2).toFloat()
            TURBINE -> this.hp = (HP * 3 / 2).toFloat()
            HALF_WOOD_BLOCK -> this.hp = (HP / 2).toFloat()
            HALF_STEEL_BLOCK -> this.hp = HP.toFloat()
            STEEL_GUN -> this.hp = (HP * 2 + 1).toFloat()
            WOOD_GUN -> this.hp = (HP + 1).toFloat()
            EYE -> this.hp = (HP / 2).toFloat()
        }
    }
}
