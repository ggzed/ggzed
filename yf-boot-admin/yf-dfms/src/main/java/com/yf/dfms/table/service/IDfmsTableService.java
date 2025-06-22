package com.yf.dfms.table.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.dfms.table.model.entity.DfmsTable;
import com.yf.dfms.table.model.form.DfmsTableForm;
import com.yf.dfms.table.model.query.DfmsTablePageQuery;
import com.yf.dfms.table.model.vo.DfmsTablePageVO;

import java.util.List;

/**
 * 数据表信息-DfmsTableService
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:49
 */
public interface IDfmsTableService extends IService<DfmsTable> {

    /**
     * 分页查询数据表信息
     *
     * @param queryParams 查询参数
     * @return 数据表信息分页数据
     */
    IPage<DfmsTablePageVO> getDfmsTablePage(DfmsTablePageQuery queryParams);

    /**
     * 删除数据表信息
     *
     * @param ids 数据表信息id集合
     * @return 是否删除成功
     */
    boolean deleteDfmsTable(List<Integer> ids);

    /**
     * 数据表信息表单数据
     *
     * @param id 数据表信息主键
     * @return 数据表信息表单数据
     */
    DfmsTableForm getDfmsTableForm(Integer id);

    /**
     * 保存数据表信息
     *
     * @param dfmsTableForm 数据表信息表单
     * @return 数据表信息主键
     */
    Integer saveDfmsTable(DfmsTableForm dfmsTableForm);

    /**
     * 修改数据表信息
     *
     * @param id   数据表信息主键
     * @param dfmsTableForm 数据表信息表单
     * @return 是否修改成功
     */
    boolean updateDfmsTable(Integer id, DfmsTableForm dfmsTableForm);
}
