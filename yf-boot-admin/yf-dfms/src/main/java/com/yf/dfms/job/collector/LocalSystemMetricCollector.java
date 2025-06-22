//package com.yf.dfms.job.collector;
//
//import com.dfm.job.MetricCollector;
//import com.dfm.model.bo.SystemDatasBO;
//import com.dfm.repository.SystemDatasRepository;
//import com.dfm.util.SystemUtil;
//import com.yf.dfms.job.MetricCollector;
//import com.yf.dfms.job.util.SystemUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import oshi.SystemInfo;
//import oshi.hardware.*;
//import oshi.software.os.OperatingSystem;
//
//import java.time.LocalDateTime;
//
////实现获取系统性能数据的业务逻辑
//@Component
//public class LocalSystemMetricCollector implements MetricCollector {
//
//    @Autowired
//    private SystemDatasRepository systemDataRepository;
//
//    private final SystemInfo systemInfo = new SystemInfo();
//
//    @Override
//    public void execute() {
//        HardwareAbstractionLayer hardware = systemInfo.getHardware();
//        OperatingSystem os = systemInfo.getOperatingSystem();
//
//        int cpuUsage = (int) SystemUtil.getCpuUsage(hardware.getProcessor());
//        int memoryUsage = SystemUtil.getMemoryUsage(hardware.getMemory());
//        int diskUsage = SystemUtil.getDiskUsage(os);         // 实现见下
//        int networkUsage = SystemUtil.getNetworkUsage(hardware); // 实现见下
//
//        SystemDatasBO data = new SystemDatasBO(LocalDateTime.now(), cpuUsage, memoryUsage, diskUsage, networkUsage);
//        systemDataRepository.save(data);
//    }
//
//
//}
//
//
//
//
