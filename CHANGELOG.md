# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-09-19

### Added
- 4 read-only armor HUD slots to the left of the vanilla hotbar displaying equipped Helmet, Chestplate, Leggings, and Boots.
- 1 offhand HUD slot to the right of the vanilla hotbar displaying offhand equipment (Shield, Totem of Undying, etc.).
- Real-time durability status bars and numeric percentage indicators for all tracked slots.
- Two-tier animated warning alert system:
  - Bouncing `❗` icon when item durability reaches the warning zone (default 30%).
  - Fast-bouncing `‼️` icon when item durability reaches critical break risk (default 10%).
- In-game configuration screen accessible via the `K` key (fully rebindable).
- Granular configuration settings:
  - Toggle visibility for each armor and offhand slot individually.
  - Configurable durability percentage thresholds for warning and critical alerts.
  - Per-slot X and Y screen coordinate offsets for custom HUD positioning.
- Optional ModMenu integration providing in-game configuration access through the Mods catalog.
- Standalone client-side architecture compatible with singleplayer and multiplayer servers.

[1.0.0]: https://github.com/modclaim/armoralert/releases/tag/v1.0.0
