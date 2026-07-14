package com.sjym.optionsrewriter;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OptionsRewriterConfigScreen extends Screen {
    private final Screen parent;
    private Button applyMode;
    private EditBox restartMessage;
    private Component status = Component.empty();

    public OptionsRewriterConfigScreen(Screen parent) {
        super(Component.translatable("options_rewriter.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = width / 2;
        int top = height / 2 - 94;

        applyMode = Button.builder(applyModeLabel(), button -> {
            OptionsRewriterConfig.toggleApplyMode();
            button.setMessage(applyModeLabel());
        }).bounds(center - 150, top, 300, 20)
                .tooltip(Tooltip.create(Component.translatable("options_rewriter.config.apply_mode.tooltip")))
                .build();
        addRenderableWidget(applyMode);

        restartMessage = new EditBox(font, center - 150, top + 42, 300, 20, Component.translatable("options_rewriter.config.restart_message"));
        restartMessage.setMaxLength(200);
        restartMessage.setValue(OptionsRewriterConfig.restartMessageForEditor());
        restartMessage.setTooltip(Tooltip.create(Component.translatable("options_rewriter.config.restart_message.tooltip")));
        addRenderableWidget(restartMessage);

        addRenderableWidget(Button.builder(Component.translatable("options_rewriter.config.copy_options"), button -> {
            status = OptionsRewriterService.copyCurrentOptionsToTemplate().message();
        }).bounds(center - 150, top + 82, 145, 20)
                .tooltip(Tooltip.create(Component.translatable("options_rewriter.config.copy_options.tooltip")))
                .build());

        addRenderableWidget(Button.builder(Component.translatable("options_rewriter.config.reset_flag"), button -> {
            status = OptionsRewriterService.deleteFlag().message();
        }).bounds(center + 5, top + 82, 145, 20)
                .tooltip(Tooltip.create(Component.translatable("options_rewriter.config.reset_flag.tooltip")))
                .build());

        addRenderableWidget(Button.builder(Component.translatable("options_rewriter.config.save"), button -> saveAndClose())
                .bounds(center - 102, top + 130, 98, 20)
                .tooltip(Tooltip.create(Component.translatable("options_rewriter.config.save.tooltip")))
                .build());

        addRenderableWidget(Button.builder(Component.translatable("options_rewriter.config.cancel"), button -> minecraft.setScreen(parent))
                .bounds(center + 4, top + 130, 98, 20)
                .tooltip(Tooltip.create(Component.translatable("options_rewriter.config.cancel.tooltip")))
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int top = height / 2 - 94;
        graphics.drawCenteredString(font, title, width / 2, top - 24, 0xFFFFFF);
        graphics.drawString(font, Component.translatable("options_rewriter.config.apply_mode"), width / 2 - 150, top - 12, 0xA0A0A0);
        graphics.drawString(font, Component.translatable("options_rewriter.config.restart_message"), width / 2 - 150, top + 26, 0xA0A0A0);
        graphics.drawString(font, Component.translatable("options_rewriter.config.template_path", "config/options_rewriter/options/options.txt"), width / 2 - 150, top + 108, 0xA0A0A0);
        graphics.drawCenteredString(font, status, width / 2, top + 160, 0xFFFFA0);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    private void saveAndClose() {
        OptionsRewriterConfig.setRestartMessage(restartMessage.getValue());
        OptionsRewriterConfig.save();
        minecraft.setScreen(parent);
    }

    private Component applyModeLabel() {
        return Component.translatable("options_rewriter.config.apply_mode.value", OptionsRewriterConfig.applyMode().displayName());
    }
}
