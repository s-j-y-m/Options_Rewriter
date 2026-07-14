# Options Rewriter

Options Rewriter is a small client-side Minecraft mod for modpacks. It replaces the player's `options.txt` with a pack-provided template on first client startup, making it useful for distributing default keybindings and client options with a modpack.

中文名：`options重覆写`

## Features

- Replaces the instance root `options.txt` from a template file on first title-screen load.
- Writes an `applied.flag` after the replacement so it does not overwrite the player's settings every launch.
- Provides a mod config screen for saving the current `options.txt` as the template.
- Provides a config action to delete `applied.flag` and allow the next launch to rewrite again.
- Supports either a restart notice after replacement or silent replacement.
- Includes English and Simplified Chinese language files.

## How It Works

Place the template file here in the Minecraft instance:

```text
config/options_rewriter/options/options.txt
```

On the first client title-screen load, the mod checks:

```text
config/options_rewriter/options/applied.flag
```

If the flag does not exist and the template exists, the mod copies the template to:

```text
options.txt
```

After the copy succeeds, the mod writes `applied.flag`. Future launches skip the rewrite unless the flag is deleted.

## Player Usage

1. Install the correct Options Rewriter jar for your Minecraft loader/version.
2. Start the game once, or use the mod config screen to save the current settings as a template.
3. Put the desired template at `config/options_rewriter/options/options.txt` in your modpack or instance.
4. Delete `config/options_rewriter/options/applied.flag` if you want the template to apply again.

## Config Screen

The mod config screen provides:

- `Save current options.txt as template`: copies the current instance `options.txt` to `config/options_rewriter/options/options.txt`.
- `Allow replacement next time`: deletes `applied.flag`.
- `After replacement`: switches between restart notice and silent replacement.
- `Confirmation message`: custom message shown after replacement when restart notice mode is enabled.

## Supported Versions

| Minecraft | Loader | Java | Status |
| --- | --- | --- | --- |
| 1.18.2 | Forge 40.3.11 | 17 | Implemented |
| 1.19.2 | Forge 43.5.1 | 17 | Implemented |
| 1.20.1 | Forge 47.2.0 | 17 | Root project |
| 1.20.4 | NeoForge 20.4.251 | 17 | Implemented |
| 1.21.1 | NeoForge 21.1.235 | 21 | Implemented |
| 1.21.11 | NeoForge 21.11.42 | 21 | Implemented |

NeoForge `26.2` is intentionally skipped until a stable toolchain is available.

## Repository Layout

```text
common/                         Shared disk/settings logic
src/                            Forge 1.20.1 root implementation
versions/forge-1.18.2/          Forge 1.18.2 implementation
versions/forge-1.19.2/          Forge 1.19.2 implementation
versions/neoforge-1.20.4/       NeoForge 1.20.4 implementation
versions/neoforge-1.21.1/       NeoForge 1.21.1 implementation
versions/neoforge-1.21.11/      NeoForge 1.21.11 implementation
```

Each version directory is an independent Gradle project. The root project targets Forge 1.20.1.

## Development

### Forge 1.20.1

Run from the repository root:

```powershell
.\gradlew.bat runClient
```

Build:

```powershell
.\gradlew.bat build
```

### Other Versions

Run Gradle from the target version directory.

Forge 1.18.2:

```powershell
cd versions\forge-1.18.2
.\gradlew.bat runClient
```

Forge 1.19.2:

```powershell
cd versions\forge-1.19.2
.\gradlew.bat runClient
```

NeoForge 1.20.4:

```powershell
cd versions\neoforge-1.20.4
.\gradlew.bat runClient
```

NeoForge 1.21.1:

```powershell
cd versions\neoforge-1.21.1
.\gradlew.bat runClient
```

NeoForge 1.21.11:

```powershell
cd versions\neoforge-1.21.11
.\gradlew.bat runClient
```

## Build Notes

- Java 17 is used for Minecraft 1.18.2 through 1.20.4.
- Java 21 is used for Minecraft 1.21.1 and 1.21.11.
- Version-specific `gradle.properties` files define the expected local Java path.
- Generated build output and run directories are not required for source distribution.

## License

Options Rewriter is licensed under the MIT License.

This repository also contains Minecraft Forge/NeoForge MDK-generated files and related license notices. See `LICENSE.txt` and version-specific template license files where applicable.
