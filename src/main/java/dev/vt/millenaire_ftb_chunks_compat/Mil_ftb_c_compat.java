package dev.vt.millenaire_ftb_chunks_compat;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(Mil_ftb_c_compat.MODID)
public class Mil_ftb_c_compat {
    public static final String MODID = "millenaire_ftb_chunks_compat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Mil_ftb_c_compat(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Millenaire FTB Chunks Compat initialized successfully.");
    }
}
