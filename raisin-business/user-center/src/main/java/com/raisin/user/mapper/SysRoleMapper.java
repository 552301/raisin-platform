package com.raisin.user.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.raisin.db.mapper.SuperMapper;

import com.raisin.common.model.SysRole;
import org.apache.ibatis.annotations.Param;

/**
* @author zlt
 * 角色
 */
public interface SysRoleMapper extends SuperMapper<SysRole> {
	List<SysRole> findList(Page<SysRole> page, @Param("r") Map<String, Object> params);
}
