package com.sjym.optionsrewriter;

import com.sjym.optionsrewriter.common.OptionsSettings;
import com.sjym.optionsrewriter.common.OptionsStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod(OptionsRewriter.MODID)
public final class OptionsRewriter {
    public static final String MODID = "options_rewriter";
    private static boolean checked;
    private static boolean pendingNotice;
    private static OptionsSettings settings;

    public OptionsRewriter() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new OptionsRewriterConfigScreen(parent)));
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class ClientEvents {
        @SubscribeEvent
        public static void onTitleScreen(ScreenEvent.Init.Post event) {
            if (checked || !(event.getScreen() instanceof TitleScreen)) return;
            checked = true;
            Minecraft minecraft = Minecraft.getInstance();
            settings = OptionsSettings.load(minecraft.gameDirectory.toPath());
            try {
                if (OptionsStorage.applyOnce(minecraft.gameDirectory.toPath())) {
                    minecraft.options.load();
                    pendingNotice = settings.applyMode() == OptionsSettings.ApplyMode.RESTART_NOTICE;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END && pendingNotice && Minecraft.getInstance().screen instanceof TitleScreen title) {
                pendingNotice = false;
                Minecraft.getInstance().setScreen(new OptionsRewriterNoticeScreen(title, settings.restartMessage()));
            }
        }
    }
}
