package net.bananap0wer.bananaadventuretime.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Items;

public class ModFoodComponents {
    public static final FoodComponent SWEET_BERRIES_JUICE = new FoodComponent.Builder()
        .nutrition(4)
        .saturationModifier(0.2f)
        .usingConvertsTo(Items.GLASS_BOTTLE)
        .build();
}
