package net.bananap0wer.bananaadventuretime.item.custom;

import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GrapeSwordItem extends SwordItem {
    private static final Identifier ONE_GOOD_HIT_ID = Identifier.of(BananaAdventureTime.MOD_ID, "combat/one_good_hit");
    private static final Identifier BERRY_THE_WITHER_ID = Identifier.of(BananaAdventureTime.MOD_ID,
        "combat/berry_the_wither");
    private static final String ONE_GOOD_HIT_CRITERION = "hit_and_broke_grape_sword";
    private static final String BERRY_THE_WITHER_CRITERION = "killed_wither_with_grape_sword";

    public GrapeSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean killedWither = false;

        if (!attacker.getWorld().isClient() && target instanceof WitherEntity) {
            killedWither = target.damage(attacker.getDamageSources().genericKill(), Float.MAX_VALUE)
                && !target.isAlive();
        }

        stack.damage(stack.getMaxDamage(), attacker, EquipmentSlot.MAINHAND);

        if (!(attacker instanceof PlayerEntity player && player.isCreative())) {
            attacker.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.EMPTY_DEMON_BLOOD_SWORD));
            grantOneGoodHit(attacker);
            if (killedWither) {
                grantBerryTheWither(attacker);
            }
        }
    }

    private static void grantOneGoodHit(LivingEntity attacker) {
        grantAdvancement(attacker, ONE_GOOD_HIT_ID, ONE_GOOD_HIT_CRITERION);
    }

    private static void grantBerryTheWither(LivingEntity attacker) {
        grantAdvancement(attacker, BERRY_THE_WITHER_ID, BERRY_THE_WITHER_CRITERION);
    }

    private static void grantAdvancement(LivingEntity attacker, Identifier id, String criterion) {
        if (!(attacker instanceof ServerPlayerEntity player)) {
            return;
        }

        AdvancementEntry advancement = player.getServer().getAdvancementLoader().get(id);
        if (advancement != null) {
            player.getAdvancementTracker().grantCriterion(advancement, criterion);
        }
    }
}
