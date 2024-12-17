package com.yf.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.GenTableConverter;
import com.yf.mapper.generate.GenTableMapper;
import com.yf.model.generate.bo.SyncGenTableBo;
import com.yf.model.generate.entity.GenTable;
import com.yf.service.IGenTableFieldsService;
import com.yf.service.IGenTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GenTableIServiceImpl
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */
@Service("genTableService")
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    private final IGenTableFieldsService genTableFieldsService;
    private final GenTableConverter genTableConverter;

    /**
     * 同步数据库
     *
     * @return 是否同步成功
     */
    @Override
    @Transactional
    public boolean syncDatabase() {
        // 1. 同步表
        List<Integer> genTableNames = this.syncTable();
        // 2. 同步对应表字段
        genTableFieldsService.syncFields(genTableNames);
        return true;
    }

    /**
     * 同步数据表到 GenTableName
     *
     * @return 新增后的 GenTableName
     */
    @Transactional
    public List<Integer> syncTable() {
        // 1. 获取数据库所有表
        List<SyncGenTableBo> syncGenTableBos = this.getBaseMapper().getDatabaseTable();
        // 2. 排除当前已有表
        List<GenTable> existingTables = this.lambdaQuery().list();
        Set<String> existingTableNames = existingTables.stream()
                .map(GenTable::getTableName)
                .collect(Collectors.toSet());

        // 3. 过滤出不存在于 existingTables 的表
        List<GenTable> newTables = syncGenTableBos.stream()
                .filter(syncGenTableBo -> !existingTableNames.contains(syncGenTableBo.getTableName()))
                .map(syncGenTableBo -> {
                    // 3.1 转换出 GenTable
                    GenTable genTable = genTableConverter.syncBo2entity(syncGenTableBo);
                    // 3.2 填充必要字段
                    String tableName = genTable.getTableName();
                    String tableComment = genTable.getTableComment();
                    String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName, '_'));

                    genTable.setClassName(className);
                    genTable.setFunctionNotes(tableComment);
                    return genTable;
                }).toList();

        // 4. 存储新表
        this.saveBatch(newTables);
        // 5. 返回表名
        return newTables.stream().map(GenTable::getId).toList();
    }
}

