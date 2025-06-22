package com.yf.dfms.table.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.dfms.table.converter.DfmsTableConverter;
import com.yf.dfms.table.mapper.DfmsTableMapper;
import com.yf.dfms.table.model.entity.DfmsTable;
import com.yf.dfms.table.model.form.DfmsTableForm;
import com.yf.dfms.table.model.query.DfmsTablePageQuery;
import com.yf.dfms.table.model.vo.DfmsTablePageVO;
import com.yf.dfms.table.service.IDfmsTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据表信息-DfmsTableServiceImpl
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:49
 */
@Service("dfmsTableService" )
@RequiredArgsConstructor
public class DfmsTableServiceImpl extends ServiceImpl<DfmsTableMapper, DfmsTable> implements IDfmsTableService {

    private final DfmsTableConverter dfmsTableConverter;

    /**
     * 查询数据表信息
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<DfmsTablePageVO> getDfmsTablePage(DfmsTablePageQuery queryParams) {
        // 1. 分页查询数据
        Page<DfmsTable> page = this.getPageData(queryParams);
        // 2. 转换为 vo 后返回
        return dfmsTableConverter.page2pageVO(page);
    }

    /**
     * 获取分页数据
     *
     * @param query 查询参数
     * @return Page
     */
    private Page<DfmsTable> getPageData(DfmsTablePageQuery query) {
        // 1. 查询数据
        return this.lambdaQuery()
                .eq(StringUtils.hasText(query.getDbId()), DfmsTable::getDbId, query.getDbId())
                .eq(StringUtils.hasText(query.getTableName()), DfmsTable::getTableName, query.getTableName())
                .like(StringUtils.hasText(query.getTableComment()), DfmsTable::getTableComment, query.getTableComment())
                .eq(StringUtils.hasText(query.getDdl()), DfmsTable::getDdl, query.getDdl())
                .in(!CollectionUtils.isEmpty(query.getStatus()), DfmsTable::getStatus,query.getStatus())
                .eq(query.getCreateBy() != null, DfmsTable::getCreateBy, query.getCreateBy())
                .between(query.getCreateTimeBegin() != null && query.getCreateTimeEnd() != null, DfmsTable::getCreateTime,
                        query.getCreateTimeBegin(),
                        query.getCreateTimeEnd())
                .between(query.getUpdateByBegin() != null && query.getUpdateByEnd() != null, DfmsTable::getUpdateBy,
                        query.getUpdateByBegin(),
                        query.getUpdateByEnd())
                .between(query.getUpdateTimeBegin() != null && query.getUpdateTimeEnd() != null, DfmsTable::getUpdateTime,
                        query.getUpdateTimeBegin(),
                        query.getUpdateTimeEnd())
                .page(query.toPage());
    }

    /**
     * 获取数据表信息表单数据
     *
     * @param id 数据表信息表主键
     * @return 数据表信息表单数据
     */
    @Override
    public DfmsTableForm getDfmsTableForm(Integer id) {
        // 1. 查询对应数据
        DfmsTable dfmsTable = this.lambdaQuery()
                .eq(DfmsTable::getId, id)
                .one();
        // 2. entity 2 form
        return dfmsTableConverter.entity2form(dfmsTable);
    }

    /**
     * 新增数据表信息
     *
     * @param dfmsTableForm 数据表信息表单
     * @return 主键
     */
    @Override
    public Integer saveDfmsTable(DfmsTableForm dfmsTableForm) {
        // 1. form 转 entity
        DfmsTable dfmsTable = dfmsTableConverter.form2entity(dfmsTableForm);
        // 2. 存储数据
        this.save(dfmsTable);
        // 3. 返回主键
        return dfmsTable.getId();
    }

    /**
     * 删除数据表信息
     *
     * @param ids 主键集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDfmsTable(List<Integer> ids) {
        this.lambdaUpdate()
                .in(DfmsTable::getId, ids)
                .remove();
        return true;
    }

    /**
     * 修改数据表信息信息
     *
     * @param id   数据表信息Id
     * @param dfmsTableForm 数据表信息表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateDfmsTable(Integer id, DfmsTableForm dfmsTableForm) {
        // 1. form 转 entity
        DfmsTable dfmsTable = dfmsTableConverter.form2entity(dfmsTableForm);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(DfmsTable::getId, id)
                .update(dfmsTable);
        return true;
    }
}
