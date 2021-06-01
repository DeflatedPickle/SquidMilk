/* Copyright (c) 2021 DeflatedPickle under the MIT license */

@file:Suppress("SpellCheckingInspection", "unused")

package com.deflatedpickle.squidmilk

import com.deflatedpickle.squidmilk.SquidMilk.MOD_ID
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Hand
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.entity.player.FillBucketEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod(MOD_ID)
@Mod.EventBusSubscriber
object SquidMilk {
    const val MOD_ID = "squidmilk"

    @SubscribeEvent
    // Problem: If a squid in water is milked, it would get rid of the water
    // Solution: This checks if there are any squids around the block position for the event,
    // if there are, it cancels the event to fill the bucket with water
    fun onItemRightClick(event: FillBucketEvent) {
        event.world.let { world ->
            event.target?.hitVec?.let { vec3d ->
                world.getEntitiesWithinAABBExcludingEntity(
                    null,
                    AxisAlignedBB(BlockPos(vec3d))
                ).let { entities ->
                    if (entities.isNotEmpty()) {
                        event.isCanceled = true
                    }
                }
            }
        }
    }

    @SubscribeEvent
    // Problem: Squids can't be milked
    // Solution: This lets squids be milked
    fun onEntityInteractEvent(event: EntityInteract) {
        if (event.hand == Hand.OFF_HAND) return

        event.target?.let { entity ->
            if (entity is SquidEntity) {
                event.player?.let { player ->
                    if (player.heldItemMainhand.item == Items.BUCKET) {
                        if (!player.world.isRemote && !player.isCreative) {
                            player.heldItemMainhand.shrink(1)
                        }

                        player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0f, 1.0f)

                        if (!player.world.isRemote) {
                            player.inventory.addItemStackToInventory(
                                ItemStack(Items.MILK_BUCKET, 1)
                            )
                        }
                    }
                }
            }
        }
    }
}
