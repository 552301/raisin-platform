package com.raisin.oauth.service;

import com.raisin.common.model.PageResult;
import com.raisin.common.model.Result;
import com.raisin.common.service.ISuperService;
import com.raisin.oauth.model.Client;

import java.util.Map;

/**
 * @author zlt
 */
public interface IClientService extends ISuperService<Client> {
    Result saveClient(Client clientDto);

    /**
     * 查询应用列表
     * @param params
     * @param isPage 是否分页
     */
    PageResult<Client> listClent(Map<String, Object> params, boolean isPage);

    void delClient(long id);
}
