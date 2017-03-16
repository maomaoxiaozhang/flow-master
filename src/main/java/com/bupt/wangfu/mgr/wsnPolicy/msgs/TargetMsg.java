package com.bupt.wangfu.mgr.wsnPolicy.msgs;
//显示集群信息时，按顺序排列，所以实现Comparable接口
public class TargetMsg implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	protected String name;

	public TargetMsg(String name) {
		this.name = name;
	}

	public TargetMsg() {
		this(null);
	}

	public static void main(String[] args) {
		TargetMsg t1 = new TargetMsg("a");
		TargetMsg t2 = new TargetMsg("a");

		System.out.println(t1.equals(t2));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}

	public boolean equals(Object anObject) {
		if (!(anObject instanceof TargetMsg))
			return false;
		TargetMsg msg = (TargetMsg) anObject;
		if (this.getName().equals(msg.getName()))
			return true;
		return false;
	}
}
