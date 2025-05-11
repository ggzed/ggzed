<#-- @formatter:off -->
package com.${table.packageName}.${table.moduleName}.${table.businessName}.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.${table.packageName}.${table.moduleName}.${table.businessName}.model.entity.${table.className};

/**
 * ${table.tableComment}-${table.className}Mapper
 *
 * @author: ${table.functionAuthor}
 * @since : ${.now}
 */
@Mapper
public interface ${table.className}Mapper extends BaseMapper<${table.className}> {

}
<#-- @formatter:on -->