<#assign classNameLower = table.className?substring(0, 1)?lower_case + table.className?substring(1)>
<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.converter;

import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.entity.${table.className};
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.form.${table.className}Form;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.vo.${table.className}PageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * ${table.tableComment}-${table.className}Converter
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
@Mapper(componentModel = "spring" )
public interface ${table.className}Converter {

    Page<${table.className}PageVO> page2pageVO(Page<${table.className}> page);
    
    ${table.className} form2entity(${table.className}Form ${classNameLower}Form);

    ${table.className}Form entity2form(${table.className} ${classNameLower});
}
<#-- @formatter:on -->