package net.bananap0wer.bananaadventuretime.loot;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class ModLootTableModifiers {
    public static void registerLootTableModifiers() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) {
                return;
            }

            if (LootTables.IGLOO_CHEST_CHEST.equals(key)) {
                tableBuilder.pool(createEmptyIceKingCrownPool());
            }

            if (LootTables.VILLAGE_SNOWY_HOUSE_CHEST.equals(key)) {
                tableBuilder.pool(createEmptyIceKingCrownPool());
            }
        });
    }

    private static LootPool.Builder createEmptyIceKingCrownPool() {
        return LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1))
            .with(ItemEntry.builder(ModItems.EMPTY_ICE_KING_CROWN));
    }
}
