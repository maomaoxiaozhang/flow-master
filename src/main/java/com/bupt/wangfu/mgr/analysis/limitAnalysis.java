package com.bupt.wangfu.mgr.analysis;

import com.bupt.wangfu.info.entry.FlowInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bupt.wangfu.mgr.admin.database.DataCollect.selectSumByPort;

/**
 * Created by lenovo on 2017/3/17.
 * 流量上限分析，输入为交换机某一端口5s内的流量信息，输出为分析结果
 * 注意：本次分析应发生于有新的流量信息到来时，取出数据库内各个端口的流量总量，加上当前流量值，若大于上限则视为流量超出
 */
public class limitAnalysis {
	public static analysisResult analysis(FlowInfo flow) throws SQLException {
		analysisResult result = analysisResult.normal;

		String controllerId = flow.getControllerId();
		String switchId = flow.getSwitchId();
		String portId = flow.getPortId();

		//流量上限
		Long limit = Long.valueOf(0);

		ResultSet rs = selectSumByPort(controllerId, switchId, portId);
		Long sum = rs.getLong("sum");
		if((sum + flow.getTransmitted()) > limit){
			result = analysisResult.overLimit;
		}

		return result;
	}
}
