package com.bupt.wangfu.mgr.analysis;

import com.bupt.wangfu.info.entry.FlowInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.bupt.wangfu.mgr.admin.database.DataCollect.selectAvgSpdByPort;


/**
 * Created by lenovo on 2017/3/17.
 * 异常数据流（吞吐量）分析，输入为某个交换机某一端口在5s内的流量信息和取样次数times，输出为流量分析结果
 * 采用的分析方法是：将每次5s传来的流量信息抽象为离散的点（根据交换机号、端口号可以建立不同的表）,
 *     点的横坐标为时间（取点的次数），纵坐标为该端口在此时刻的流量传输速率，并由此得到两点间的距离
 */
public class throughputAnalysis {
	public static analysisResult analysis(FlowInfo flow, int times) throws SQLException {
		//预先设定阈值threshold
		Long threshold = Long.valueOf(1);

		analysisResult result = analysisResult.normal;

		String controllerId = flow.getControllerId();
		String switchId = flow.getSwitchId();
		String portId = flow.getPortId();

		if(flow.getMaxSpeed() > threshold){
			//流量峰值超出阈值
			result = analysisResult.overThreshold;
		}else{
			ResultSet rs = selectAvgSpdByPort(times, controllerId, switchId, portId);
			ArrayList<Long> speedList = new ArrayList<>();
			//将查询得到的速率存储在speedList中
			while(rs.next()){
				speedList.add(rs.getLong("speed"));
				result = method_1.method(speedList, flow.getSpeed());
				result = method_2.method(speedList, flow.getSpeed());
			}
		}

		return result;
	}
}
