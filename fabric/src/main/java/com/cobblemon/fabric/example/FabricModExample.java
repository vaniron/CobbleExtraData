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
        // Listen for the POKEMON_CAPTURED event with a priority
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, event -> {
            try {
                // Get the caught Pokémon and player
                Pokemon pokemon = event.getPokemon();
                ServerPlayer player = event.getPlayer();

                // Get the world where the Pokémon was caught
                ServerLevel world = (ServerLevel) player.level();

                // Set the met date
                LocalDate currentDate = LocalDate.now();
                String metDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                pokemon.getPersistentData().putString("MetDate", metDate);

                //Set the met location
                Biome biome = world.getBiome(player.blockPosition()).value(); // Use player's position here
                ResourceLocation biomeId = world.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
                String biomeName = biomeId.toString();
                pokemon.getPersistentData().putString("MetLocation", biomeName);

                //Set the met level
                int metLevel = pokemon.getLevel(); // Get the Pokémon's level
                pokemon.getPersistentData().putInt("MetLevel", metLevel);


                // Log the met date and location for debugging
                System.out.println("Pokémon caught on: " + metDate);
                System.out.println("Pokémon caught in biome: " + biomeName);
                System.out.println("Pokémon caught at level: " + metLevel);


            } catch (Exception e) {
                // Log any unexpected errors
                System.err.println("An error occurred in the POKEMON_CAPTURED event handler:");
                e.printStackTrace();
            }

            return null; // Return null to satisfy the event handler's return type requirement
        });
    }
}