package net.bananap0wer.bananaadventuretime.item.custom;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MarcelineAxeGuitarItem extends Item {
    public MarcelineAxeGuitarItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        if (entity.isInLava()) {
            for (int i = 0; i < entity.getStack().getCount(); i++) {
                entity.dropStack(new ItemStack(ModItems.MARCELINE_AXE));
            }
        }
    }
}
