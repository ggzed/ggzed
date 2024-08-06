package ${table.packageName}.${table.moduleName}.controller;

import ${table.packageName}.${table.moduleName}.${table.businessName}.I${table.className}Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-实体类
*
* @author ${table.functionAuthor}
* @since ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
@Tag(name = "${table.functionNotes}")
@RestController
@RequestMapping("/${table.businessName}")
@RequiredArgsConstructor
public class ${table.className}Controller {

/**
* ${(table.tableComment?string?trim?length > 0) ? string(table.tableComment,table.className)}-Service
*/
private final I${table.className}Service ${table.className?uncap_first}Service;

}