package net.bananap0wer.bananaadventuretime.item.custom;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SweetBerriesJuiceItem extends Item {
    private static final int MAX_USE_TIME = 40;
    private static final int DRINK_WINDOW_TICKS = 20 * 60;
    private static final int HEAVY_DRINK_COUNT = 3;
    private static final int HEAVINESS_DURATION_TICKS = 20 * 60;
    private static final int HEAVINESS_AMPLIFIER = 0;
    private static final Map<UUID, Deque<Long>> DRINK_TIMES = new HashMap<>();

    public SweetBerriesJuiceItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return MAX_USE_TIME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);

        if (!world.isClient() && user instanceof PlayerEntity player) {
            applyHeavinessAfterRepeatedDrinks(world, player);
        }

        return result;
    }

    private static void applyHeavinessAfterRepeatedDrinks(World world, PlayerEntity player) {
        long currentTime = world.getTime();
        Deque<Long> drinkTimes = DRINK_TIMES.computeIfAbsent(player.getUuid(), uuid -> new ArrayDeque<>());

        while (!drinkTimes.isEmpty() && currentTime - drinkTimes.peekFirst() > DRINK_WINDOW_TICKS) {
            drinkTimes.removeFirst();
        }

        drinkTimes.addLast(currentTime);

        if (drinkTimes.size() >= HEAVY_DRINK_COUNT) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,
                HEAVINESS_DURATION_TICKS, HEAVINESS_AMPLIFIER));
            drinkTimes.clear();
        }
    }
}
