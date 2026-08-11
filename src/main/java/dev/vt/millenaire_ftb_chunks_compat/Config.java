package dev.vt.millenaire_ftb_chunks_compat;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Mil_ftb_c_compat.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_FTB_PROTECTION = BUILDER
            .comment("Whether to create server-side FTB Chunks team claims for village protection (Default: false)")
            .define("enableFtbProtection", false);

    private static final ModConfigSpec.BooleanValue SHOW_VILLAGE_BOUNDARIES = BUILDER
            .comment("Whether to render Millenaire village chunk boundaries on FTB Chunks map (Default: true)")
            .define("showVillageBoundaries", true);

    private static final ModConfigSpec.BooleanValue SHOW_VILLAGE_ICONS = BUILDER
            .comment("Whether to render Millenaire village labels on FTB Chunks map (Default: true)")
            .define("showVillageIcons", true);

    private static final ModConfigSpec.ConfigValue<String> NORMAN_COLOR = BUILDER
            .comment("Color for Norman culture in Hex format (Default: #3B82F6)")
            .define("normanColor", "#3B82F6");

    private static final ModConfigSpec.ConfigValue<String> JAPANESE_COLOR = BUILDER
            .comment("Color for Japanese culture in Hex format (Default: #EF4444)")
            .define("japaneseColor", "#EF4444");

    private static final ModConfigSpec.ConfigValue<String> SELJUK_COLOR = BUILDER
            .comment("Color for Seljuk culture in Hex format (Default: #10B981)")
            .define("seljukColor", "#10B981");

    private static final ModConfigSpec.ConfigValue<String> MAYAN_COLOR = BUILDER
            .comment("Color for Mayan culture in Hex format (Default: #EAB308)")
            .define("mayanColor", "#EAB308");

    private static final ModConfigSpec.ConfigValue<String> INDIAN_COLOR = BUILDER
            .comment("Color for Indian culture in Hex format (Default: #F97316)")
            .define("indianColor", "#F97316");

    private static final ModConfigSpec.ConfigValue<String> BYZANTINES_COLOR = BUILDER
            .comment("Color for Byzantine culture in Hex format (Default: #A855F7)")
            .define("byzantinesColor", "#A855F7");

    private static final ModConfigSpec.ConfigValue<String> INUITS_COLOR = BUILDER
            .comment("Color for Inuits culture in Hex format (Default: #06B6D4)")
            .define("inuitsColor", "#06B6D4");

    private static final ModConfigSpec.ConfigValue<String> DEFAULT_CULTURE_COLOR = BUILDER
            .comment("Default fallback color for unknown cultures in Hex format (Default: #06B6D4)")
            .define("defaultCultureColor", "#06B6D4");

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableFtbProtection;
    public static boolean showVillageBoundaries;
    public static boolean showVillageIcons;
    public static int defaultCultureColor;
    public static final Map<String, Integer> cultureColors = new HashMap<>();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableFtbProtection = ENABLE_FTB_PROTECTION.get();
        showVillageBoundaries = SHOW_VILLAGE_BOUNDARIES.get();
        showVillageIcons = SHOW_VILLAGE_ICONS.get();

        cultureColors.clear();
        cultureColors.put("norman", parseHexColor(NORMAN_COLOR.get(), 0xFF3B82F6));
        cultureColors.put("japanese", parseHexColor(JAPANESE_COLOR.get(), 0xFFEF4444));
        cultureColors.put("seljuk", parseHexColor(SELJUK_COLOR.get(), 0xFF10B981));
        cultureColors.put("mayan", parseHexColor(MAYAN_COLOR.get(), 0xFFEAB308));
        cultureColors.put("indian", parseHexColor(INDIAN_COLOR.get(), 0xFFF97316));
        cultureColors.put("hindi", parseHexColor(INDIAN_COLOR.get(), 0xFFF97316));
        cultureColors.put("byzantines", parseHexColor(BYZANTINES_COLOR.get(), 0xFFA855F7));
        cultureColors.put("byzantine", parseHexColor(BYZANTINES_COLOR.get(), 0xFFA855F7));
        cultureColors.put("inuits", parseHexColor(INUITS_COLOR.get(), 0xFF06B6D4));
        defaultCultureColor = parseHexColor(DEFAULT_CULTURE_COLOR.get(), 0xFF06B6D4);
    }

    private static int parseHexColor(String hex, int fallback) {
        if (hex == null || hex.isEmpty()) return fallback;
        try {
            String clean = hex.trim().replace("#", "");
            if (clean.length() == 6) {
                return (int) (Long.parseLong("FF" + clean, 16) & 0xFFFFFFFFL);
            } else if (clean.length() == 8) {
                return (int) (Long.parseLong(clean, 16) & 0xFFFFFFFFL);
            }
        } catch (Throwable ignored) {
        }
        return fallback;
    }
}
