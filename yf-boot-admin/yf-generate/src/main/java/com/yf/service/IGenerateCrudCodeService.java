package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.model.common.Option;
import com.yf.model.generate.form.GenTableMenuForm;
import com.yf.model.generate.query.GenCrudTablePageQuery;
import com.yf.model.vo.GenCrudTableVO;
import com.yf.model.vo.PreviewGenCodeTreeVO;

import java.util.List;

/**
 * GenTableService
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */
public interface IGenerateCrudCodeService {
    /**
     * 分页查询目前已经可生成的数据
     *
     * @param queryParams 查询参数
     * @return IPage<GenCrudTableVO> 分页展示内容
     */
    IPage<GenCrudTableVO> getGenCrudTablePage(GenCrudTablePageQuery queryParams);

    /**
     * 导入数据库表到生成表中
     *
     * @param dbTableNames 数据库表名
     * @return tableId
     */
    void importDBToTable(List<String> dbTableNames);

    /**
     * 生成 Crud 代码
     *
     * @param tableId 表ID
     * @return byte[]
     */
    byte[] generateCrudCodeZip(Integer tableId);

    /**
     * 删除生成表
     *
     * @param tableIds 生成表ID集合
     * @return 是否删除成功
     */
    boolean deleteGenTable(List<Integer> tableIds);

    /**
     * 效果图
     *
     * @param tableId 表ID
     * @return 效果图 => html
     */
    String previewGenCrudDisplay(Integer tableId);

    /**
     * 预览生成代码树
     *
     * @param tableId 表ID
     * @return 预览生成代码树
     */
    PreviewGenCodeTreeVO previewGenCrudCode(Integer tableId);

    /**
     * 添加生成表菜单
     *
     * @param tableId 表ID
     * @param form
     * @return 是否添加成功
     */
    boolean addGenTableMenu(Integer tableId, GenTableMenuForm form);

    /**
     * Crud 新增菜单目录下拉列表
     *
     * @return 菜单列表
     */
    List<Option<Integer>> listMenuOptions();
}

