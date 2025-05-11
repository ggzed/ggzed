package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.model.generate.bo.DBTableInfoBO;
import com.yf.model.generate.entity.GenTable;
import com.yf.model.generate.form.GenTableForm;
import com.yf.model.generate.query.DBTablePageQuery;
import com.yf.model.vo.DBTableVO;

/**
 * GenTableService
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */
public interface IGenTableService extends IService<GenTable> {

    /**
     * 查询数据库所有可导入表
     *
     * @param queryParams 查询参数
     * @return IPage<DBTableVO> 分页展示内容
     */
    IPage<DBTableVO> getDBTablePage(DBTablePageQuery queryParams);

    /**
     * 查询数据库表信息
     *
     * @param dbTableName 表名
     * @return DBTableInfoBO 表信息
     */
    DBTableInfoBO getDBTableInfo(String dbTableName);

    /**
     * 修改生成表
     *
     * @param tableId 生成表ID
     * @param form    生成表表单
     * @return 是否修改成功
     */
    boolean updateGenTable(Integer tableId, GenTableForm form);

    /**
     * tableId 是否存在
     *
     * @param tableId 生成表ID
     * @return 是否删除成功
     */
    boolean genTableIsExist(Integer tableId);

    /**
     * 生成文件名
     *
     * @param tableId 生成表ID集合
     * @return 文件名
     */
    String generateFileName(Integer tableId);

    /**
     * 获取生成表详情
     *
     * @param tableId 生成表ID
     * @return GenTableForm
     */
    GenTableForm getGenTableForm(Integer tableId);
}

