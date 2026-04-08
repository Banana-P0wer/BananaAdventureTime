package net.bananap0wer.bananaadventuretime.util;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class ModLootTableModifiers {
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
        });
    }

    private static void addEmptyIceKingCrownPool(LootTable.Builder tableBuilder) {
        LootPool.Builder poolBuilder = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(ModItems.EMPTY_ICE_KING_CROWN));

        tableBuilder.pool(poolBuilder.build());
    }
}
