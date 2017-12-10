package com.mygdx.game

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.mygdx.game.Tests.Bridge
import com.mygdx.game.Tests.LibGDXTest
import com.mygdx.game.Tests.SimpleTest

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(LibGDXTest(), config)
    }
}
