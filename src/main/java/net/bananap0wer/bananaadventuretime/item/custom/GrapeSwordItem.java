package net.bananap0wer.bananaadventuretime.item.custom;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class GrapeSwordItem extends SwordItem {
    public GrapeSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient() && target instanceof WitherEntity) {
            target.damage(attacker.getDamageSources().genericKill(), Float.MAX_VALUE);
        }

        stack.damage(stack.getMaxDamage(), attacker, EquipmentSlot.MAINHAND);

        if (!(attacker instanceof PlayerEntity player && player.isCreative())) {
            attacker.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.EMPTY_DEMON_BLOOD_SWORD));
        }
    }
}
