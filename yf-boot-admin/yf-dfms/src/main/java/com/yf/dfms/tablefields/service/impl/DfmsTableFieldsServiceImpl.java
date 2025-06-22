package com.yf.dfms.tablefields.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.dfms.tablefields.converter.DfmsTableFieldsConverter;
import com.yf.dfms.tablefields.mapper.DfmsTableFieldsMapper;
import com.yf.dfms.tablefields.model.entity.DfmsTableFields;
import com.yf.dfms.tablefields.model.form.DfmsTableFieldsForm;
import com.yf.dfms.tablefields.model.query.DfmsTableFieldsPageQuery;
import com.yf.dfms.tablefields.model.vo.DfmsTableFieldsPageVO;
import com.yf.dfms.tablefields.service.IDfmsTableFieldsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据表字段信息-DfmsTableFieldsServiceImpl
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:51
 */
@Service("dfmsTableFieldsService" )
@RequiredArgsConstructor
public class DfmsTableFieldsServiceImpl extends ServiceImpl<DfmsTableFieldsMapper, DfmsTableFields> implements IDfmsTableFieldsService {

    private final DfmsTableFieldsConverter dfmsTableFieldsConverter;

    /**
     * 查询数据表字段信息
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<DfmsTableFieldsPageVO> getDfmsTableFieldsPage(DfmsTableFieldsPageQuery queryParams) {
        // 1. 分页查询数据
        Page<DfmsTableFields> page = this.getPageData(queryParams);
        // 2. 转换为 vo 后返回
        return dfmsTableFieldsConverter.page2pageVO(page);
    }

    /**
     * 获取分页数据
     *
     * @param query 查询参数
     * @return Page
     */
    private Page<DfmsTableFields> getPageData(DfmsTableFieldsPageQuery query) {
        // 1. 查询数据
        return this.lambdaQuery()
                .eq(StringUtils.hasText(query.getShowName()), DfmsTableFields::getShowName, query.getShowName())
                .eq(StringUtils.hasText(query.getColumnName()), DfmsTableFields::getColumnName, query.getColumnName())
                .in(!CollectionUtils.isEmpty(query.getColumnType()), DfmsTableFields::getColumnType,query.getColumnType())
                .like(StringUtils.hasText(query.getColumnComment()), DfmsTableFields::getColumnComment, query.getColumnComment())
                .in(!CollectionUtils.isEmpty(query.getStatus()), DfmsTableFields::getStatus,query.getStatus())
                .eq(query.getCreateBy() != null, DfmsTableFields::getCreateBy, query.getCreateBy())
                .between(query.getCreateTimeBegin() != null && query.getCreateTimeEnd() != null, DfmsTableFields::getCreateTime,
                        query.getCreateTimeBegin(),
                        query.getCreateTimeEnd())
                .between(query.getUpdateByBegin() != null && query.getUpdateByEnd() != null, DfmsTableFields::getUpdateBy,
                        query.getUpdateByBegin(),
                        query.getUpdateByEnd())
                .between(query.getUpdateTimeBegin() != null && query.getUpdateTimeEnd() != null, DfmsTableFields::getUpdateTime,
                        query.getUpdateTimeBegin(),
                        query.getUpdateTimeEnd())
                .page(query.toPage());
    }

    /**
     * 获取数据表字段信息表单数据
     *
     * @param id 数据表字段信息表主键
     * @return 数据表字段信息表单数据
     */
    @Override
    public DfmsTableFieldsForm getDfmsTableFieldsForm(Integer id) {
        // 1. 查询对应数据
        DfmsTableFields dfmsTableFields = this.lambdaQuery()
                .eq(DfmsTableFields::getId, id)
                .one();
        // 2. entity 2 form
        return dfmsTableFieldsConverter.entity2form(dfmsTableFields);
    }

    /**
     * 新增数据表字段信息
     *
     * @param dfmsTableFieldsForm 数据表字段信息表单
     * @return 主键
     */
    @Override
    public Integer saveDfmsTableFields(DfmsTableFieldsForm dfmsTableFieldsForm) {
        // 1. form 转 entity
        DfmsTableFields dfmsTableFields = dfmsTableFieldsConverter.form2entity(dfmsTableFieldsForm);
        // 2. 存储数据
        this.save(dfmsTableFields);
        // 3. 返回主键
        return dfmsTableFields.getId();
    }

    /**
     * 删除数据表字段信息
     *
     * @param ids 主键集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDfmsTableFields(List<Integer> ids) {
        this.lambdaUpdate()
                .in(DfmsTableFields::getId, ids)
                .remove();
        return true;
    }

    /**
     * 修改数据表字段信息信息
     *
     * @param id   数据表字段信息Id
     * @param dfmsTableFieldsForm 数据表字段信息表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateDfmsTableFields(Integer id, DfmsTableFieldsForm dfmsTableFieldsForm) {
        // 1. form 转 entity
        DfmsTableFields dfmsTableFields = dfmsTableFieldsConverter.form2entity(dfmsTableFieldsForm);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(DfmsTableFields::getId, id)
                .update(dfmsTableFields);
        return true;
    }
}
