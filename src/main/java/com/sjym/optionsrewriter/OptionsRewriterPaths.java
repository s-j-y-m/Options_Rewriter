package com.sjym.optionsrewriter;

import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public final class OptionsRewriterPaths {
    private OptionsRewriterPaths() {
    }

    public static Path gameDirectory() {
        return Minecraft.getInstance().gameDirectory.toPath();
    }

    public static Path modDirectory() {
        return gameDirectory().resolve("config").resolve("options_rewriter").resolve("options");
    }

    public static Path templateOptions() {
        return modDirectory().resolve("options.txt");
    }

    public static Path targetOptions() {
        return gameDirectory().resolve("options.txt");
    }

    public static Path appliedFlag() {
        return modDirectory().resolve("applied.flag");
    }

    public static Path settingsFile() {
        return modDirectory().resolve("settings.properties");
    }
}
