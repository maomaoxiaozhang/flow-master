package com.bupt.wangfu.info.entry;

import com.bupt.wangfu.mgr.admin.GlobalUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ Created by root on 15-10-5.
 */
public class Controller {
	public String url;
	private String groupName;
	private Map<String, Switch> switchMap = new ConcurrentHashMap<>();

	public Controller(String controllerAddr) {
		this.url = controllerAddr;
		if (!controllerAddr.startsWith("http://"))
			this.url = "http://" + controllerAddr;
	}

	public boolean isAlive() {
		return true;
	}

	public Map<String, Switch> getSwitchMap() {
		return switchMap;
	}

	public void reflashSwitchMap() {
		switchMap = new GlobalUtil().getAllSwitch(this);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
