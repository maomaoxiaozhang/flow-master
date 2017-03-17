package com.bupt.wangfu.mgr.analysis;

/**
 * Created by lenovo on 2017/3/17.
 * 流量分析结果：
 *     1.流量正常 —— normal
 *     2.流量峰值超出阈值 —— overThreshold
 *     3.流量拥塞 —— jam
 *     4.流量总量超出上限 —— overLimit
 *     5.丢包率过大 —— packetLoss
 *     6.非法用户 —— illegalUser
 *     7.异常数据流 —— abnormal
 */
public enum analysisResult {
	normal, overThreshold, jam, overLimit, packetLoss, illegalUser, abnormal;
}

