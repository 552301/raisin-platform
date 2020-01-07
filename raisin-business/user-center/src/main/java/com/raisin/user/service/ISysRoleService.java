package com.raisin.user.service;

import java.util.Map;

import com.raisin.common.model.PageResult;
import com.raisin.common.model.Result;
import com.raisin.common.model.SysRole;
import com.raisin.common.service.ISuperService;

/**
* @author zlt
 */
public interface ISysRoleService extends ISuperService<SysRole> {
	void saveRole(SysRole sysRole);

	void deleteRole(Long id);

	/**
	 * 角色列表
	 * @param params
	 * @return
	 */
	PageResult<SysRole> findRoles(Map<String, Object> params);

	/**
	 * 新增或更新角色
	 * @param sysRole
	 * @return Result
	 */
	Result saveOrUpdateRole(SysRole sysRole);
}
