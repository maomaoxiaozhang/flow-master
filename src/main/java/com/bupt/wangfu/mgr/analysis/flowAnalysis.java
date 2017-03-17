package com.bupt.wangfu.mgr.analysis;

import com.bupt.wangfu.info.entry.FlowInfo;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/17.
 * 流量分析模块，输入为每5s得到的流量信息FlowInfo对象和取样次数times，输出为流量管控对应的处理措施
 */
public class flowAnalysis {
	public static analysisResult analysis(ArrayList<FlowInfo> flowInfo, int times) throws SQLException {
		//result为返回值，初始化为正常
		analysisResult result = analysisResult.normal;
		//根据交换机的不同依次分析每5s的流量信息
		for(FlowInfo flow : flowInfo){
			//异常数据流（吞吐量）
			result = throughputAnalysis.analysis(flow, times);
			//丢包率
			result = packetLossAnalysis.analysis(flow);
			//可用性
			result = usabilityAnalysis.analysis(flow);
			//流量上限
			result = limitAnalysis.analysis(flow);
		}
		return result;

	}
}
