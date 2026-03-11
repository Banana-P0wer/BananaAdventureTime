package net.bananap0wer.bananaadventuretime.mixin;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    private static final int VOID_OFFSET = 64;

    @Inject(method = "tick", at = @At("HEAD"))
    private void bananaadventuretime$convertScarletInVoid(CallbackInfo info) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;

        if (itemEntity.getWorld().isClient() || !isBelowVoidLimit(itemEntity)) {
            return;
        }

        if (!itemEntity.getStack().isOf(ModItems.SCARLET)) {
            return;
        }

        Entity owner = itemEntity.getOwner();
        if (!(owner instanceof ServerPlayerEntity player)) {
            return;
        }

        ItemStack reward = new ItemStack(ModItems.FOURTH_DIMENSION_SWORD);
        if (!player.giveItemStack(reward)) {
            player.dropItem(reward, false);
        }

        itemEntity.discard();
    }

    private static boolean isBelowVoidLimit(ItemEntity itemEntity) {
        return itemEntity.getY() < itemEntity.getWorld().getBottomY() - VOID_OFFSET;
    }
}
