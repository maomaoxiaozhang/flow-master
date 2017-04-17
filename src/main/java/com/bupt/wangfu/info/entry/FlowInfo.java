package com.bupt.wangfu.info.entry;
/**
 * @ Created by TangLu on 2016/10/11 0011.
 */
public class FlowInfo {
    private String controllerId,switchId,portId;
    private Long transmitted,received,bytes,maxSpeed,speed,packets,drop;
    private Float lossRate,avgSpeed;

    public Long getDrop() {
        return drop;
    }

    public void setDrop(Long drop) {
        this.drop = drop;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public Long getPackets() {
        return packets;
    }

    public void setPackets(Long packets) {
        this.packets = packets;
    }

    public Long getTransmitted() {
        return transmitted;
    }

    public void setTransmitted(Long transmitted) {
        this.transmitted = transmitted;
    }

    public String getSwitchId() {
        return switchId;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }

    public void setSwitchId(String switchId) {
        this.switchId = switchId;
    }

    public Long getReceived() {
        return received;
    }

    public void setReceived(Long received) {
        this.received = received;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }

    public Long getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Long maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Float getLossRate() {
        return lossRate;
    }

    public void setLossRate(Float lossRate) {
        this.lossRate = lossRate;
    }

    public String getPortId() {
        return portId;

    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }
}
