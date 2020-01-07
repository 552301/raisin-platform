package com.raisin.search.client.feign;

import com.alibaba.fastjson.JSONObject;
import com.raisin.common.constant.ServiceNameConstants;
import com.raisin.common.model.PageResult;
import com.raisin.search.client.feign.fallback.SearchServiceFallbackFactory;
import com.raisin.search.model.SearchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author zlt
 */
@FeignClient(name = ServiceNameConstants.SEARCH_SERVICE, fallbackFactory = SearchServiceFallbackFactory.class, decode404 = true)
public interface SearchService {
    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     */
    @PostMapping(value = "/search/{indexName}")
    PageResult<JSONObject> strQuery(@PathVariable("indexName") String indexName, @RequestBody SearchDto searchDto);
}
