# 项目包缺陷修复总结

本文档总结了项目中修复的重大包缺陷。

## 修复的严重安全漏洞

### 1. commons-beanutils 1.9.4 (已移除)
- **问题**: 存在已知的安全漏洞 (CVE-2014-0114, CVE-2019-10086, CVE-2024-26308)
- **修复**: 从根 pom.xml 中移除显式版本依赖,使用 Spring Boot BOM 管理的版本
- **影响**: 避免远程代码执行风险

### 2. velocity 1.7 (已升级到 2.3)
- **问题**: 极其老旧(2010年发布),已弃用,存在安全漏洞,与 Java 21 不兼容
- **修复**: 
  - 升级到 velocity-engine-core 2.3
  - 更新代码生成器 GenUtils.java 以使用新的 Velocity 2 API
  - 代码中从 `org.apache.velocity.app.Velocity` 改为 `org.apache.velocity.app.VelocityEngine`
- **影响**: 修复安全漏洞,确保 Java 21 兼容性

### 3. commons-configuration 1.10 (已升级到 2.11.0)
- **问题**: 极其老旧(2013年发布),已弃用,存在安全漏洞
- **修复**: 
  - 升级到 commons-configuration2 2.11.0
  - 更新代码生成器 GenUtils.java 以使用新的 Configuration 2 API
  - 代码中从 `org.apache.commons.configuration` 改为 `org.apache.commons.configuration2`
- **影响**: 修复安全漏洞,提供更好的维护

### 4. Springfox Swagger 3.0.0 (已替换为 Springdoc OpenAPI 2.3.0)
- **问题**: 
  - 项目自 2020 年起不再维护
  - 已弃用,与新版本的 Spring 不兼容
  - 存在安全风险
- **修复**:
  - 替换为 Springdoc OpenAPI 2.3.0 (springdoc-openapi-starter-webmvc-ui)
  - 更新 raisin-swagger2-spring-boot-starter 模块依赖
  - 更新 zuul-gateway 的 SwaggerConfig 以使用新的 Springdoc API
  - 移除了已弃用的 swagger-butler-core, swagger-bootstrap-ui, swagger-mg-ui
- **影响**: 使用活跃维护的 OpenAPI 3.0 实现,更好的安全性和兼容性

## 修复的维护问题

### 5. spring-social-security (已替换为自定义实现)
- **问题**: 
  - Spring Social 项目自 2019 年起不再维护
  - 已弃用,不再有安全更新
- **修复**:
  - 创建了替代接口 `com.raisin.common.social.SocialUserDetails`
  - 创建了替代接口 `com.raisin.common.social.SocialUserDetailsService`
  - 更新所有使用 spring-social 的代码以使用新接口
  - 移除 uaa 模块中的 spring-social-security 依赖
- **影响**: 移除已弃用的依赖,使用自定义实现提供更好的维护

### 6. sharding-jdbc 3.1.0 (已升级到 ShardingSphere 5.5.0)
- **问题**: 
  - ShardingSphere 3.x 已弃用
  - 不再维护,存在安全问题
- **修复**:
  - 升级到 shardingsphere-jdbc-core 5.5.0
  - 更新根 pom.xml 的依赖
  - 更新 sharding-jdbc-demo 的 pom.xml
- **影响**: 使用活跃维护的 ShardingSphere 5.x,提供更好的功能和安全

### 7. logback-classic 1.2.3 (已移除显式版本)
- **问题**: 
  - Spring Boot 3.x 需要更新版本(1.4+)
  - 显式指定旧版本可能导致兼容性问题
- **修复**: 
  - 从 uaa pom.xml 中移除显式版本
  - 使用 Spring Boot BOM 管理的版本
- **影响**: 使用正确的 Logback 版本,确保 Spring Boot 3.x 兼容性

### 8. logstash-logback-encoder 6.3 (已移除显式版本)
- **问题**: 
  - Spring Boot 3.x 提供更新的版本
  - 显式指定旧版本可能导致兼容性问题
- **修复**: 
  - 从 uaa pom.xml 中移除显式版本
  - 使用 Spring Boot BOM 管理的版本
- **影响**: 使用正确的版本,确保最佳兼容性

### 9. lettuce-core 5.1.8.RELEASE (已移除显式版本)
- **问题**: 
  - Spring Boot 2.x 时代的版本
  - Spring Boot 3.x 使用 Lettuce 6.x
- **修复**: 
  - 从根 pom.xml 的 dependencyManagement 中移除
  - 使用 Spring Boot BOM 管理的版本
- **影响**: 使用正确的 Lettuce 版本,确保 Spring Boot 3.x 兼容性

### 10. commons-lang (已更新到 commons-lang3)
- **问题**: 
  - 使用已弃用的 commons-lang (1.x)
  - 应该使用 commons-lang3
- **修复**:
  - 更新 SysMenuController.java: `org.apache.commons.lang.ObjectUtils` → `org.apache.commons.lang3.ObjectUtils`
  - 更新 ValidateCodeServiceImpl.java: `org.apache.commons.lang.StringUtils` → `org.apache.commons.lang3.StringUtils`
- **影响**: 使用维护中的库,更好的性能和功能

### 11. transmittable-thread-local (已升级到 2.14.2)
- **修复**: 
  - 从 2.11.0 升级到 2.14.2
- **影响**: 使用更新版本,获得 bug 修复和新功能

## 依赖版本变更汇总

| 依赖包 | 旧版本 | 新版本 | 变更类型 |
|--------|--------|--------|----------|
| commons-beanutils | 1.9.4 | (使用 BOM) | 移除显式版本 |
| velocity | 1.7 | 2.3 (velocity-engine-core) | 重大升级 |
| commons-configuration | 1.10 | 2.11.0 (commons-configuration2) | 重大升级 |
| Springfox Swagger | 3.0.0 | - (已移除) | 替换 |
| Springdoc OpenAPI | - | 2.3.0 | 新增 |
| spring-social-security | 1.1.6.RELEASE | - (已移除) | 替换 |
| sharding-jdbc | 3.1.0 | 5.5.0 (shardingsphere-jdbc-core) | 重大升级 |
| logback-classic | 1.2.3 | (使用 BOM) | 移除显式版本 |
| logstash-logback-encoder | 6.3 | (使用 BOM) | 移除显式版本 |
| lettuce-core | 5.1.8.RELEASE | (使用 BOM) | 移除显式版本 |
| transmittable-thread-local | 2.11.0 | 2.14.2 | 小版本升级 |
| commons-lang | 1.x | commons-lang3 | 包变更 |

## 受影响的模块

### 核心模块
- pom.xml (根)
- raisin-commons/pom.xml
- raisin-commons/raisin-swagger2-spring-boot-starter/pom.xml
- raisin-commons/raisin-common-core/ (新增 social 包)

### 业务模块
- raisin-business/code-generator/pom.xml
- raisin-business/code-generator/src/main/java/com/raisin/generator/utils/GenUtils.java
- raisin-business/user-center/src/main/java/com/raisin/user/controller/SysMenuController.java

### 认证模块
- raisin-uaa/pom.xml
- raisin-uaa/src/main/java/com/raisin/oauth/openid/OpenIdAuthenticationProvider.java
- raisin-uaa/src/main/java/com/raisin/oauth/openid/OpenIdAuthenticationSecurityConfig.java
- raisin-uaa/src/main/java/com/raisin/oauth/service/impl/UserDetailServiceImpl.java
- raisin-uaa/src/main/java/com/raisin/oauth/service/impl/ValidateCodeServiceImpl.java
- raisin-commons/raisin-common-core/src/main/java/com/raisin/common/model/LoginAppUser.java

### 网关模块
- raisin-gateway/zuul-gateway/src/main/java/com/raisin/gateway/config/SwaggerConfig.java

### 演示模块
- raisin-demo/sharding-jdbc-demo/pom.xml

## API 变更说明

### Velocity 1.x → 2.x API 变更
- `Velocity.init()` → `new VelocityEngine(properties)`
- `Velocity.getTemplate()` → `engine.getTemplate()`
- 属性配置: `file.resource.loader.class` → `resource.loader`, `class.resource.loader.class`

### Commons Configuration 1.x → 2.x API 变更
- `new PropertiesConfiguration("file")` → `new Configurations().properties("file")`
- 包名: `org.apache.commons.configuration` → `org.apache.commons.configuration2`
- 异常: `org.apache.commons.configuration.ConfigurationException` → `org.apache.commons.configuration2.ex.ConfigurationException`

### Springfox → Springdoc 注解迁移 (待完成)
当前代码仍使用 Swagger 2 注解 (`@Api`, `@ApiOperation`),Springdoc 提供有限兼容性。
建议后续迁移到 OpenAPI 3 注解 (`@Tag`, `@Operation`)。

## 建议

### 短期建议
1. 测试所有更新的模块,特别是代码生成器功能
2. 验证 Springdoc OpenAPI 在所有服务中正常工作
3. 运行完整的集成测试套件

### 中期建议
1. 将 Swagger 2 注解迁移到 OpenAPI 3 注解
2. 评估是否需要继续使用 OpenID 社交登录功能
3. 考虑将 fastjson 替换为 Jackson (虽然 2.0.43 版本较安全,但 Jackson 是 Spring Boot 默认)

### 长期建议
1. 建立依赖管理策略,定期检查和更新依赖
2. 引入 Snyk 或 OWASP Dependency-Check 进行自动化安全扫描
3. 考虑使用 Renovate 或 Dependabot 进行依赖更新自动化

## 验证清单

- [x] 移除 commons-beanutils 显式版本
- [x] 升级 velocity 到 2.3
- [x] 升级 commons-configuration 到 2.11.0
- [x] 替换 Springfox Swagger 为 Springdoc OpenAPI
- [x] 移除 spring-social-security 依赖
- [x] 创建自定义 SocialUserDetails 和 SocialUserDetailsService 接口
- [x] 升级 sharding-jdbc 到 ShardingSphere 5.5.0
- [x] 移除 logback-classic 显式版本
- [x] 移除 logstash-logback-encoder 显式版本
- [x] 移除 lettuce-core 显式版本
- [x] 更新 commons-lang 到 commons-lang3
- [x] 更新 GenUtils.java 使用新 API
- [x] 更新所有相关 Java 文件的导入语句
