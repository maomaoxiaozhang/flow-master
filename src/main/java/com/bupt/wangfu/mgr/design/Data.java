package com.bupt.wangfu.mgr.design;

import com.bupt.wangfu.info.msg.UpdateTree;
import com.bupt.wangfu.mgr.admin.AdminMgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//集群信息数据结构
class GroupInfo {
	public String GroupName;
	public String GroupAddress;
	public Date date;
	public int port;
}

//主机信息数据结构
class SwitchInfo {
	public String GroupName;
	public String dpid;
	public String SubInfo;
}

//主机信息数据结构
class HostInfo {
	public String dpid;
	public String ipAddress;
	public String subInfo;
}

//数据类
public class Data {

	private List<GroupInfo> groupList = new ArrayList<>();
	private List<SwitchInfo> switchList = new ArrayList<>();
	private List<HostInfo> hostList = new ArrayList<>();

	public void sendNotification(UpdateTree ut) {
		AdminMgr.bloodMsg(ut);
	}

	//返回GroupList
	public List<GroupInfo> getAllGroup() {
		return groupList;
	}

	//增加组成员
	public int addGroup(GroupInfo aGroup) {
		int stat;
		//待添加检查十分存在该组的代码
		if (getGroupIndex(aGroup.GroupName) == -1) {
			groupList.add(aGroup);
			stat = 1;
		} else {
			stat = -1;
		}
		return stat;
	}


	//根据特定的组名称查找对应的序号
	public int getGroupIndex(String grpName) {
		int index = -1;
		int count = 0;
		Iterator<GroupInfo> itr = groupList.iterator();
		while (itr.hasNext()) {
			GroupInfo temGroup;
			temGroup = itr.next();
			if (temGroup.GroupName.equals(grpName)) {
				index = count;
			}
			count++;
		}
		return index;
	}
}