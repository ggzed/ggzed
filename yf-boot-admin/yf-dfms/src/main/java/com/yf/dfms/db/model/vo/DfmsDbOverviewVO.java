package com.yf.dfms.db.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 数据库信息-DfmsDbPageVO
 *
 * @author: YiFei
 * @since : 2025年6月22日 下午12:44:42
 */
@Schema(description = "数据库信息PageVO" )
@Data
public class DfmsDbOverviewVO {

    /**
     * 概览
     */
    @Schema(description = "概览")
    private List<DfmsDbCardVO> overview;

}
