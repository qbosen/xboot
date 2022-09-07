# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

```text
Added 新添加的功能。
Changed 对现有功能的变更。
Deprecated 已经不建议使用，准备很快移除的功能。
Removed 已经移除的功能。
Fixed 对bug的修复
Security 对安全的改进
```

## [Unreleased]

## [0.1.2]

### Added

- [properties-adaptor](xboot-configuration-properties-adaptor)
    - 配置bean属性修改工具;
    - 提供null string映射工具

- [consul-config](xboot-consul-config-util)
    - 增加acl token;
    - 并行获取kv/service;
    - 支持 随机选取/组合 服务;
    - 增加测试;

### Changed

- [common](xboot-spring-common)
    - **不兼容升级**mybatis plus到3.5.2
