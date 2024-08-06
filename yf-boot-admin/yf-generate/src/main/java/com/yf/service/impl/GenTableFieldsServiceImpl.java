package com.yf.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.converter.GenTableFieldsConverter;
import com.yf.mapper.GenTableFieldsMapper;
import com.yf.model.bo.SyncGenTableFieldsBo;
import com.yf.model.entity.GenTableFields;
import com.yf.model.enums.BackendQueryMethod;
import com.yf.model.enums.FrontendComponentType;
import com.yf.service.IGenTableFieldsService;
import com.yf.utils.GenCodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * GenTableFieldsIServiceImpl
 *
 * @author YiFei
 * @since 2024-06-14 16:53:13
 */
@Service("genTableFieldsService")
@RequiredArgsConstructor
public class GenTableFieldsServiceImpl extends ServiceImpl<GenTableFieldsMapper, GenTableFields> implements IGenTableFieldsService {

    private final GenTableFieldsConverter fieldsConverter;

    /**
     * 同步表字段
     *
     * @param genTableIds 同步表Id集合
     * @return 是否同步成功
     */
    @Override
    @Transactional
    public boolean syncFields(List<Integer> genTableIds) {
        // 注 : 字段超过 20 请考虑设计数据库合理性
        List<GenTableFields> genTableFieldsList = new ArrayList<>(genTableIds.size() * 20);
        for (Integer genTableId : genTableIds) {
            // 1. 查询表对应字段 ， 同步数据表字段
            List<SyncGenTableFieldsBo> tableFieldsBos = this.getBaseMapper().getDatabaseTableFields(genTableId);
            // 2. 构建 tableField
            List<GenTableFields> list = tableFieldsBos.stream().map(tableFieldsBo -> {
                // 2.1 转换 bo to entity
                GenTableFields fields = fieldsConverter.bo2entity(tableFieldsBo);
                // 2.2 填充必要字段
                fields.setTableId(genTableId);
                populateFields(fields);
                return fields;
            }).toList();
            // 3. 添加到集合中统一保存
            genTableFieldsList.addAll(list);
        }
        // 4. 存储所有数据到数据库
        this.saveBatch(genTableFieldsList);
        return true;
    }

    /**
     * 填充字段
     *
     * @param fields fields
     */
    private void populateFields(GenTableFields fields) {
        // 数据库 字段名/ 类型
        String columnName = fields.getColumnName();
        String columnType = fields.getColumnType();
        // java 字段名/ 类型
        String javaField = StrUtil.toCamelCase(columnName, '_');
        String javaType = GenCodeUtils.convertColumnTypeToJavaType(columnType);
        // ts 字段名/ 类型
        String tsField = StrUtil.toCamelCase(columnName, '_');
        String tsType = GenCodeUtils.convertColumnTypeToTsType(columnType);
        // 推断字段进行填充
        Integer isQuery = GenCodeUtils.needsQuery(columnName) ? 1 : 0;      // 是否查询
        Integer isModify = GenCodeUtils.needsModify(columnName) ? 1 : 0;    // 是否修改 / 保存
        Integer isDisplay = GenCodeUtils.needsDisplay(columnName) ? 1 : 0;  // 是否展示
        FrontendComponentType frontendComponentType = GenCodeUtils.frontendComponentType(columnName);
        BackendQueryMethod backendQueryMethod = GenCodeUtils.backendQueryMethod(columnName);

        // 填充字段
        fields.setTsField(tsField);
        fields.setTsType(tsType);
        fields.setJavaField(javaField);
        fields.setJavaType(javaType);
        fields.setIsQuery(isQuery);
        fields.setIsInsertEdit(isModify);
        fields.setIsShow(isDisplay);
        fields.setHtmlType(frontendComponentType.getType());
        fields.setQueryType(backendQueryMethod.getMethod());

    }
}

