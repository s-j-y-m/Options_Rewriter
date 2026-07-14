package com.sjym.optionsrewriter;
import com.sjym.optionsrewriter.common.OptionsSettings;
import com.sjym.optionsrewriter.common.OptionsStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.io.IOException;
public final class OptionsRewriterConfigScreen extends Screen {
 private final Screen parent;private OptionsSettings settings;private EditBox message;private Component status=Component.empty();
 public OptionsRewriterConfigScreen(Screen p){super(Component.translatable("options_rewriter.config.title"));parent=p;}
 @Override protected void init(){settings=OptionsSettings.load(minecraft.gameDirectory.toPath());addRenderableWidget(Button.builder(modeLabel(),b->{settings.toggleApplyMode();b.setMessage(modeLabel());}).bounds(width/2-150,height/2-80,300,20).build());message=addRenderableWidget(new EditBox(font,width/2-150,height/2-40,300,20,Component.translatable("options_rewriter.config.restart_message")));message.setValue(settings.restartMessage());addRenderableWidget(Button.builder(Component.translatable("options_rewriter.config.copy_options"),b->copy()).bounds(width/2-150,height/2,145,20).build());addRenderableWidget(Button.builder(Component.translatable("options_rewriter.config.reset_flag"),b->reset()).bounds(width/2+5,height/2,145,20).build());addRenderableWidget(Button.builder(Component.translatable("options_rewriter.config.save"),b->save()).bounds(width/2-102,height/2+45,98,20).build());addRenderableWidget(Button.builder(Component.translatable("options_rewriter.config.cancel"),b->minecraft.setScreen(parent)).bounds(width/2+4,height/2+45,98,20).build());}
 private Component modeLabel(){String k=settings.applyMode()==OptionsSettings.ApplyMode.RESTART_NOTICE?"options_rewriter.config.apply_mode.restart_notice":"options_rewriter.config.apply_mode.silent";return Component.translatable("options_rewriter.config.apply_mode.value",Component.translatable(k));}
 private void copy(){try{OptionsStorage.saveCurrentOptionsAsTemplate(minecraft.gameDirectory.toPath());status=Component.translatable("options_rewriter.status.options_copied");}catch(IOException e){status=Component.translatable("options_rewriter.status.options_copy_failed");}}
 private void reset(){try{OptionsStorage.deleteFlag(minecraft.gameDirectory.toPath());status=Component.translatable("options_rewriter.status.flag_deleted");}catch(IOException e){status=Component.translatable("options_rewriter.status.flag_delete_failed");}}
 private void save(){try{settings.restartMessage(message.getValue());settings.save(minecraft.gameDirectory.toPath());minecraft.setScreen(parent);}catch(IOException e){status=Component.translatable("options_rewriter.status.options_copy_failed");}}
 @Override public void render(GuiGraphics g,int x,int y,float pt){renderBackground(g,x,y,pt);g.drawCenteredString(font,title,width/2,height/2-112,0xffffff);g.drawCenteredString(font,status,width/2,height/2+72,0xffffa0);super.render(g,x,y,pt);}
}
