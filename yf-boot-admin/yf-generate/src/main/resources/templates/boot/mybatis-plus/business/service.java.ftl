package ${table.packageName}.${table.moduleName}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ${table.packageName}${table.moduleName}${table.businessName}.entity.${table.className};

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-服务层
*
* @author ${table.functionAuthor}
* @since  ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
public interface I${table.className}Service extends IService<${table.className}> {

}

