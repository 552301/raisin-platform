package com.raisin.common.social;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * SocialUserDetails 替代接口 (替换已废弃的 Spring Social)
 * 
 * @author raisin
 */
public interface SocialUserDetails extends UserDetails {
    
    /**
     * 获取用户ID (openId)
     * @return 用户ID
     */
    String getUserId();
}
