package net.bananap0wer.bananaadventuretime.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.bananap0wer.bananaadventuretime.item.custom.DemonBloodSwordItem;
import net.bananap0wer.bananaadventuretime.item.custom.FourthDimensionSwordItem;
import net.bananap0wer.bananaadventuretime.item.custom.GrapeSwordItem;
import net.bananap0wer.bananaadventuretime.item.custom.MarcelineAxeGuitarItem;
import net.bananap0wer.bananaadventuretime.item.custom.SweetBerriesJuiceItem;
import net.bananap0wer.bananaadventuretime.sound.ModSounds;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModItems {
    private static final int PLAYER_BASE_ATTACK_DAMAGE = 1;
    private static final float PLAYER_BASE_ATTACK_SPEED = 4.0f;

    //Add items here 1/2
    public static final Item RUBY = refisterItem("ruby", new Item(new Item.Settings()));
    
    public static final Item MARCELINE_AXE = registerNetheriteAxe("marceline_axe", true, 8, 1.0f, 1);
    public static final Item MARCELINE_AXE_GUITAR = registerMarcelineAxeGuitar("marceline_axe_guitar");
    
    public static final Item ICE_KING_CROWN = refisterItem("ice_king_crown", new Item(new Item.Settings()));
    public static final Item EMPTY_ICE_KING_CROWN = refisterItem("empty_ice_king_crown", new Item(new Item.Settings()));
    
    public static final Item SCARLET = registerNetheriteSword("scarlet", true, 9, 2.0f, 5);
    public static final Item FOURTH_DIMENSION_SWORD = registerFourthDimensionSword("fourth_dimension_sword", 9, 1.7f, 5);
    public static final Item ROOT_SWORD = registerNetheriteSword("root_sword", false, 6, 1.9f, 1);
    public static final Item DEMON_BLOOD_SWORD = registerDemonBloodSword("demon_blood_sword", 12, 1.7f, 1);
    public static final Item GRAPE_SWORD = registerGrapeSword("grape_sword", 8, 1.6f, 1);
    public static final Item GRASS_SWORD = registerNetheriteSword("grass_sword", false, 3, 5.0f, 1);
    public static final Item FINN_SWORD = registerNetheriteSword("finn_sword", false, 14, 1.6f, 3);

    public static final Item SCARLET_BLADE = refisterItem("scarlet_blade", new Item(new Item.Settings()));
    public static final Item SCARLET_HANDLE = refisterItem("scarlet_handle", new Item(new Item.Settings()));

    public static final Item EMPTY_DEMON_BLOOD_SWORD = refisterItem("empty_demon_blood_sword", new Item(new Item.Settings()));
    public static final Item ECHO_OF_FINN = refisterItem("echo_of_finn", new Item(new Item.Settings()));

    public static final Item SWEET_BERRIES_JUICE = refisterItem("sweet_berries_juice",
        new SweetBerriesJuiceItem(new Item.Settings().food(ModFoodComponents.SWEET_BERRIES_JUICE).maxCount(16)));
    public static final Item APPLE_PIE = refisterItem("apple_pie",
        new Item(new Item.Settings().food(ModFoodComponents.APPLE_PIE)));
    public static final Item TIME_SANDWICH = refisterItem("time_sandwich",
        new Item(new Item.Settings().food(ModFoodComponents.TIME_SANDWICH)));

    public static final Item COME_ALONG_WITH_ME = refisterItem("come_along_with_me",
        new Item(new Item.Settings().jukeboxPlayable(ModSounds.COME_ALONG_WITH_ME_KEY).maxCount(1)));

    private static Item registerNetheriteSword(String name, boolean fireproof, int attackDamage, float attackSpeed, int durabilityMultiplier) {
        Item.Settings settings = createNetheriteSwordSettings(fireproof, attackDamage, attackSpeed, durabilityMultiplier);
        return refisterItem(name, new SwordItem(ToolMaterials.NETHERITE, settings));
    }

    private static Item registerFourthDimensionSword(String name, int attackDamage, float attackSpeed, int durabilityMultiplier) {
        Item.Settings settings = createNetheriteSwordSettings(true, attackDamage, attackSpeed, durabilityMultiplier);
        return refisterItem(name, new FourthDimensionSwordItem(ToolMaterials.NETHERITE, settings));
    }

    private static Item registerGrapeSword(String name, int attackDamage, float attackSpeed, int durabilityMultiplier) {
        Item.Settings settings = createNetheriteSwordSettings(false, attackDamage, attackSpeed, durabilityMultiplier);
        return refisterItem(name, new GrapeSwordItem(ToolMaterials.NETHERITE, settings));
    }

    private static Item registerDemonBloodSword(String name, int attackDamage, float attackSpeed, int durabilityMultiplier) {
        Item.Settings settings = createNetheriteSwordSettings(true, attackDamage, attackSpeed, durabilityMultiplier);
        return refisterItem(name, new DemonBloodSwordItem(ToolMaterials.NETHERITE, settings));
    }

    private static Item registerNetheriteAxe(String name, boolean fireproof, int attackDamage, float attackSpeed, int durabilityMultiplier) {
        Item.Settings settings = new Item.Settings()
            .maxDamage(ToolMaterials.NETHERITE.getDurability() * durabilityMultiplier)
            .attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.NETHERITE,
                getNetheriteWeaponAttackDamage(attackDamage), getWeaponAttackSpeed(attackSpeed)));

        if (fireproof) {
            settings.fireproof();
        }

        return refisterItem(name, new AxeItem(ToolMaterials.NETHERITE, settings));
    }

    private static Item registerMarcelineAxeGuitar(String name) {
        Item.Settings settings = new Item.Settings()
            .maxDamage(ToolMaterials.NETHERITE.getDurability())
            .attributeModifiers(HoeItem.createAttributeModifiers(ToolMaterials.NETHERITE,
                getNetheriteWeaponAttackDamage(1), getWeaponAttackSpeed(4.0f)));

        return refisterItem(name, new MarcelineAxeGuitarItem(ToolMaterials.NETHERITE, settings));
    }

    private static Item.Settings createNetheriteSwordSettings(boolean fireproof, int attackDamage, float attackSpeed, int durabilityMultiplier) {
        Item.Settings settings = new Item.Settings()
            .maxDamage(ToolMaterials.NETHERITE.getDurability() * durabilityMultiplier)
            .attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE,
                getNetheriteWeaponAttackDamage(attackDamage), getWeaponAttackSpeed(attackSpeed)));

        if (fireproof) {
            settings.fireproof();
        }

        return settings;
    }

    private static int getNetheriteWeaponAttackDamage(int attackDamage) {
        return attackDamage - PLAYER_BASE_ATTACK_DAMAGE - (int) ToolMaterials.NETHERITE.getAttackDamage();
    }

    private static float getWeaponAttackSpeed(float attackSpeed) {
        return attackSpeed - PLAYER_BASE_ATTACK_SPEED;
    }

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
            entries.addAfter(Items.PUMPKIN_PIE, APPLE_PIE);
            entries.addAfter(APPLE_PIE, TIME_SANDWICH);
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
