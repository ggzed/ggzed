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

    @Scheduled(cron = "0 0 0 * * MON") // 每周一凌晨执行
    public void backupAndClearDatabaseTable() {
        log.info("1. 备份日志信息");
        log.info("2. 删除数据库日志信息");
    }
}