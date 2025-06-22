package com.yf.dfms.job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricCollectionScheduler {
    private final List<MetricCollector> tasks;

    @Autowired
    public MetricCollectionScheduler(List<MetricCollector> tasks) {
        this.tasks = tasks;
    }

    // 每 5 秒执行一次
    @Scheduled(cron = "0 0 23 * * ?")
    public void runTasks() {
        for (MetricCollector task : tasks) {
            task.execute();
        }
    }
}








