package com.yf.dfms.table.model.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 数据表信息-DfmsTablePageQuery
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:49
 */
@Schema(description = "数据表信息分页查询对象" )
@EqualsAndHashCode(callSuper = true)
@Data
public class DfmsTablePageQuery extends BasePageQuery {

    /**
     * 数据库表名
     */
    @Schema(description = "数据库id")
    private String dbId;

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
     * ddl
     */
    @Schema(description = "ddl")
    private String ddl;

    /**
     * 状态(1-在线；0-离线)集合
     */
    @Schema(description = "状态(1-在线；0-离线)集合")
    private Set<Integer> status;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private Long createBy;

    /**
    * 创建时间Begin
    */
    @Schema(description = "创建时间Start")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeBegin;

    /**
    * 创建时间End
    */
    @Schema(description = "创建时间End")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    /**
    * 修改人IdBegin
    */
    @Schema(description = "修改人IdStart")
    private Long updateByBegin;

    /**
    * 修改人IdEnd
    */
    @Schema(description = "修改人IdEnd")
    private Long updateByEnd;

    /**
    * 修改时间Begin
    */
    @Schema(description = "修改时间Start")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTimeBegin;

    /**
    * 修改时间End
    */
    @Schema(description = "修改时间End")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTimeEnd;

}
