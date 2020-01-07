package com.raisin.gateway.service.impl;

import com.raisin.common.model.SysMenu;
import com.raisin.gateway.feign.MenuService;
import com.raisin.oauth2.common.service.impl.DefaultPermissionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 请求权限判断service
 *
 * @author zlt
 * @date 2018/10/28
 */
@Slf4j
@Service("permissionService")
public class PermissionServiceImpl extends DefaultPermissionServiceImpl {
    @Resource
    private MenuService menuService;

    @Override
    public List<SysMenu> findMenuByRoleCodes(String roleCodes) {
        return menuService.findByRoleCodes(roleCodes);
    }

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        return hasPermission(authentication, request.getMethod(), request.getRequestURI());
    }
}
