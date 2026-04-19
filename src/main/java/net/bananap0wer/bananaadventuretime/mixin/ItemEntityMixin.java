package net.bananap0wer.bananaadventuretime.mixin;

import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.bananap0wer.bananaadventuretime.util.ItemEntityOwnerHelper;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    private static final int VOID_OFFSET = 64;
    private static final double SCARLET_PARTICLE_START_Y = 0.0;
    private static final double SCARLET_TRAIL_FALL_SPEED = -0.03;
    private static final int SCARLET_TRAIL_PARTICLE_COUNT = 4;
    private static final int SCARLET_TRANSFORM_PARTICLE_COUNT = 45;
    private static final DustParticleEffect SCARLET_VOID_PARTICLE =
        new DustParticleEffect(new Vector3f(1.0f, 1.0f, 1.0f), 1.0f);
    private static final Identifier WHERE_DID_IT_GO_ID = Identifier.of(BananaAdventureTime.MOD_ID,
        "dimension/where_did_it_go");
    private static final Identifier INTO_THE_FOURTH_DIMENSION_ID = Identifier.of(BananaAdventureTime.MOD_ID,
        "dimension/into_the_fourth_dimension");
    private static final String WHERE_DID_IT_GO_CRITERION = "threw_scarlet_into_void";
    private static final String INTO_THE_FOURTH_DIMENSION_CRITERION = "obtained_fourth_dimension_sword";

    @Inject(method = "tick", at = @At("HEAD"))
    private void bananaadventuretime$convertScarletInVoid(CallbackInfo info) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;

        if (!(itemEntity.getWorld() instanceof ServerWorld world)) {
            return;
        }

        if (!itemEntity.getStack().isOf(ModItems.SCARLET)) {
            return;
        }

        spawnScarletTrailParticles(world, itemEntity);

        if (!isBelowVoidLimit(itemEntity)) {
            return;
        }

        ServerPlayerEntity player = ItemEntityOwnerHelper.getServerPlayer(itemEntity);
        if (player == null) {
            return;
        }

        grantWhereDidItGo(player);
        spawnScarletTransformParticles(world, itemEntity);

        ItemStack reward = new ItemStack(ModItems.FOURTH_DIMENSION_SWORD);
        if (!player.giveItemStack(reward)) {
            player.dropItem(reward, false);
        }

        grantIntoTheFourthDimension(player);
        itemEntity.discard();
    }

    private static boolean isBelowVoidLimit(ItemEntity itemEntity) {
        return itemEntity.getY() < itemEntity.getWorld().getBottomY() - VOID_OFFSET;
    }

    private static void spawnScarletTrailParticles(ServerWorld world, ItemEntity itemEntity) {
        if (itemEntity.getY() >= SCARLET_PARTICLE_START_Y
                || itemEntity.getVelocity().y >= SCARLET_TRAIL_FALL_SPEED) {
            return;
        }

        world.spawnParticles(SCARLET_VOID_PARTICLE, itemEntity.getX(), itemEntity.getY() + 0.25, itemEntity.getZ(),
            SCARLET_TRAIL_PARTICLE_COUNT, 0.12, 0.12, 0.12, 0.01);
    }

    private static void spawnScarletTransformParticles(ServerWorld world, ItemEntity itemEntity) {
        world.spawnParticles(SCARLET_VOID_PARTICLE, itemEntity.getX(), itemEntity.getY() + 0.25, itemEntity.getZ(),
            SCARLET_TRANSFORM_PARTICLE_COUNT, 0.35, 0.35, 0.35, 0.04);
    }

    private static void grantWhereDidItGo(ServerPlayerEntity player) {
        AdvancementEntry advancement = player.getServer().getAdvancementLoader().get(WHERE_DID_IT_GO_ID);
        if (advancement != null) {
            player.getAdvancementTracker().grantCriterion(advancement, WHERE_DID_IT_GO_CRITERION);
        }
    }

    private static void grantIntoTheFourthDimension(ServerPlayerEntity player) {
        AdvancementEntry advancement = player.getServer().getAdvancementLoader().get(INTO_THE_FOURTH_DIMENSION_ID);
        if (advancement != null) {
            player.getAdvancementTracker().grantCriterion(advancement, INTO_THE_FOURTH_DIMENSION_CRITERION);
        }
    }
}
