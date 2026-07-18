package com.sjym.optionsrewriter;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class OptionsRewriterNoticeScreen extends Screen {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int WARNING = 0xFFFFD080;

    private final Screen parent;
    private final String custom;

    public OptionsRewriterNoticeScreen(Screen parent, String custom) {
        super(Component.translatable("options_rewriter.notice.title"));
        this.parent = parent;
        this.custom = custom;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(
                        Component.translatable("options_rewriter.notice.restart_now"),
                        button -> minecraft.stop())
                .bounds(width / 2 - 102, height / 2 + 42, 98, 20)
                .build());
        addRenderableWidget(Button.builder(
                        Component.translatable("options_rewriter.notice.restart_later"),
                        button -> minecraft.setScreen(parent))
                .bounds(width / 2 + 4, height / 2 + 42, 98, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 50, WHITE);
        graphics.drawCenteredString(
                font,
                custom.isBlank()
                        ? Component.translatable("options_rewriter.notice.message")
                        : Component.literal(custom),
                width / 2,
                height / 2 - 14,
                WHITE);
        graphics.drawCenteredString(
                font,
                Component.translatable("options_rewriter.notice.restart_required"),
                width / 2,
                height / 2 + 8,
                WARNING);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
