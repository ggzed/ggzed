package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.generate.entity.GenTableFields;

import java.util.List;

/**
 * GenTableFieldsService
 *
 * @author YiFei
 * @since 2024-06-14 16:53:13
 */

public interface IGenTableFieldsService extends IService<GenTableFields> {
    /**
     * 同步表字段
     *
     * @param genTableIds 同步表Id集合
     * @return 是否同步成功
     */
    boolean syncFields(List<Integer> genTableIds);
}

