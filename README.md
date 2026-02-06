# raisin-platform 微服务企业级开发平台

<p align="center">
  <img src='https://img.shields.io/badge/license-Apache%202-4EB1BA.svg' alt='License'/>
  <img src="https://img.shields.io/badge/Spring%20Boot-2.1.9.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-Greenwich.SR3-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2.1.1.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Java-8-orange" alt="Downloads"/>
</p>

## 项目简介

raisin-platform是一个基于Spring Boot 2.1.x、Spring Cloud Greenwich和Spring Cloud Alibaba 2.1.x的企业级微服务开发平台。平台采用Java 8开发，通过Maven多模块架构构建，为企业级微服务应用提供完整的解决方案。

### 技术栈

- **核心框架**: Spring Boot 2.1.9.RELEASE
- **微服务框架**: Spring Cloud Greenwich.SR3
- **微服务生态**: Spring Cloud Alibaba 2.1.1.RELEASE
- **开发语言**: Java 8
- **构建工具**: Maven
- **数据库**: MySQL + MyBatis-Plus
- **缓存**: Redis + Redisson
- **消息队列**: 支持RocketMQ等
- **监控**: Spring Boot Admin + Micrometer + Prometheus
- **分布式事务**: TX-LCN
- **API文档**: Swagger2
- **安全认证**: Spring Security OAuth2 + JWT
- **容器化**: Docker

### 核心特性

- 🚀 **微服务架构**: 基于Spring Cloud的完整微服务解决方案
- 🔐 **统一认证**: Spring Security OAuth2 + JWT实现的无状态认证
- 📊 **监控运维**: 完整的监控体系，包括应用监控、日志中心、APM等
- 🛡️ **服务治理**: Sentinel流量控制、Hystrix熔断降级
- 💾 **数据访问**: MyBatis-Plus + Druid连接池，支持分库分表
- 🔍 **服务发现**: Nacos作为注册中心和配置中心
- 📋 **任务调度**: 分布式任务调度系统
- 🔄 **分布式事务**: TX-LCN分布式事务解决方案
- 📁 **文件服务**: 支持多种文件存储（本地、阿里云OSS、七牛云等）
- 🔍 **搜索服务**: 完整的搜索解决方案
- ⚡ **性能优化**: 自定义Spring Boot Starters，优化启动性能

## 模块架构

```
raisin-platform -- 父项目，公共依赖
│
├─raisin-commons -- 通用工具及自定义Starters
│  ├─raisin-common-core -- 核心通用组件
│  ├─raisin-common-spring-boot-starter -- 通用Spring Boot Starter
│  ├─raisin-db-spring-boot-starter -- 数据库操作Starter
│  ├─raisin-redis-spring-boot-starter -- Redis操作Starter
│  ├─raisin-log-spring-boot-starter -- 日志处理Starter
│  ├─raisin-swagger2-spring-boot-starter -- API文档Starter
│  ├─raisin-ribbon-spring-boot-starter -- 服务调用Starter
│  ├─raisin-auth-client-spring-boot-starter -- 认证客户端Starter
│  └─raisin-sentinel-spring-boot-starter -- 流控降级Starter
│
├─raisin-uaa -- 统一认证授权中心 [8000]
│  ├─基于Spring Security OAuth2 + JWT
│  ├─支持多种认证方式
│  ├─集成Redis会话管理
│  └─提供权限管理功能
│
├─raisin-gateway -- API网关层
│  ├─sc-gateway -- Spring Cloud Gateway网关 [9900]
│  └─zuul-gateway -- Netflix Zuul网关 [9900]
│
├─raisin-business -- 业务服务层
│  ├─user-center -- 用户中心服务 [7000]
│  ├─file-center -- 文件中心服务 [5000]
│  ├─code-generator -- 代码生成器 [7300]
│  └─search-center -- 搜索中心
│     ├─search-client -- 搜索客户端
│     └─search-server -- 搜索服务端 [7100]
│
├─raisin-web -- 后台管理系统 [8066]
│  └─基于Vue.js + Element UI
│
├─raisin-job -- 分布式任务调度
│  ├─job-admin -- 任务管理平台 [8081]
│  ├─job-core -- 任务调度核心
│  └─job-executor-samples -- 执行器示例 [8082]
│
├─raisin-monitor -- 监控与日志
│  ├─sc-admin -- Spring Boot Admin监控 [6500]
│  └─log-center -- 日志中心 [6200]
│
├─raisin-transaction -- 分布式事务
│  └─txlcn-tm -- TX-LCN事务管理器 [7970]
│
├─raisin-config -- 配置中心集成
│
├─raisin-register -- 服务注册中心
│  └─集成Nacos [8848]
│
└─raisin-demo -- 示例代码
   ├─txlcn-demo -- TX-LCN分布式事务示例
   ├─seata-demo -- Seata分布式事务示例
   ├─sharding-jdbc-demo -- 分库分表示例
   └─rocketmq-demo -- RocketMQ消息队列示例
```

## 技术亮点

### 1. 自定义Spring Boot Starters
平台提供了一系列自定义的Spring Boot Starters，用于标准化和简化常用功能的集成：

- `raisin-db-starter`: 数据库访问封装，包含MyBatis-Plus增强
- `raisin-redis-starter`: Redis操作封装，支持Redisson
- `raisin-auth-client-starter`: 认证客户端封装，简化OAuth2集成
- `raisin-sentinel-starter`: Sentinel流控降级配置封装
- `raisin-ribbon-starter`: 服务调用负载均衡封装
- `raisin-log-starter`: 统一日志处理
- `raisin-swagger2-starter`: API文档自动生成

### 2. 企业级非功能性需求支持
- **安全认证**: 基于RBAC的权限控制，支持JWT和OAuth2
- **服务治理**: Sentinel流量控制、Hystrix熔断、负载均衡
- **监控运维**: 全链路监控、日志聚合、性能监控
- **配置管理**: Nacos配置中心，支持多环境配置
- **服务发现**: Nacos注册中心，自动服务发现
- **任务调度**: 分布式任务调度，支持集群模式

### 3. 完整的数据存储方案
- **关系型数据库**: MySQL + Druid连接池 + MyBatis-Plus
- **缓存**: Redis + Redisson分布式缓存
- **分库分表**: ShardingSphere支持
- **文件存储**: 本地存储、阿里云OSS、七牛云存储
- **搜索引擎**: Elasticsearch集成

### 4. 容器化支持
- 基于Docker的容器化部署
- 统一的Docker镜像构建配置
- 支持Kubernetes部署

## 快速开始

### 环境要求
- Java 8+
- Maven 3.6+
- MySQL 5.7+
- Redis 3.2+
- Nacos Server

### 启动步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd raisin-platform
```

2. **初始化数据库**
```bash
# 执行数据库初始化脚本
mysql -u root -p < sql/init.sql
```

3. **启动基础服务**
```bash
# 启动Nacos注册中心和配置中心
cd raisin-register
java -jar nacos-server.jar

# 启动Redis
redis-server
```

4. **启动核心服务**
```bash
# 按顺序启动服务
cd raisin-uaa && mvn spring-boot:run
cd raisin-gateway && mvn spring-boot:run
cd raisin-business/user-center && mvn spring-boot:run
cd raisin-web && mvn spring-boot:run
```

### 默认端口配置

| 服务 | 端口 | 描述 |
|------|------|------|
| raisin-uaa | 8000 | 认证中心 |
| raisin-gateway | 9900 | API网关 |
| user-center | 7000 | 用户中心 |
| file-center | 5000 | 文件中心 |
| code-generator | 7300 | 代码生成器 |
| search-server | 7100 | 搜索服务 |
| sc-admin | 6500 | 应用监控 |
| log-center | 6200 | 日志中心 |
| job-admin | 8081 | 任务管理 |
| job-executor | 8082 | 任务执行 |
| txlcn-tm | 7970 | 事务管理 |
| back-web | 8066 | 后台管理 |

## 开发指南

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用统一的项目结构和命名规范
- 强制代码审查和静态检查

### 自定义Starter使用

```xml
<!-- 在你的项目pom.xml中引入 -->
<dependency>
    <groupId>com.raisin</groupId>
    <artifactId>raisin-db-spring-boot-starter</artifactId>
</dependency>
```

### 配置示例

```yaml
# application.yml
spring:
  application:
    name: your-service-name
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
        file-extension: yml
```

## 监控运维

### 应用监控
- **Spring Boot Admin**: 应用状态监控、管理
- **Actuator**: 应用健康检查、指标采集
- **Micrometer + Prometheus**: 指标收集和监控

### 日志管理
- **统一日志格式**: 结构化日志输出
- **ELK集成**: Elasticsearch + Logstash + Kibana
- **日志聚合**: 分布式日志收集和分析

### 链路追踪
- **Sleuth + Zipkin**: 分布式链路追踪
- **性能分析**: 请求链路性能监控

## 部署架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Load Balancer │    │   Load Balancer │    │   Load Balancer │
│     (Nginx)     │    │     (Nginx)     │    │     (Nginx)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
┌─────────────────────────────────────────────────────────────────┐
│                       API Gateway Layer                         │
│  ┌─────────────────┐    ┌─────────────────┐                   │
│  │  SC Gateway     │    │   Zuul Gateway  │                   │
│  │     [9900]      │    │     [9900]      │                   │
│  └─────────────────┘    └─────────────────┘                   │
└─────────────────────────────────────────────────────────────────┘
         │                       │                       │
┌─────────────────────────────────────────────────────────────────┐
│                      Business Service Layer                     │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │ User Center │ │File Center  │ │Code Generator│ │Search Center││
│  │   [7000]    │ │   [5000]    │ │   [7300]    │ │   [7100]    ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
└─────────────────────────────────────────────────────────────────┘
         │                       │                       │
┌─────────────────────────────────────────────────────────────────┐
│                      Support Service Layer                      │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │   UAA Auth  │ │  Job Admin  │ │  Monitor    │ │  Transaction││
│  │   [8000]    │ │   [8081]    │ │   [6500]    │ │   [7970]    ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
└─────────────────────────────────────────────────────────────────┘
         │                       │                       │
┌─────────────────────────────────────────────────────────────────┐
│                       Infrastructure Layer                      │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│
│  │    Nacos    │ │   Redis     │ │   MySQL     │ │    MQ       ││
│  │  [8848]     │ │   [6379]    │ │   [3306]    │ │             ││
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│
└─────────────────────────────────────────────────────────────────┘
```

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 Apache 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目作者: raisin-team
- 项目地址: [GitHub Repository URL]
- 邮箱: support@raisin.com

---

**注意**: 这是一个企业级微服务开发平台，适用于大型分布式系统的开发和部署。在使用前请确保充分了解各个组件的功能和配置要求。