package com.bupt.wangfu.mgr.admin;

import com.bupt.wangfu.info.msg.GroupUnit;
import com.bupt.wangfu.info.msg.LSA;
import com.bupt.wangfu.mgr.design.PSManagerUI;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AdminUtil {

	public static String localAddr; // 管理员本地地址
	public static String ldapAddr; // ldap地址
	public static String globalControllerAddr; // 管理员所连控制器地址
	public static int port; // 管理者监听的端口号
	public static ConcurrentHashMap<String, GroupUnit> groups; // 名字:group信息，保存所有group的信息
	public static int groupCount; // 集群规模
	public static int[][] globalTopo; // 群间拓扑
	public static PSManagerUI ui; // 界面UI
	public static ArrayList<LSA> lsdb; // 全网lsdb
}
