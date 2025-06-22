package com.yf.dfms.db.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据库信息-DfmsDbPageVO
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
@Schema(description = "数据库信息CardVO" )
@Data
public class DfmsDbCardVO {

    /**
     * 类型
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String type;

    /**
     * 数量
     */
    @Schema(description = "数量")
    private Integer num;

    /**
     * 容量
     */
    @Schema(description = "容量")
    private String size;


    /**
     * 状态(1-在线；0-离线)
     */
    private Integer status;
}
