package com.yf.mapper.generate;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.model.generate.bo.SyncGenTableFieldsBo;
import com.yf.model.generate.entity.GenTableFields;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * GenTableFields
 *
 * @author YiFei
 * @since 2024-06-14 16:53:13
 */
public interface GenTableFieldsMapper extends BaseMapper<GenTableFields> {
    /**
     * 查询表对应字段
     *
     * @param genTableId 表Id
     * @return SyncGenTableFieldsBo 表字段集合
     */
    List<SyncGenTableFieldsBo> getDatabaseTableFields(@Param("genTableId") Integer genTableId);
}

