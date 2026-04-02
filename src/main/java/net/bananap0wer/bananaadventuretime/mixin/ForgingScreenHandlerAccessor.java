package net.bananap0wer.bananaadventuretime.mixin;

import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ForgingScreenHandler.class)
public interface ForgingScreenHandlerAccessor {
    @Accessor("input")
    Inventory bananaadventuretime$getInput();

    @Accessor("output")
    CraftingResultInventory bananaadventuretime$getOutput();
}
