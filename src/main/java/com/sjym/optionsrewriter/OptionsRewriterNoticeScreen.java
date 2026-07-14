package com.sjym.optionsrewriter;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OptionsRewriterNoticeScreen extends Screen {
    private final Screen parent;
    private final Component message;

    public OptionsRewriterNoticeScreen(Screen parent, Component message) {
        super(Component.translatable("options_rewriter.notice.title"));
        this.parent = parent;
        this.message = message;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.translatable("options_rewriter.notice.restart_now"), button -> minecraft.stop())
                .bounds(width / 2 - 102, height / 2 + 42, 98, 20)
                .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("options_rewriter.notice.restart_now.tooltip")))
                .build());

        addRenderableWidget(Button.builder(Component.translatable("options_rewriter.notice.restart_later"), button -> minecraft.setScreen(parent))
                .bounds(width / 2 + 4, height / 2 + 42, 98, 20)
                .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("options_rewriter.notice.restart_later.tooltip")))
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int left = width / 2 - 160;
        int top = height / 2 - 68;
        graphics.fill(left, top, left + 320, top + 146, 0xE0181818);
        graphics.fill(left, top, left + 320, top + 2, 0xFFE0A020);
        graphics.drawCenteredString(font, title, width / 2, top + 18, 0xFFFFFF);
        graphics.drawCenteredString(font, message, width / 2, top + 50, 0xFFF0F0F0);
        graphics.drawCenteredString(font, Component.translatable("options_rewriter.notice.restart_required"), width / 2, top + 72, 0xFFFFD080);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}
