package net.bananap0wer.bananaadventuretime.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilScreenHandler.class)
public interface AnvilScreenHandlerAccessor {
    @Accessor("repairItemUsage")
    void bananaadventuretime$setRepairItemUsage(int repairItemUsage);

    @Accessor("levelCost")
    Property bananaadventuretime$getLevelCost();
}
