# Raisin Platform 文档中心

欢迎使用 Raisin Platform 微服务企业级开发平台！本文档中心提供完整的使用和开发指导。

## 📚 文档导航

### 快速上手

- **[快速启动指南](QUICK_START.md)** ⭐ 必读
  - 环境准备和软件安装
  - 基础设施启动（MySQL、Redis、Nacos）
  - 数据库初始化
  - 配置说明
  - 服务启动步骤
  - 验证测试方法
  - 常见问题解决

- **[快速开发指南](QUICK_DEVELOPMENT.md)** ⭐ 必读
  - 开发环境配置（IDE、Maven、Git）
  - 项目结构详解
  - 核心组件使用（MyBatis-Plus、Redis、Feign）
  - 开发新服务完整流程
  - 开发新接口最佳实践
  - 使用自定义 Starter
  - 开发规范和代码风格
  - 调试技巧和单元测试

### 深度学习

#### 架构设计

- **系统架构**
  - 微服务架构设计
  - 服务分层设计
  - 技术选型说明
  - 模块划分原则

- **数据架构**
  - 数据库设计规范
  - 分库分表方案
  - 缓存设计策略
  - 数据一致性保证

#### 核心功能

- **认证授权**
  - OAuth2 + JWT 认证流程
  - 权限管理（RBAC）
  - 单点登录（SSO）
  - 社交登录集成

- **API 网关**
  - 路由配置
  - 限流熔断
  - 灰度发布
  - 统一鉴权

- **服务治理**
  - 服务注册与发现（Nacos）
  - 配置中心（Nacos Config）
  - 流量控制（Sentinel）
  - 分布式事务（TX-LCN）

- **监控运维**
  - 应用监控（Spring Boot Admin）
  - 日志中心（ELK）
  - 链路追踪（Sleuth + Zipkin）
  - 指标监控（Prometheus + Grafana）

- **任务调度**
  - XXL-Job 任务管理
  - 分布式任务调度
  - 任务监控和报警

#### 自定义 Starter

- **raisin-common-spring-boot-starter**
  - 通用工具类
  - 统一返回格式
  - 异常处理

- **raisin-db-spring-boot-starter**
  - MyBatis-Plus 配置
  - 分页插件
  - Druid 数据源

- **raisin-redis-spring-boot-starter**
  - RedisTemplate 配置
  - Redisson 客户端
  - 分布式锁

- **raisin-log-spring-boot-starter**
  - 日志格式化
  - 日志切面
  - ELK 集成

- **raisin-swagger2-spring-boot-starter**
  - Swagger 自动配置
  - API 文档生成
  - 多种 UI 支持

- **raisin-auth-client-spring-boot-starter**
  - OAuth2 资源服务器
  - JWT Token 解析
  - 权限拦截

- **raisin-sentinel-spring-boot-starter**
  - 流量控制
  - 熔断降级
  - 系统保护

### 部署运维

- **本地开发环境**
  - Docker Compose 快速启动
  - IDE 运行配置
  - 本地调试技巧

- **测试环境部署**
  - 容器化部署
  - 服务编排
  - 环境隔离

- **生产环境部署**
  - 高可用部署方案
  - 性能优化建议
  - 安全加固措施
  - 灾备方案

- **容器化部署**
  - Docker 镜像构建
  - Docker Compose 编排
  - Kubernetes 部署
  - Helm Charts

### 最佳实践

- **开发最佳实践**
  - 代码规范
  - 分层设计
  - 异常处理
  - 事务管理
  - 接口设计

- **性能优化**
  - 数据库优化
  - 缓存优化
  - JVM 调优
  - 慢查询优化

- **安全实践**
  - 认证授权
  - 数据加密
  - SQL 注入防护
  - XSS 防护
  - CSRF 防护

- **测试实践**
  - 单元测试
  - 集成测试
  - 性能测试
  - 压力测试

### API 参考

- **RESTful API 设计规范**
  - URL 设计
  - HTTP 方法使用
  - 状态码规范
  - 请求响应格式

- **在线 API 文档**
  - Swagger UI: http://localhost:8000/swagger-ui.html
  - Bootstrap UI: http://localhost:8000/doc.html
  - MG UI: http://localhost:8000/document.html

### 常见问题

#### 环境问题

- **Q: Maven 依赖下载失败怎么办？**
  
  A: 配置阿里云 Maven 镜像，或清理本地仓库后重试。详见 [快速启动指南 - 常见问题](QUICK_START.md#常见问题)

- **Q: 数据库连接失败？**
  
  A: 检查 MySQL 是否启动，数据库是否创建，连接信息是否正确。详见 [快速启动指南 - 常见问题](QUICK_START.md#数据库连接失败)

- **Q: Redis 连接失败？**
  
  A: 检查 Redis 是否启动，配置是否正确。详见 [快速启动指南 - 常见问题](QUICK_START.md#redis-连接失败)

#### 启动问题

- **Q: 服务启动时端口冲突？**
  
  A: 修改 bootstrap.yml 中的端口配置，或停止占用端口的进程。详见 [快速启动指南 - 常见问题](QUICK_START.md#端口冲突)

- **Q: 内存溢出 (OutOfMemoryError)？**
  
  A: 增加 JVM 内存参数 `-Xms512m -Xmx1024m`。详见 [快速启动指南 - 常见问题](QUICK_START.md#内存不足)

#### 开发问题

- **Q: 如何创建新的微服务？**
  
  A: 参考 [快速开发指南 - 开发新服务](QUICK_DEVELOPMENT.md#开发新服务)

- **Q: 如何使用自定义 Starter？**
  
  A: 参考 [快速开发指南 - 使用自定义Starter](QUICK_DEVELOPMENT.md#使用自定义starter)

- **Q: 如何调试微服务？**
  
  A: 参考 [快速开发指南 - 调试技巧](QUICK_DEVELOPMENT.md#调试技巧)

#### 部署问题

- **Q: 如何打包部署到生产环境？**
  
  A: 使用 `mvn clean package -DskipTests` 打包，然后使用 `java -jar` 运行。

- **Q: 如何使用 Docker 部署？**
  
  A: 项目已配置 docker-maven-plugin，使用 `mvn clean package docker:build` 构建镜像。

## 🔗 相关链接

### 项目资源

- [项目主页](../README.md)
- [GitHub 仓库](#)
- [问题反馈](#)
- [更新日志](#)

### 技术文档

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Cloud 官方文档](https://spring.io/projects/spring-cloud)
- [Spring Cloud Alibaba 文档](https://github.com/alibaba/spring-cloud-alibaba)
- [MyBatis-Plus 文档](https://baomidou.com/)
- [Nacos 文档](https://nacos.io/zh-cn/docs/what-is-nacos.html)
- [Sentinel 文档](https://sentinelguard.io/zh-cn/docs/introduction.html)
- [XXL-Job 文档](https://www.xuxueli.com/xxl-job/)
- [TX-LCN 文档](https://www.txlcn.org/)

### 开发规范

- [阿里巴巴 Java 开发手册](https://github.com/alibaba/p3c)
- [RESTful API 设计指南](https://restfulapi.net/)
- [Git 提交规范](https://www.conventionalcommits.org/)

## 📞 获取帮助

如果您在使用过程中遇到问题：

1. **查看文档** - 首先查看相关文档，特别是常见问题部分
2. **查看日志** - 检查服务日志，通常会有详细的错误信息
3. **搜索 Issues** - 在 GitHub Issues 中搜索相似问题
4. **提交 Issue** - 如果以上方式无法解决，请提交详细的问题描述

### 问题反馈模板

```markdown
## 问题描述
简要描述您遇到的问题

## 环境信息
- 操作系统：Windows 10 / macOS / Linux
- JDK 版本：1.8.0_xxx
- Maven 版本：3.6.x
- 项目版本：3.1.0

## 复现步骤
1. 第一步
2. 第二步
3. ...

## 错误信息
粘贴完整的错误日志

## 期望行为
描述您期望的正确行为

## 其他信息
其他可能有帮助的信息
```

## 🤝 贡献指南

欢迎为本项目贡献代码或文档！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 许可证

本项目采用 Apache 2.0 许可证。详见 [LICENSE](../LICENSE) 文件。

---

**最后更新时间：** 2024-02-06

如有疑问，请联系项目维护者或在 Issues 中提问。
