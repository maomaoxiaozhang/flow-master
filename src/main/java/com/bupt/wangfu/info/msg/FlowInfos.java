package com.bupt.wangfu.info.msg;

import com.bupt.wangfu.info.entry.FlowInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tang Lu on 2017/3/11.
 */
public class FlowInfos implements Serializable {
    String controllerId;
    private Map<Integer, ArrayList<FlowInfo>> flowMap = new ConcurrentHashMap<>();
    int day,month,year;
    boolean newDay,newMonth,newYear;

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isNewDay() {
        return newDay;
    }

    public void setNewDay(boolean newDay) {
        this.newDay = newDay;
    }

    public boolean isNewMonth() {
        return newMonth;
    }

    public void setNewMonth(boolean newMonth) {
        this.newMonth = newMonth;
    }

    public boolean isNewYear() {
        return newYear;
    }

    public void setNewYear(boolean newYear) {
        this.newYear = newYear;
    }
}
