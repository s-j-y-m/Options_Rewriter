# Multi-Version Support

Shared disk logic lives in `common/`. Each loader/version project must compile its own thin client adapter because Forge and NeoForge event, screen, and configuration APIs are not binary compatible across the supported Minecraft versions.

| Target | Loader | Java | Stable loader target | Status |
| --- | --- | --- | --- | --- |
| 1.18.2 | Forge | 17 | 40.3.11 | Implemented and build verified |
| 1.19.2 | Forge | 17 | 43.5.1 | Implemented and build verified |
| 1.20.1 | Forge | 17 | 47.2.0 | Implemented at repository root |
| 1.20.4 | NeoForge | 17 | 20.4.251 | Implemented and build verified |
| 1.21.1 | NeoForge | 21 | 21.1.235 | Implemented and build verified |
| 1.21.11 | NeoForge | 21 | 21.11.42 | Implemented and build verified |
| 26.2 | NeoForge | 25 | 26.2.0.11-beta | Skipped: beta only |

`26.2` has a published NeoForge beta, but no stable release is available. It is intentionally excluded from release builds until a stable toolchain is published.
