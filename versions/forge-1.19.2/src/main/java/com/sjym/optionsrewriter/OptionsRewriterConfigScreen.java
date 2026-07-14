package com.sjym.optionsrewriter;

import com.sjym.optionsrewriter.common.OptionsSettings;
import com.sjym.optionsrewriter.common.OptionsStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.IOException;

public final class OptionsRewriterConfigScreen extends Screen {
    private final Screen parent;
    private OptionsSettings settings;
    private EditBox message;
    private Button mode;
    private Component status = Component.empty();

    public OptionsRewriterConfigScreen(Screen parent) {
        super(Component.translatable("options_rewriter.config.title"));
        this.parent = parent;
    }

    @Override protected void init() {
        settings = OptionsSettings.load(minecraft.gameDirectory.toPath());
        mode = addRenderableWidget(new Button(width / 2 - 150, height / 2 - 80, 300, 20, modeLabel(), button -> {
            settings.toggleApplyMode(); button.setMessage(modeLabel());
        }));
        message = addRenderableWidget(new EditBox(font, width / 2 - 150, height / 2 - 40, 300, 20, Component.translatable("options_rewriter.config.restart_message")));
        message.setValue(settings.restartMessage());
        addRenderableWidget(new Button(width / 2 - 150, height / 2, 145, 20, Component.translatable("options_rewriter.config.copy_options"), button -> copyOptions()));
        addRenderableWidget(new Button(width / 2 + 5, height / 2, 145, 20, Component.translatable("options_rewriter.config.reset_flag"), button -> deleteFlag()));
        addRenderableWidget(new Button(width / 2 - 102, height / 2 + 45, 98, 20, Component.translatable("options_rewriter.config.save"), button -> save()));
        addRenderableWidget(new Button(width / 2 + 4, height / 2 + 45, 98, 20, Component.translatable("options_rewriter.config.cancel"), button -> minecraft.setScreen(parent)));
    }

    private Component modeLabel() {
        String key = settings.applyMode() == OptionsSettings.ApplyMode.RESTART_NOTICE ? "options_rewriter.config.apply_mode.restart_notice" : "options_rewriter.config.apply_mode.silent";
        return Component.translatable("options_rewriter.config.apply_mode.value", Component.translatable(key));
    }

    private void copyOptions() { try { OptionsStorage.saveCurrentOptionsAsTemplate(minecraft.gameDirectory.toPath()); status = Component.translatable("options_rewriter.status.options_copied"); } catch (IOException e) { status = Component.translatable("options_rewriter.status.options_copy_failed"); } }
    private void deleteFlag() { try { OptionsStorage.deleteFlag(minecraft.gameDirectory.toPath()); status = Component.translatable("options_rewriter.status.flag_deleted"); } catch (IOException e) { status = Component.translatable("options_rewriter.status.flag_delete_failed"); } }
    private void save() { try { settings.restartMessage(message.getValue()); settings.save(minecraft.gameDirectory.toPath()); minecraft.setScreen(parent); } catch (IOException e) { status = Component.translatable("options_rewriter.status.options_copy_failed"); } }

    @Override public void render(com.mojang.blaze3d.vertex.PoseStack pose, int mouseX, int mouseY, float partialTick) {
        renderBackground(pose); GuiComponent.drawCenteredString(pose, font, title, width / 2, height / 2 - 112, 0xFFFFFF); GuiComponent.drawCenteredString(pose, font, status, width / 2, height / 2 + 72, 0xFFFFA0); super.render(pose, mouseX, mouseY, partialTick);
    }
}
