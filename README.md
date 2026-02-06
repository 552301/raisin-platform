# raisin-platform 微服务企业级开发平台

<p align="center">
  <img src='https://img.shields.io/badge/license-Apache%202-4EB1BA.svg' alt='License'/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.3-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-2023.0.0-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2023.0.1.0-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Java-21-orange" alt="Downloads"/>
</p>

## 项目简介

raisin-platform是一个基于Spring Boot 3.2.x、Spring Cloud 2023和Spring Cloud Alibaba 2023的企业级微服务开发平台。平台采用Java 21开发，通过Maven多模块架构构建，为企业级微服务应用提供完整的解决方案。

### 技术栈

- **核心框架**: Spring Boot 3.2.3
- **微服务框架**: Spring Cloud 2023.0.0
- **微服务生态**: Spring Cloud Alibaba 2023.0.1.0
- **开发语言**: Java 21
- **构建工具**: Maven
- **数据库**: MySQL + MyBatis-Plus
- **缓存**: Redis + Redisson
- **消息队列**: 支持RocketMQ等
- **监控**: Spring Boot Admin + Micrometer + Prometheus
- **API文档**: Swagger3 (SpringDoc / OpenAPI 3)
- **安全认证**: Spring Security OAuth2 (Spring Authorization Server) + JWT
- **容器化**: Docker

### 核心特性

- 🚀 **微服务架构**: 基于Spring Cloud的完整微服务解决方案
- 🔐 **统一认证**: Spring Authorization Server + JWT实现的无状态认证
- 📊 **监控运维**: 完整的监控体系，包括应用监控、日志中心、APM等
- 🛡️ **服务治理**: Sentinel流量控制、LoadBalancer服务发现与负载均衡
- 💾 **数据访问**: MyBatis-Plus + Druid连接池
- 🔍 **服务发现**: Nacos作为注册中心和配置中心
- 📋 **任务调度**: 分布式任务调度系统
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
│  ├─raisin-loadbalancer-spring-boot-starter -- 服务调用及负载均衡Starter
│  ├─raisin-auth-client-spring-boot-starter -- 认证客户端Starter
│  └─raisin-sentinel-spring-boot-starter -- 流控降级Starter
│
├─raisin-uaa -- 统一认证授权中心 [8000]
│  ├─基于Spring Authorization Server + JWT
│  ├─支持多种认证方式
│  ├─集成Redis会话管理
│  └─提供权限管理功能
│
├─raisin-gateway -- API网关层
│  └─sc-gateway -- Spring Cloud Gateway网关 [9900]
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
```
