package ${table.packageName}.${table.moduleName}.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${table.packageName}${table.moduleName}${table.businessName}.mapper.${table.className}Mapper;
import ${table.packageName}${table.moduleName}${table.businessName}.model.entity.${table.className};
import ${table.packageName}${table.moduleName}${table.businessName}.service.I${table.className}Service;

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-服务层实现类
*
* @author ${table.functionAuthor}
* @since  ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
@Service("${table.className?uncap_first}Service")
public class ${table.className}ServiceImpl extends ServiceImpl< ${table.className}Mapper, ${table.className} > implements I${table.className}Service {

}

