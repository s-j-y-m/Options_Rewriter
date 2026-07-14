package com.sjym.optionsrewriter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
public final class OptionsRewriterNoticeScreen extends Screen {
 private final Screen parent; private final String custom;
 public OptionsRewriterNoticeScreen(Screen p,String c){super(Component.translatable("options_rewriter.notice.title"));parent=p;custom=c;}
 @Override protected void init(){addRenderableWidget(Button.builder(Component.translatable("options_rewriter.notice.restart_now"),b->minecraft.stop()).bounds(width/2-102,height/2+42,98,20).build());addRenderableWidget(Button.builder(Component.translatable("options_rewriter.notice.restart_later"),b->minecraft.setScreen(parent)).bounds(width/2+4,height/2+42,98,20).build());}
 @Override public void render(GuiGraphics g,int x,int y,float pt){renderBackground(g,x,y,pt);g.drawCenteredString(font,title,width/2,height/2-50,0xffffff);g.drawCenteredString(font,custom.isBlank()?Component.translatable("options_rewriter.notice.message"):Component.literal(custom),width/2,height/2-14,0xffffff);g.drawCenteredString(font,Component.translatable("options_rewriter.notice.restart_required"),width/2,height/2+8,0xffd080);super.render(g,x,y,pt);}
 @Override public boolean shouldCloseOnEsc(){return false;}
}
