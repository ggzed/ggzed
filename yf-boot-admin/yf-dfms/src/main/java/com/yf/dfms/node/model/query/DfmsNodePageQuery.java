package com.yf.dfms.node.model.query;

import com.yf.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 节点信息-DfmsNodePageQuery
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:11
 */
@Schema(description = "节点信息分页查询对象" )
@EqualsAndHashCode(callSuper = true)
@Data
public class DfmsNodePageQuery extends BasePageQuery {

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * IP地址
     */
    @Schema(description = "IP地址")
    private String ip;

    /**
     * 端口
     */
    @Schema(description = "端口")
    private String port;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 角色(1-主节点；2-数据节点)
     */
    @Schema(description = "角色(1-主节点；2-数据节点)")
    private Integer role;

    /**
     * 角色状态(1-在线；0-离线)集合
     */
    @Schema(description = "角色状态(1-在线；0-离线)集合")
    private Set<Integer> status;

    /**
     * cpu占用
     */
    @Schema(description = "cpu占用")
    private Long cpu;

    /**
     * 内存占用
     */
    @Schema(description = "内存占用")
    private Long memory;

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
