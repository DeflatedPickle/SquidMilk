/* Copyright (c) 2022 DeflatedPickle under the CC0 license */

package com.deflatedpickle.squidmilk.mixin;

import com.deflatedpickle.squidmilk.SquidMilk;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(SquidEntity.class)
public class MixinSquid extends MobEntity {
  public MixinSquid(EntityType<? extends MobEntity> entityType, World world) {
    super(entityType, world);
  }

  @Override
  public ActionResult interactMob(PlayerEntity player, Hand hand) {
    if (SquidMilk.INSTANCE.interact(this, player, hand)) {
      return ActionResult.success(this.world.isClient);
    }
    return super.interactMob(player, hand);
  }
}
