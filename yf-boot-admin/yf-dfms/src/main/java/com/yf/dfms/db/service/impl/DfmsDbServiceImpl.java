package com.yf.dfms.db.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.dfms.db.converter.DfmsDbConverter;
import com.yf.dfms.db.mapper.DfmsDbMapper;
import com.yf.dfms.db.model.entity.DfmsDb;
import com.yf.dfms.db.model.form.DfmsDbForm;
import com.yf.dfms.db.model.query.DfmsDbPageQuery;
import com.yf.dfms.db.model.vo.DfmsDbCardVO;
import com.yf.dfms.db.model.vo.DfmsDbOverviewVO;
import com.yf.dfms.db.model.vo.DfmsDbPageVO;
import com.yf.dfms.db.service.IDfmsDbService;
import com.yf.model.dfms.enums.DfmsDbTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据库信息-DfmsDbServiceImpl
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
@Service("dfmsDbService" )
@RequiredArgsConstructor
public class DfmsDbServiceImpl extends ServiceImpl<DfmsDbMapper, DfmsDb> implements IDfmsDbService {

    private final DfmsDbConverter dfmsDbConverter;

    /**
     * 查询数据库信息
     *
     * @param queryParams 分页参数
     * @return 分页数据
     */
    @Override
    public IPage<DfmsDbPageVO> getDfmsDbPage(DfmsDbPageQuery queryParams) {
        // 1. 分页查询数据
        Page<DfmsDb> page = this.getPageData(queryParams);
        // 2. 转换为 vo 后返回
        return dfmsDbConverter.page2pageVO(page);
    }

    /**
     * 获取分页数据
     *
     * @param query 查询参数
     * @return Page
     */
    private Page<DfmsDb> getPageData(DfmsDbPageQuery query) {
        // 1. 查询数据
        return this.lambdaQuery()
                .eq(StringUtils.hasText(query.getName()), DfmsDb::getName, query.getName())
                .eq(StringUtils.hasText(query.getCharset()), DfmsDb::getCharset, query.getCharset())
                .eq(StringUtils.hasText(query.getCollation()), DfmsDb::getCollation, query.getCollation())
                .eq(StringUtils.hasText(query.getSize()), DfmsDb::getSize, query.getSize())
                .eq(query.getTables() != null, DfmsDb::getTables, query.getTables())
                .in(!CollectionUtils.isEmpty(query.getType()), DfmsDb::getType,query.getType())
                .eq(StringUtils.hasText(query.getRetentionPolicy()), DfmsDb::getRetentionPolicy, query.getRetentionPolicy())
                .ge(query.getSequenceNumber() != null, DfmsDb::getSequenceNumber, query.getSequenceNumber())
                .ge(query.getDataPointsNumber() != null, DfmsDb::getDataPointsNumber, query.getDataPointsNumber())
                .eq(StringUtils.hasText(query.getDimension()), DfmsDb::getDimension, query.getDimension())
                .ge(query.getVectorNumber() != null, DfmsDb::getVectorNumber, query.getVectorNumber())
                .in(!CollectionUtils.isEmpty(query.getIndexType()), DfmsDb::getIndexType,query.getIndexType())
                .in(!CollectionUtils.isEmpty(query.getStatus()), DfmsDb::getStatus,query.getStatus())
                .eq(query.getCreateBy() != null, DfmsDb::getCreateBy, query.getCreateBy())
                .between(query.getCreateTimeBegin() != null && query.getCreateTimeEnd() != null, DfmsDb::getCreateTime,
                        query.getCreateTimeBegin(),
                        query.getCreateTimeEnd())
                .between(query.getUpdateByBegin() != null && query.getUpdateByEnd() != null, DfmsDb::getUpdateBy,
                        query.getUpdateByBegin(),
                        query.getUpdateByEnd())
                .between(query.getUpdateTimeBegin() != null && query.getUpdateTimeEnd() != null, DfmsDb::getUpdateTime,
                        query.getUpdateTimeBegin(),
                        query.getUpdateTimeEnd())
                .page(query.toPage());
    }

    /**
     * 获取数据库信息表单数据
     *
     * @param id 数据库信息表主键
     * @return 数据库信息表单数据
     */
    @Override
    public DfmsDbForm getDfmsDbForm(Integer id) {
        // 1. 查询对应数据
        DfmsDb dfmsDb = this.lambdaQuery()
                .eq(DfmsDb::getId, id)
                .one();
        // 2. entity 2 form
        return dfmsDbConverter.entity2form(dfmsDb);
    }

    /**
     * 新增数据库信息
     *
     * @param dfmsDbForm 数据库信息表单
     * @return 主键
     */
    @Override
    public Integer saveDfmsDb(DfmsDbForm dfmsDbForm) {
        // 1. form 转 entity
        DfmsDb dfmsDb = dfmsDbConverter.form2entity(dfmsDbForm);
        // 2. 存储数据
        this.save(dfmsDb);
        // 3. 返回主键
        return dfmsDb.getId();
    }

    /**
     * 删除数据库信息
     *
     * @param ids 主键集合
     * @return 是否删除成功
     */
    @Override
    public boolean deleteDfmsDb(List<Integer> ids) {
        this.lambdaUpdate()
                .in(DfmsDb::getId, ids)
                .remove();
        return true;
    }

    /**
     * 修改数据库信息信息
     *
     * @param id   数据库信息Id
     * @param dfmsDbForm 数据库信息表单数据
     * @return 是否修改成功
     */
    @Override
    public boolean updateDfmsDb(Integer id, DfmsDbForm dfmsDbForm) {
        // 1. form 转 entity
        DfmsDb dfmsDb = dfmsDbConverter.form2entity(dfmsDbForm);
        // 2. 修改数据
        this.lambdaUpdate()
                .eq(DfmsDb::getId, id)
                .update(dfmsDb);
        return true;
    }

    @Override
    public List<DfmsDbCardVO> getOverview() {
        List<DfmsDb> dfmsDbs = this.lambdaQuery().list();
        DfmsDbOverviewVO dfmsDbOverviewVO = new DfmsDbOverviewVO();
        DfmsDbCardVO pg = new DfmsDbCardVO();
        pg.setName(DfmsDbTypeEnum.PGSQL.name());
        pg.setNum(1);
        pg.setStatus(1);
        pg.setType(DfmsDbTypeEnum.PGSQL.name());
        pg.setSize("1TB");

        DfmsDbCardVO timescale = new DfmsDbCardVO();
        timescale.setName(DfmsDbTypeEnum.timescale.name());
        timescale.setNum(1);
        timescale.setSize("1TB");

        DfmsDbCardVO vector = new DfmsDbCardVO();
        vector.setName(DfmsDbTypeEnum.vector.name());
        vector.setNum(1);
        vector.setSize("1TB");

        DfmsDbCardVO geospatial = new DfmsDbCardVO();
        geospatial.setName(DfmsDbTypeEnum.geospatial.name());
        geospatial.setNum(1);
        geospatial.setSize("1TB");

        List<DfmsDbCardVO> overview = new ArrayList<>();
        overview.add(pg);
        overview.add(timescale);
        overview.add(vector);
        overview.add(geospatial);
        dfmsDbOverviewVO.setOverview(overview);

        return overview;
    }
}
