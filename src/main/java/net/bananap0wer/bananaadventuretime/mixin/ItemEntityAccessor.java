package net.bananap0wer.bananaadventuretime.mixin;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {
    @Accessor("throwerUuid")
    UUID bananaadventuretime$getThrowerUuid();

    @Accessor("thrower")
    Entity bananaadventuretime$getThrower();

    @Accessor("owner")
    UUID bananaadventuretime$getOwnerUuid();
}
