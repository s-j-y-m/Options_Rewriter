package com.sjym.optionsrewriter;
import com.sjym.optionsrewriter.common.OptionsSettings;
import com.sjym.optionsrewriter.common.OptionsStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import java.io.IOException;
@Mod(value=OptionsRewriter.MODID, dist=Dist.CLIENT)
@EventBusSubscriber(modid=OptionsRewriter.MODID, value=Dist.CLIENT)
public final class OptionsRewriterClient {
 public OptionsRewriterClient(ModContainer c){ c.registerExtensionPoint(IConfigScreenFactory.class, (container,parent)->new OptionsRewriterConfigScreen(parent)); }
 private static boolean checked;
 @SubscribeEvent public static void render(ScreenEvent.Render.Post e){ if(checked||!(e.getScreen() instanceof TitleScreen title))return; checked=true; Minecraft mc=Minecraft.getInstance(); OptionsSettings s=OptionsSettings.load(mc.gameDirectory.toPath()); try{if(OptionsStorage.applyOnce(mc.gameDirectory.toPath())){mc.options.load();if(s.applyMode()==OptionsSettings.ApplyMode.RESTART_NOTICE)mc.execute(()->mc.setScreen(new OptionsRewriterNoticeScreen(title,s.restartMessage())));}}catch(IOException x){x.printStackTrace();}}
}
