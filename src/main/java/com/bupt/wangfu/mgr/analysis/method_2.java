package com.bupt.wangfu.mgr.analysis;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/17.
 * 异常数据检测算法2：预先设定参考值i和Ni，计算历史数据中超出当前数据浮动i范围的数据点数量N，若N大于Ni，则视为异常
 */
public class method_2 {
	public static analysisResult method(ArrayList<Long> speedList, Long speed){
		analysisResult result = analysisResult.normal;

		//预先设定参考值i和Ni
		Long i = Long.valueOf(0);
		int Ni = 0, N = 0;

		for(Long sp : speedList){
			if(Math.abs(speed - sp) > i){
				N++;
			}
		}
		if(N > Ni){
			result = analysisResult.abnormal;
		}

		return result;
	}
}
