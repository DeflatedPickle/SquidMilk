package com.deflatedpickle.betaunfix.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {
    @SubscribeEvent
    public void onPlayerInteractEvent(PlayerInteractEvent.EntityInteract event) {
        EntityPlayer player = event.getEntityPlayer();
        Entity entity = event.getTarget();

        if (entity instanceof EntitySquid){
            if (player.getHeldItemMainhand().getItem() instanceof ItemBucket){
                player.getHeldItemMainhand().shrink(1);
                player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET, 1));
            }
        }
    }
}
