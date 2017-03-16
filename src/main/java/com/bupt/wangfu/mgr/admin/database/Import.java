package com.bupt.wangfu.mgr.admin.database;

import com.bupt.wangfu.info.entry.FlowInfo;
import com.bupt.wangfu.info.msg.FlowInfos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tang Lu on 2017/3/14.
 */
public class Import
{
    public void collect(Map<String, Integer> times,FlowInfos flowInfos){
        Map<String, FlowInfo> flowInfo = new ConcurrentHashMap<>();
        Map<String, Integer> num = new ConcurrentHashMap<>();
        DataCollect dc = new DataCollect();
        for(ArrayList<FlowInfo> flows : flowInfos.getFlowMap().values()){
            for(FlowInfo flow : flows){
                String port = flow.getPortId();
                if((flowInfo.get(port) == null)||(num.get(port) == null)){
                    flowInfo.put(flow.getPortId(),flow);
                    num.put(flow.getPortId(),0);
                }
                else{
                    num.put(flow.getPortId(),num.get(port)+1);
                    long speed = flowInfo.get(port).getSpeed();
                    float lossRate = flowInfo.get(port).getLossRate();
                    flowInfo.get(port).setSpeed(speed+flow.getSpeed());
                    flowInfo.get(port).setLossRate(flow.getLossRate()+lossRate);
                    if((flow.getTransmitted()>=flowInfo.get(port).getTransmitted()&&(flow.getReceived()>=flowInfo.get(port).getReceived()))){
                        flowInfo.get(port).setTransmitted(flow.getTransmitted());
                        flowInfo.get(port).setReceived(flow.getReceived());
                        flowInfo.get(port).setBytes(flow.getBytes());
                    }
                }
            }
        }
        for(FlowInfo flow: flowInfo.values()){
            flow.setSpeed(flow.getSpeed()/num.get(flow.getPortId()));
            flow.setLossRate(flow.getLossRate()/num.get(flow.getPortId()));
            dc.insertDay(flow);
        }
        SimpleDateFormat dataForm =new SimpleDateFormat("yyyy-mm-dd");
        String time = dataForm.format(new Date());
        String[] ti = time.split("-");
        int year=Integer.parseInt(ti[0]);
        int month=Integer.parseInt(ti[1]);
        int day=Integer.parseInt(ti[2]);
        if(times.get(flowInfos.getControllerId())==0){
            times.put(flowInfos.getControllerId(),1);
        }
        if(times.get(flowInfos.getControllerId())==48){//新一天
            if(day == 1){
                if((month == 1)||(month==2)||(month==4)||(month==6)||(month==8)||(month==9)||(month==11)){
                    dc.updateMonth(flowInfos.getControllerId(),31);
                }
                else if((month==5)||(month==7)||(month==10)||(month==12)){
                    dc.updateMonth(flowInfos.getControllerId(),30);
                }
                else if(month==3){
                    if(year%4==0){
                        dc.updateMonth(flowInfos.getControllerId(),29);
                    }
                    else{
                        dc.updateMonth(flowInfos.getControllerId(),28);
                    }
                }
            }
            else{
                dc.updateMonth(flowInfos.getControllerId(),day-1);
            }
            dc.deleteDay(flowInfos.getControllerId());
            times.put(flowInfos.getControllerId(),0);
        }
        if((day==1)&&(times.get(flowInfos.getControllerId())==1)){//一个新月
            //记录流量总数的初始数据
            if(month==1){//新一年
                dc.updateYear(flowInfos.getControllerId(), 12);
                dc.deleteMonth(flowInfos.getControllerId());
                dc.updateSum(flowInfos.getControllerId(),year);
                dc.deleteYear(flowInfos.getControllerId());
            }
            else{
                dc.updateYear(flowInfos.getControllerId(), month-1);
                dc.deleteMonth(flowInfos.getControllerId());
            }
        }
        else{
            int i = times.get(flowInfos.getControllerId());
            times.put(flowInfos.getControllerId(),1+i);
        }
    }
}
