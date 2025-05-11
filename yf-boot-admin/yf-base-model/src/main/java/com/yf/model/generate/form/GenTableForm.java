package com.yf.model.generate.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * GenTableForm
 *
 * @author YiFei
 * @since 2025-04-02 16:53:13
 */
@Data
public class GenTableForm {

    @Schema(description = "菜单ID")
    private Integer menuId;

    @Length(max = 100, message = "作者名最大值100")
    @Schema(description = "作者")
    private String functionAuthor;

    @NotBlank(message = "数据库表名不能为空")
    @Size(max = 64, message = "数据库表名最大值64")
    @Schema(description = "数据库表名")
    private String tableName;

    @NotBlank(message = "数据库表描述不能为空")
    @Size(max = 500, message = "数据库表描述最大值500")
    @Schema(description = "数据库表描述")
    private String tableComment;

    @NotBlank(message = "类名不能为空")
    @Size(max = 64, message = "类名最大值64")
    @Schema(description = "类名(根据数据库表名生成)")
    private String className;

    @NotBlank(message = "组件名不能为空")
    @Size(max = 64, message = "组件名最大值64")
    @Schema(description = "前端 component 名(根据数据库表名生成)")
    private String componentName;

    @NotBlank(message = "后端生成类型不能为空")
    @Schema(description = "后端生成类型")
    private String backEndType;

    @NotBlank(message = "前端生成类型不能为空")
    @Schema(description = "前端生成类型")
    private String frontEndType;

    @NotBlank(message = "主包名不能为空")
    @Size(max = 32, message = "模块名最大值32")
    @Schema(description = "主包名")
    private String packageName;

    @NotBlank(message = "模块名不能为空")
    @Size(max = 64, message = "模块名最大值64")
    @Schema(description = "模块名（外层包名）")
    private String moduleName;

    @NotBlank(message = "业务名不能为空")
    @Size(max = 64, message = "业务名最大值64")
    @Schema(description = "业务名（内层包名）")
    private String businessName;

    @Size(max = 500, message = "备注最大值500")
    @Schema(description = "备注")
    private String remark;
}
