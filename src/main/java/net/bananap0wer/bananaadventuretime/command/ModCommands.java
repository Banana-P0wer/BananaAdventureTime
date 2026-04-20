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
                                .executes(context -> executeLoot(context.getSource(), LootTables.VILLAGE_SNOWY_HOUSE_CHEST, "snowy_house")))
                        .then(literal("nether_fortress")
                                .executes(context -> executeLoot(context.getSource(), LootTables.NETHER_BRIDGE_CHEST,
                                        "nether_fortress"))))
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
                                                ModItems.DEMON_BLOOD_SWORD)))
                                .then(literal("grape_sword")
                                        .executes(context -> executeWeaponBreak(context.getSource(),
                                                ModItems.GRAPE_SWORD)))))
                .then(literal("food")
                        .then(literal("juice")
                                .executes(context -> executeFoodJuice(context.getSource())))
                        .then(literal("hunger")
                                .executes(context -> executeFoodHunger(context.getSource())))));
    }

    private static int executeHelp(ServerCommandSource source) {
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.help.loot"), false);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.help.ore"), false);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.help.weapon"), false);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.help.food"), false);
        return 1;
    }

    private static int executeLoot(ServerCommandSource source, RegistryKey<LootTable> lootTable, String lootName)
            throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = player.getServerWorld();
        HitResult hitResult = player.raycast(TARGET_RANGE, 0.0f, false);

        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            source.sendError(Text.translatable("command.bananaadventuretime.atdebug.loot.no_container"));
            return 0;
        }

        BlockPos pos = blockHitResult.getBlockPos();
        if (!(world.getBlockEntity(pos) instanceof LootableInventory lootableInventory)) {
            source.sendError(Text.translatable("command.bananaadventuretime.atdebug.loot.not_lootable", formatPos(pos)));
            return 0;
        }

        lootableInventory.setLootTable(null);
        lootableInventory.clear();
        lootableInventory.setLootTable(lootTable, world.getRandom().nextLong());
        lootableInventory.generateLoot(player);
        lootableInventory.markDirty();

        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.loot.generated",
                Text.translatable(getLootNameKey(lootName)), formatPos(pos)), false);
        return 1;
    }

    private static int executeOreInfo(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getBlockPos();
        RegistryEntry<Biome> biome = world.getBiome(pos);
        String biomeId = biome.getKey().map(key -> key.getValue().toString()).orElse("[unregistered]");

        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.ore.biome", biomeId), false);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.ore.mountain_biome",
                formatBoolean(biome.isIn(BiomeTags.IS_MOUNTAIN))), false);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.ore.placed_feature"), false);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.ore.placement"), false);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.ore.vein"), false);
        return 1;
    }

    private static int executeOrePlace(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ServerWorld world = player.getServerWorld();
        BlockPos targetPos = getTargetPos(player);
        Optional<BlockPos> origin = findNearestOreReplaceable(world, targetPos);

        if (origin.isEmpty()) {
            source.sendError(Text.translatable("command.bananaadventuretime.atdebug.ore.no_replaceable",
                    formatPos(targetPos)));
            return 0;
        }

        Optional<RegistryEntry.Reference<ConfiguredFeature<?, ?>>> feature = world.getRegistryManager()
                .get(RegistryKeys.CONFIGURED_FEATURE)
                .getEntry(ModConfiguredFeatures.RUBY_ORE_KEY);
        if (feature.isEmpty()) {
            source.sendError(Text.translatable("command.bananaadventuretime.atdebug.ore.not_registered"));
            return 0;
        }

        boolean generated = feature.get().value().generate(world, world.getChunkManager().getChunkGenerator(),
                world.getRandom(), origin.get());
        if (!generated) {
            source.sendError(Text.translatable("command.bananaadventuretime.atdebug.ore.not_generated",
                    formatPos(origin.get())));
            return 0;
        }

        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.ore.generated",
                formatPos(origin.get())), false);
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
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.ore.scan", radius,
                finalRubyOreCount, finalDeepslateRubyOreCount, total), false);
        return Math.max(total, 1);
    }

    private static int executeWeaponBreak(ServerCommandSource source, Item item) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ItemStack stack = new ItemStack(item);
        Text itemName = stack.getName();
        if (stack.isDamageable()) {
            stack.setDamage(Math.max(0, stack.getMaxDamage() - 1));
        }

        equipMainHand(player, stack);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.weapon.equipped",
                itemName), false);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.weapon.test_mode"), false);
        return 1;
    }

    private static int executeFoodJuice(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        Text juiceName = new ItemStack(ModItems.SWEET_BERRIES_JUICE).getName();

        giveOrDrop(player, new ItemStack(ModItems.SWEET_BERRIES_JUICE, JUICE_COUNT));
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.food.gave_item",
                JUICE_COUNT, juiceName), false);
        return 1;
    }

    private static int executeFoodHunger(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel(DEBUG_HUNGER_LEVEL);
        hungerManager.setSaturationLevel(0.0f);
        hungerManager.setExhaustion(0.0f);
        source.sendFeedback(() -> Text.translatable("command.bananaadventuretime.atdebug.food.hunger",
                DEBUG_HUNGER_LEVEL), false);
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

    private static String getLootNameKey(String lootName) {
        return "command.bananaadventuretime.atdebug.loot.type." + lootName;
    }

    private static Text formatBoolean(boolean value) {
        return Text.translatable(value ? "command.bananaadventuretime.boolean.true"
                : "command.bananaadventuretime.boolean.false");
    }
}
