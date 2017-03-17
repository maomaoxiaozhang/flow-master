package com.bupt.wangfu.mgr.analysis;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/17.
 * 异常数据检测方法1：计算历史数据的速率均值、标准差，比较当前速率是否在均值的2倍标准差范围内
 */
public class method_1 {
	public static analysisResult method(ArrayList<Long> speedList, Long speed){
		analysisResult result = analysisResult.normal;

		//得到历史数据的均值
		Long sum = Long.valueOf(0);
		for(Long sp : speedList){
			sum += sp;
		}
		Long avg = sum/speedList.size();
		sum = Long.valueOf(0);

		//得到历史数据的标准差
		for(Long sp : speedList){
			sum += (sp - avg)^2;
		}
		sum = sum/speedList.size();
		double std = Math.sqrt(sum);

		//当前速率与历史平均速率的差超过两倍的标准差
		if(Math.abs(speed - avg) > 2*std){
			result = analysisResult.abnormal;
		}

		return result;
	}
}
