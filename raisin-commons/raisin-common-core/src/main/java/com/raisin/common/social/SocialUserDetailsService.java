package com.raisin.common.social;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * SocialUserDetailsService 替代接口 (替换已废弃的 Spring Social)
 * 
 * @author raisin
 */
public interface SocialUserDetailsService extends UserDetailsService {
    
    /**
     * 根据用户ID (openId) 加载用户信息
     * @param userId 用户ID (openId)
     * @return 用户详情
     * @throws UsernameNotFoundException 用户未找到异常
     */
    SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException;
}
