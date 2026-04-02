package net.bananap0wer.bananaadventuretime.item.custom;

import java.util.Optional;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FourthDimensionSwordItem extends SwordItem {
    public FourthDimensionSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (isTooDamagedToTeleport(stack)) {
            return TypedActionResult.fail(stack);
        }

        if (!(user instanceof ServerPlayerEntity player)) {
            return TypedActionResult.success(stack, world.isClient());
        }

        if (isTeleportStateBlocked(player)) {
            return TypedActionResult.fail(stack);
        }

        ServerWorld overworld = player.getServer().getWorld(World.OVERWORLD);
        if (overworld == null) {
            return TypedActionResult.fail(stack);
        }

        Optional<TeleportDestination> destination = getOverworldSpawn(player, overworld);
        if (destination.isEmpty()) {
            return TypedActionResult.fail(stack);
        }

        TeleportDestination teleportDestination = destination.get();
        player.teleport(overworld, teleportDestination.pos().x, teleportDestination.pos().y, teleportDestination.pos().z,
            teleportDestination.yaw(), player.getPitch());
        stack.damage(1, player, getEquipmentSlot(hand));

        return TypedActionResult.success(stack, false);
    }

    private static boolean isTooDamagedToTeleport(ItemStack stack) {
        return stack.isDamageable() && stack.getDamage() >= stack.getMaxDamage() - 1;
    }

    private static boolean isTeleportStateBlocked(ServerPlayerEntity player) {
        return player.hasVehicle() || player.isSleeping() || !player.isAlive() || player.isDead();
    }

    private static Optional<TeleportDestination> getOverworldSpawn(ServerPlayerEntity player, ServerWorld overworld) {
        BlockPos playerSpawnPos = player.getSpawnPointPosition();
        if (playerSpawnPos != null && World.OVERWORLD.equals(player.getSpawnPointDimension())) {
            Optional<ServerPlayerEntity.RespawnPos> respawnPos = ServerPlayerEntity.findRespawnPosition(overworld,
                playerSpawnPos, player.getSpawnAngle(), player.isSpawnForced(), true);

            if (respawnPos.isPresent()) {
                ServerPlayerEntity.RespawnPos pos = respawnPos.get();
                return Optional.of(new TeleportDestination(pos.pos(), pos.yaw()));
            }

            return Optional.empty();
        }

        return Optional.of(new TeleportDestination(Vec3d.ofBottomCenter(overworld.getSpawnPos()),
            overworld.getSpawnAngle()));
    }

    private static EquipmentSlot getEquipmentSlot(Hand hand) {
        return hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    private record TeleportDestination(Vec3d pos, float yaw) {
    }
}
