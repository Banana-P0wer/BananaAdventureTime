package net.bananap0wer.bananaadventuretime.mixin;

import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    private static final int INPUT_SLOT = 0;
    private static final int REPAIR_SLOT = 1;
    private static final int OUTPUT_SLOT = 0;
    private static final int SAPLINGS_FOR_FULL_REPAIR = 32;
    private static final int ROOT_SWORD_REPAIR_LEVEL_COST = 1;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void bananaadventuretime$repairRootSwordWithSaplings(CallbackInfo info) {
        Inventory input = ((ForgingScreenHandlerAccessor) this).bananaadventuretime$getInput();
        ItemStack rootSword = input.getStack(INPUT_SLOT);
        ItemStack repairStack = input.getStack(REPAIR_SLOT);

        if (!canRepairRootSword(rootSword, repairStack)) {
            return;
        }

        int saplingsUsed = getSaplingsUsed(rootSword, repairStack);
        if (saplingsUsed <= 0) {
            return;
        }

        ItemStack result = rootSword.copy();
        result.setCount(1);
        result.setDamage(Math.max(0, rootSword.getDamage() - getRepairAmount(rootSword, saplingsUsed)));

        CraftingResultInventory output = ((ForgingScreenHandlerAccessor) this).bananaadventuretime$getOutput();
        output.setStack(OUTPUT_SLOT, result);
        output.markDirty();

        AnvilScreenHandlerAccessor anvil = (AnvilScreenHandlerAccessor) this;
        anvil.bananaadventuretime$setRepairItemUsage(saplingsUsed);
        anvil.bananaadventuretime$getLevelCost().set(ROOT_SWORD_REPAIR_LEVEL_COST);
        ((ScreenHandler) (Object) this).sendContentUpdates();

        info.cancel();
    }

    private static boolean canRepairRootSword(ItemStack rootSword, ItemStack repairStack) {
        return rootSword.isOf(ModItems.ROOT_SWORD)
            && rootSword.isDamaged()
            && repairStack.isIn(ItemTags.SAPLINGS);
    }

    private static int getSaplingsUsed(ItemStack rootSword, ItemStack repairStack) {
        int repairAmountPerSapling = getRepairAmountPerSapling(rootSword);
        int neededSaplings = (rootSword.getDamage() + repairAmountPerSapling - 1) / repairAmountPerSapling;
        return Math.min(repairStack.getCount(), neededSaplings);
    }

    private static int getRepairAmount(ItemStack rootSword, int saplingsUsed) {
        return getRepairAmountPerSapling(rootSword) * saplingsUsed;
    }

    private static int getRepairAmountPerSapling(ItemStack rootSword) {
        return (rootSword.getMaxDamage() + SAPLINGS_FOR_FULL_REPAIR - 1) / SAPLINGS_FOR_FULL_REPAIR;
    }
}
