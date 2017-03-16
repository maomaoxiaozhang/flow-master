package com.bupt.wangfu.info.entry;

/**
 * Created by HanB on 2016/7/13 0013.
 */
public class Port {
    private String portName; // 端口名
    private String tpid; // 交换机dpid+端口号
    private String switchId; // 交换机dpid
    private String enport; // 入端口数据
    private String deport; // 出端口数据

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getTpid() {
        return tpid;
    }

    public void setTpid(String tpid) {
        this.tpid = tpid;
    }

    public String getSwitchId() {
        return switchId;
    }

    public void setSwitchId(String switchId) {
        this.switchId = switchId;
    }

    public String getEnport() {
        return enport;
    }

    public void setEnport(String enport) {
        this.enport = enport;
    }

    public String getDeport() {
        return deport;
    }

    public void setDeport(String deport) {
        this.deport = deport;
    }
}
