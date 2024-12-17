package com.yf.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(cron = "0 0 0 1 * ?") // 每月 1 日凌晨执行
    public void backupAndClearDatabaseTable() {
        // TODO 后续新增表 back_operation_log , 用户查询 30 天内数据走 operation_log 更以前的数据 , 走 back_operation_log 统计
        log.info("1. 备份日志信息");
        log.info("2. 删除数据库日志信息");
    }
}