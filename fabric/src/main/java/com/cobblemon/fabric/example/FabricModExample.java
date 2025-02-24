package com.cobblemon.fabric.example;

import com.cobblemon.mod.common.*;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import net.fabricmc.api.ModInitializer;

public class FabricModExample implements ModInitializer {

    @Override
    public void onInitialize() {
        // Listen for the POKEMON_CAPTURED event
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, event -> {
            try {
                handlePokemonEvent(event.getPokemon(), event.getPlayer());
            } catch (Exception e) {
                logError("POKEMON_CAPTURED", e);
            }
            return null;
        });

        // Listen for the STARTER_CHOSEN event
        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, event -> {
            try {
                handlePokemonEvent(event.getPokemon(), event.getPlayer());
            } catch (Exception e) {
                logError("STARTER_CHOSEN", e);
            }
            return null;
        });

    }

    /**
     * Method to handle shared logic for Pokémon events.
     *
     * @param pokemon The Pokémon object.
     * @param player  The player interacting with the Pokémon.
     */
    private void handlePokemonEvent(Pokemon pokemon, ServerPlayer player) {
        // Get the world where the Pokémon event occurred
        ServerLevel world = (ServerLevel) player.level();

        // Set the met date
        LocalDate currentDate = LocalDate.now();
        String metDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        pokemon.getPersistentData().putString("MetDate", metDate);

        // Set the met location
        Biome biome = world.getBiome(player.blockPosition()).value(); // Use player's position here
        ResourceLocation biomeId = world.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
        String biomeName = biomeId.toString();
        pokemon.getPersistentData().putString("MetLocation", biomeName);

        // Set the met level
        int metLevel = pokemon.getLevel(); // Get the Pokémon's level
        pokemon.getPersistentData().putInt("MetLevel", metLevel);

        // Set origin game
        pokemon.getPersistentData().putString("OriginGame", "Cobblemon");

        // Log the details for debugging
        System.out.println("Pokémon event handled:");
        System.out.println("Met Date: " + metDate);
        System.out.println("Met Biome: " + biomeName);
        System.out.println("Met Level: " + metLevel);
    }

    /**
     * Logs errors occurring during event handling.
     *
     * @param eventName The name of the event.
     * @param e         The exception that occurred.
     */
    private void logError(String eventName, Exception e) {
        System.err.println("An error occurred in the " + eventName + " event handler:");
        e.printStackTrace();
    }
}