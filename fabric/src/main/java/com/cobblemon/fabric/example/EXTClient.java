package com.cobblemon.fabric.example;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EXTClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("CobbleExtraData");
    private static KeyMapping periodKeyBinding;

    @Override
    public void onInitializeClient() {
        // Initialize client-side keybinding
        periodKeyBinding = new KeyMapping(
                "Pokémon Info", // Keybinding name
                InputConstants.Type.KEYSYM, // Input type
                GLFW.GLFW_KEY_PERIOD, // Default key: period key
                "key.categories.cobbleextradata" // Keybinding category
        );

        KeyBindingHelper.registerKeyBinding(periodKeyBinding); // Register the keybinding
        LOGGER.info("Keybinding registered.");

        // Add a client-side tick event listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (periodKeyBinding.consumeClick()) { // Check if the keybinding is triggered
                try {
                    Pokemon activePokemon = CobblemonClient.INSTANCE.getStorage().getMyParty()
                            .get(CobblemonClient.INSTANCE.getStorage().getSelectedSlot());

                    if (activePokemon != null) {
                        // Open the custom GUI screen with the Pokémon data
                        client.setScreen(new PokemonInfoScreen(activePokemon));
                    } else {
                        LOGGER.info("No Pokémon in the active slot.");
                    }
                } catch (Exception e) {
                    LOGGER.error("Error fetching the active Pokémon:", e);
                }
            }
        });

        LOGGER.info("Client-side initialization complete.");
    }
}