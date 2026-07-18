package com.sjym.optionsrewriter;

import com.sjym.optionsrewriter.common.OptionsSettings;
import com.sjym.optionsrewriter.common.OptionsStorage;
import java.io.IOException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class OptionsRewriterConfigScreen extends Screen {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int STATUS_COLOR = 0xFFFFFFA0;

    private final Screen parent;
    private OptionsSettings settings;
    private EditBox message;
    private Component status = Component.empty();

    public OptionsRewriterConfigScreen(Screen parent) {
        super(Component.translatable("options_rewriter.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        settings = OptionsSettings.load(minecraft.gameDirectory.toPath());
        addRenderableWidget(Button.builder(modeLabel(), button -> {
                    settings.toggleApplyMode();
                    button.setMessage(modeLabel());
                })
                .bounds(width / 2 - 150, height / 2 - 80, 300, 20)
                .build());

        message = addRenderableWidget(new EditBox(
                font,
                width / 2 - 150,
                height / 2 - 40,
                300,
                20,
                Component.translatable("options_rewriter.config.restart_message")));
        message.setValue(settings.restartMessage());

        addRenderableWidget(Button.builder(
                        Component.translatable("options_rewriter.config.copy_options"),
                        button -> copy())
                .bounds(width / 2 - 150, height / 2, 145, 20)
                .build());
        addRenderableWidget(Button.builder(
                        Component.translatable("options_rewriter.config.reset_flag"),
                        button -> reset())
                .bounds(width / 2 + 5, height / 2, 145, 20)
                .build());
        addRenderableWidget(Button.builder(
                        Component.translatable("options_rewriter.config.save"),
                        button -> save())
                .bounds(width / 2 - 102, height / 2 + 45, 98, 20)
                .build());
        addRenderableWidget(Button.builder(
                        Component.translatable("options_rewriter.config.cancel"),
                        button -> minecraft.setScreen(parent))
                .bounds(width / 2 + 4, height / 2 + 45, 98, 20)
                .build());
    }

    private Component modeLabel() {
        String key = settings.applyMode() == OptionsSettings.ApplyMode.RESTART_NOTICE
                ? "options_rewriter.config.apply_mode.restart_notice"
                : "options_rewriter.config.apply_mode.silent";
        return Component.translatable(
                "options_rewriter.config.apply_mode.value",
                Component.translatable(key));
    }

    private void copy() {
        try {
            OptionsStorage.saveCurrentOptionsAsTemplate(minecraft.gameDirectory.toPath());
            status = Component.translatable("options_rewriter.status.options_copied");
        } catch (IOException exception) {
            status = Component.translatable("options_rewriter.status.options_copy_failed");
        }
    }

    private void reset() {
        try {
            OptionsStorage.deleteFlag(minecraft.gameDirectory.toPath());
            status = Component.translatable("options_rewriter.status.flag_deleted");
        } catch (IOException exception) {
            status = Component.translatable("options_rewriter.status.flag_delete_failed");
        }
    }

    private void save() {
        try {
            settings.restartMessage(message.getValue());
            settings.save(minecraft.gameDirectory.toPath());
            minecraft.setScreen(parent);
        } catch (IOException exception) {
            status = Component.translatable("options_rewriter.status.options_copy_failed");
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 112, WHITE);
        graphics.drawCenteredString(font, status, width / 2, height / 2 + 72, STATUS_COLOR);
    }
}
