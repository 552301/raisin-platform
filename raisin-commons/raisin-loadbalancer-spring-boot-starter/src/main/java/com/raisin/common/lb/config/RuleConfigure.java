package com.raisin.common.lb.config;

import com.raisin.common.lb.rule.CustomIsolationRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;

/**
 * @author zlt
 * @date 2019/9/3
 */
public class RuleConfigure {
    @Bean
    public IRule isolationRule() {
        return new CustomIsolationRule();
    }
}
