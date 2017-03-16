package com.bupt.wangfu.snmp.info;

/**
 * @ Created by HanB on 2016/7/28 0028.
 */
public class HostInfo {
    private String sysInfo; // 系统描述
    private String sysName; // 机器名
    private String sysMemory; // 系统内存
    private String cpuRate; // CPU 利用率
    private String ifNum; // 接口数量

    public HostInfo(String sysInfo, String sysName, String sysMemory, String cpuRate, String ifNum) {
        this.sysInfo = sysInfo;
        this.sysName = sysName;
        this.sysMemory = sysMemory;
        this.cpuRate = cpuRate;
        this.ifNum = ifNum;
    }

    public String getSysInfo() {
        return sysInfo;
    }

    public void setSysInfo(String sysInfo) {
        this.sysInfo = sysInfo;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getIfNum() {
        return ifNum;
    }

    public void setIfNum(String ifNum) {
        this.ifNum = ifNum;
    }

    public String getSysMemory() {
        return sysMemory;
    }

    public void setSysMemory(String sysMemory) {
        this.sysMemory = sysMemory;
    }

    public String getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(String cpuRate) {
        this.cpuRate = cpuRate;
    }
}
