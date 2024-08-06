package ${table.packageName}.${table.moduleName}.converter;

import ${table.packageName}.${table.moduleName}.${table.businessName}.model.bo.????????;
import ${table.packageName}.${table.moduleName}.${table.businessName}.model.entity.????????;
import org.mapstruct.Mapper;

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-转换器
*
* @author ${table.functionAuthor}
* @since  ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
@Mapper(componentModel = "spring")
public interface ${table.className}Converter {


}
