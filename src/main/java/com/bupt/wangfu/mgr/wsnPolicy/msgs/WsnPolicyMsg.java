package com.bupt.wangfu.mgr.wsnPolicy.msgs;

import java.util.*;

/**
 * 策略信息类
 * 策略信息保存在磁盘文件中，更新后就直接替换或修改文件。
 * 所有信息放在一个xml文件中，因为策略信息不是很多哈。
 */
public class WsnPolicyMsg implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	//这个暂时用不上
//	private String targetMsgStyle;
	protected String targetTopic;
	protected List<TargetGroup> targetGroups;
	protected List<ComplexGroup> complexGroups;
	//这个是将所有的ComplexGroup里面和
	private Set<TargetGroup> allGroups;  

	public WsnPolicyMsg() {
		this(null, null, null);
	}


	//生成一条完整策略信息
	public WsnPolicyMsg(String targetTopic, List<ComplexGroup> complexGroups, List<TargetGroup> targetGroups) {
		this.targetTopic = targetTopic;
		this.complexGroups = new ArrayList<>();
		this.targetGroups = new ArrayList<>();
		this.allGroups = new HashSet<>();

		if (complexGroups != null) {
			for (int i = 0; i < complexGroups.size(); i++) {
				this.complexGroups.add(complexGroups.get(i));
			}
		}

		if (targetGroups != null) {
			for (int i = 0; i < targetGroups.size(); i++) {
				this.targetGroups.add(targetGroups.get(i));
			}
		}
	}

	public Set<TargetGroup> getAllGroups() {
		if (!complexGroups.isEmpty()) {
			for (int i = 0; i < complexGroups.size(); i++) {
				allGroups.addAll(complexGroups.get(i).getGroups());
			}
		}
		if (!targetGroups.isEmpty()) {
			allGroups.addAll(targetGroups);
		}
		return allGroups;
	}

	public List<ComplexGroup> getComplexGroups() {
		return complexGroups;
	}

	public void setComplexGroups(List<ComplexGroup> complexGroups) {
		this.complexGroups = complexGroups;
	}

	public String getTargetTopic() {
		return targetTopic;
	}

	public void setTargetTopic(String targetTopic) {
		this.targetTopic = targetTopic;
	}

	public List<TargetGroup> getTargetGroups() {
		return targetGroups;
	}

	public void setTargetGroups(List<TargetGroup> targetGroups) {
		this.targetGroups = targetGroups;
	}
}