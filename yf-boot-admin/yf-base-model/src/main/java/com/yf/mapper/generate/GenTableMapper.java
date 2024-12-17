package com.yf.mapper.generate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.generate.bo.SyncGenTableBo;
import com.yf.model.generate.entity.GenTable;

import java.util.List;

/**
 * GenTable
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */
public interface GenTableMapper extends BaseMapper<GenTable> {
    /**
     * 获取所有数据库表
     *
     * @return SyncGenTableBo
     */
    List<SyncGenTableBo> getDatabaseTable();
}

