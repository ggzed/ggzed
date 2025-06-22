package com.yf.dfms.tableindex.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.dfms.tableindex.converter.DfmsTableIndexConverter;
import com.yf.dfms.tableindex.mapper.DfmsTableIndexMapper;
import com.yf.dfms.tableindex.model.entity.DfmsTableIndex;
import com.yf.dfms.tableindex.model.form.DfmsTableIndexForm;
import com.yf.dfms.tableindex.model.query.DfmsTableIndexPageQuery;
import com.yf.dfms.tableindex.model.vo.DfmsTableIndexPageVO;
import com.yf.dfms.tableindex.service.IDfmsTableIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据表索引信息-DfmsTableIndexServiceImpl
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:54
 */
@Service("dfmsTableIndexService" )
@RequiredArgsConstructor
public class DfmsTableIndexServiceImpl extends ServiceImpl<DfmsTableIndexMapper, DfmsTableIndex> implements IDfmsTableIndexService {

    private final DfmsTableIndexConverter dfmsTableIndexConverter;

    /**
     * 查询数据表索引信息
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<DfmsTableIndexPageVO> getDfmsTableIndexPage(DfmsTableIndexPageQuery queryParams) {
        // 1. 分页查询数据
        Page<DfmsTableIndex> page = this.getPageData(queryParams);
        // 2. 转换为 vo 后返回
        return dfmsTableIndexConverter.page2pageVO(page);
    }

    /**
     * 获取分页数据
     *
     * @param query 查询参数
     * @return Page
     */
    private Page<DfmsTableIndex> getPageData(DfmsTableIndexPageQuery query) {
        // 1. 查询数据
        return this.lambdaQuery()
                .eq(StringUtils.hasText(query.getIndexName()), DfmsTableIndex::getIndexName, query.getIndexName())
                .eq(StringUtils.hasText(query.getColumns()), DfmsTableIndex::getColumns, query.getColumns())
                .in(!CollectionUtils.isEmpty(query.getIndexType()), DfmsTableIndex::getIndexType,query.getIndexType())
                .eq(query.getIsOnly() != null, DfmsTableIndex::getIsOnly, query.getIsOnly())
                .in(!CollectionUtils.isEmpty(query.getStatus()), DfmsTableIndex::getStatus,query.getStatus())
                .eq(query.getCreateBy() != null, DfmsTableIndex::getCreateBy, query.getCreateBy())
                .between(query.getCreateTimeBegin() != null && query.getCreateTimeEnd() != null, DfmsTableIndex::getCreateTime,
                        query.getCreateTimeBegin(),
                        query.getCreateTimeEnd())
                .between(query.getUpdateByBegin() != null && query.getUpdateByEnd() != null, DfmsTableIndex::getUpdateBy,
                        query.getUpdateByBegin(),
                        query.getUpdateByEnd())
                .between(query.getUpdateTimeBegin() != null && query.getUpdateTimeEnd() != null, DfmsTableIndex::getUpdateTime,
                        query.getUpdateTimeBegin(),
                        query.getUpdateTimeEnd())
                .page(query.toPage());
    }

    /**
     * 获取数据表索引信息表单数据
     *
     * @param id 数据表索引信息表主键
     * @return 数据表索引信息表单数据
     */
    @Override
    public DfmsTableIndexForm getDfmsTableIndexForm(Integer id) {
        // 1. 查询对应数据
        DfmsTableIndex dfmsTableIndex = this.lambdaQuery()
                .eq(DfmsTableIndex::getId, id)
                .one();
        // 2. entity 2 form
        return dfmsTableIndexConverter.entity2form(dfmsTableIndex);
    }

    /**
     * 新增数据表索引信息
     *
     * @param dfmsTableIndexForm 数据表索引信息表单
     * @return 主键
     */
    @Override
    public Integer saveDfmsTableIndex(DfmsTableIndexForm dfmsTableIndexForm) {
        // 1. form 转 entity
        DfmsTableIndex dfmsTableIndex = dfmsTableIndexConverter.form2entity(dfmsTableIndexForm);
        // 2. 存储数据
        this.save(dfmsTableIndex);
        // 3. 返回主键
        return dfmsTableIndex.getId();
    }

    /**
     * 删除数据表索引信息
     *
     * @param ids 主键集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDfmsTableIndex(List<Integer> ids) {
        this.lambdaUpdate()
                .in(DfmsTableIndex::getId, ids)
                .remove();
        return true;
    }

    /**
     * 修改数据表索引信息信息
     *
     * @param id   数据表索引信息Id
     * @param dfmsTableIndexForm 数据表索引信息表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateDfmsTableIndex(Integer id, DfmsTableIndexForm dfmsTableIndexForm) {
        // 1. form 转 entity
        DfmsTableIndex dfmsTableIndex = dfmsTableIndexConverter.form2entity(dfmsTableIndexForm);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(DfmsTableIndex::getId, id)
                .update(dfmsTableIndex);
        return true;
    }
}
