package net.bananap0wer.bananaadventuretime.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Items;

public class ModFoodComponents {
    public static final FoodComponent SWEET_BERRIES_JUICE = new FoodComponent.Builder()
        .nutrition(4)
        .saturationModifier(0.2f)
        .usingConvertsTo(Items.GLASS_BOTTLE)
        .build();

    public static final FoodComponent APPLE_PIE = new FoodComponent.Builder()
        .nutrition(8)
        .saturationModifier(0.3f)
        .build();

    public static final FoodComponent TIME_SANDWICH = new FoodComponent.Builder()
        .nutrition(16)
        .saturationModifier(0.6f)
        .build();
}
