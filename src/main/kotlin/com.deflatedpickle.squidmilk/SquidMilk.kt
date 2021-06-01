/* Copyright (c) 2021 DeflatedPickle under the MIT license */

@file:Suppress("SpellCheckingInspection", "unused")

package com.deflatedpickle.squidmilk

import com.deflatedpickle.squidmilk.SquidMilk.ACCEPTED_VERSIONS
import com.deflatedpickle.squidmilk.SquidMilk.ADAPTER
import com.deflatedpickle.squidmilk.SquidMilk.DEPENDENCIES
import com.deflatedpickle.squidmilk.SquidMilk.MOD_ID
import com.deflatedpickle.squidmilk.SquidMilk.NAME
import com.deflatedpickle.squidmilk.SquidMilk.VERSION
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.event.entity.player.FillBucketEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod(
    modid = MOD_ID,
    name = NAME,
    version = VERSION,
    acceptedMinecraftVersions = ACCEPTED_VERSIONS,
    dependencies = DEPENDENCIES,
    modLanguageAdapter = ADAPTER
)
@Mod.EventBusSubscriber
object SquidMilk {
    const val MOD_ID = "squidmilk"
    const val NAME = "SquidMilk"
    const val VERSION = "1.11.2-2.0.0.1"
    const val ACCEPTED_VERSIONS = "[1.11,1.11.2]"
    const val DEPENDENCIES = "required-after:forgelin;"
    const val ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"

    @SubscribeEvent
    @JvmStatic
    // Problem: If a squid in water is milked, it would get rid of the water
    // Solution: This checks if there are any squids around the block position for the event,
    // if there are, it cancels the event to fill the bucket with water
    fun onItemRightClick(event: FillBucketEvent) {
        event.world.let { world ->
            event.target?.blockPos?.let { blockPos ->
                world.getEntitiesWithinAABBExcludingEntity(
                    null,
                    AxisAlignedBB(blockPos)
                ).let { entities ->
                    if (entities.isNotEmpty()) {
                        event.isCanceled = true
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @JvmStatic
    // Problem: Squids can't be milked
    // Solution: This lets squids be milked
    fun onEntityInteractEvent(event: EntityInteract) {
        if (event.hand == EnumHand.OFF_HAND) return

        event.target?.let { entity ->
            if (entity is EntitySquid) {
                event.entityPlayer?.let { player ->
                    if (player.heldItemMainhand.item == Items.BUCKET) {
                        if (!player.world.isRemote && !player.capabilities.isCreativeMode) {
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
