package net.bananap0wer.bananaadventuretime.item.custom;

import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.bananap0wer.bananaadventuretime.sound.ModSounds;
import net.bananap0wer.bananaadventuretime.util.ItemEntityOwnerHelper;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MarcelineAxeGuitarItem extends HoeItem {
    private static final double SONG_RADIUS = 10.0;
    private static final float SONG_DAMAGE = 5.0f;
    private static final int SONG_COOLDOWN_TICKS = 20;
    private static final double SONG_KNOCKBACK_STRENGTH = 0.5;
    private static final Identifier BACK_TO_THE_AXE_ID = Identifier.of(BananaAdventureTime.MOD_ID, "marceline/back_to_the_axe");
    private static final Identifier VAMPIRE_QUEENS_GUITAR_ID = Identifier.of(BananaAdventureTime.MOD_ID,
        "marceline/vampire_queens_guitar");
    private static final String BACK_TO_THE_AXE_CRITERION = "transformed_in_lava";

    public MarcelineAxeGuitarItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (world.isClient()) {
            return TypedActionResult.success(stack, true);
        }

        boolean hitAnyTarget = damageNearbyHostileTargets(world, user);
        if (!hitAnyTarget) {
            return TypedActionResult.pass(stack);
        }

        playAttackSound(world, user);
        stack.damage(1, user, getEquipmentSlot(hand));
        user.getItemCooldownManager().set(this, SONG_COOLDOWN_TICKS);

        return TypedActionResult.success(stack, false);
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        if (entity.isInLava()) {
            for (int i = 0; i < entity.getStack().getCount(); i++) {
                entity.dropStack(new ItemStack(ModItems.MARCELINE_AXE));
            }

            grantBackToTheAxe(entity);
        }
    }

    private static void grantBackToTheAxe(ItemEntity entity) {
        ServerPlayerEntity player = ItemEntityOwnerHelper.getServerPlayer(entity);
        if (player == null) {
            return;
        }

        MinecraftServer server = entity.getWorld().getServer();
        if (server == null) {
            return;
        }

        AdvancementEntry vampireQueensGuitar = server.getAdvancementLoader().get(VAMPIRE_QUEENS_GUITAR_ID);
        AdvancementEntry backToTheAxe = server.getAdvancementLoader().get(BACK_TO_THE_AXE_ID);
        if (vampireQueensGuitar == null || backToTheAxe == null) {
            return;
        }

        if (player.getAdvancementTracker().getProgress(vampireQueensGuitar).isDone()) {
            player.getAdvancementTracker().grantCriterion(backToTheAxe, BACK_TO_THE_AXE_CRITERION);
        }
    }

    private static boolean damageNearbyHostileTargets(World world, PlayerEntity user) {
        boolean hitAnyTarget = false;

        for (LivingEntity target : world.getEntitiesByClass(LivingEntity.class,
            user.getBoundingBox().expand(SONG_RADIUS), target -> shouldDamageTarget(target, user))) {
            if (target.damage(user.getDamageSources().playerAttack(user), SONG_DAMAGE)) {
                knockTargetAway(target, user);
                hitAnyTarget = true;
            }
        }

        return hitAnyTarget;
    }

    private static void playAttackSound(World world, PlayerEntity user) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
            ModSounds.MARCELINE_AXE_GUITAR_ATTACK, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    private static boolean shouldDamageTarget(LivingEntity target, PlayerEntity user) {
        if (target == user || !target.isAlive() || target instanceof PlayerEntity) {
            return false;
        }

        return target instanceof Monster || target instanceof MobEntity mob && mob.getTarget() == user;
    }

    private static void knockTargetAway(LivingEntity target, PlayerEntity user) {
        target.takeKnockback(SONG_KNOCKBACK_STRENGTH, user.getX() - target.getX(), user.getZ() - target.getZ());
    }

    private static EquipmentSlot getEquipmentSlot(Hand hand) {
        return hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }
}
