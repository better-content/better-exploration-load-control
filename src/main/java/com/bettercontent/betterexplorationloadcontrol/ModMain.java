package com.bettercontent.betterexplorationloadcontrol;

import com.bettercontent.betterexplorationloadcontrol.config.GovernorConfig;
import com.bettercontent.betterexplorationloadcontrol.performance.DistantHorizonsGenerationControl;
import com.bettercontent.betterexplorationloadcontrol.performance.PerformanceGovernorService;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;

@Mod(ModMain.MOD_ID)
public final class ModMain {
    public static final String MOD_ID = "better_exploration_load_control";

    public ModMain() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GovernorConfig.SPEC);
        PerformanceGovernorService.configure(GovernorConfig::configuration, DistantHorizonsGenerationControl::create);
        MinecraftForge.EVENT_BUS.register(PerformanceGovernorService.class);
    }
}
