package com.yf.model.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结构体
 */
@Slf4j
@Data
@Schema(description = "分页统一响应类")
public class PageResult<T> implements Serializable {
    /**
     * 响应状态码
     */
    @Schema(description = "响应状态码", example = "00000")
    private String code;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private PageVo<T> data;

    /**
     * 响应消息
     */
    @Schema(description = "响应消息")
    private String msg;

    public static <T> PageResult<T> success(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        // 分页都是成功
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        // 写入数据集合，总数据条数
        long total = page.getTotal();
        if (total > Integer.MAX_VALUE) {
            // 记录警告日志
            log.warn("Total count {} exceeds Integer.MAX_VALUE. It will be truncated to {}", total, Integer.MAX_VALUE);
            // 设置警告信息
            result.setMsg(result.getMsg() + " (Total count exceeds maximum limit and has been truncated)");
        }
        int totalAsInt = (int) Math.min(total, Integer.MAX_VALUE); // 转换为int并确保不超过Integer.MAX_VALUE
        result.setData(new PageVo<>(page.getRecords(), totalAsInt));
        return result;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @Schema(description = "分页展示类")
    public static class PageVo<T> {

        @Schema(description = "数据集合")
        private List<T> list;

        @Schema(description = "总数据条数")
        private int total;
    }
}
