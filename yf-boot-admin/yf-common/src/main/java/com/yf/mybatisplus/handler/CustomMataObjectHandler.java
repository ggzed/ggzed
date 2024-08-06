package com.yf.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yf.utils.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义MataObjectHandler
 *
 * @author YiFei
 * @since 2024/4/19 10:50
 */
@Component
public class CustomMataObjectHandler implements MetaObjectHandler {

    /**
     * 新增时填充创建时间 、 用户Id （可自定义插入用户名还是Id）
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = SecurityUtil.getUserId();
        LocalDateTime now = LocalDateTime.now();
        // 1. 填充修改人的用户名
        if (userId != null) {
            this.strictInsertFill(metaObject, "createBy", Long.class, userId);
            this.strictUpdateFill(metaObject, "updateBy", Long.class, userId);
        }
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
    }


    /**
     * 更新时填充更新时间 、 用户Id （可自定义插入用户名还是Id）
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = SecurityUtil.getUserId();
        LocalDateTime now = LocalDateTime.now();
        // 1. 填充修改人的用户名
        if (userId != null) {
            this.strictUpdateFill(metaObject, "updateBy", Long.class, userId);
        }
        // 2. 填充
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
    }
}
