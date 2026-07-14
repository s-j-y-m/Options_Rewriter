package com.sjym.optionsrewriter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public final class OptionsRewriterNoticeScreen extends Screen {
    private final Screen parent;
    private final String customMessage;

    public OptionsRewriterNoticeScreen(Screen parent, String customMessage) {
        super(new TranslatableComponent("options_rewriter.notice.title"));
        this.parent = parent;
        this.customMessage = customMessage;
    }

    @Override protected void init() {
        addRenderableWidget(new Button(width / 2 - 102, height / 2 + 42, 98, 20,
                new TranslatableComponent("options_rewriter.notice.restart_now"), button -> minecraft.stop()));
        addRenderableWidget(new Button(width / 2 + 4, height / 2 + 42, 98, 20,
                new TranslatableComponent("options_rewriter.notice.restart_later"), button -> minecraft.setScreen(parent)));
    }

    @Override public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
        renderBackground(pose);
        Component message = customMessage.isEmpty() ? new TranslatableComponent("options_rewriter.notice.message") : new TextComponent(customMessage);
        GuiComponent.drawCenteredString(pose, font, title, width / 2, height / 2 - 50, 0xFFFFFF);
        GuiComponent.drawCenteredString(pose, font, message, width / 2, height / 2 - 14, 0xFFFFFF);
        GuiComponent.drawCenteredString(pose, font, new TranslatableComponent("options_rewriter.notice.restart_required"), width / 2, height / 2 + 8, 0xFFD080);
        super.render(pose, mouseX, mouseY, partialTick);
    }

    @Override public boolean shouldCloseOnEsc() { return false; }
}
