package com.sjym.optionsrewriter;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(OptionsRewriter.MODID)
public class OptionsRewriter {
    public static final String MODID = "options_rewriter";
    public static final Logger LOGGER = LogUtils.getLogger();

    private boolean checkedThisSession;
    private boolean pendingRestartNotice;

    public OptionsRewriter() {
        if (FMLEnvironmentHolder.isClient()) {
            MinecraftForge.EVENT_BUS.addListener(this::onScreenOpening);
            MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new OptionsRewriterConfigScreen(parent)));
        }
    }

    private void onScreenOpening(ScreenEvent.Opening event) {
        if (checkedThisSession || !(event.getScreen() instanceof TitleScreen)) {
            return;
        }

        checkedThisSession = true;
        OptionsRewriterConfig.load();
        OptionsRewriterService.ApplyResult result = OptionsRewriterService.applyOnFirstTitleScreen(Minecraft.getInstance(), event.getScreen());
        pendingRestartNotice = result.applied() && OptionsRewriterConfig.applyMode() == OptionsRewriterConfig.ApplyMode.RESTART_NOTICE;
    }

    private void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !pendingRestartNotice) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof TitleScreen titleScreen) {
            pendingRestartNotice = false;
            minecraft.setScreen(new OptionsRewriterNoticeScreen(titleScreen,
                    OptionsRewriterConfig.restartMessage()));
        }
    }

    private static final class FMLEnvironmentHolder {
        private static boolean isClient() {
            return net.minecraftforge.fml.loading.FMLEnvironment.dist == Dist.CLIENT;
        }
    }
}
