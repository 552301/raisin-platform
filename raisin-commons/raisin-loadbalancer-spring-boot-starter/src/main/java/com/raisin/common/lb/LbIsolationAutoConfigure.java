package com.raisin.common.lb;

import com.raisin.common.constant.ConfigConstants;
import com.raisin.common.lb.config.RuleConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

/**
 * 负载均衡隔离扩展配置类
 *
 * @author zlt
 * @date 2018/11/17 9:24
 */
@ConditionalOnProperty(value = ConfigConstants.CONFIG_RIBBON_ISOLATION_ENABLED, havingValue = "true")
@LoadBalancerClients(defaultConfiguration = {RuleConfigure.class})
public class LbIsolationAutoConfigure {

}
