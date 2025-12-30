package net.bananap0wer.bananaadventuretime;

import net.bananap0wer.bananaadventuretime.block.ModBlocks;
import net.bananap0wer.bananaadventuretime.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaAdventureTime implements ModInitializer {
	public static final String MOD_ID = "bananaadventuretime";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}