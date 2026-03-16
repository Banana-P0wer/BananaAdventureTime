package net.bananap0wer.bananaadventuretime.item.custom;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class DemonBloodSwordItem extends SwordItem {
    private static final float RECOIL_DAMAGE = 2.0f;

    public DemonBloodSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        damageAttacker(attacker);
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);

        if (stack.isEmpty()) {
            attacker.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.EMPTY_DEMON_BLOOD_SWORD));
        }
    }

    private static void damageAttacker(LivingEntity attacker) {
        if (!attacker.getWorld().isClient()) {
            attacker.damage(attacker.getDamageSources().magic(), RECOIL_DAMAGE);
        }
    }
}
