package com.bupt.wangfu.test;

import com.bupt.wangfu.info.entry.FlowInfo;
import java.util.*;

/**
 * Created by he on 2017/4/11.
 */
public class test {
    public static void main(String[] args){
        SwitchUtil su = new SwitchUtil();
        //Analysis analysis = new Analysis();
        //ArrayList<FlowInfo> flowInfo = new ArrayList<>();
        ArrayList<String> switchList;
        Map<String, ArrayList<String>> switchMap ;
        switchList = su.getSwitchList("http://10.108.165.188:8181");
        switchMap = su.getPortList("http://10.108.165.188:8181",switchList);
        for(String switchId : switchMap.keySet()){
            ArrayList<String> ports = switchMap.get(switchId);
            for(String portId : ports){
                FlowInfo flow = su.getFlowInfo("http://10.108.165.188:8181",switchId,portId);
                System.out.println(flow.getPackets()+"  "+flow.getReceived()+"  "+flow.getTransmitted()+"  "+flow.getSpeed());
            }

        }
    }



}
