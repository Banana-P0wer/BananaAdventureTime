package net.bananap0wer.bananaadventuretime.util;

import java.util.UUID;

import net.bananap0wer.bananaadventuretime.mixin.ItemEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ItemEntityOwnerHelper {
    public static ServerPlayerEntity getServerPlayer(ItemEntity itemEntity) {
        ItemEntityAccessor accessor = (ItemEntityAccessor) itemEntity;
        Entity thrower = accessor.bananaadventuretime$getThrower();
        if (thrower instanceof ServerPlayerEntity player) {
            return player;
        }

        MinecraftServer server = itemEntity.getWorld().getServer();
        if (server == null) {
            return null;
        }

        ServerPlayerEntity owner = getPlayer(server, accessor.bananaadventuretime$getOwnerUuid());
        if (owner != null) {
            return owner;
        }

        return getPlayer(server, accessor.bananaadventuretime$getThrowerUuid());
    }

    private static ServerPlayerEntity getPlayer(MinecraftServer server, UUID uuid) {
        return uuid == null ? null : server.getPlayerManager().getPlayer(uuid);
    }
}
