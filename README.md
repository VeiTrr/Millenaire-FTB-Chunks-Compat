# Millénaire — FTB Chunks Compat

![Mod Banner](art/banner.jpg)

[![GitHub](https://img.shields.io/badge/GitHub-VeiTrr%2FMillenaire--FTB--Chunks--Compat-blue?logo=github)](https://github.com/VeiTrr/Millenaire-FTB-Chunks-Compat)
[![License](https://img.shields.io/badge/License-LGPL--3.0-green.svg)](LICENSE)

Seamlessly renders **Millénaire** village territories and chunk boundaries directly on the **FTB Chunks** minimap & large map for Minecraft 1.21.1 (NeoForge).

---

## ✨ Features

- **Native Data Injection**: Injects village chunk ownership directly into FTB Chunks' map rendering pipeline.
- **Full Culture Color Coding**: All 7 Millénaire cultures (Norman, Japanese, Seljuk, Mayan, Indian, Byzantine, Inuits) render with their distinctive translucent color fill.
- **Interactive Map Support**: Hovering over village chunks on the FTB Chunks map displays village name and team claim info.
- **Configurable Culture Colors**: Customize HEX colors for any culture in the mod configuration.
- **Optional Server Protection**: Server-side FTB team protection for villages is optional and disabled by default (`enableFtbProtection = false`).

---

## 📋 Requirements

- **Minecraft**: `1.21.1`
- **NeoForge**: `21.1.248+`
- **Millénaire**: `9.0.0+`
- **FTB Chunks**: `2101.1.21+`

---

## ⚙️ Configuration

Config file location: `config/millenaire_ftb_chunks_compat-common.toml`

```toml
[general]
    # Whether to create server-side FTB Chunks team claims for village protection (Default: false)
    enableFtbProtection = false
    # Whether to render Millenaire village chunk boundaries on FTB Chunks map (Default: true)
    showVillageBoundaries = true
    # Whether to render Millenaire village labels on FTB Chunks map (Default: true)
    showVillageIcons = true

    # Culture Colors (Hex format)
    normanColor = "#3B82F6"
    japaneseColor = "#EF4444"
    seljukColor = "#10B981"
    mayanColor = "#EAB308"
    indianColor = "#F97316"
    byzantinesColor = "#A855F7"
    inuitsColor = "#06B6D4"
    defaultCultureColor = "#06B6D4"
```

---

## 📦 Building

To build the mod from source:

```bash
./gradlew build
```

The output JAR file will be located in `build/libs/millenaire_ftb_chunks_compat-1.21.1-0.1.jar`.

---

## 📄 License

This project is licensed under the [GNU Lesser General Public License v3.0 (LGPL-3.0)](LICENSE).

---

## 🔗 Repository & Issues

- **GitHub Repository**: [https://github.com/VeiTrr/Millenaire-FTB-Chunks-Compat](https://github.com/VeiTrr/Millenaire-FTB-Chunks-Compat)
- **Issue Tracker**: [https://github.com/VeiTrr/Millenaire-FTB-Chunks-Compat/issues](https://github.com/VeiTrr/Millenaire-FTB-Chunks-Compat/issues)
