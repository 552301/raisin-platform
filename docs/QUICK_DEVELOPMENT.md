# Raisin Platform 快速开发指南

本文档提供详细的开发指导，帮助您快速上手 Raisin Platform 的微服务开发。

## 目录

- [开发环境配置](#开发环境配置)
- [项目结构说明](#项目结构说明)
- [核心组件使用](#核心组件使用)
- [开发新服务](#开发新服务)
- [开发新接口](#开发新接口)
- [使用自定义Starter](#使用自定义starter)
- [开发规范](#开发规范)
- [调试技巧](#调试技巧)
- [单元测试](#单元测试)

---

## 开发环境配置

### IDE 推荐

#### IntelliJ IDEA（强烈推荐）

**必装插件：**

1. **Lombok** - 简化 Java 代码
2. **MyBatis X** - MyBatis 增强支持
3. **Maven Helper** - Maven 依赖管理
4. **RestfulTool** - REST 接口测试
5. **Spring Assistant** - Spring Boot 配置提示
6. **Alibaba Java Coding Guidelines** - 代码规范检查

**IDEA 配置：**

```
1. 启用注解处理
   Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   勾选 "Enable annotation processing"

2. 配置代码格式化
   Settings → Editor → Code Style → Java
   导入项目根目录下的 code-style.xml

3. 配置 Maven
   Settings → Build, Execution, Deployment → Build Tools → Maven
   Maven home directory: /path/to/maven
   User settings file: /path/to/settings.xml
   Local repository: ~/.m2/repository

4. 配置 JDK
   File → Project Structure → Project
   Project SDK: 1.8
   Project language level: 8

5. 设置文件编码
   Settings → Editor → File Encodings
   Global Encoding: UTF-8
   Project Encoding: UTF-8
```

#### Eclipse

**必装插件：**

1. **Spring Tools Suite (STS)** - Spring Boot 支持
2. **Lombok** - https://projectlombok.org/download
3. **MyBatipse** - MyBatis 支持

### Maven 配置

**`~/.m2/settings.xml` 配置：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
    
    <!-- 本地仓库路径 -->
    <localRepository>~/.m2/repository</localRepository>
    
    <!-- 镜像配置（加速依赖下载） -->
    <mirrors>
        <mirror>
            <id>aliyun-maven</id>
            <name>Aliyun Maven Mirror</name>
            <url>https://maven.aliyun.com/repository/public</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>
    
    <!-- Profile 配置 -->
    <profiles>
        <profile>
            <id>jdk-1.8</id>
            <activation>
                <activeByDefault>true</activeByDefault>
                <jdk>1.8</jdk>
            </activation>
            <properties>
                <maven.compiler.source>1.8</maven.compiler.source>
                <maven.compiler.target>1.8</maven.compiler.target>
                <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
            </properties>
        </profile>
    </profiles>
</settings>
```

### Git 配置

```bash
# 配置用户信息
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# 配置换行符（Windows）
git config --global core.autocrlf true

# 配置换行符（Linux/Mac）
git config --global core.autocrlf input

# 配置默认编辑器
git config --global core.editor vim

# 配置 .gitignore
git config --global core.excludesfile ~/.gitignore_global
```

**`.gitignore_global` 内容：**

```
# IDE
.idea/
*.iml
.eclipse/
.settings/
.classpath
.project

# Build
target/
build/
out/
*.class

# Logs
logs/
*.log

# OS
.DS_Store
Thumbs.db

# Other
*.swp
*~
```

---

## 项目结构说明

### 整体架构

```
raisin-platform/                    # 父项目根目录
│
├── raisin-commons/                 # 公共组件和自定义 Starters
│   ├── raisin-common-core/         # 核心工具类
│   │   ├── constant/               # 常量定义
│   │   ├── exception/              # 异常定义
│   │   ├── model/                  # 公共实体类
│   │   ├── utils/                  # 工具类
│   │   └── annotation/             # 自定义注解
│   │
│   ├── raisin-common-spring-boot-starter/      # 通用 Starter
│   ├── raisin-db-spring-boot-starter/          # 数据库 Starter
│   ├── raisin-redis-spring-boot-starter/       # Redis Starter
│   ├── raisin-log-spring-boot-starter/         # 日志 Starter
│   ├── raisin-swagger2-spring-boot-starter/    # Swagger Starter
│   ├── raisin-ribbon-spring-boot-starter/      # Ribbon Starter
│   ├── raisin-auth-client-spring-boot-starter/ # 认证客户端 Starter
│   └── raisin-sentinel-spring-boot-starter/    # Sentinel Starter
│
├── raisin-uaa/                     # 认证授权中心 [8000]
│   ├── src/main/java/              # Java 源码
│   │   └── com/raisin/oauth/
│   │       ├── config/             # 配置类
│   │       ├── controller/         # 控制器
│   │       ├── service/            # 服务层
│   │       ├── mapper/             # 数据访问层
│   │       ├── model/              # 实体类
│   │       └── UAAServerApplication.java  # 启动类
│   └── src/main/resources/         # 资源文件
│       ├── application.yml         # 应用配置
│       ├── bootstrap.yml           # 启动配置
│       └── mapper/                 # MyBatis XML
│
├── raisin-gateway/                 # API 网关层
│   ├── sc-gateway/                 # Spring Cloud Gateway [9900]
│   └── zuul-gateway/               # Zuul Gateway [9900]
│
├── raisin-business/                # 业务服务层
│   ├── user-center/                # 用户中心 [7000]
│   ├── file-center/                # 文件中心 [5000]
│   ├── code-generator/             # 代码生成器 [7300]
│   └── search-center/              # 搜索中心
│       ├── search-client/          # 搜索客户端
│       └── search-server/          # 搜索服务端 [7100]
│
├── raisin-monitor/                 # 监控服务
│   ├── sc-admin/                   # Spring Boot Admin [6500]
│   └── log-center/                 # 日志中心 [6200]
│
├── raisin-job/                     # 任务调度
│   ├── job-admin/                  # 任务管理 [8081]
│   ├── job-core/                   # 任务核心
│   └── job-executor-samples/       # 执行器示例 [8082]
│
├── raisin-transaction/             # 分布式事务
│   └── txlcn-tm/                   # TX-LCN 事务管理器 [7970]
│
├── raisin-web/                     # 前端管理系统 [8066]
│
├── raisin-doc/                     # 文档目录
│   ├── sql/                        # SQL 脚本
│   └── 规范/                       # 开发规范
│
└── raisin-demo/                    # 示例代码
    ├── txlcn-demo/                 # 分布式事务示例
    ├── seata-demo/                 # Seata 示例
    ├── sharding-jdbc-demo/         # 分库分表示例
    └── rocketmq-demo/              # 消息队列示例
```

### 单个微服务标准结构

```
service-name/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/raisin/xxx/
│   │   │       ├── XxxApplication.java      # 启动类
│   │   │       ├── config/                  # 配置类
│   │   │       │   ├── SwaggerConfig.java   # Swagger 配置
│   │   │       │   ├── WebMvcConfig.java    # Web 配置
│   │   │       │   └── SecurityConfig.java  # 安全配置
│   │   │       ├── controller/              # 控制器
│   │   │       │   └── UserController.java
│   │   │       ├── service/                 # 服务接口
│   │   │       │   ├── IUserService.java
│   │   │       │   └── impl/                # 服务实现
│   │   │       │       └── UserServiceImpl.java
│   │   │       ├── mapper/                  # 数据访问层
│   │   │       │   └── UserMapper.java
│   │   │       ├── model/                   # 实体类
│   │   │       │   ├── entity/              # 数据库实体
│   │   │       │   │   └── User.java
│   │   │       │   ├── dto/                 # 数据传输对象
│   │   │       │   │   └── UserDTO.java
│   │   │       │   └── vo/                  # 视图对象
│   │   │       │       └── UserVO.java
│   │   │       ├── exception/               # 异常处理
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── utils/                   # 工具类
│   │   │       ├── constant/                # 常量
│   │   │       └── aspect/                  # 切面
│   │   └── resources/
│   │       ├── application.yml              # 应用配置
│   │       ├── bootstrap.yml                # 启动配置
│   │       ├── mapper/                      # MyBatis XML
│   │       │   └── UserMapper.xml
│   │       ├── static/                      # 静态资源
│   │       └── templates/                   # 模板文件
│   └── test/                                # 测试代码
│       └── java/
│           └── com/raisin/xxx/
│               └── XxxApplicationTests.java
└── pom.xml                                  # Maven 配置
```

---

## 核心组件使用

### 1. 数据库操作 (MyBatis-Plus)

#### 实体类定义

```java
package com.raisin.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("sys_user")
public class User {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 手机号
     */
    private String mobile;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}
```

#### Mapper 接口

```java
package com.raisin.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.raisin.user.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 * 继承 BaseMapper 获得通用 CRUD 方法
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 自定义查询方法
     * 对应 mapper/UserMapper.xml 中的 SQL
     */
    User findByUsername(String username);
}
```

#### Mapper XML

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.raisin.user.mapper.UserMapper">

    <!-- 结果映射 -->
    <resultMap id="BaseResultMap" type="com.raisin.user.model.entity.User">
        <id column="id" property="id" />
        <result column="username" property="username" />
        <result column="password" property="password" />
        <result column="nickname" property="nickname" />
        <result column="mobile" property="mobile" />
        <result column="email" property="email" />
        <result column="status" property="status" />
        <result column="create_time" property="createTime" />
        <result column="update_time" property="updateTime" />
    </resultMap>

    <!-- 根据用户名查询 -->
    <select id="findByUsername" resultMap="BaseResultMap">
        SELECT * FROM sys_user WHERE username = #{username}
    </select>

</mapper>
```

#### Service 层

```java
package com.raisin.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.raisin.user.model.entity.User;

/**
 * 用户服务接口
 */
public interface IUserService extends IService<User> {
    
    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);
    
    /**
     * 注册用户
     */
    User register(User user);
}
```

```java
package com.raisin.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.raisin.user.mapper.UserMapper;
import com.raisin.user.model.entity.User;
import com.raisin.user.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> 
        implements IUserService {
    
    @Resource
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User getByUsername(String username) {
        return baseMapper.findByUsername(username);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(User user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 保存用户
        this.save(user);
        return user;
    }
}
```

#### Controller 层

```java
package com.raisin.user.controller;

import com.raisin.common.model.Result;
import com.raisin.user.model.entity.User;
import com.raisin.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/users")
@Api(tags = "用户管理")
public class UserController {
    
    @Resource
    private IUserService userService;
    
    @ApiOperation("根据用户名查询用户")
    @GetMapping("/{username}")
    public Result<User> getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return Result.succeed(user);
    }
    
    @ApiOperation("注册用户")
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return Result.succeed(registeredUser);
    }
}
```

### 2. Redis 操作

#### 使用 RedisTemplate

```java
package com.raisin.user.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class UserCacheService {
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String USER_CACHE_KEY = "user:";
    
    /**
     * 缓存用户信息
     */
    public void cacheUser(Long userId, User user) {
        String key = USER_CACHE_KEY + userId;
        redisTemplate.opsForValue().set(key, user, 1, TimeUnit.HOURS);
    }
    
    /**
     * 获取缓存的用户信息
     */
    public User getCachedUser(Long userId) {
        String key = USER_CACHE_KEY + userId;
        return (User) redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 删除缓存
     */
    public void deleteCache(Long userId) {
        String key = USER_CACHE_KEY + userId;
        redisTemplate.delete(key);
    }
}
```

#### 使用 Redisson

```java
package com.raisin.user.service.impl;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class DistributedLockService {
    
    @Resource
    private RedissonClient redissonClient;
    
    /**
     * 分布式锁示例
     */
    public void processWithLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 尝试获取锁，最多等待 10 秒，锁自动释放时间 30 秒
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                try {
                    // 执行业务逻辑
                    doSomething();
                } finally {
                    // 释放锁
                    lock.unlock();
                }
            } else {
                // 获取锁失败
                throw new RuntimeException("获取锁失败");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取锁被中断", e);
        }
    }
    
    private void doSomething() {
        // 业务逻辑
    }
}
```

### 3. 服务调用 (Feign)

#### 定义 Feign 客户端

```java
package com.raisin.order.feign;

import com.raisin.common.model.Result;
import com.raisin.user.model.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务 Feign 客户端
 */
@FeignClient(
    name = "user-center",              // 服务名
    path = "/users",                   // 服务路径
    fallback = UserServiceFallback.class  // 降级处理
)
public interface UserService {
    
    @GetMapping("/{username}")
    Result<User> getByUsername(@PathVariable("username") String username);
}
```

#### 降级处理

```java
package com.raisin.order.feign;

import com.raisin.common.model.Result;
import com.raisin.user.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceFallback implements UserService {
    
    @Override
    public Result<User> getByUsername(String username) {
        log.error("调用用户服务失败，username: {}", username);
        return Result.failed("用户服务暂不可用");
    }
}
```

#### 使用 Feign 客户端

```java
package com.raisin.order.service.impl;

import com.raisin.order.feign.UserService;
import com.raisin.user.model.entity.User;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class OrderServiceImpl {
    
    @Resource
    private UserService userService;
    
    public void createOrder(String username) {
        // 调用用户服务
        Result<User> result = userService.getByUsername(username);
        if (result.isSuccess()) {
            User user = result.getData();
            // 处理订单逻辑
        }
    }
}
```

### 4. 统一异常处理

```java
package com.raisin.user.exception;

import com.raisin.common.exception.BusinessException;
import com.raisin.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.failed(e.getMessage());
    }
    
    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        log.error("参数校验异常: {}", message);
        return Result.failed(message);
    }
    
    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.failed("系统繁忙，请稍后再试");
    }
}
```

### 5. 参数校验

```java
package com.raisin.user.model.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class UserRegisterDTO {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;
    
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

```java
@PostMapping("/register")
public Result<User> register(@Valid @RequestBody UserRegisterDTO dto) {
    // @Valid 会自动进行参数校验
    User user = userService.register(dto);
    return Result.succeed(user);
}
```

---

## 开发新服务

### 1. 创建服务模块

#### 使用 Maven 创建模块

```bash
cd raisin-business
mvn archetype:generate \
  -DgroupId=com.raisin \
  -DartifactId=product-center \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

#### 配置 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>raisin-business</artifactId>
        <groupId>com.raisin</groupId>
        <version>3.1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>product-center</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 自定义 Starters -->
        <dependency>
            <groupId>com.raisin</groupId>
            <artifactId>raisin-common-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.raisin</groupId>
            <artifactId>raisin-db-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.raisin</groupId>
            <artifactId>raisin-redis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.raisin</groupId>
            <artifactId>raisin-log-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.raisin</groupId>
            <artifactId>raisin-swagger2-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.raisin</groupId>
            <artifactId>raisin-auth-client-spring-boot-starter</artifactId>
        </dependency>

        <!-- MySQL 驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2. 创建启动类

```java
package com.raisin.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 商品中心启动类
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ProductCenterApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductCenterApplication.class, args);
    }
}
```

### 3. 创建配置文件

**bootstrap.yml：**

```yaml
server:
  port: 7200

spring:
  application:
    name: product-center
```

**application.yml：**

```yaml
spring:
  datasource:
    url: jdbc:mysql://${zlt.datasource.ip:localhost}:3306/product-center?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai
    username: ${zlt.datasource.username:root}
    password: ${zlt.datasource.password:root}
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  mapper-locations: classpath:/mapper/*Mapper.xml
  type-aliases-package: com.raisin.product.model
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

zlt:
  swagger:
    enabled: true
    title: 商品中心
    description: 商品中心接口文档
    version: 1.0
    base-package: com.raisin.product.controller
```

### 4. 创建数据库表

```sql
CREATE DATABASE IF NOT EXISTS `product-center` DEFAULT CHARACTER SET utf8mb4;

USE `product-center`;

CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存',
  `description` text COMMENT '商品描述',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记：0-未删除，1-已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';
```

### 5. 生成代码

使用项目自带的代码生成器快速生成基础代码：

访问：http://localhost:7300（启动 code-generator 服务后）

或手动创建 Entity、Mapper、Service、Controller。

### 6. 添加到父 pom

编辑 `raisin-business/pom.xml`，添加模块：

```xml
<modules>
    <module>user-center</module>
    <module>file-center</module>
    <module>code-generator</module>
    <module>search-center</module>
    <module>product-center</module>  <!-- 新增 -->
</modules>
```

---

## 开发新接口

### 1. RESTful API 设计规范

```
GET    /api/users          # 查询用户列表
GET    /api/users/{id}     # 查询指定用户
POST   /api/users          # 创建用户
PUT    /api/users/{id}     # 更新用户
DELETE /api/users/{id}     # 删除用户
```

### 2. 完整示例

#### DTO 定义

```java
package com.raisin.product.model.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ProductCreateDTO {
    
    @NotBlank(message = "商品名称不能为空")
    private String name;
    
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    private BigDecimal price;
    
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;
    
    private String description;
}
```

#### VO 定义

```java
package com.raisin.product.model.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductVO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Integer status;
    private Date createTime;
}
```

#### Service 实现

```java
package com.raisin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.raisin.common.exception.BusinessException;
import com.raisin.product.mapper.ProductMapper;
import com.raisin.product.model.dto.ProductCreateDTO;
import com.raisin.product.model.entity.Product;
import com.raisin.product.model.vo.ProductVO;
import com.raisin.product.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> 
        implements IProductService {
    
    @Override
    public Page<ProductVO> listProducts(int page, int size, String keyword) {
        Page<Product> productPage = new Page<>(page, size);
        
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getName, keyword);
        }
        wrapper.eq(Product::getDeleted, 0);
        
        Page<Product> result = this.page(productPage, wrapper);
        
        // 转换为 VO
        Page<ProductVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream()
            .map(this::convertToVO)
            .collect(Collectors.toList()));
        
        return voPage;
    }
    
    @Override
    public ProductVO getProductById(Long id) {
        Product product = this.getById(id);
        if (product == null || product.getDeleted() == 1) {
            throw new BusinessException("商品不存在");
        }
        return convertToVO(product);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductVO createProduct(ProductCreateDTO dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        product.setStatus(1);
        
        this.save(product);
        return convertToVO(product);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        Product product = this.getById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        // 逻辑删除
        product.setDeleted(1);
        this.updateById(product);
    }
    
    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        return vo;
    }
}
```

#### Controller 实现

```java
package com.raisin.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.raisin.common.model.Result;
import com.raisin.product.model.dto.ProductCreateDTO;
import com.raisin.product.model.vo.ProductVO;
import com.raisin.product.service.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/products")
@Api(tags = "商品管理")
public class ProductController {
    
    @Resource
    private IProductService productService;
    
    @ApiOperation("查询商品列表")
    @GetMapping("/list")
    public Result<Page<ProductVO>> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") int size,
            @ApiParam("关键词") @RequestParam(required = false) String keyword) {
        Page<ProductVO> result = productService.listProducts(page, size, keyword);
        return Result.succeed(result);
    }
    
    @ApiOperation("查询商品详情")
    @GetMapping("/{id}")
    public Result<ProductVO> get(@ApiParam("商品ID") @PathVariable Long id) {
        ProductVO product = productService.getProductById(id);
        return Result.succeed(product);
    }
    
    @ApiOperation("创建商品")
    @PostMapping
    public Result<ProductVO> create(@Valid @RequestBody ProductCreateDTO dto) {
        ProductVO product = productService.createProduct(dto);
        return Result.succeed(product);
    }
    
    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@ApiParam("商品ID") @PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.succeed();
    }
}
```

---

## 使用自定义Starter

### 1. 数据库 Starter (raisin-db-spring-boot-starter)

**功能：**
- MyBatis-Plus 自动配置
- 分页插件
- 性能分析插件
- Druid 数据源

**使用方式：**

```xml
<dependency>
    <groupId>com.raisin</groupId>
    <artifactId>raisin-db-spring-boot-starter</artifactId>
</dependency>
```

无需额外配置，自动装配。

### 2. Redis Starter (raisin-redis-spring-boot-starter)

**功能：**
- RedisTemplate 配置
- Redisson 客户端
- 序列化配置

**使用方式：**

```xml
<dependency>
    <groupId>com.raisin</groupId>
    <artifactId>raisin-redis-spring-boot-starter</artifactId>
</dependency>
```

**配置：**

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password:
    database: 0
```

### 3. 日志 Starter (raisin-log-spring-boot-starter)

**功能：**
- 统一日志格式
- 日志切面
- ELK 集成

**使用方式：**

```xml
<dependency>
    <groupId>com.raisin</groupId>
    <artifactId>raisin-log-spring-boot-starter</artifactId>
</dependency>
```

### 4. Swagger Starter (raisin-swagger2-spring-boot-starter)

**功能：**
- Swagger2 自动配置
- 多种 UI 支持

**使用方式：**

```xml
<dependency>
    <groupId>com.raisin</groupId>
    <artifactId>raisin-swagger2-spring-boot-starter</artifactId>
</dependency>
```

**配置：**

```yaml
zlt:
  swagger:
    enabled: true
    title: 商品中心
    description: 商品中心接口文档
    version: 1.0
    base-package: com.raisin.product.controller
```

### 5. 认证客户端 Starter (raisin-auth-client-spring-boot-starter)

**功能：**
- OAuth2 资源服务器配置
- JWT Token 解析
- 权限拦截

**使用方式：**

```xml
<dependency>
    <groupId>com.raisin</groupId>
    <artifactId>raisin-auth-client-spring-boot-starter</artifactId>
</dependency>
```

**配置：**

```yaml
zlt:
  security:
    oauth2:
      resource:
        user-info-uri: http://localhost:8000/api-uaa/users/current
```

### 6. Sentinel Starter (raisin-sentinel-spring-boot-starter)

**功能：**
- 流量控制
- 熔断降级
- 系统保护

**使用方式：**

```xml
<dependency>
    <groupId>com.raisin</groupId>
    <artifactId>raisin-sentinel-spring-boot-starter</artifactId>
</dependency>
```

**配置：**

```yaml
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8080
        port: 8719
```

---

## 开发规范

### 1. 代码规范

遵循 **《阿里巴巴 Java 开发手册》**：

#### 命名规范

- **类名**：大驼峰（PascalCase），如 `UserService`
- **方法名**：小驼峰（camelCase），如 `getUserById`
- **常量**：全大写+下划线，如 `MAX_COUNT`
- **包名**：全小写，如 `com.raisin.user`

#### 注释规范

```java
/**
 * 用户服务接口
 *
 * @author raisin
 * @date 2024-02-06
 */
public interface IUserService {
    
    /**
     * 根据用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(Long id);
}
```

#### 日志规范

```java
// 使用 Lombok 的 @Slf4j
@Slf4j
public class UserServiceImpl {
    
    public void deleteUser(Long id) {
        log.info("删除用户，id: {}", id);
        try {
            // 业务逻辑
            log.debug("用户删除成功");
        } catch (Exception e) {
            log.error("删除用户失败，id: {}", id, e);
            throw e;
        }
    }
}
```

### 2. 分层规范

```
Controller → Service → Mapper
     ↓          ↓         ↓
    DTO       Entity     SQL
     ↓
    VO
```

- **Controller**：接收请求，参数校验，调用 Service
- **Service**：业务逻辑处理，事务控制
- **Mapper**：数据访问，SQL 操作

### 3. 异常处理规范

```java
// 业务异常
if (user == null) {
    throw new BusinessException("用户不存在");
}

// 系统异常
try {
    // 操作
} catch (Exception e) {
    log.error("操作失败", e);
    throw new SystemException("系统异常", e);
}
```

### 4. 事务处理规范

```java
@Service
public class UserServiceImpl {
    
    // 方法级事务
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        // 更新用户
        userMapper.updateById(user);
        // 更新其他相关数据
        // 如果抛出任何异常，整体回滚
    }
}
```

### 5. 接口返回规范

统一使用 `Result` 对象：

```java
// 成功
return Result.succeed(data);

// 失败
return Result.failed("错误信息");

// 分页
return Result.succeed(pageData);
```

---

## 调试技巧

### 1. 远程调试

**启动参数：**

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar app.jar
```

**IDEA 配置：**

1. Run → Edit Configurations
2. 点击 + → Remote JVM Debug
3. 设置 Host: localhost, Port: 5005
4. 点击 Debug 启动

### 2. 日志调试

**临时调整日志级别：**

```yaml
logging:
  level:
    com.raisin: DEBUG
    com.raisin.user.mapper: DEBUG
```

**使用 Actuator 动态调整：**

```bash
# 查看日志级别
curl http://localhost:7000/actuator/loggers/com.raisin.user

# 修改日志级别
curl -X POST http://localhost:7000/actuator/loggers/com.raisin.user \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'
```

### 3. SQL 调试

**开启 SQL 打印：**

```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

**或使用 Druid 监控：**

访问：http://localhost:7000/druid/index.html

### 4. Redis 调试

```bash
# 连接 Redis
redis-cli

# 查看所有 key
KEYS *

# 查看某个 key 的值
GET user:1

# 查看 key 的类型
TYPE user:1

# 查看 key 的过期时间
TTL user:1
```

---

## 单元测试

### 1. Service 层测试

```java
package com.raisin.user.service;

import com.raisin.user.model.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    
    @Resource
    private IUserService userService;
    
    @Test
    public void testGetByUsername() {
        User user = userService.getByUsername("admin");
        Assert.assertNotNull(user);
        Assert.assertEquals("admin", user.getUsername());
    }
    
    @Test
    public void testRegister() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setNickname("测试用户");
        
        User result = userService.register(user);
        Assert.assertNotNull(result.getId());
    }
}
```

### 2. Controller 层测试

```java
package com.raisin.user.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    
    @Resource
    private MockMvc mockMvc;
    
    @Test
    public void testGetByUsername() throws Exception {
        mockMvc.perform(get("/users/admin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.data.username").value("admin"));
    }
    
    @Test
    public void testRegister() throws Exception {
        String json = "{\"username\":\"test\",\"password\":\"123456\",\"nickname\":\"测试\"}";
        
        mockMvc.perform(post("/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));
    }
}
```

### 3. Mapper 层测试

```java
package com.raisin.user.mapper;

import com.raisin.user.model.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    
    @Resource
    private UserMapper userMapper;
    
    @Test
    public void testFindByUsername() {
        User user = userMapper.findByUsername("admin");
        Assert.assertNotNull(user);
    }
    
    @Test
    public void testInsert() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        
        int rows = userMapper.insert(user);
        Assert.assertEquals(1, rows);
        Assert.assertNotNull(user.getId());
    }
}
```

---

## 常用命令

### Maven 命令

```bash
# 清理编译
mvn clean compile

# 打包（跳过测试）
mvn clean package -DskipTests

# 安装到本地仓库
mvn clean install -DskipTests

# 运行单个模块
cd raisin-uaa
mvn spring-boot:run

# 运行测试
mvn test

# 查看依赖树
mvn dependency:tree

# 更新依赖
mvn clean install -U
```

### Git 命令

```bash
# 创建功能分支
git checkout -b feature/product-management

# 提交代码
git add .
git commit -m "feat: 添加商品管理功能"

# 推送分支
git push origin feature/product-management

# 合并分支
git checkout master
git merge feature/product-management

# 查看状态
git status

# 查看日志
git log --oneline --graph
```

---

## 下一步

- 📖 查看 [部署指南](DEPLOYMENT.md) 了解生产环境部署
- 🔧 查看 [配置指南](CONFIGURATION.md) 了解高级配置
- 🛠️ 查看 [运维指南](OPERATIONS.md) 了解运维监控

---

**最后更新时间：** 2024-02-06
