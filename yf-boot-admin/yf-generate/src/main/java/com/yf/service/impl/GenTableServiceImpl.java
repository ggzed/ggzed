package com.yf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.GenTableConverter;
import com.yf.mapper.generate.GenTableMapper;
import com.yf.mapper.system.SysMenuMapper;
import com.yf.model.generate.bo.DBTableBO;
import com.yf.model.generate.bo.DBTableInfoBO;
import com.yf.model.generate.entity.GenTable;
import com.yf.model.generate.form.GenTableForm;
import com.yf.model.generate.query.DBTablePageQuery;
import com.yf.model.system.entity.SysMenu;
import com.yf.model.vo.DBTableVO;
import com.yf.service.IGenTableFieldsService;
import com.yf.service.IGenTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.yf.constants.GenTableDefaultConstants.GEN_FILENAME;

/**
 * GenTableIServiceImpl
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */
@Service("genTableService")
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    private final SysMenuMapper menuMapper;
    private final GenTableConverter genTableConverter;
    private final IGenTableFieldsService genTableFieldsService;

    /**
     * 查询数据库所有可导入表
     *
     * @param queryParams 查询参数
     * @return IPage<DBTableVO> 分页展示内容
     */
    @Override
    public IPage<DBTableVO> getDBTablePage(DBTablePageQuery queryParams) {
        IPage<DBTableBO> dbTableBOIPage = this.getBaseMapper()
                .getDBTablePage(queryParams.getTableName(), queryParams.toPage());
        return genTableConverter.pageBO2VO(dbTableBOIPage);
    }

    /**
     * 查询数据库表信息
     *
     * @param dbTableName 表名
     * @return DBTableInfoBO 表信息
     */
    @Override
    public DBTableInfoBO getDBTableInfo(String dbTableName) {
        DBTableInfoBO dbTableInfoBO = this.getBaseMapper().getDBTableByName(dbTableName);
        // 如果数据为空，或者名字不匹配，返回空
        if (dbTableInfoBO == null || !dbTableName.equals(dbTableInfoBO.getTableName())) {
            return null;
        }
        // 表存在则查询字段
        dbTableInfoBO.setFieldList(genTableFieldsService.getDBFields(dbTableName));
        return dbTableInfoBO;
    }

    /**
     * 修改生成表
     *
     * @param tableId 生成表ID
     * @param form    生成表表单
     * @return 是否修改成功
     */
    @Override
    public boolean updateGenTable(Integer tableId, GenTableForm form) {
        this.lambdaUpdate()
                .eq(GenTable::getId, tableId)
                .update(genTableConverter.form2Entity(form));
        return true;
    }

    /**
     * tableId 是否存在
     *
     * @param tableId 生成表ID
     * @return 是否删除成功
     */
    @Override
    public boolean genTableIsExist(Integer tableId) {
        return this.lambdaQuery().select(GenTable::getId).eq(GenTable::getId, tableId).exists();
    }

    /**
     * 生成文件名
     *
     * @param tableId 生成表ID集合
     * @return 文件名
     */
    @Override
    public String generateFileName(Integer tableId) {
        // 1. 查询 genTable
        GenTable genTable = this.lambdaQuery()
                .select(GenTable::getModuleName, GenTable::getBusinessName)
                .eq(GenTable::getId, tableId).one();
        // 2. 返回文件名
        if (genTable != null) {
            return genTable.getModuleName() + "-" + genTable.getBusinessName();
        } else {
            return GEN_FILENAME;
        }
    }

    /**
     * 获取生成表详情
     *
     * @param tableId 生成表ID
     * @return GenTableForm
     */
    @Override
    public GenTableForm getGenTableForm(Integer tableId) {
        GenTable table = this.lambdaQuery().eq(GenTable::getId, tableId).one();
        SysMenu sysMenu = menuMapper.selectById(table.getMenuId());
        if (sysMenu == null) {
            table.setMenuId(null);
            this.lambdaUpdate()
                    .eq(GenTable::getId, tableId)
                    .set(GenTable::getMenuId, null)
                    .update();
        }
        return genTableConverter.entity2Form(table);
    }
}

