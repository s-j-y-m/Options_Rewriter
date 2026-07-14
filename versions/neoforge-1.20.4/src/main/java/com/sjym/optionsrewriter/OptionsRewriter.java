package com.sjym.optionsrewriter;

import com.sjym.optionsrewriter.common.OptionsSettings;
import com.sjym.optionsrewriter.common.OptionsStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.ScreenEvent;
import java.io.IOException;

@Mod(OptionsRewriter.MODID)
public final class OptionsRewriter {
    public static final String MODID = "options_rewriter";
    public OptionsRewriter(IEventBus bus) {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> new OptionsRewriterConfigScreen(parent)));
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static final class ClientEvents {
        private static boolean checked;
        @SubscribeEvent public static void render(ScreenEvent.Render.Post event) {
            if (checked || !(event.getScreen() instanceof TitleScreen title)) return;
            checked = true;
            Minecraft mc = Minecraft.getInstance();
            OptionsSettings settings = OptionsSettings.load(mc.gameDirectory.toPath());
            try {
                if (OptionsStorage.applyOnce(mc.gameDirectory.toPath())) {
                    mc.options.load();
                    if (settings.applyMode() == OptionsSettings.ApplyMode.RESTART_NOTICE)
                        mc.execute(() -> mc.setScreen(new OptionsRewriterNoticeScreen(title, settings.restartMessage())));
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
