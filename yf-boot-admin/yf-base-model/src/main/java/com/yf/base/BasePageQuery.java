package com.yf.base;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 基础分页请求对象
 */
@Data
@Schema(description = "分页基类")
public class BasePageQuery {

    @Schema(description = "页码", example = "1")
    private int pageNum = 1;

    @Schema(description = "每页记录数", example = "10")
    private int pageSize = 10;

    /**
     * 转换为 mybatis-plus 的 Page 对象
     *
     * @return Page<T>
     */
    public <T> Page<T> toPage() {
        return new Page<>(this.pageNum, this.pageSize);
    }

    /**
     * 转换为 mybatis-plus 的 Page 对象
     *
     * @param orderBy 字段排序
     * @param isAsc   是否倒叙
     * @return Page<T>
     */
    public <T> Page<T> lambdaMpPage(SFunction<T, ?> orderBy, boolean isAsc) {
        String fieldName = LambdaUtils.extract(orderBy).getImplMethodName().substring(3);
        String underscored = StringUtils.join(
                        StringUtils.splitByCharacterTypeCamelCase(fieldName), '_')
                .toLowerCase();
        return mpPage(underscored, isAsc);
    }

    /**
     * 转换为 mybatis-plus 的 Page 对象
     *
     * @param orderBy 字段排序
     * @param isAsc   是否倒叙
     * @return Page<T>
     */
    public <T> Page<T> mpPage(String orderBy, boolean isAsc) {
        // 1. 构建 page 对象
        Page<T> ts = new Page<>(this.pageNum, this.pageSize);
        // 2. 构建 orderItem 对象
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(orderBy);
        orderItem.setAsc(isAsc);
        ts.addOrder(orderItem);
        return ts;
    }

    /**
     * 转换为 mybatis-plus 的 Page 对象
     *
     * @param orderItems mp的OrderItem直接构建到 page 中
     * @return Page<T>
     */
    public <T> Page<T> mpPage(OrderItem... orderItems) {
        // 1. 构建 page 对象
        Page<T> ts = new Page<>(this.pageNum, this.pageSize);
        // 2. 添加 orderItem 对象
        ts.addOrder(orderItems);
        return ts;
    }

    /**
     * 构建 lambda 的 OrderItem 对象
     *
     * @param orderBy 排序字段
     * @param isAsc   是否倒叙
     * @return OrderItem
     */
    public <T> OrderItem lambdaBuilderOrderItem(SFunction<T, ?> orderBy, boolean isAsc) {
        // 1. 解析 orderBy db字段名
        String fieldName = LambdaUtils.extract(orderBy).getImplMethodName().substring(3);
        String dbFieldName = StringUtils.join(
                        StringUtils.splitByCharacterTypeCamelCase(fieldName), '_')
                .toLowerCase();
        // 2. 构建 orderItem 返回
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(dbFieldName);
        orderItem.setAsc(isAsc);
        return orderItem;
    }
}
