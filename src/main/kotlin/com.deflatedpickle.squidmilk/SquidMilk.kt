/* Copyright (c) 2021 DeflatedPickle under the MIT license */

@file:Suppress("SpellCheckingInspection", "unused")

package com.deflatedpickle.squidmilk

import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.jetbrains.annotations.Nullable

const val area = 3.0

fun init() {
    // Problem: If a squid in water is milked, it would get rid of the water
    // Solution: This checks if there are any squids around the block position for the event,
    // if there are, it cancels the event to fill the bucket with water
    UseItemCallback.EVENT.register { playerEntity: PlayerEntity, world: World, hand: Hand ->
        if (playerEntity.getStackInHand(hand).item == Items.BUCKET) {
            val start = playerEntity.pos.subtract(area, area, area)
            val end = playerEntity.pos.add(area, area, area)

            ProjectileUtil.raycast(
                playerEntity,
                start,
                end,
                Box(
                    start,
                    end
                ),
                EntityPredicates.EXCEPT_SPECTATOR,
                0.0
            )?.let { cast ->
                world.getOtherEntities(
                    playerEntity,
                    Box(BlockPos(cast.pos)).expand(area)
                ).let { entities ->
                    if (entities.any { it is SquidEntity }) {
                        return@register TypedActionResult.fail(ItemStack.EMPTY)
                    }
                }
            }
        }

        TypedActionResult.pass(ItemStack.EMPTY)
    }

    // Problem: Squids can't be milked
    // Solution: This lets squids be milked
    UseEntityCallback.EVENT.register { playerEntity: PlayerEntity, world: World, hand: Hand, entity: Entity, _: @Nullable EntityHitResult? ->
        if (hand == Hand.OFF_HAND) return@register ActionResult.PASS

        if (entity is SquidEntity) {
            playerEntity.let { player ->
                if (player.mainHandStack.item == Items.BUCKET) {
                    if (!world.isClient && !player.isCreative) {
                        player.mainHandStack.decrement(1)
                    }

                    player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0f, 1.0f)

                    if (!world.isClient) {
                        player.inventory.insertStack(
                            ItemStack(Items.MILK_BUCKET, 1)
                        )
                    }

                    return@let ActionResult.SUCCESS
                }

                ActionResult.PASS
            }
        }

        ActionResult.PASS
    }
}
