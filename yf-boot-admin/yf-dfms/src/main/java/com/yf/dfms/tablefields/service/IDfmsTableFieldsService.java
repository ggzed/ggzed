package com.yf.dfms.tablefields.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.dfms.tablefields.model.entity.DfmsTableFields;
import com.yf.dfms.tablefields.model.form.DfmsTableFieldsForm;
import com.yf.dfms.tablefields.model.query.DfmsTableFieldsPageQuery;
import com.yf.dfms.tablefields.model.vo.DfmsTableFieldsPageVO;

import java.util.List;

/**
 * 数据表字段信息-DfmsTableFieldsService
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:51
 */
public interface IDfmsTableFieldsService extends IService<DfmsTableFields> {

    /**
     * 分页查询数据表字段信息
     *
     * @param queryParams 查询参数
     * @return 数据表字段信息分页数据
     */
    IPage<DfmsTableFieldsPageVO> getDfmsTableFieldsPage(DfmsTableFieldsPageQuery queryParams);

    /**
     * 删除数据表字段信息
     *
     * @param ids 数据表字段信息id集合
     * @return 是否删除成功
     */
    boolean deleteDfmsTableFields(List<Integer> ids);

    /**
     * 数据表字段信息表单数据
     *
     * @param id 数据表字段信息主键
     * @return 数据表字段信息表单数据
     */
    DfmsTableFieldsForm getDfmsTableFieldsForm(Integer id);

    /**
     * 保存数据表字段信息
     *
     * @param dfmsTableFieldsForm 数据表字段信息表单
     * @return 数据表字段信息主键
     */
    Integer saveDfmsTableFields(DfmsTableFieldsForm dfmsTableFieldsForm);

    /**
     * 修改数据表字段信息
     *
     * @param id   数据表字段信息主键
     * @param dfmsTableFieldsForm 数据表字段信息表单
     * @return 是否修改成功
     */
    boolean updateDfmsTableFields(Integer id, DfmsTableFieldsForm dfmsTableFieldsForm);
}
