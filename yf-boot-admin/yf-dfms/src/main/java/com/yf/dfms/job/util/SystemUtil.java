package com.yf.dfms.job.util;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.List;

public class SystemUtil {
    public static final SystemInfo systemInfo = new SystemInfo();

    public static double getCpuUsage(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();

        long userDelta = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long sysDelta = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idleDelta = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalUsed = userDelta + sysDelta;
        long totalDelta = totalUsed + idleDelta;

        return totalDelta > 0 ? (100d * totalUsed) / totalDelta : 0;
    }

    public static int getMemoryUsage(GlobalMemory memory) {
        long total = memory.getTotal();
        long available = memory.getAvailable();
        long used = total - available;
        return (int) ((double) used / total * 100);
    }

    // ✅ 获取磁盘使用率（%）
    public static int getDiskUsage(OperatingSystem os) {
        List<OSFileStore> fileStores = os.getFileSystem().getFileStores();
        long totalSpace = 0;
        long usedSpace = 0;

        for (OSFileStore fs : fileStores) {
            String description = fs.getDescription() == null ? "" : fs.getDescription().toLowerCase();
            if (!description.contains("cd-rom") && !description.contains("loopback")) {
                long total = fs.getTotalSpace();
                long free = fs.getUsableSpace();
                totalSpace += total;
                usedSpace += (total - free);
            }
        }

        if (totalSpace <= 0) {
            return 0;
        }

        return (int) ((double) usedSpace / totalSpace * 100);
    }

    // ✅ 获取网络流量使用情况（KB/s）
    public static int getNetworkUsage(HardwareAbstractionLayer hardware) {
        List<NetworkIF> networks = hardware.getNetworkIFs();
        long totalBytesSent = 0;
        long totalBytesRecv = 0;

        for (NetworkIF net : networks) {
            net.updateAttributes();
            totalBytesSent += net.getBytesSent();
            totalBytesRecv += net.getBytesRecv();
        }

        try {
            Thread.sleep(500); // 等待 0.5 秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long newTotalBytesSent = 0;
        long newTotalBytesRecv = 0;

        for (NetworkIF net : networks) {
            net.updateAttributes();
            newTotalBytesSent += net.getBytesSent();
            newTotalBytesRecv += net.getBytesRecv();
        }

        long diffSent = newTotalBytesSent - totalBytesSent;
        long diffRecv = newTotalBytesRecv - totalBytesRecv;
        long totalDiff = diffSent + diffRecv;

        // 转换为 KB/s，并取整数部分
        return (int) (totalDiff / 1024 / 0.5);
    }
}
