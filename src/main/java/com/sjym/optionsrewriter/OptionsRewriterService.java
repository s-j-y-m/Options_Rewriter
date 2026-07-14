package com.sjym.optionsrewriter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

public final class OptionsRewriterService {
    private OptionsRewriterService() {
    }

    public static ApplyResult applyOnFirstTitleScreen(Minecraft minecraft, Screen currentScreen) {
        Path source = OptionsRewriterPaths.templateOptions();
        Path target = OptionsRewriterPaths.targetOptions();
        Path flag = OptionsRewriterPaths.appliedFlag();

        if (Files.exists(flag)) {
            return ApplyResult.skipped("Flag already exists.");
        }

        if (!Files.exists(source)) {
            OptionsRewriter.LOGGER.info("Options template not found: {}", source);
            return ApplyResult.skipped("Template options.txt not found.");
        }

        try {
            Files.createDirectories(source.getParent());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            minecraft.options.load();
            Files.writeString(flag, "Applied at " + Instant.now() + System.lineSeparator());

            OptionsRewriter.LOGGER.info("Replaced options.txt from {}", source);
            return ApplyResult.success();
        } catch (IOException exception) {
            OptionsRewriter.LOGGER.error("Failed to replace options.txt", exception);
            return ApplyResult.failed(exception.getMessage());
        }
    }

    public static OperationResult deleteFlag() {
        Path flag = OptionsRewriterPaths.appliedFlag();
        try {
            if (Files.deleteIfExists(flag)) {
                return OperationResult.success(Component.translatable("options_rewriter.status.flag_deleted"));
            }
            return OperationResult.success(Component.translatable("options_rewriter.status.flag_missing"));
        } catch (IOException exception) {
            OptionsRewriter.LOGGER.error("Failed to delete applied flag", exception);
            return OperationResult.failure(Component.translatable("options_rewriter.status.flag_delete_failed"));
        }
    }

    public static OperationResult copyCurrentOptionsToTemplate() {
        Path source = OptionsRewriterPaths.targetOptions();
        Path target = OptionsRewriterPaths.templateOptions();

        if (!Files.exists(source)) {
            return OperationResult.failure(Component.translatable("options_rewriter.status.options_missing"));
        }

        try {
            Files.createDirectories(target.getParent());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return OperationResult.success(Component.translatable("options_rewriter.status.options_copied"));
        } catch (IOException exception) {
            OptionsRewriter.LOGGER.error("Failed to copy current options.txt to template", exception);
            return OperationResult.failure(Component.translatable("options_rewriter.status.options_copy_failed"));
        }
    }

    public record ApplyResult(boolean applied, boolean failed, String message) {
        public static ApplyResult success() {
            return new ApplyResult(true, false, "Applied");
        }

        public static ApplyResult skipped(String message) {
            return new ApplyResult(false, false, message);
        }

        public static ApplyResult failed(String message) {
            return new ApplyResult(false, true, message);
        }
    }

    public record OperationResult(boolean success, Component message) {
        public static OperationResult success(Component message) {
            return new OperationResult(true, message);
        }

        public static OperationResult failure(Component message) {
            return new OperationResult(false, message);
        }
    }
}
