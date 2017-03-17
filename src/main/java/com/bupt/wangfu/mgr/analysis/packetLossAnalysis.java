package com.bupt.wangfu.mgr.analysis;

import com.bupt.wangfu.info.entry.FlowInfo;

/**
 * Created by lenovo on 2017/3/17.
 * 丢包率分析，输入为交换机某一端口5s内的流量信息，输出为分析结果
 */
public class packetLossAnalysis {
	public static analysisResult analysis(FlowInfo flow){
		analysisResult result = analysisResult.normal;

		//当丢包率大于2%时，判断为丢包率过大
		if(flow.getLossRate() > 0.02){
			result = analysisResult.packetLoss;
		}

		return result;
	}
}
