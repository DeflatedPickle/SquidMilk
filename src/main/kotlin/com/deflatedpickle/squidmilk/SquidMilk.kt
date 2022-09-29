/* Copyright (c) 2021-2022 DeflatedPickle under the CC0 license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.squidmilk

import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemUsage
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
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

    fun interact(squid: MobEntity, player: PlayerEntity, hand: Hand): Boolean {
        val itemStack = player.getStackInHand(hand)
        if (itemStack.isOf(Items.BUCKET) && !squid.isBaby) {
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0f, 1.0f)
            val itemStack2 = ItemUsage.exchangeStack(itemStack, player, Items.MILK_BUCKET.defaultStack)
            player.setStackInHand(hand, itemStack2)
            return true
        }
        return false
    }
}
