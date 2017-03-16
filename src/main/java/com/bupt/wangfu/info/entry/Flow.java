package com.bupt.wangfu.info.entry;

import org.json.JSONObject;

public class Flow {
	public Controller controller;
	private String dpid;
	private int flow_id;
	private int table_id;
	private String flowCount;
	private JSONObject content;

	public Flow(String dpid) {
		this.dpid = dpid;
	}

	public JSONObject getContent() {
		return content;
	}

	public void setContent(JSONObject content) {
		this.content = content;
	}


	public String getDpid() {
		return dpid;
	}

	public void setDpid(String dpid) {
		this.dpid = dpid;
	}

	public int getFlow_id() {
		return flow_id;
	}

	public void setFlow_id(int flow_id) {
		this.flow_id = flow_id;
	}

	public int getTable_id() {
		return table_id;
	}

	public void setTable_id(int table_id) {
		this.table_id = table_id;
	}

	public String getFlowCount() {
		return flowCount;
	}

	public void setFlowCount(String flowCount) {
		this.flowCount = flowCount;
	}


}
