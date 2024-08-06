package com.yf.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GenTable
 *
 * @author YiFei
 * @since 2024-06-14 16:52:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "")
@TableName("gen_table")
public class GenTable implements Serializable {

    @Serial
    private static final long serialVersionUID = 380941061129062827L;

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 作者
     */
    @Schema(description = "作者")
    private String functionAuthor;

    /**
     * 方法注释
     */
    @Schema(description = "方法注释")
    private String functionNotes;

    /**
     * 数据库表名
     */
    @Schema(description = "数据库表名")
    private String tableName;

    /**
     * 数据库表描述
     */
    @Schema(description = "数据库表描述")
    private String tableComment;

    /**
     * 类名(根据数据库表名生成)
     */
    @Schema(description = "类名(根据数据库表名生成)")
    private String className;

    /**
     * 是否记录日志(0:不记录日志,1:记录Aop注解日志)
     */
    @Schema(description = "是否记录日志(0:不记录日志,1:记录Aop注解日志)")
    private Integer logType;

    /**
     * 生成类型(1:单表,2:树形结构)
     */
    @Schema(description = "生成类型(1:单表,2:树形结构)")
    private Integer genType;

    /**
     * Springboot生成类型
     */
    @Schema(description = "Springboot生成类型")
    private String bootType;

    /**
     * 前端生成类型
     */
    @Schema(description = "前端生成类型")
    private String frontType;

    /**
     * 生成包名
     */
    @Schema(description = "生成包名")
    private String packageName;

    /**
     * 模块名（外层包名）
     */
    @Schema(description = "模块名（外层包名）")
    private String moduleName;

    /**
     * 业务名（前端 : 内层包名 , 后端并不使用）
     */
    @Schema(description = "业务名（前端 : 内层包名 , 后端并不使用）")
    private String businessName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}
