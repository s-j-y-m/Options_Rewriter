package com.sjym.optionsrewriter.common;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class OptionsSettings {
    private ApplyMode applyMode = ApplyMode.RESTART_NOTICE;
    private String restartMessage = "";

    public static OptionsSettings load(Path gameDirectory) {
        OptionsSettings result = new OptionsSettings();
        Path file = OptionsStorage.settings(gameDirectory);
        if (!Files.exists(file)) {
            return result;
        }
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            properties.load(reader);
            String mode = properties.getProperty("applyMode");
            if (mode == null) {
                result.applyMode = Boolean.parseBoolean(properties.getProperty("showRestartMessage", "true"))
                        ? ApplyMode.RESTART_NOTICE : ApplyMode.SILENT;
            } else {
                result.applyMode = ApplyMode.fromSerialized(mode);
            }
            result.restartMessage = properties.getProperty("restartMessage", "");
        } catch (IOException ignored) {
            // Defaults remain usable when a damaged settings file cannot be read.
        }
        return result;
    }

    public void save(Path gameDirectory) throws IOException {
        Path file = OptionsStorage.settings(gameDirectory);
        Files.createDirectories(file.getParent());
        Properties properties = new Properties();
        properties.setProperty("applyMode", applyMode.serializedName);
        properties.setProperty("restartMessage", restartMessage);
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            properties.store(writer, "Options Rewriter settings");
        }
    }

    public ApplyMode applyMode() {
        return applyMode;
    }

    public void toggleApplyMode() {
        applyMode = applyMode == ApplyMode.RESTART_NOTICE ? ApplyMode.SILENT : ApplyMode.RESTART_NOTICE;
    }

    public String restartMessage() {
        return restartMessage;
    }

    public void restartMessage(String value) {
        restartMessage = value == null ? "" : value.trim();
    }

    public enum ApplyMode {
        RESTART_NOTICE("restart_notice"), SILENT("silent");

        private final String serializedName;

        ApplyMode(String serializedName) {
            this.serializedName = serializedName;
        }

        private static ApplyMode fromSerialized(String value) {
            return SILENT.serializedName.equals(value) ? SILENT : RESTART_NOTICE;
        }
    }
}
