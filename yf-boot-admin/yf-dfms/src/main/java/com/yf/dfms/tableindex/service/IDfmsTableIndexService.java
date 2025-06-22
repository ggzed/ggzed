package com.yf.dfms.tableindex.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.dfms.tableindex.model.entity.DfmsTableIndex;
import com.yf.dfms.tableindex.model.form.DfmsTableIndexForm;
import com.yf.dfms.tableindex.model.query.DfmsTableIndexPageQuery;
import com.yf.dfms.tableindex.model.vo.DfmsTableIndexPageVO;

import java.util.List;

/**
 * 数据表索引信息-DfmsTableIndexService
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:54
 */
public interface IDfmsTableIndexService extends IService<DfmsTableIndex> {

    /**
     * 分页查询数据表索引信息
     *
     * @param queryParams 查询参数
     * @return 数据表索引信息分页数据
     */
    IPage<DfmsTableIndexPageVO> getDfmsTableIndexPage(DfmsTableIndexPageQuery queryParams);

    /**
     * 删除数据表索引信息
     *
     * @param ids 数据表索引信息id集合
     * @return 是否删除成功
     */
    boolean deleteDfmsTableIndex(List<Integer> ids);

    /**
     * 数据表索引信息表单数据
     *
     * @param id 数据表索引信息主键
     * @return 数据表索引信息表单数据
     */
    DfmsTableIndexForm getDfmsTableIndexForm(Integer id);

    /**
     * 保存数据表索引信息
     *
     * @param dfmsTableIndexForm 数据表索引信息表单
     * @return 数据表索引信息主键
     */
    Integer saveDfmsTableIndex(DfmsTableIndexForm dfmsTableIndexForm);

    /**
     * 修改数据表索引信息
     *
     * @param id   数据表索引信息主键
     * @param dfmsTableIndexForm 数据表索引信息表单
     * @return 是否修改成功
     */
    boolean updateDfmsTableIndex(Integer id, DfmsTableIndexForm dfmsTableIndexForm);
}
