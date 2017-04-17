package com.bupt.wangfu.test;

import com.bupt.wangfu.info.entry.WSNHost;
import com.bupt.wangfu.snmp.SnmpMgr;
import com.bupt.wangfu.snmp.info.HostInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/3/17.
 * 测试数据库内容
 */
public class test_caoming {
	public static void main(String[] args) {
//		createAvgSpdByPort();
//		createSumByPort();
//		insertAvgSpdByPort(10, "controlid", "switchid", "portid", Long.valueOf(123456789));
//		insertAvgSpdByPort(10, "controllerid", "switchid", "portid", Long.valueOf(66666));
//		ResultSet rs = selectAvgSpdByPort(10, "controllerid", "switchid", "portid");
//		while(rs.next()){
//			System.out.println(rs.getLong("speed"));
//		}
//		insertSumByPort("cid", "switchid", "portid", Long.valueOf(123456));
//		insertSumByPort("controllerid", "switchid", "portid", Long.valueOf(9999999));
//		ResultSet rs = selectSumByPort("cid", "switchid", "portid");
//		while(rs.next()){
//			System.out.println(rs.getLong("sum"));
//		}
//		truncateTable("avgSpdByPort");
//		truncateTable("sumbyport");
		//


//		SwitchUtil su = new SwitchUtil();
//		ArrayList<FlowInfo> flowInfo = new ArrayList<>();
//		ArrayList<String> switchList;
//		Map<String, ArrayList<String>> switchMap;
//
//		switchList = su.getSwitchList("http://10.108.165.188:8181");
//
//		switchMap = su.getPortList("http://10.108.165.188:8181",switchList);
//		System.out.println(switchList.size());
//		for(String switchId : switchMap.keySet()){
//			ArrayList<String> ports = switchMap.get(switchId);
//			for(String portId : ports){
//				FlowInfo flow = su.getFlowInfo("http://10.108.165.188:8181",switchId,portId);
//				System.out.println(flow.getReceived() + "\n"+ flow.getTransmitted()+ "\n" + flow.getPackets());
//				flowInfo.add(flow);
//			}
//
//		}

		Map<String, String> index = new HashMap<>();
		List<String> primaryKey = new ArrayList<>();
		Map<String, String> values = new HashMap<>();
		String tableName;

//		tableName = "switch";
//		index.put("id", "string");
//		index.put("software", "string");
//		index.put("manufacturer", "string");
//		index.put("hardware", "string");
//		index.put("ip_address", "string");
//		index.put("max_buffers", "int");
//		index.put("max_tables", "int");
//		primaryKey.add("id");
//		createTable(tableName, index, primaryKey);

//		tableName = "switch";
//		values.put("id", "143366665789517");
//		values.put("software", "2.4.0");
//		values.put("manufacturer", "Nicira, Inc.");
//		values.put("hardware", "Open vSwitch");
//		values.put("ip_address", "10.108.165.188");
//		values.put("max_buffers", "256");
//		values.put("max_tables", "254");
//		insertTable(tableName, values);

//		tableName = "switch";
//		index.put("ip_address", "10.108.165.188");
//		index.put("id", "Open Vswitch");
//		ResultSet rs = searchTable(tableName, index);
//		try {
//			while (rs.next()){
//				System.out.print("id: " + rs.getString("id") + "  ");
//				System.out.print("software: " + rs.getString("software") + "  ");
//				System.out.print("manufacturer: " + rs.getString("manufacturer") + "  ");
//				System.out.print("hardware: " + rs.getString("hardware") + "  ");
//				System.out.print("ip_address: " + rs.getString("ip_address") + "  ");
//				System.out.print("max_buffers: " + rs.getString("max_buffers") + "  ");
//				System.out.print("max_tables: " + rs.getString("max_tables") + "  ");
//				System.out.println();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

//		tableName = "switch";
//		index.put("ip_address", "10.108.165.188");
//		index.put("id", "Open Vswitch");
//		deleteTable(tableName, index);

//		tableName = "switch";
//		truncateTable(tableName);
//		dropTable(tableName);

		WSNHost host = new WSNHost();
		host.setMac("FE:54:00:B4:46:58");
		host.setIpAddr("10.108.165.188");
		host.setSwitch_id("openflow:143366665789517");
		SnmpMgr sn = new SnmpMgr();
		HostInfo rs = sn.getHostInfo(host);
		System.out.println("结果为：" + rs.getSysInfo());


	}
}
