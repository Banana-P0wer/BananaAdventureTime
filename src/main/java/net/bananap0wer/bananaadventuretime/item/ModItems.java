package net.bananap0wer.bananaadventuretime.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item RUBY = refisterItem("ruby", new Item(new Item.Settings()));

    public static final Item SWEET_BERRIES_JUICE = refisterItem("sweet_berries_juice", new Item(new Item.Settings()));


    private static Item refisterItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BananaAdventureTime.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BananaAdventureTime.LOGGER.info("Registering Mod Items for " + BananaAdventureTime.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.EMERALD, RUBY);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addAfter(Items.HONEY_BOTTLE, SWEET_BERRIES_JUICE);
        });
    }
}
