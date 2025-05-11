package com.yf.mapper.generate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.model.generate.bo.DBTableBO;
import com.yf.model.generate.bo.DBTableFieldInfoBO;
import com.yf.model.generate.bo.DBTableInfoBO;
import com.yf.model.generate.entity.GenTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * GenTable
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */
public interface GenTableMapper extends BaseMapper<GenTable> {

    IPage<DBTableBO> getDBTablePage(@Param("tableName") String tableName, Page<Object> page);

    DBTableInfoBO getDBTableByName(String dbTableName);
}

