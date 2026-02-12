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
   
    //Add items here 1/2
    public static final Item RUBY = refisterItem("ruby", new Item(new Item.Settings()));
    
    public static final Item MARCELINE_AXE = refisterItem("marceline_axe", new Item(new Item.Settings()));
    public static final Item MARCELINE_AXE_GUITAR = refisterItem("marceline_axe_guitar", new Item(new Item.Settings()));
    
    public static final Item ICE_KING_CROWN = refisterItem("ice_king_crown", new Item(new Item.Settings().fireproof()));
    public static final Item EMPTY_ICE_KING_CROWN = refisterItem("empty_ice_king_crown", new Item(new Item.Settings().fireproof()));
    
    public static final Item SCARLET = refisterItem("scarlet", new Item(new Item.Settings().fireproof()));
    public static final Item FOURTH_DIMENSION_SWORD = refisterItem("fourth_dimension_sword", new Item(new Item.Settings().fireproof()));
    public static final Item ROOT_SWORD = refisterItem("root_sword", new Item(new Item.Settings()));
    public static final Item DEMON_BLOOD_SWORD = refisterItem("demon_blood_sword", new Item(new Item.Settings().fireproof()));
    public static final Item GRAPE_SWORD = refisterItem("grape_sword", new Item(new Item.Settings().fireproof()));
    public static final Item GRASS_SWORD = refisterItem("grass_sword", new Item(new Item.Settings()));
    public static final Item FINN_SWORD = refisterItem("finn_sword", new Item(new Item.Settings()));

    public static final Item SCARLET_BLADE = refisterItem("scarlet_blade", new Item(new Item.Settings().fireproof()));
    public static final Item SCARLET_HANDLE = refisterItem("scarlet_handle", new Item(new Item.Settings().fireproof()));

    public static final Item EMPTY_DEMON_BLOOD_SWORD = refisterItem("empty_demon_blood_sword", new Item(new Item.Settings().fireproof()));
    public static final Item ECHO_OF_FINN = refisterItem("echo_of_finn", new Item(new Item.Settings()));

    public static final Item SWEET_BERRIES_JUICE = refisterItem("sweet_berries_juice", new Item(new Item.Settings()));

    public static final Item COME_ALONG_WITH_ME = refisterItem("come_along_with_me", new Item(new Item.Settings()));

    private static Item refisterItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BananaAdventureTime.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BananaAdventureTime.LOGGER.info("Registering Mod Items for " + BananaAdventureTime.MOD_ID);
        
        //Add items here 2/2
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.EMERALD, RUBY);
            entries.addAfter(Items.NETHERITE_INGOT, SCARLET_HANDLE);
            entries.addAfter(SCARLET_HANDLE, SCARLET_BLADE);
            entries.addAfter(SCARLET_BLADE, EMPTY_DEMON_BLOOD_SWORD);
            entries.addAfter(Items.HEART_OF_THE_SEA, ECHO_OF_FINN);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addAfter(Items.HONEY_BOTTLE, SWEET_BERRIES_JUICE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {          
            entries.addAfter(Items.NETHERITE_SWORD, SCARLET);
            entries.addAfter(SCARLET, FOURTH_DIMENSION_SWORD);
            entries.addAfter(FOURTH_DIMENSION_SWORD, ROOT_SWORD);
            entries.addAfter(ROOT_SWORD, DEMON_BLOOD_SWORD);
            entries.addAfter(DEMON_BLOOD_SWORD, GRAPE_SWORD);
            entries.addAfter(GRAPE_SWORD, GRASS_SWORD);
            entries.addAfter(GRASS_SWORD, FINN_SWORD);
            

            entries.addAfter(Items.NETHERITE_AXE, MARCELINE_AXE);
            entries.addAfter(MARCELINE_AXE, MARCELINE_AXE_GUITAR);
            entries.addAfter(Items.TURTLE_HELMET, EMPTY_ICE_KING_CROWN);
            entries.addAfter(EMPTY_ICE_KING_CROWN, ICE_KING_CROWN);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.MUSIC_DISC_PIGSTEP, COME_ALONG_WITH_ME);
        });
    }
}
