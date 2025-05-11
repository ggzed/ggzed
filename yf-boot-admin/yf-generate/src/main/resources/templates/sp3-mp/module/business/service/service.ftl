<#assign classNameLower = table.className?substring(0, 1)?lower_case + table.className?substring(1)>
<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.entity.${table.className};
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.form.${table.className}Form;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.query.${table.className}PageQuery;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.vo.${table.className}PageVO;

import java.util.List;

/**
 * ${table.tableComment}-${table.className}Service
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
public interface I${table.className}Service extends IService<${table.className}> {

    /**
     * 分页查询${table.tableComment}
     *
     * @param queryParams 查询参数
     * @return ${table.tableComment}分页数据
     */
    IPage<${table.className}PageVO> get${table.className}Page(${table.className}PageQuery queryParams);

    /**
     * 删除${table.tableComment}
     *
     * @param ${mapFields.pk[0].javaTsFieldName}s ${table.tableComment}id集合
     * @return 是否删除成功
     */
    boolean delete${table.className}(List<${mapFields.pk[0].javaType}> ${mapFields.pk[0].javaTsFieldName}s);

    /**
     * ${table.tableComment}表单数据
     *
     * @param ${mapFields.pk[0].javaTsFieldName} ${table.tableComment}主键
     * @return ${table.tableComment}表单数据
     */
    ${table.className}Form get${table.className}Form(${mapFields.pk[0].javaType} ${mapFields.pk[0].javaTsFieldName});

    /**
     * 保存${table.tableComment}
     *
     * @param ${classNameLower}Form ${table.tableComment}表单
     * @return ${table.tableComment}主键
     */
    ${mapFields.pk[0].javaType} save${table.className}(${table.className}Form ${classNameLower}Form);

    /**
     * 修改${table.tableComment}
     *
     * @param ${mapFields.pk[0].javaTsFieldName}   ${table.tableComment}主键
     * @param ${classNameLower}Form ${table.tableComment}表单
     * @return 是否修改成功
     */
    boolean update${table.className}(${mapFields.pk[0].javaType} ${mapFields.pk[0].javaTsFieldName}, ${table.className}Form ${classNameLower}Form);
}
<#-- @formatter:on -->