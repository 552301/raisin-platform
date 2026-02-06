package com.raisin.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.raisin.common.model.PageResult;
import com.raisin.search.model.SearchDto;

/**
 * @author zlt
 * @date 2019/4/24
 */
public interface ISearchService {
    /**
     * StringQuery通用搜索
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     * @return
     */
    PageResult<JSONObject> strQuery(String indexName, SearchDto searchDto);
}
