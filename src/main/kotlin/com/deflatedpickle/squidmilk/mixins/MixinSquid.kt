/* Copyright (c) 2021 DeflatedPickle under the MIT license */

@file:Suppress("unused")

package com.deflatedpickle.squidmilk.mixins

import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemUsage
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import org.spongepowered.asm.mixin.Mixin

@Mixin(SquidEntity::class)
abstract class MixinSquid : MobEntity(null, null) {
    // this is just copied from cows
    override fun interactMob(player: PlayerEntity, hand: Hand?): ActionResult? {
        val itemStack = player.getStackInHand(hand)
        if (itemStack.isOf(Items.BUCKET) && !this.isBaby) {
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0f, 1.0f)
            val itemStack2 = ItemUsage.exchangeStack(itemStack, player, Items.MILK_BUCKET.defaultStack)
            player.setStackInHand(hand, itemStack2)
            return ActionResult.success(this.world.isClient)
        }
        return super.interactMob(player, hand)
    }
}
