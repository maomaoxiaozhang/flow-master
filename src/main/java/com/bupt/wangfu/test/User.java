package com.bupt.wangfu.test;

/**
 * Created by Tang L on 2017/4/17.
 */
public class User {
    private String mac,ipAddr,lastSeen;
    private int serve;
    private long bytesLimit,Threshold,lossRate;

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getServe() {
        return serve;
    }

    public void setServe(int serve) {
        this.serve = serve;
    }

    public long getBytesLimit() {
        return bytesLimit;
    }

    public void setBytesLimit(long bytesLimit) {
        this.bytesLimit = bytesLimit;
    }

    public long getThreshold() {
        return Threshold;
    }

    public void setThreshold(long threshold) {
        Threshold = threshold;
    }

    public long getLossRate() {
        return lossRate;
    }

    public void setLossRate(long lossRate) {
        this.lossRate = lossRate;
    }
}
