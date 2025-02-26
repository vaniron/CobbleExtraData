package com.cobblemon.fabric.example;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EXTServer implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("CobbleExtraData");

    @Override
    public void onInitialize() {
        // Subscribe to Cobblemon events for server-side handling
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, event -> {
            try {
                handlePokemonEvent(event.getPokemon(), event.getPlayer());
            } catch (Exception e) {
                logError("POKEMON_CAPTURED", e);
            }
            return null;
        });

        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, event -> {
            try {
                handlePokemonEvent(event.getPokemon(), event.getPlayer());
            } catch (Exception e) {
                logError("STARTER_CHOSEN", e);
            }
            return null;
        });

        LOGGER.info("Server-side initialization complete.");
    }

    private void handlePokemonEvent(Pokemon pokemon, ServerPlayer player) {
        try {
            // Server-level logic for Pokémon event handling
            ServerLevel world = (ServerLevel) player.level();
            LocalDate currentDate = LocalDate.now();
            String metDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            pokemon.getPersistentData().putString("MetDate", metDate);

            Biome biome = world.getBiome(player.blockPosition()).value();
            ResourceLocation biomeId = world.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
            String biomeName = biomeId.toString();
            pokemon.getPersistentData().putString("MetLocation", biomeName);

            int metLevel = pokemon.getLevel();
            pokemon.getPersistentData().putInt("MetLevel", metLevel);

            pokemon.getPersistentData().putString("OriginGame", "Cobblemon");

            LOGGER.info("Pokémon event handled successfully:");
            LOGGER.info("Met Date: " + metDate);
            LOGGER.info("Met Biome: " + biomeName);
            LOGGER.info("Met Level: " + metLevel);
        } catch (Exception e) {
            logError("PokemonEvent", e);
        }
    }

    private void logError(String eventName, Exception e) {
        // Generalized error logging for event handlers
        LOGGER.error("An error occurred in the " + eventName + " event handler:", e);
    }
}