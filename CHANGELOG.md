<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Enum-Ordinal Persistence Companion Changelog

## [Unreleased]

## [0.1.0]

### Added

- Warning on a JPA `@Enumerated` field with no explicit
  `EnumType.STRING` on a real `@Entity` class -- JPA's default
  `ORDINAL` silently corrupts existing records if the enum is ever
  reordered.
- Confirms real persistence (the containing class is a genuine
  `@Entity`) -- never flags the annotation on a plain DTO/POJO.

[Unreleased]: https://github.com/GapHunterLabs/enum-ordinal-persistence-companion/compare/0.1.0...HEAD
[0.1.0]: https://github.com/GapHunterLabs/enum-ordinal-persistence-companion/commits/0.1.0
