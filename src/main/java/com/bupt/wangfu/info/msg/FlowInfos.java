package com.bupt.wangfu.info.msg;

import com.bupt.wangfu.info.entry.FlowInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tang Lu on 2017/3/11.
 */
public class FlowInfos {
    String controllerId;
    private Map<Integer, ArrayList<FlowInfo>> flowMap = new ConcurrentHashMap<>();

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public Map<Integer, ArrayList<FlowInfo>> getFlowMap() {
        return flowMap;
    }

    public void setFlowMap(Map<Integer, ArrayList<FlowInfo>> flowMap) {
        this.flowMap = flowMap;
    }
}
