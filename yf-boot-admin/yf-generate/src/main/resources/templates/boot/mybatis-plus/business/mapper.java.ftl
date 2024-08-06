package ${table.packageName}.${table.moduleName}.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${table.packageName}.${table.moduleName}.model.entity.${table.className};

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-持久层
*
* @author ${table.functionAuthor}
* @since  ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
public interface ${table.className}Mapper extends BaseMapper<${table.className}> {

}

