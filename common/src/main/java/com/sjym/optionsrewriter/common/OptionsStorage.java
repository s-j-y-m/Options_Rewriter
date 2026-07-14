package com.sjym.optionsrewriter.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

/** Shared disk operations. Loader-specific modules provide the game directory and UI. */
public final class OptionsStorage {
    public static final Path RELATIVE_DIRECTORY = Path.of("config", "options_rewriter", "options");

    private OptionsStorage() {
    }

    public static Path template(Path gameDirectory) {
        return gameDirectory.resolve(RELATIVE_DIRECTORY).resolve("options.txt");
    }

    public static Path flag(Path gameDirectory) {
        return gameDirectory.resolve(RELATIVE_DIRECTORY).resolve("applied.flag");
    }

    public static Path settings(Path gameDirectory) {
        return gameDirectory.resolve(RELATIVE_DIRECTORY).resolve("settings.properties");
    }

    public static Path options(Path gameDirectory) {
        return gameDirectory.resolve("options.txt");
    }

    public static boolean applyOnce(Path gameDirectory) throws IOException {
        Path flag = flag(gameDirectory);
        Path template = template(gameDirectory);
        if (Files.exists(flag) || !Files.exists(template)) {
            return false;
        }

        Files.copy(template, options(gameDirectory), StandardCopyOption.REPLACE_EXISTING);
        Files.writeString(flag, "Applied at " + Instant.now() + System.lineSeparator());
        return true;
    }

    public static boolean deleteFlag(Path gameDirectory) throws IOException {
        return Files.deleteIfExists(flag(gameDirectory));
    }

    public static void saveCurrentOptionsAsTemplate(Path gameDirectory) throws IOException {
        Path source = options(gameDirectory);
        if (!Files.exists(source)) {
            throw new IOException("options.txt does not exist");
        }
        Path target = template(gameDirectory);
        Files.createDirectories(target.getParent());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }
}
