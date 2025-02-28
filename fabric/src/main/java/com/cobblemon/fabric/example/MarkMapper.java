package com.cobblemon.fabric.example;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkMapper {
    // Logger instance
    public static final Logger LOGGER = LoggerFactory.getLogger("CobbleExtraData");

    private static final String MARK_TEXTURE_PATH = "textures/marks/";

    public static ResourceLocation getMarkTexture(int markId) {
        String markName = getMarkNameById(markId); // Get the mark name from its ID
        if (markName != null) {
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("cobblemonextradata", MARK_TEXTURE_PATH + markName + ".png");
            //LOGGER.info("Loading texture for mark ID {}: {}", markId, texture); // Debug logging
            return texture;
        }
        //LOGGER.error("Invalid mark ID: {}", markId); // Log invalid mark IDs
        return null; // Return null if the mark ID is invalid
    }

    private static String getMarkNameById(int markId) {
        return switch (markId) {
            case 53 -> "lunchtimemark";
            case 54 -> "sleepy-timemark";
            case 55 -> "duskmark";
            case 56 -> "dawnmark";
            case 57 -> "cloudymark";
            case 58 -> "rainymark";
            case 59 -> "stormymark";
            case 60 -> "snowymark";
            case 61 -> "blizzardmark";
            case 62 -> "drymark";
            case 63 -> "sandstormmark";
            case 64 -> "mistymark";
            case 65 -> "destinymark";
            case 66 -> "fishingmark";
            case 67 -> "currymark";
            case 68 -> "uncommonmark";
            case 69 -> "raremark";
            case 70 -> "rowdymark";
            case 71 -> "absent-mindedmark";
            case 72 -> "jitterymark";
            case 73 -> "excitedmark";
            case 74 -> "charismaticmark";
            case 75 -> "calmnessmark";
            case 76 -> "intensemark";
            case 77 -> "zoned-outmark";
            case 78 -> "joyfulmark";
            case 79 -> "angrymark";
            case 80 -> "smileymark";
            case 81 -> "tearymark";
            case 82 -> "upbeatmark";
            case 83 -> "peevedmark";
            case 84 -> "intellectualmark";
            case 85 -> "ferociousmark";
            case 86 -> "craftymark";
            case 87 -> "scowlingmark";
            case 88 -> "kindlymark";
            case 89 -> "flusteredmark";
            case 90 -> "pumpedupmark";
            case 91 -> "zeroenergymark";
            case 92 -> "pridefulmark";
            case 93 -> "unsuremark";
            case 94 -> "humblemark";
            case 95 -> "thornymark";
            case 96 -> "vigormark";
            case 97 -> "slumpmark";
            default -> {
                LOGGER.warn("Unknown mark ID: {}", markId); // Log unknown mark IDs
                yield null;
            }
        };
    }

    // Test case
    public static void main(String[] args) {
        ResourceLocation markTexture = getMarkTexture(53); // Test with mark ID 53
        LOGGER.info("Mark Texture: {}", markTexture);
    }

    public static String getMarkName(int markId) {

        return getMarkNameById(markId);
    }
}