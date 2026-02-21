package net.bananap0wer.bananaadventuretime.sound;

import net.bananap0wer.bananaadventuretime.BananaAdventureTime;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent COME_ALONG_WITH_ME = registerSoundEvent("come_along_with_me");
    public static final RegistryKey<JukeboxSong> COME_ALONG_WITH_ME_KEY =
        RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(BananaAdventureTime.MOD_ID, "come_along_with_me"));

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(BananaAdventureTime.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        BananaAdventureTime.LOGGER.info("Registering Mod Sounds for " + BananaAdventureTime.MOD_ID);
    }
}
