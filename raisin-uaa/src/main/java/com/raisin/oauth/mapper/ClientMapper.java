package com.raisin.oauth.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.raisin.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import com.raisin.oauth.model.Client;

/**
 * @author zlt
 */
public interface ClientMapper extends SuperMapper<Client> {
    List<Client> findList(Page<Client> page, @Param("params") Map<String, Object> params );
}
