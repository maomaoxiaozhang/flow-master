package com.bupt.wangfu.info.entry;


import com.bupt.wangfu.info.entry.Flow;
import com.bupt.wangfu.info.entry.DevInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 15-7-14.
 */
public class Switch extends DevInfo {


	private String DPID;
	private String mac;
	private String ipAddr;//ipv4
	private String controller; //控制器地址

	private double load;//参数无法通过flootlight获取

	private List<Flow> flows;

	//key是端口号，value是设备
	private Map<Integer, DevInfo> wsnDevMap = new ConcurrentHashMap<>();

	public Map<Integer, DevInfo> getWsnDevMap() {
		return wsnDevMap;
	}

	public void setWsnDevMap(Map<Integer, DevInfo> wsnDevMap) {
		this.wsnDevMap = wsnDevMap;
	}

	public void put(int port, DevInfo devInfo) {
		this.wsnDevMap.put(port, devInfo);
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getDPID() {
		return DPID;
	}

	public void setDPID(String DPID) {
		this.DPID = DPID;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

}