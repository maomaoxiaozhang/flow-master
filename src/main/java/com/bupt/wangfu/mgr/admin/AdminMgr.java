package com.bupt.wangfu.mgr.admin;

import com.bupt.wangfu.info.entry.*;
import com.bupt.wangfu.info.msg.FlowInfos;
import com.bupt.wangfu.info.msg.GroupUnit;
import com.bupt.wangfu.info.msg.LSA;
import com.bupt.wangfu.mgr.admin.database.DataCollect;
import com.bupt.wangfu.mgr.design.PSManagerUI;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdminMgr extends AdminUtil implements Runnable {

	public GlobalUtil globalUtil;
	public MulticastSocket serverMutiSocket;
	public SdnConfig sdnConfig;
	public Map<String, Integer> times = new ConcurrentHashMap<>();

	public AdminMgr() {
		//初始化一些基本变量
		Configuration configuration = new Configuration();
		configuration.configure();

		System.out.println("local addr: " + localAddr + ", port: " + port);

		groups = new ConcurrentHashMap<>();
		lsdb = new ArrayList<>();
		globalTopo = new int[groupCount][groupCount];

		globalUtil = GlobalUtil.getInstance();
		globalUtil.init();

		sdnConfig = new SdnConfig();
		sdnConfig.init();

		// 开启webservice 服务，为拓扑管理提供数据
		new Thread(new TopoService()).start();
		try {
			serverMutiSocket = new MulticastSocket(port);//监听30006,与集群代表交互
			Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName("FF01:0000:0000:0000:0001:2345:6789:abcd");
			serverMutiSocket.joinGroup(inetAddress);//多播套接字加入多播组
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
		ui = new PSManagerUI(this,globalUtil);
		ui.reloadAllGroup();

	}

	public static void main(String[] args) {
		new AdminMgr();
	}


	@Override
	public void run() {
		while (true) {
            //分配一个线程处理这个连接
            new Thread(new AdMsgService(this,serverMutiSocket)).start();
        }
	}

	/**
	 * new added on 2016/7/25 by HanB
     */
	public ConcurrentHashMap<String, ArrayList<String>> getGroupSubscriptions(String groupName) {
		ConcurrentHashMap<String,ArrayList<String>> groupSubscriptions = new ConcurrentHashMap<>();
		GroupUnit g = groups.get(groupName);
		Controller ctrl = GlobalUtil.getController(g.controllerAddr);
		if (ctrl == null) return groupSubscriptions;
		for (Switch s : ctrl.getSwitchMap().values()) {
			Map<String,WSNHost> hosts = globalUtil.getHostsOfSwitch(s);
			for (LSA lsa : lsdb) {
				if (hosts.containsKey(lsa.originator)) {
					groupSubscriptions.put(lsa.originator, lsa.subsTopics);
					hosts.get(lsa.originator).getSubTopics().addAll(lsa.subsTopics);
				}
			}
		}
		return groupSubscriptions;
	}

	// 群发消息，待添加。。。。
	public static void bloodMsg(Object msg) {
		System.out.println("群发" + msg.getClass().getSimpleName()+"消息!");

		/*try {
			ByteArrayOutputStream baos;
			ObjectOutputStream oos;
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(msg);
			byte[] data = baos.toByteArray();
			Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName("FF01:0000:0000:0000:0001:2345:6789:abcd");//根据主机名返回主机的IP地址

			DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, 7777);//数据包包含消息内容，消息长度，多播IP和端口

			MulticastSocket multicastSocket = new MulticastSocket();

			multicastSocket.send(datagramPacket);//发送数据包
		}catch (Exception e) { System.out.println("群发消息出错");}*/
	}

	public static GroupUnit[] lookupGroups() {
		Collection<GroupUnit> values = groups.values();
		return values.toArray(new GroupUnit[values.size()]);
	}


}
