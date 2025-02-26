package com.cobblemon.fabric.example;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import com.cobblemon.mod.common.pokemon.Pokemon;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Dimension;

public class PokemonInfoScreen extends Screen {
    private final Pokemon pokemon;
    private ResourceLocation pokemonSprite;
    private ResourceLocation originMark;

    protected PokemonInfoScreen(Pokemon pokemon) {
        super(Component.literal("Pokémon Info"));
        this.pokemon = pokemon;
        String originMarkSprite = pokemon.getPersistentData().getString("OriginGame").toLowerCase();;

        // Assuming the species name is in lowercase and matches the file name
        String speciesName = pokemon.getSpecies().getName().toLowerCase();
        this.pokemonSprite = ResourceLocation.fromNamespaceAndPath("cobblemon_multiplatform_mdk", "textures/sprites/regular/" + speciesName + ".png");
        this.originMark = ResourceLocation.fromNamespaceAndPath("cobblemon_multiplatform_mdk", "textures/origin/" + originMarkSprite + ".png");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Render the background with the correct arguments
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        // Define the position and size of the background panel
        int panelWidth = 220; // Slightly wider for better spacing
        int panelHeight = 120; // Adjusted height for better fit
        int x = (this.width - panelWidth) / 2; // Center horizontally
        int y = (this.height - panelHeight) / 2; // Center vertically


        // Size of the Pokémon sprite
        int spriteWidth = 68;
        int spriteHeight = 56;

        // Compute the coordinates to center the sprite horizontally and position it properly
        int spriteX = (this.width - spriteWidth) / 2; // Center horizontally
        int spriteY = y - spriteHeight;           // Position above the panel

        // Set the sprite texture and draw it
        RenderSystem.setShaderTexture(0, pokemonSprite);
        guiGraphics.blit(pokemonSprite, spriteX, spriteY, 0, 0, spriteWidth, spriteHeight, spriteWidth, spriteHeight);

        // Draw a semi-transparent background panel with a shadow
        guiGraphics.fill(x, y, x + panelWidth, y + panelHeight, 0x80000000); // Semi-transparent black
        guiGraphics.fill(x + 2, y + 2, x + panelWidth + 2, y + panelHeight + 2, 0x20000000); // Shadow effect

        // Draw a border around the panel
        guiGraphics.fill(x - 1, y - 1, x + panelWidth + 1, y, 0xFFAAAAAA); // Top border (light gray)
        guiGraphics.fill(x - 1, y + panelHeight, x + panelWidth + 1, y + panelHeight + 1, 0xFFAAAAAA); // Bottom border
        guiGraphics.fill(x - 1, y, x, y + panelHeight, 0xFFAAAAAA); // Left border
        guiGraphics.fill(x + panelWidth, y, x + panelWidth + 1, y + panelHeight, 0xFFAAAAAA); // Right border

        // Draw the Pokémon data on the screen
        int textX = x + 10; // Padding from the left
        int textY = y + 10; // Padding from the top

        // Title: Pokémon Info (with shadow)
        guiGraphics.drawString(this.font, "Pokémon Info", textX, textY, 0xFFFFFF, true); // Text with shadow

        // Origin Game (with shadow)
        String originGame = pokemon.getPersistentData().getString("OriginGame").toLowerCase();
        String regionName = OriginGameMapper.getRegion(originGame);
        guiGraphics.drawString(this.font, "This Pokémon originated from " + regionName, textX, textY + 20, 0xFFFFFF, true);

        // Met Date (with shadow)
        String dateString = pokemon.getPersistentData().getString("MetDate");
        guiGraphics.drawString(this.font, dateString, textX, textY + 40, 0xFFFFFF, true);

        // Met Location (with shadow)
        String metLocation = pokemon.getPersistentData().getString("MetLocation");
        if (!metLocation.contains(":")) {
            metLocation = "Cobblemon Transporter";
        } else {
            // Remove the ":" prefix
            metLocation = metLocation.substring(metLocation.indexOf(":") + 1);
            // Replace underscores with spaces
            metLocation = metLocation.replace("_", " ");
            // Capitalize the first letter of each word
            metLocation = capitalizeEachWord(metLocation);
        }
        guiGraphics.drawString(this.font, metLocation, textX, textY + 60, 0xFFFFFF, true);

        // Met Level (with shadow)
        guiGraphics.drawString(this.font, "Met at Lv. " + pokemon.getPersistentData().getInt("MetLevel") + ".", textX, textY + 80, 0xFFFFFF, true);

        // Draw the origin mark at the bottom right of the panel
        int originMarkSize = 16; // Origin mark is 64x64
        int originMarkX = x + panelWidth - originMarkSize - 5; // 5 pixels from the right edge
        int originMarkY = y + panelHeight - originMarkSize - 5; // 5 pixels from the bottom edge

        // Enable nearest-neighbor filtering for the origin mark
        RenderSystem.setShaderTexture(0, originMark);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        guiGraphics.blit(originMark, originMarkX, originMarkY, 0, 0, originMarkSize, originMarkSize, originMarkSize, originMarkSize);

        // Reset texture filtering to default (optional)
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    // Helper method to capitalize each word
    private String capitalizeEachWord(String text) {
        String[] words = text.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            capitalized.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return capitalized.toString().trim();
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}