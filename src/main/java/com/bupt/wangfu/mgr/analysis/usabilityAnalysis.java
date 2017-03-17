package com.bupt.wangfu.mgr.analysis;

import com.bupt.wangfu.info.entry.FlowInfo;

/**
 * Created by lenovo on 2017/3/17.
 * 可用性分析，输入为交换机5s内的流量信息，输出为分析结果
 */
public class usabilityAnalysis {
	public static analysisResult analysis(FlowInfo flow){
		analysisResult result = analysisResult.normal;

		/*
		1.每当新用户接入时，需进行用户认证
		2.每半小时检查控制器下的用户，查找是否存在非法用户
		 */

		return result;
	}
}
