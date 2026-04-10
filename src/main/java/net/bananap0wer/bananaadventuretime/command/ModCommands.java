package net.bananap0wer.bananaadventuretime.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.bananap0wer.bananaadventuretime.block.ModBlocks;
import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.bananap0wer.bananaadventuretime.world.ModConfiguredFeatures;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Optional;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {
    private static final double TARGET_RANGE = 8.0;
    private static final int ORE_SEARCH_RANGE = 4;
    private static final int DEBUG_HUNGER_LEVEL = 16;
    private static final int JUICE_COUNT = 3;

    public static void registerCommands() {
        BananaAdventureTime.LOGGER.info("Registering Mod Commands for " + BananaAdventureTime.MOD_ID);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("atdebug")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> executeHelp(context.getSource()))
                .then(literal("loot")
                        .then(literal("igloo")
                                .executes(context -> executeLoot(context.getSource(), LootTables.IGLOO_CHEST_CHEST, "igloo")))
                        .then(literal("snowy_house")
                                .executes(context -> executeLoot(context.getSource(), LootTables.VILLAGE_SNOWY_HOUSE_CHEST, "snowy_house"))))
                .then(literal("ore")
                        .then(literal("info")
                                .executes(context -> executeOreInfo(context.getSource())))
                        .then(literal("place")
                                .executes(context -> executeOrePlace(context.getSource())))
                        .then(literal("scan")
                                .then(argument("radius", IntegerArgumentType.integer(1, 128))
                                        .executes(context -> executeOreScan(context.getSource(),
                                                IntegerArgumentType.getInteger(context, "radius"))))))
                .then(literal("weapon")
                        .then(literal("break")
                                .then(literal("demon_blood_sword")
                                        .executes(context -> executeWeaponBreak(context.getSource(),
                                                ModItems.DEMON_BLOOD_SWORD, "Demon Blood Sword")))
                                .then(literal("grape_sword")
                                        .executes(context -> executeWeaponBreak(context.getSource(),
                                                ModItems.GRAPE_SWORD, "Grape Sword")))))
                .then(literal("food")
                        .then(literal("juice")
                                .executes(context -> executeFoodJuice(context.getSource())))
                        .then(literal("hunger")
                                .executes(context -> executeFoodHunger(context.getSource())))));
    }

    private static int executeHelp(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Available: /atdebug loot igloo|snowy_house"), false);
        source.sendFeedback(() -> Text.literal("Available: /atdebug ore info|place|scan <radius>"), false);
        source.sendFeedback(() -> Text.literal("Available: /atdebug weapon break demon_blood_sword|grape_sword"), false);
        source.sendFeedback(() -> Text.literal("Available: /atdebug food juice|hunger"), false);
        return 1;
    }

    private static int executeLoot(ServerCommandSource source, RegistryKey<LootTable> lootTable, String lootName)
            throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = player.getServerWorld();
        HitResult hitResult = player.raycast(TARGET_RANGE, 0.0f, false);

        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            source.sendError(Text.literal("Look at a lootable container first."));
            return 0;
        }

        BlockPos pos = blockHitResult.getBlockPos();
        if (!(world.getBlockEntity(pos) instanceof LootableInventory lootableInventory)) {
            source.sendError(Text.literal("Target block is not a lootable container: " + formatPos(pos)));
            return 0;
        }

        lootableInventory.setLootTable(null);
        lootableInventory.clear();
        lootableInventory.setLootTable(lootTable, world.getRandom().nextLong());
        lootableInventory.generateLoot(player);
        lootableInventory.markDirty();

        source.sendFeedback(() -> Text.literal("Generated " + lootName + " loot at " + formatPos(pos)), false);
        return 1;
    }

    private static int executeOreInfo(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getBlockPos();
        RegistryEntry<Biome> biome = world.getBiome(pos);
        String biomeId = biome.getKey().map(key -> key.getValue().toString()).orElse("[unregistered]");

        source.sendFeedback(() -> Text.literal("Biome: " + biomeId), false);
        source.sendFeedback(() -> Text.literal("Mountain biome: " + biome.isIn(BiomeTags.IS_MOUNTAIN)), false);
        source.sendFeedback(() -> Text.literal("Ruby ore placed feature: bananaadventuretime:ruby_ore_placed"), false);
        source.sendFeedback(() -> Text.literal("Placement: count=100, height=trapezoid -16..480, biome=#minecraft:is_mountain"), false);
        source.sendFeedback(() -> Text.literal("Vein: size=3, targets=stone/deepslate ore replaceables"), false);
        return 1;
    }

    private static int executeOrePlace(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = player.getServerWorld();
        BlockPos targetPos = getTargetPos(player);
        Optional<BlockPos> origin = findNearestOreReplaceable(world, targetPos);

        if (origin.isEmpty()) {
            source.sendError(Text.literal("No stone/deepslate ore replaceable block near " + formatPos(targetPos)));
            return 0;
        }

        Optional<RegistryEntry.Reference<ConfiguredFeature<?, ?>>> feature = world.getRegistryManager()
                .get(RegistryKeys.CONFIGURED_FEATURE)
                .getEntry(ModConfiguredFeatures.RUBY_ORE_KEY);
        if (feature.isEmpty()) {
            source.sendError(Text.literal("Ruby ore configured feature is not registered."));
            return 0;
        }

        boolean generated = feature.get().value().generate(world, world.getChunkManager().getChunkGenerator(),
                world.getRandom(), origin.get());
        if (!generated) {
            source.sendError(Text.literal("Ruby ore feature did not generate at " + formatPos(origin.get())));
            return 0;
        }

        source.sendFeedback(() -> Text.literal("Generated ruby ore feature near " + formatPos(origin.get())), false);
        return 1;
    }

    private static int executeOreScan(ServerCommandSource source, int radius) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = player.getServerWorld();
        BlockPos center = player.getBlockPos();
        int rubyOreCount = 0;
        int deepslateRubyOreCount = 0;

        for (BlockPos pos : BlockPos.iterate(center.add(-radius, -radius, -radius), center.add(radius, radius, radius))) {
            if (!world.isInBuildLimit(pos)) {
                continue;
            }

            BlockState state = world.getBlockState(pos);
            if (state.isOf(ModBlocks.RUBY_ORE)) {
                rubyOreCount++;
            }

            if (state.isOf(ModBlocks.DEEPSLATE_RUBY_ORE)) {
                deepslateRubyOreCount++;
            }
        }

        int total = rubyOreCount + deepslateRubyOreCount;
        int finalRubyOreCount = rubyOreCount;
        int finalDeepslateRubyOreCount = deepslateRubyOreCount;
        source.sendFeedback(() -> Text.literal("Ruby ore scan radius " + radius + ": ruby=" + finalRubyOreCount
                + ", deepslate=" + finalDeepslateRubyOreCount + ", total=" + total), false);
        return Math.max(total, 1);
    }

    private static int executeWeaponBreak(ServerCommandSource source, Item item, String itemName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ItemStack stack = new ItemStack(item);
        if (stack.isDamageable()) {
            stack.setDamage(Math.max(0, stack.getMaxDamage() - 1));
        }

        equipMainHand(player, stack);
        source.sendFeedback(() -> Text.literal("Equipped almost-broken " + itemName + " in main hand."), false);
        source.sendFeedback(() -> Text.literal("Use survival/adventure mode when testing break replacement."), false);
        return 1;
    }

    private static int executeFoodJuice(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        giveOrDrop(player, new ItemStack(ModItems.SWEET_BERRIES_JUICE, JUICE_COUNT));
        source.sendFeedback(() -> Text.literal("Gave " + JUICE_COUNT + " Sweet Berries Juice."), false);
        return 1;
    }

    private static int executeFoodHunger(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel(DEBUG_HUNGER_LEVEL);
        hungerManager.setSaturationLevel(0.0f);
        hungerManager.setExhaustion(0.0f);
        source.sendFeedback(() -> Text.literal("Set hunger to " + DEBUG_HUNGER_LEVEL + "/20 and saturation to 0."), false);
        return 1;
    }

    private static BlockPos getTargetPos(ServerPlayerEntity player) {
        HitResult hitResult = player.raycast(TARGET_RANGE, 0.0f, false);
        if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
            return blockHitResult.getBlockPos();
        }

        return player.getBlockPos().down();
    }

    private static Optional<BlockPos> findNearestOreReplaceable(ServerWorld world, BlockPos center) {
        for (BlockPos pos : BlockPos.iterateOutwards(center, ORE_SEARCH_RANGE, ORE_SEARCH_RANGE, ORE_SEARCH_RANGE)) {
            if (world.isInBuildLimit(pos) && isOreReplaceable(world.getBlockState(pos))) {
                return Optional.of(pos.toImmutable());
            }
        }

        return Optional.empty();
    }

    private static boolean isOreReplaceable(BlockState state) {
        return state.isIn(BlockTags.STONE_ORE_REPLACEABLES) || state.isIn(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    }

    private static void equipMainHand(ServerPlayerEntity player, ItemStack stack) {
        ItemStack previousStack = player.getMainHandStack().copy();

        player.getInventory().setStack(player.getInventory().selectedSlot, stack);
        if (!previousStack.isEmpty()) {
            giveOrDrop(player, previousStack);
        }

        player.currentScreenHandler.sendContentUpdates();
    }

    private static void giveOrDrop(ServerPlayerEntity player, ItemStack stack) {
        if (!player.giveItemStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    private static String formatPos(BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }
}
