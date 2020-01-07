package com.raisin.user.config;

import com.raisin.db.config.DefaultMybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zlt
 * @date 2018/12/10
 */
@Configuration
@MapperScan({"com.raisin.user.mapper*"})
public class MybatisPlusConfig extends DefaultMybatisPlusConfig {
}
