package net.bananap0wer.bananaadventuretime.item;

import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.bananap0wer.bananaadventuretime.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup BANANAADVENTURETIME_TUB = Registry.register(Registries.ITEM_GROUP, 
        Identifier.of(BananaAdventureTime.MOD_ID, "bananaadventuretime_tub"), 
        FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.SWEET_BERRIES_JUICE))
            .displayName(Text.translatable("itemgroup.bananaadventuretime.tub"))
            .entries((displayContext, entries) -> {
                entries.add(ModItems.RUBY);
                entries.add(ModBlocks.RUBY_BLOCK);
                entries.add(ModBlocks.RUBY_ORE);
                entries.add(ModBlocks.DEEPSLATE_RUBY_ORE);

                entries.add(ModItems.SCARLET_HANDLE);
                entries.add(ModItems.SCARLET_BLADE);
                entries.add(ModItems.EMPTY_DEMON_BLOOD_SWORD);
                entries.add(ModItems.ECHO_OF_FINN);

                entries.add(ModItems.SWEET_BERRIES_JUICE);

                entries.add(ModItems.SCARLET);
                entries.add(ModItems.FOURTH_DIMENSION_SWORD);
                entries.add(ModItems.DEATH_SWORD);
                entries.add(ModItems.ROOT_SWORD);
                entries.add(ModItems.DEMON_BLOOD_SWORD);
                entries.add(ModItems.GRAPE_SWORD);
                entries.add(ModItems.GRASS_SWORD);
                entries.add(ModItems.FINN_SWORD);
                

                entries.add(ModItems.MARCELINE_AXE);
                entries.add(ModItems.MARCELINE_AXE_GUITAR);
                entries.add(ModItems.EMPTY_ICE_KING_CROWN);
                entries.add(ModItems.ICE_KING_CROWN);

                entries.add(ModItems.COME_ALONG_WITH_ME);
            }).build());


    public static void registerItemGroups() {
        BananaAdventureTime.LOGGER.info("Registering Mod Groups for " + BananaAdventureTime.MOD_ID);
    }
}
