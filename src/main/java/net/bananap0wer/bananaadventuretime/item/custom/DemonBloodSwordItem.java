package net.bananap0wer.bananaadventuretime.item.custom;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public class DemonBloodSwordItem extends SwordItem {
    private static final float RECOIL_DAMAGE = 2.0f;

    public DemonBloodSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        damageRecoilTarget(target, attacker);
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);

        if (stack.isEmpty()) {
            attacker.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.EMPTY_DEMON_BLOOD_SWORD));
        }
    }

    private static void damageRecoilTarget(LivingEntity target, LivingEntity attacker) {
        if (attacker.getWorld().isClient()) {
            return;
        }

        if (hasThorns(attacker)) {
            target.damage(attacker.getDamageSources().thorns(attacker), RECOIL_DAMAGE);
        } else {
            attacker.damage(attacker.getDamageSources().magic(), RECOIL_DAMAGE);
        }
    }

    private static boolean hasThorns(LivingEntity entity) {
        RegistryEntry<Enchantment> thorns = entity.getWorld()
            .getRegistryManager()
            .getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
            .getOrThrow(Enchantments.THORNS);

        return EnchantmentHelper.getEquipmentLevel(thorns, entity) > 0;
    }
}
