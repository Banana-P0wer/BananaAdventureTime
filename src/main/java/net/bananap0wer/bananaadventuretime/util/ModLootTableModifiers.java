package net.bananap0wer.bananaadventuretime.util;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class ModLootTableModifiers {
    private static final float EMPTY_ICE_KING_CROWN_CHANCE = 0.15f;
    private static final float DEMON_BLOOD_SWORD_CHANCE = 0.05f;

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
            if (!source.isBuiltin()) {
                return;
            }

            if (LootTables.IGLOO_CHEST_CHEST.equals(key)) {
                addEmptyIceKingCrownPool(tableBuilder);
            }

            if (LootTables.VILLAGE_SNOWY_HOUSE_CHEST.equals(key)) {
                addEmptyIceKingCrownPool(tableBuilder);
            }

            if (LootTables.NETHER_BRIDGE_CHEST.equals(key)) {
                addDemonBloodSwordPool(tableBuilder);
            }
        });
    }

    private static void addEmptyIceKingCrownPool(LootTable.Builder tableBuilder) {
        addRareItemPool(tableBuilder, ModItems.EMPTY_ICE_KING_CROWN, EMPTY_ICE_KING_CROWN_CHANCE);
    }

    private static void addDemonBloodSwordPool(LootTable.Builder tableBuilder) {
        addRareItemPool(tableBuilder, ModItems.DEMON_BLOOD_SWORD, DEMON_BLOOD_SWORD_CHANCE);
    }

    private static void addRareItemPool(LootTable.Builder tableBuilder, Item item, float chance) {
        LootPool.Builder poolBuilder = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(ItemEntry.builder(item));

        tableBuilder.pool(poolBuilder.build());
    }
}
