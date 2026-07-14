package com.sjym.optionsrewriter;

import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class OptionsRewriterConfig {
    public static final String DEFAULT_RESTART_MESSAGE_KEY = "options_rewriter.notice.message";
    private static final String LEGACY_DEFAULT_RESTART_MESSAGE = "已自动替换键位，请重启游戏后游玩";

    private static ApplyMode applyMode = ApplyMode.RESTART_NOTICE;
    private static String restartMessage = "";

    private OptionsRewriterConfig() {
    }

    public static ApplyMode applyMode() {
        return applyMode;
    }

    public static void toggleApplyMode() {
        applyMode = applyMode == ApplyMode.RESTART_NOTICE ? ApplyMode.SILENT : ApplyMode.RESTART_NOTICE;
    }

    public static Component restartMessage() {
        return restartMessage.isBlank()
                ? Component.translatable(DEFAULT_RESTART_MESSAGE_KEY)
                : Component.literal(restartMessage);
    }

    public static String restartMessageForEditor() {
        return restartMessage;
    }

    public static void setRestartMessage(String value) {
        restartMessage = value == null || value.isBlank() || LEGACY_DEFAULT_RESTART_MESSAGE.equals(value) ? "" : value;
    }

    public static void load() {
        Path settings = OptionsRewriterPaths.settingsFile();
        if (!Files.exists(settings)) {
            save();
            return;
        }

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(settings, StandardCharsets.UTF_8)) {
            properties.load(reader);
            String savedMode = properties.getProperty("applyMode");
            if (savedMode == null) {
                applyMode = Boolean.parseBoolean(properties.getProperty("showRestartMessage", "true"))
                        ? ApplyMode.RESTART_NOTICE
                        : ApplyMode.SILENT;
            } else {
                applyMode = ApplyMode.fromSerialized(savedMode);
            }
            setRestartMessage(properties.getProperty("restartMessage", ""));
        } catch (IOException exception) {
            OptionsRewriter.LOGGER.error("Failed to load options rewriter settings", exception);
        }
    }

    public static void save() {
        Path settings = OptionsRewriterPaths.settingsFile();
        Properties properties = new Properties();
        properties.setProperty("applyMode", applyMode.serializedName());
        properties.setProperty("restartMessage", restartMessage);

        try {
            Files.createDirectories(settings.getParent());
            try (Writer writer = Files.newBufferedWriter(settings, StandardCharsets.UTF_8)) {
                properties.store(writer, "Options Rewriter settings");
            }
        } catch (IOException exception) {
            OptionsRewriter.LOGGER.error("Failed to save options rewriter settings", exception);
        }
    }

    public enum ApplyMode {
        RESTART_NOTICE("restart_notice", "options_rewriter.config.apply_mode.restart_notice"),
        SILENT("silent", "options_rewriter.config.apply_mode.silent");

        private final String serializedName;
        private final String displayNameKey;

        ApplyMode(String serializedName, String displayNameKey) {
            this.serializedName = serializedName;
            this.displayNameKey = displayNameKey;
        }

        public String serializedName() {
            return serializedName;
        }

        public Component displayName() {
            return Component.translatable(displayNameKey);
        }

        private static ApplyMode fromSerialized(String value) {
            for (ApplyMode mode : values()) {
                if (mode.serializedName.equals(value)) {
                    return mode;
                }
            }
            return RESTART_NOTICE;
        }
    }
}
