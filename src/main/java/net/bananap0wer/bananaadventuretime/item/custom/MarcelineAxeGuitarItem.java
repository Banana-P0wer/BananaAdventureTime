package net.bananap0wer.bananaadventuretime.item.custom;

import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MarcelineAxeGuitarItem extends Item {
    private static final Identifier BACK_TO_THE_AXE_ID = Identifier.of(BananaAdventureTime.MOD_ID, "marceline/back_to_the_axe");
    private static final Identifier VAMPIRE_QUEENS_GUITAR_ID = Identifier.of(BananaAdventureTime.MOD_ID,
        "marceline/vampire_queens_guitar");
    private static final String BACK_TO_THE_AXE_CRITERION = "transformed_in_lava";

    public MarcelineAxeGuitarItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        if (entity.isInLava()) {
            for (int i = 0; i < entity.getStack().getCount(); i++) {
                entity.dropStack(new ItemStack(ModItems.MARCELINE_AXE));
            }

            grantBackToTheAxe(entity);
        }
    }

    private static void grantBackToTheAxe(ItemEntity entity) {
        Entity owner = entity.getOwner();
        if (!(owner instanceof ServerPlayerEntity player)) {
            return;
        }

        MinecraftServer server = entity.getWorld().getServer();
        if (server == null) {
            return;
        }

        AdvancementEntry vampireQueensGuitar = server.getAdvancementLoader().get(VAMPIRE_QUEENS_GUITAR_ID);
        AdvancementEntry backToTheAxe = server.getAdvancementLoader().get(BACK_TO_THE_AXE_ID);
        if (vampireQueensGuitar == null || backToTheAxe == null) {
            return;
        }

        if (player.getAdvancementTracker().getProgress(vampireQueensGuitar).isDone()) {
            player.getAdvancementTracker().grantCriterion(backToTheAxe, BACK_TO_THE_AXE_CRITERION);
        }
    }
}
