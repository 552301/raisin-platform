package com.raisin.file.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.raisin.db.mapper.SuperMapper;

import com.raisin.file.model.FileInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 上传存储db
 *
 * @author zlt
 */
public interface FileMapper extends SuperMapper<FileInfo> {
    List<FileInfo> findList(Page<FileInfo> page, @Param("f") Map<String, Object> params);
}
