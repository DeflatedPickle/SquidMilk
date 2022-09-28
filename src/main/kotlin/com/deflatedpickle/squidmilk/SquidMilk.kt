/* Copyright (c) 2021-2022 DeflatedPickle under the CC0 license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.squidmilk

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

@Suppress("UNUSED")
object SquidMilk : ModInitializer {
    private const val MOD_ID = "$[id]"
    private const val NAME = "$[name]"
    private const val GROUP = "$[group]"
    private const val AUTHOR = "$[author]"
    private const val VERSION = "$[version]"

    override fun onInitialize(mod: ModContainer) {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))
    }
}
