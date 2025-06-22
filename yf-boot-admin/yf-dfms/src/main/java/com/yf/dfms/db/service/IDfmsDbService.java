package com.yf.dfms.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.dfms.db.model.entity.DfmsDb;
import com.yf.dfms.db.model.form.DfmsDbForm;
import com.yf.dfms.db.model.query.DfmsDbPageQuery;
import com.yf.dfms.db.model.vo.DfmsDbCardVO;
import com.yf.dfms.db.model.vo.DfmsDbOverviewVO;
import com.yf.dfms.db.model.vo.DfmsDbPageVO;

import java.util.List;

/**
 * 数据库信息-DfmsDbService
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
public interface IDfmsDbService extends IService<DfmsDb> {

    /**
     * 分页查询数据库信息
     *
     * @param queryParams 查询参数
     * @return 数据库信息分页数据
     */
    IPage<DfmsDbPageVO> getDfmsDbPage(DfmsDbPageQuery queryParams);

    /**
     * 删除数据库信息
     *
     * @param ids 数据库信息id集合
     * @return 是否删除成功
     */
    boolean deleteDfmsDb(List<Integer> ids);

    /**
     * 数据库信息表单数据
     *
     * @param id 数据库信息主键
     * @return 数据库信息表单数据
     */
    DfmsDbForm getDfmsDbForm(Integer id);

    /**
     * 保存数据库信息
     *
     * @param dfmsDbForm 数据库信息表单
     * @return 数据库信息主键
     */
    Integer saveDfmsDb(DfmsDbForm dfmsDbForm);

    /**
     * 修改数据库信息
     *
     * @param id   数据库信息主键
     * @param dfmsDbForm 数据库信息表单
     * @return 是否修改成功
     */
    boolean updateDfmsDb(Integer id, DfmsDbForm dfmsDbForm);

    List<DfmsDbCardVO> getOverview();
}
