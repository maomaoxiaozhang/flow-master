package com.bupt.wangfu.info.msg;

import com.bupt.wangfu.mgr.wsnPolicy.msgs.WsnPolicyMsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PolicyDB implements Serializable {

	/**
	 * 策略信息库
	 */
	private static final long serialVersionUID = 1L;
	public long time;
	public boolean clearAll; //是否为全库更新
	public ArrayList<WsnPolicyMsg> pdb;
	public HashMap<String, GroupUnit> groupMsg;

	public PolicyDB() {
		pdb = new ArrayList<>();
		groupMsg = new HashMap<>();
	}
}