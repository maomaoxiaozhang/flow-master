package com.bupt.wangfu.info.entry;

import java.util.ArrayList;

/**
 * @ Created by root on 15-10-5.
 */
public class WSNHost extends DevInfo {
	public String mac;

	public String ipAddr;

	public String switch_id;//所属交换机
	//private Map<String, List<String>> subers = new ConcurrentHashMap<>();
	private ArrayList<String> subTopics = new ArrayList<>();

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	@Override
	public String getMac() {
		return mac;
	}

	@Override
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSwitch_id() {
		return switch_id;
	}

	public void setSwitch_id(String switch_id) {
		this.switch_id = switch_id;
	}

	public ArrayList<String> getSubTopics() {
		return subTopics;
	}

	public void setSubTopics(ArrayList<String> subTopics) {
		this.subTopics = subTopics;
	}
}
