# Options Rewriter

[English](README.md) | 简体中文

Options Rewriter 是一个面向 Minecraft 整合包的小型客户端 Mod。它会在客户端首次进入标题界面时，用整合包提供的模板文件覆盖玩家实例根目录下的 `options.txt`，适合用于随整合包分发默认键位和客户端设置。

中文名：`options重覆写`

## 功能

- 在首次进入标题界面时，从模板文件替换实例根目录的 `options.txt`。
- 替换完成后写入 `applied.flag`，避免每次启动都覆盖玩家设置。
- 提供 Mod 配置界面，可将当前 `options.txt` 保存为模板。
- 提供删除 `applied.flag` 的配置操作，允许下次启动重新覆写。
- 支持替换后提示重启，或静默替换。
- 内置英文和简体中文语言文件。

## 工作方式

将模板文件放在 Minecraft 实例中的以下位置：

```text
config/options_rewriter/options/options.txt
```

客户端首次进入标题界面时，Mod 会检查：

```text
config/options_rewriter/options/applied.flag
```

如果 flag 不存在且模板存在，Mod 会把模板复制到：

```text
options.txt
```

复制成功后，Mod 会写入 `applied.flag`。之后再次启动会跳过覆写，除非手动删除该 flag 文件。

## 玩家使用方式

1. 安装与你的 Minecraft 加载器和版本对应的 Options Rewriter jar。
2. 启动游戏一次，或通过 Mod 配置界面将当前设置保存为模板。
3. 在整合包或实例中放置模板文件：`config/options_rewriter/options/options.txt`。
4. 如果需要再次应用模板，删除 `config/options_rewriter/options/applied.flag`。

## 配置界面

Mod 配置界面提供以下操作：

- `保存当前 options.txt 为模板`：将当前实例的 `options.txt` 复制到 `config/options_rewriter/options/options.txt`。
- `允许下次重新覆写`：删除 `applied.flag`。
- `替换完成后的行为`：在提示重启和静默替换之间切换。
- `确认框提示文本`：在启用提示重启模式时，自定义替换完成后的提示文本。

## 支持版本

| Minecraft | Loader | Java | 状态 |
| --- | --- | --- | --- |
| 1.18.2 | Forge 40.3.11 | 17 | 已实现 |
| 1.19.2 | Forge 43.5.1 | 17 | 已实现 |
| 1.20.1 | Forge 47.2.0 | 17 | 根项目 |
| 1.20.4 | NeoForge 20.4.251 | 17 | 已实现 |
| 1.21.1 | NeoForge 21.1.235 | 21 | 已实现 |
| 1.21.11 | NeoForge 21.11.42 | 21 | 已实现 |

NeoForge `26.2` 因暂时只有 beta 工具链，当前故意跳过。

## 仓库结构

```text
common/                         共享磁盘和设置逻辑
src/                            Forge 1.20.1 根项目实现
versions/forge-1.18.2/          Forge 1.18.2 实现
versions/forge-1.19.2/          Forge 1.19.2 实现
versions/neoforge-1.20.4/       NeoForge 1.20.4 实现
versions/neoforge-1.21.1/       NeoForge 1.21.1 实现
versions/neoforge-1.21.11/      NeoForge 1.21.11 实现
```

每个版本目录都是独立的 Gradle 项目。根项目目标为 Forge 1.20.1。

## 开发

### Forge 1.20.1

在仓库根目录运行：

```powershell
.\gradlew.bat runClient
```

构建：

```powershell
.\gradlew.bat build
```

### 其他版本

需要进入目标版本目录后运行 Gradle。

Forge 1.18.2：

```powershell
cd versions\forge-1.18.2
.\gradlew.bat runClient
```

Forge 1.19.2：

```powershell
cd versions\forge-1.19.2
.\gradlew.bat runClient
```

NeoForge 1.20.4：

```powershell
cd versions\neoforge-1.20.4
.\gradlew.bat runClient
```

NeoForge 1.21.1：

```powershell
cd versions\neoforge-1.21.1
.\gradlew.bat runClient
```

NeoForge 1.21.11：

```powershell
cd versions\neoforge-1.21.11
.\gradlew.bat runClient
```

## 构建说明

- Minecraft 1.18.2 到 1.20.4 使用 Java 17。
- Minecraft 1.21.1 和 1.21.11 使用 Java 21。
- 各版本的 `gradle.properties` 中定义了预期的本地 Java 路径。
- 源码分发不需要包含生成的构建输出和运行目录。

## 协议

Options Rewriter 使用 MIT License。

本仓库也包含 Minecraft Forge/NeoForge MDK 生成的文件及相关协议声明。相关内容见 `LICENSE.txt` 和各版本目录中的模板协议文件。
