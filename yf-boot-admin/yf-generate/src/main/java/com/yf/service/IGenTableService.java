package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.generate.entity.GenTable;

/**
 * GenTableService
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */

public interface IGenTableService extends IService<GenTable> {
    /**
     * 同步数据库
     *
     * @return 是否同步成功
     */
    boolean syncDatabase();
}

