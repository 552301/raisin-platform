# Raisin Platform 快速启动指南

本文档提供详细的快速启动步骤，帮助您快速搭建和运行 Raisin Platform 微服务平台。

## 目录

- [环境准备](#环境准备)
- [基础设施启动](#基础设施启动)
- [数据库初始化](#数据库初始化)
- [配置说明](#配置说明)
- [服务启动](#服务启动)
- [验证测试](#验证测试)
- [常见问题](#常见问题)

---

## 环境准备

### 必需软件

在开始之前，请确保您的开发环境已安装以下软件：

| 软件 | 版本要求 | 说明 |
|------|---------|------|
| **JDK** | 1.8+ | 建议使用 OpenJDK 8 或 Oracle JDK 8 |
| **Maven** | 3.6+ | 用于项目构建和依赖管理 |
| **MySQL** | 5.7+ / 8.0+ | 关系型数据库 |
| **Redis** | 3.2+ | 缓存和会话存储 |
| **Nacos** | 1.1.4+ | 服务注册与配置中心（可选，默认不启用） |
| **Git** | 最新版 | 版本控制工具 |

### 环境变量配置

#### 1. 配置 JAVA_HOME

```bash
# Linux/MacOS
export JAVA_HOME=/path/to/your/jdk
export PATH=$JAVA_HOME/bin:$PATH

# Windows
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx
set PATH=%JAVA_HOME%\bin;%PATH%
```

#### 2. 配置 MAVEN_HOME

```bash
# Linux/MacOS
export MAVEN_HOME=/path/to/your/maven
export PATH=$MAVEN_HOME/bin:$PATH

# Windows
set MAVEN_HOME=C:\Program Files\apache-maven-3.x.x
set PATH=%MAVEN_HOME%\bin;%PATH%
```

#### 3. 验证安装

```bash
# 验证 Java 版本
java -version
# 应该显示: java version "1.8.0_xxx"

# 验证 Maven 版本
mvn -version
# 应该显示: Apache Maven 3.x.x
```

---

## 基础设施启动

### 1. 启动 MySQL

#### Docker 方式（推荐）

```bash
docker run -d \
  --name raisin-mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=raisin \
  -v mysql-data:/var/lib/mysql \
  mysql:5.7 \
  --character-set-server=utf8mb4 \
  --collation-server=utf8mb4_unicode_ci
```

#### 本地安装方式

```bash
# Linux (Ubuntu/Debian)
sudo apt-get install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql

# MacOS (使用 Homebrew)
brew install mysql@5.7
brew services start mysql@5.7

# Windows
# 下载并安装 MySQL 官方安装程序
# https://dev.mysql.com/downloads/mysql/
```

### 2. 启动 Redis

#### Docker 方式（推荐）

```bash
docker run -d \
  --name raisin-redis \
  -p 6379:6379 \
  -v redis-data:/data \
  redis:6.2-alpine \
  redis-server --appendonly yes
```

#### 本地安装方式

```bash
# Linux (Ubuntu/Debian)
sudo apt-get install redis-server
sudo systemctl start redis
sudo systemctl enable redis

# MacOS (使用 Homebrew)
brew install redis
brew services start redis

# Windows
# 下载 Redis Windows 版本或使用 WSL
```

### 3. 启动 Nacos（可选）

Nacos 用于服务注册与配置管理。如果不需要服务发现和集中配置管理，可以暂时跳过此步骤。

#### Docker 方式

```bash
docker run -d \
  --name raisin-nacos \
  -p 8848:8848 \
  -e MODE=standalone \
  -e SPRING_DATASOURCE_PLATFORM=mysql \
  -e MYSQL_SERVICE_HOST=host.docker.internal \
  -e MYSQL_SERVICE_PORT=3306 \
  -e MYSQL_SERVICE_DB_NAME=nacos \
  -e MYSQL_SERVICE_USER=root \
  -e MYSQL_SERVICE_PASSWORD=root \
  nacos/nacos-server:1.4.2
```

#### 本地方式

```bash
# 1. 下载 Nacos
wget https://github.com/alibaba/nacos/releases/download/1.4.2/nacos-server-1.4.2.tar.gz
tar -xzf nacos-server-1.4.2.tar.gz
cd nacos/bin

# 2. 启动 Nacos (单机模式)
# Linux/MacOS
sh startup.sh -m standalone

# Windows
startup.cmd -m standalone
```

访问 Nacos 控制台：http://localhost:8848/nacos  
默认账号密码：nacos/nacos

### 4. 验证基础设施

```bash
# 测试 MySQL 连接
mysql -h 127.0.0.1 -P 3306 -u root -p

# 测试 Redis 连接
redis-cli ping
# 应该返回: PONG

# 测试 Nacos（如果已启动）
curl http://localhost:8848/nacos/
```

---

## 数据库初始化

### 1. 创建数据库

项目需要创建多个数据库，每个服务使用独立的数据库：

```bash
# 连接到 MySQL
mysql -u root -p

# 或使用一键创建脚本（推荐）
mysql -u root -p < raisin-doc/sql/create-databases.sql
```

**手动创建数据库：**

```sql
-- 1. 认证中心数据库
CREATE DATABASE IF NOT EXISTS `oauth-center` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 用户中心数据库
CREATE DATABASE IF NOT EXISTS `user-center` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. 文件中心数据库
CREATE DATABASE IF NOT EXISTS `file-center` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 4. 任务调度数据库
CREATE DATABASE IF NOT EXISTS `xxl-job` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 5. 分布式事务数据库
CREATE DATABASE IF NOT EXISTS `tx-manager` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 导入初始化脚本

按照以下顺序执行 SQL 脚本：

```bash
# 进入 SQL 目录
cd raisin-doc/sql

# 1. 认证中心
mysql -u root -p oauth-center < oauth-center.sql

# 2. 用户中心
mysql -u root -p user-center < user-center.sql

# 3. 文件中心
mysql -u root -p file-center < file-center.sql

# 4. 任务调度
mysql -u root -p xxl-job < xxl-job.sql

# 5. 分布式事务管理器
mysql -u root -p tx-manager < tx-manager.sql

# 6. 分布式事务日志
mysql -u root -p tx-manager < tx_logger.sql
```

### 3. 验证数据库初始化

```sql
-- 检查数据库
SHOW DATABASES;

-- 检查表（以 oauth-center 为例）
USE `oauth-center`;
SHOW TABLES;

-- 检查初始数据
SELECT * FROM oauth_client_details;
```

应该能看到已创建的数据库和初始化数据。

---

## 配置说明

### 1. 数据源配置

根据实际环境修改数据库连接配置。有两种配置方式：

#### 方式一：环境变量配置（推荐）

设置以下环境变量：

```bash
# Linux/MacOS
export ZLT_DATASOURCE_IP=localhost
export ZLT_DATASOURCE_USERNAME=root
export ZLT_DATASOURCE_PASSWORD=root

# Windows
set ZLT_DATASOURCE_IP=localhost
set ZLT_DATASOURCE_USERNAME=root
set ZLT_DATASOURCE_PASSWORD=root
```

#### 方式二：修改配置文件

如果不使用环境变量，需要修改各服务的 `application.yml` 文件：

**示例：`raisin-uaa/src/main/resources/application.yml`**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/oauth-center?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

**需要修改的服务配置文件：**

- `raisin-uaa/src/main/resources/application.yml` - 认证中心
- `raisin-business/user-center/src/main/resources/application.yml` - 用户中心
- `raisin-business/file-center/src/main/resources/application.yml` - 文件中心
- `raisin-job/job-admin/src/main/resources/application.yml` - 任务管理
- `raisin-transaction/txlcn-tm/src/main/resources/application.yml` - 事务管理器

### 2. Redis 配置

默认 Redis 连接为 `localhost:6379`，无密码。如需修改，编辑各服务的配置文件：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: # 如果有密码，在此设置
    database: 0
    timeout: 5000
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0
```

### 3. Nacos 配置（可选）

如果需要使用 Nacos 服务发现和配置管理，取消注释 `bootstrap.yml` 中的 Nacos 配置：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        enabled: true
      config:
        server-addr: localhost:8848
        file-extension: yml
        enabled: true
```

---

## 服务启动

### 启动顺序说明

微服务启动需要遵循一定的顺序，确保依赖服务已经就绪：

```
基础设施 (MySQL, Redis, Nacos)
    ↓
认证中心 (raisin-uaa)
    ↓
API 网关 (raisin-gateway)
    ↓
业务服务 (user-center, file-center, etc.)
    ↓
支撑服务 (监控、日志、任务等)
```

### 方式一：使用 Maven 启动（开发环境推荐）

#### 1. 编译整个项目

```bash
# 进入项目根目录
cd raisin-platform

# 清理并编译（跳过测试）
mvn clean install -DskipTests

# 如果编译失败，可以尝试
mvn clean install -DskipTests -U
```

#### 2. 启动认证中心（端口：8000）

```bash
# 方式 1：使用 Maven 插件
cd raisin-uaa
mvn spring-boot:run

# 方式 2：使用 java -jar
cd raisin-uaa/target
java -jar raisin-uaa-3.1.0.jar
```

等待服务启动成功，看到类似以下日志：
```
Started UAAServerApplication in 15.234 seconds
```

#### 3. 启动 API 网关（端口：9900）

**选择一个网关启动：**

**Spring Cloud Gateway（推荐）：**
```bash
cd raisin-gateway/sc-gateway
mvn spring-boot:run
```

**或 Zuul Gateway：**
```bash
cd raisin-gateway/zuul-gateway
mvn spring-boot:run
```

#### 4. 启动用户中心（端口：7000）

```bash
cd raisin-business/user-center
mvn spring-boot:run
```

#### 5. 启动文件中心（端口：5000）

```bash
cd raisin-business/file-center
mvn spring-boot:run
```

#### 6. 启动其他服务（可选）

```bash
# 代码生成器（端口：7300）
cd raisin-business/code-generator
mvn spring-boot:run

# 搜索服务（端口：7100）
cd raisin-business/search-center/search-server
mvn spring-boot:run

# 监控服务（端口：6500）
cd raisin-monitor/sc-admin
mvn spring-boot:run

# 日志中心（端口：6200）
cd raisin-monitor/log-center
mvn spring-boot:run

# 任务管理（端口：8081）
cd raisin-job/job-admin
mvn spring-boot:run

# 分布式事务管理器（端口：7970）
cd raisin-transaction/txlcn-tm
mvn spring-boot:run

# 后台管理系统（端口：8066）
cd raisin-web
mvn spring-boot:run
```

### 方式二：使用 IDE 启动（推荐）

#### IntelliJ IDEA

1. **导入项目**
   - File → Open → 选择 `raisin-platform` 目录
   - 等待 Maven 依赖下载完成

2. **配置 JDK**
   - File → Project Structure → Project SDK
   - 选择 JDK 1.8

3. **启动服务**
   - 找到各服务的启动类（*Application.java）
   - 右键 → Run 'XXXApplication'

**各服务启动类位置：**

| 服务 | 启动类路径 |
|------|----------|
| 认证中心 | `raisin-uaa/src/main/java/com/raisin/oauth/UAAServerApplication.java` |
| API网关 | `raisin-gateway/sc-gateway/src/main/java/com/raisin/gateway/GatewayApplication.java` |
| 用户中心 | `raisin-business/user-center/src/main/java/com/raisin/user/UserCenterApplication.java` |
| 文件中心 | `raisin-business/file-center/src/main/java/com/raisin/file/FileCenterApplication.java` |

#### Eclipse

1. **导入项目**
   - File → Import → Existing Maven Projects
   - 选择 `raisin-platform` 目录
   - 点击 Finish

2. **启动服务**
   - 找到各服务的启动类
   - 右键 → Run As → Java Application

### 方式三：使用脚本批量启动

创建启动脚本可以快速启动所有服务：

**Linux/MacOS (`start-all.sh`)：**

```bash
#!/bin/bash

echo "开始启动 Raisin Platform 微服务..."

# 设置数据库连接参数
export ZLT_DATASOURCE_IP=localhost
export ZLT_DATASOURCE_USERNAME=root
export ZLT_DATASOURCE_PASSWORD=root

# 启动认证中心
echo "启动认证中心 (UAA)..."
cd raisin-uaa
nohup mvn spring-boot:run > logs/uaa.log 2>&1 &
sleep 10

# 启动 API 网关
echo "启动 API 网关..."
cd ../raisin-gateway/sc-gateway
nohup mvn spring-boot:run > logs/gateway.log 2>&1 &
sleep 10

# 启动用户中心
echo "启动用户中心..."
cd ../../raisin-business/user-center
nohup mvn spring-boot:run > logs/user-center.log 2>&1 &
sleep 5

# 启动文件中心
echo "启动文件中心..."
cd ../file-center
nohup mvn spring-boot:run > logs/file-center.log 2>&1 &

echo "所有服务启动完成！"
echo "请使用 'tail -f logs/*.log' 查看日志"
```

**Windows (`start-all.bat`)：**

```batch
@echo off
echo 开始启动 Raisin Platform 微服务...

REM 设置数据库连接参数
set ZLT_DATASOURCE_IP=localhost
set ZLT_DATASOURCE_USERNAME=root
set ZLT_DATASOURCE_PASSWORD=root

REM 启动认证中心
echo 启动认证中心 (UAA)...
cd raisin-uaa
start "UAA Server" cmd /k mvn spring-boot:run
timeout /t 15

REM 启动 API 网关
echo 启动 API 网关...
cd ..\raisin-gateway\sc-gateway
start "Gateway" cmd /k mvn spring-boot:run
timeout /t 15

REM 启动用户中心
echo 启动用户中心...
cd ..\..\raisin-business\user-center
start "User Center" cmd /k mvn spring-boot:run
timeout /t 5

REM 启动文件中心
echo 启动文件中心...
cd ..\file-center
start "File Center" cmd /k mvn spring-boot:run

echo 所有服务启动完成！
pause
```

---

## 验证测试

### 1. 检查服务状态

#### 查看进程

```bash
# Linux/MacOS
ps aux | grep java

# Windows
tasklist | findstr java
```

#### 查看端口占用

```bash
# Linux/MacOS
lsof -i :8000  # UAA
lsof -i :9900  # Gateway
lsof -i :7000  # User Center

# Windows
netstat -ano | findstr :8000
netstat -ano | findstr :9900
netstat -ano | findstr :7000
```

### 2. 测试服务接口

#### 测试认证中心

```bash
# 健康检查
curl http://localhost:8000/actuator/health

# 获取 Token
curl -X POST "http://localhost:8000/oauth/token" \
  -H "Authorization: Basic d2ViQXBwOndlYkFwcA==" \
  -d "grant_type=password&username=admin&password=admin"
```

**预期响应：**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 3599,
  "scope": "app"
}
```

#### 测试用户中心

```bash
# 健康检查
curl http://localhost:7000/actuator/health

# 查询用户列表（需要 token）
curl -X GET "http://localhost:7000/users/list" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

#### 测试 API 网关

```bash
# 健康检查
curl http://localhost:9900/actuator/health

# 通过网关访问用户中心
curl -X GET "http://localhost:9900/api-user/users/list" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 3. 访问 Swagger 文档

各服务启动后，可以通过 Swagger UI 查看和测试 API：

| 服务 | Swagger 地址 |
|------|-------------|
| 认证中心 | http://localhost:8000/swagger-ui.html |
| 用户中心 | http://localhost:7000/swagger-ui.html |
| 文件中心 | http://localhost:5000/swagger-ui.html |
| 代码生成器 | http://localhost:7300/swagger-ui.html |
| 搜索服务 | http://localhost:7100/swagger-ui.html |

**增强版 Swagger UI（推荐）：**
- Swagger Bootstrap UI: http://localhost:8000/doc.html
- Swagger MG UI: http://localhost:8000/document.html

### 4. 访问监控中心

#### Spring Boot Admin

访问地址：http://localhost:6500

可以查看所有服务的：
- 运行状态
- 内存使用
- 线程情况
- 日志输出
- 环境配置

#### 任务管理平台

访问地址：http://localhost:8081/xxl-job-admin

默认账号密码：admin/123456

### 5. 后台管理系统

访问地址：http://localhost:8066

这是基于 Vue.js 的前端管理系统，可以管理：
- 用户管理
- 角色管理
- 权限管理
- 菜单管理
- 日志查询

---

## 常见问题

### 1. 编译失败

**问题：** Maven 编译时出现依赖下载失败

**解决方案：**
```bash
# 清理 Maven 本地仓库缓存
mvn clean install -U -DskipTests

# 或删除本地仓库重新下载
rm -rf ~/.m2/repository
mvn clean install -DskipTests
```

**问题：** 编译时提示 Java 版本不匹配

**解决方案：**
```bash
# 确认 Java 版本
java -version

# 如果版本不对，修改 JAVA_HOME 环境变量
export JAVA_HOME=/path/to/jdk1.8
```

### 2. 数据库连接失败

**问题：** 服务启动时提示数据库连接失败

**解决方案：**

1. 检查 MySQL 是否启动：
```bash
mysql -u root -p
```

2. 检查数据库是否创建：
```sql
SHOW DATABASES;
```

3. 检查配置文件中的连接信息是否正确

4. 如果使用 MySQL 8.0，可能需要修改密码认证方式：
```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
FLUSH PRIVILEGES;
```

### 3. Redis 连接失败

**问题：** 服务启动时提示 Redis 连接失败

**解决方案：**

1. 检查 Redis 是否启动：
```bash
redis-cli ping
```

2. 检查 Redis 配置：
```bash
redis-cli CONFIG GET bind
redis-cli CONFIG GET protected-mode
```

3. 如果需要远程访问，修改 `redis.conf`：
```
bind 0.0.0.0
protected-mode no
```

### 4. 端口冲突

**问题：** 服务启动失败，提示端口已被占用

**解决方案：**

1. 查找占用端口的进程：
```bash
# Linux/MacOS
lsof -i :8000
kill -9 <PID>

# Windows
netstat -ano | findstr :8000
taskkill /F /PID <PID>
```

2. 或修改服务端口，编辑 `bootstrap.yml`：
```yaml
server:
  port: 8001  # 修改为其他端口
```

### 5. 内存不足

**问题：** 服务启动时提示内存溢出 (OutOfMemoryError)

**解决方案：**

1. 增加 JVM 内存参数：
```bash
# Maven 启动
export MAVEN_OPTS="-Xms512m -Xmx1024m"
mvn spring-boot:run

# Jar 启动
java -Xms512m -Xmx1024m -jar app.jar
```

2. 在 IDE 中修改运行配置：
   - IntelliJ IDEA: Run → Edit Configurations → VM options
   - 添加：`-Xms512m -Xmx1024m`

### 6. Nacos 连接失败（如果使用）

**问题：** 服务无法连接到 Nacos

**解决方案：**

1. 确认 Nacos 是否启动：
```bash
curl http://localhost:8848/nacos/
```

2. 检查 Nacos 配置是否正确：
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

3. 如果暂时不需要 Nacos，可以注释掉相关配置

### 7. Token 验证失败

**问题：** 调用接口时提示 Token 无效

**解决方案：**

1. 确认认证中心（UAA）已启动

2. 重新获取 Token：
```bash
curl -X POST "http://localhost:8000/oauth/token" \
  -H "Authorization: Basic d2ViQXBwOndlYkFwcA==" \
  -d "grant_type=password&username=admin&password=admin"
```

3. 确认 Token 没有过期（默认有效期 3600 秒）

4. 检查 Redis 中的 Token 是否存在：
```bash
redis-cli
KEYS *access*
```

### 8. 前端无法访问后端

**问题：** 浏览器访问后端接口出现跨域错误

**解决方案：**

1. 通过 API 网关访问，网关已配置 CORS

2. 或在各服务中添加跨域配置：
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

---

## 下一步

现在您已经成功启动了 Raisin Platform！

- 📖 查看 [快速开发指南](QUICK_DEVELOPMENT.md) 了解如何开发新功能
- 🔧 查看 [配置指南](CONFIGURATION.md) 了解详细的配置说明
- 🚀 查看 [部署指南](DEPLOYMENT.md) 了解生产环境部署
- 📚 查看 [API 文档](http://localhost:8000/swagger-ui.html) 了解接口详情

---

## 获取帮助

如果遇到其他问题：

1. 查看服务日志：
   - 控制台输出
   - `logs/` 目录下的日志文件

2. 查看 GitHub Issues

3. 联系技术支持

---

**最后更新时间：** 2024-02-06
