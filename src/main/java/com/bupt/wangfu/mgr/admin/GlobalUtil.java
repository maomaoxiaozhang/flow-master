package com.bupt.wangfu.mgr.admin;

import com.bupt.wangfu.info.entry.*;
import com.bupt.wangfu.opendaylight.RestProcess;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ @ Created by root on 15-10-6.
 */
public class GlobalUtil {
	public static List<Flow> initFlows = new ArrayList<>();
	public static Map<Integer, String> switchMap = new HashMap<>();//用于保存邻接矩阵的下标与交换机的对应关系
	public static int[][] weight = new int[20][20];//用于保存每次更新时当前交换机之间的连接关系
	public static Map<String, Controller> controllers = new ConcurrentHashMap<>();
	private static int M = 10000; // 此路不通
	private static double Wq = 0.4;
	private static GlobalUtil INSTANCE;
	public Controller centerController;

	public Map<String, Switch> getAllSwitch(Controller controller) {//nll 利用odl控制器获取当前controller下所有交换机

		if (!controllers.containsKey(controller.getUrl())) {
			controllers.put(controller.getUrl(), controller);
			//DataCollect.insertController(controller);
		}
		String url = controller.getUrl() + "/restconf/operational/network-topology:network-topology/";
		String body = RestProcess.doClientGet(url);
		assert body != null;
		JSONObject json;
		try {
			json = new JSONObject(body);
			JSONObject net_topology = json.getJSONObject("network-topology");
			JSONArray topology = net_topology.getJSONArray("topology");
			for (int i = 0; i < topology.length(); i++) {
				JSONArray node = topology.getJSONObject(i).getJSONArray("node");
				for (int j = 0; j < node.length(); j++) {
					String node_id = node.getJSONObject(j).getString("node-id");
					if (node_id.startsWith("openflow")) {
						Switch swc = new Switch();
						System.out.println("the id of the switch is :" + node_id);
						swc.setDPID(node_id);
						swc.setController(controller.url);
						if (!isExist(controller, node_id)) {
							controller.getSwitchMap().put(node_id, swc);
							//DataCollect.insertSwitch(swc);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return controller.getSwitchMap();
	}

	public int[][] getDevOfSwitch(Controller controller) {//odl获取交换机所有端口号和对应的设备信息,并更新信息到map中
		initFunc(controller);
		String url = controller.getUrl() + "/restconf/operational/network-topology:network-topology/";
		String body = RestProcess.doClientGet(url);
		try {
			JSONObject json = new JSONObject(body);
			JSONObject net_topology = json.getJSONObject("network-topology");
			JSONArray topology = net_topology.getJSONArray("topology");
			for (int i = 0; i < topology.length(); i++) {
				JSONArray link = topology.getJSONObject(i).getJSONArray("link");
				for (int j = 0; j < link.length(); j++) {
					if (!link.getJSONObject(j).getString("link-id").contains("host")) {
						JSONObject source = link.getJSONObject(j).getJSONObject("source");
						String source_info = source.getString("source-tp");
						String str[] = source_info.split(":");
						String src_id = str[0] + ":" + str[1];
						Switch src_swt = findSwitch(src_id, controller);
						String portNum = str[2];
						JSONObject dest = link.getJSONObject(j).getJSONObject("destination");
						String dest_info = dest.getString("dest-tp");
						String str_dest[] = dest_info.split(":");
						String dest_id = str_dest[0] + ":" + str_dest[1];
						Switch dest_swc = findSwitch(dest_id, controller);
						src_swt.getWsnDevMap().put(Integer.parseInt(portNum), dest_swc);

						Link lk = new Link();
						lk.setLinkId(link.getJSONObject(j).getString("link-id"));
						lk.setSrc(src_swt.getDPID());
						lk.setDest(dest_swc.getDPID());
						//DataCollect.insertLink(lk);

						int row = 0, column = 0;
						for (int k = 0; k < switchMap.size(); k++) {
							String swcid = switchMap.get(k);
							if (swcid.equals(src_id))
								row = k;
							if (swcid.equals(dest_id))
								column = k;
						}
						weight[row][column] = 1;
						weight[column][row] = 1;

					} else { //HanB ODL获取主机信息
						if (link.getJSONObject(j).getJSONObject("source").getString("source-node").startsWith("openflow")) {
							JSONObject dest = link.getJSONObject(j).getJSONObject("destination");
							String dest_node = dest.getString("dest-node");
							WSNHost host = getHost(controller,dest_node);
							System.out.println(dest_node +" "+ host.getIpAddr()+" " + host.getLastSeen());

							Switch swch = findSwitch(host.getSwitch_id(), controller);
							swch.getWsnDevMap().put(Integer.parseInt(host.getPort()), host);
							//DataCollect.insertHost(dest_host);

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weight;
	}

	private WSNHost getHost(Controller controller, String host_tp) {
		String url = controller.getUrl() + "/restconf/operational/network-topology:network-topology/topology/flow:1/node/" + host_tp;
		String body = RestProcess.doClientGet(url);
		assert body != null;
		try {
			JSONObject json = new JSONObject(body);
			JSONArray node = json.getJSONArray("node");
			JSONArray host_info = node.getJSONObject(0).getJSONArray("host-tracker-service:addresses");
			String mac = host_info.getJSONObject(0).getString("mac");
			String ip = host_info.getJSONObject(0).getString("ip");
			long last_seen = host_info.getJSONObject(0).getLong("last-seen");
			JSONArray attach_point = node.getJSONObject(0).getJSONArray("host-tracker-service:attachment-points");
			String attach = attach_point.getJSONObject(0).getString("tp-id");

			WSNHost host = new WSNHost();
			host.setMac(mac);
			host.setSwitch_id(attach.split(":")[0] +":"+ attach.split(":")[1]);
			host.setIpAddr(ip);
			host.setPort(attach.split(":")[2]);
			host.setLastSeen(Long.toString(last_seen));
			return host;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Port> getPortsOfSwitch(Controller ctrl, Switch swch) { //HanB 获取交换机所有端口数据并保存数据库
		List<Port> ports = new ArrayList<>();
		String url = ctrl.getUrl() + "/restconf/operational/opendaylight-inventory:nodes/node/" + swch.getDPID();
		String body = RestProcess.doClientGet(url);
		assert body != null;
		try {
			JSONObject json = new JSONObject(body);
			JSONArray node = json.getJSONArray("node");
			JSONArray node_connector = node.getJSONObject(0).getJSONArray("node-connector");
			for (int i = 0; i < node_connector.length(); i ++) {
				JSONObject term = node_connector.getJSONObject(i);
				String tpid = term.getString("id");
				String portName = term.getString("flow-node-inventory:name");
				String enport = getDataOfPort(ctrl,swch,tpid).split(":")[0];
				String deport = getDataOfPort(ctrl,swch,tpid).split(":")[1];
				Port port = new Port();
				port.setTpid(tpid);
				port.setPortName(portName);
				port.setSwitchId(swch.getDPID());
				port.setEnport(enport);
				port.setDeport(deport);
				//DataCollect.insertPort(port);
				ports.add(port);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ports;
	}

	private String getDataOfPort(Controller ctrl, Switch swch, String pt) { //HanB 获取出入端口数据
		String url = ctrl.getUrl() + "/restconf/operational/opendaylight-inventory:nodes/node/" + swch.getDPID() +
				"/node-connector/" + pt;
		String body = RestProcess.doClientGet(url);
		assert body != null;
		try {
			JSONObject json = new JSONObject(body);
			JSONArray node = json.getJSONArray("node-connector");
			JSONObject statisdics = node.getJSONObject(0).getJSONObject("opendaylight-port-statistics:flow-capable-node-connector-statistics");
			JSONObject bytes = statisdics.getJSONObject("bytes");
			Long trans = bytes.getLong("transmitted");
			Long recev = bytes.getLong("received");
			return trans + ":" + recev;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public List<Queue> getQueuesOfPort (Controller ctrl, Switch swch, Port pt) { //HanB 获取端口所有队列数据并写入数据库
		List<Queue> queues = new ArrayList<>();
		String url = ctrl.getUrl() + "/restconf/operational/opendaylight-inventory:nodes/node/" + swch.getDPID() +
				"/node-connector/" + pt.getTpid();
		String body = RestProcess.doClientGet(url);
		assert body != null;
		try {
			JSONObject json = new JSONObject(body);
			JSONArray node = json.getJSONArray("node-connector");
			if (!node.getJSONObject(0).isNull("flow-node-inventory:queue")) {
				JSONArray jqueue = node.getJSONObject(0).getJSONArray("flow-node-inventory:queue");
				for (int i = 0; i < jqueue.length(); i++) {
					JSONObject jq = jqueue.getJSONObject(i);
					Queue q = new Queue();
					String queueId = Integer.toString(jq.getInt(")queue-id"));
					String dequeue = Long.toString(jq.getJSONObject("opendaylight-queue-statistics:flow-capable-node-connector-queue-statistics")
							.getLong("transmitted-bytes"));
					q.setQueueName("queue:" + queueId);
					q.setPortId(pt.getTpid());
					q.setDequeue(dequeue);
					String enqueue = getEnqueue(ctrl, swch, q);
					q.setEnqueue(enqueue);
					q.setQueueLength(avgQue(enqueue, dequeue));
					/**
					 * 如何获取队列带宽
					 * */
					q.setBrandWidth("");
					queues.add(q);
					//DataCollect.insertQueue(q);
				}
			} else {
				System.out.println("交换机" + swch.getDPID() + "下端口" + pt.getTpid() + "无队列");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queues;
	}

	private String getEnqueue(Controller ctrl, Switch swch, Queue qn) { //HnaB 获取入队数据,未指定flowID
//		String url = ctrl.getUrl() + "/restconf/operational/opendaylight-inventory:nodes/node/" + swch.getDPID() +
//				"/table" + qn.getFlow().getTable_id() + "/flow" + qn.getFlow().getFlow_id();
		String url = ctrl.getUrl() + "/restconf/operational/opendaylight-inventory:nodes/node/" + swch.getDPID() + "/table/" + "0";
		String body = RestProcess.doClientGet(url);
		assert body != null;
		try {
			JSONObject json = new JSONObject(body);
			JSONArray table = json.getJSONArray("flow-node-inventory:table");
			JSONObject total_byte = table.getJSONObject(0).getJSONObject("opendaylight-flow-statistics:aggregate-flow-statistics");
			return Long.toString(total_byte.getLong("byte-count"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	//获取队列长度
	public String avgQue(String enqueue, String dequeue){//nll根究Red算法求队列的平均长度
		double avgQue = 0;
		double outRate = Double.parseDouble(dequeue);
		double inRate = Double.parseDouble(enqueue);
		double q;//当前队列的长度
		if(outRate == 0){
			avgQue = outRate;
		}else{
			q = inRate - outRate;
			avgQue = (1 - Wq) * avgQue + Wq * q;
		}
		return Double.toString(avgQue);
	}

	public int[][] getTopology(Controller controller) {//HanB ODL获取交换机之间的连接关系，将结果更新到邻接矩阵中
		return getDevOfSwitch(controller);
	}

	private boolean isExist(Controller controller, String switch_id) {//确定此交换机是否在已有的map中
		Map<String, Switch> map = controller.getSwitchMap();
		for (Map.Entry<String, Switch> entry : map.entrySet()) {
			String id = entry.getKey();
			if (switch_id.equals(id)) {
				return true;
			}
		}
		return false;
	}

	public Switch findSwitch(String id, Controller controller) {//返回该id的交换机实体
		for (Map.Entry<String, Switch> entry : controller.getSwitchMap().entrySet()) {
			Switch swt = entry.getValue();
			if (swt.getDPID().equals(id)) {//存在则直接返回
				return swt;
			}
		}
		// Map中不存在，则新建并加入Map
		Switch swtch = new Switch();
		swtch.setDPID(id);
		swtch.setController(controller.getUrl());
		controller.getSwitchMap().put(id, swtch);
		return swtch;
	}

	public Map<String, WSNHost> getHostsOfSwitch(Switch s) { //HanB 	获取交换机下主机
		Map<Integer, DevInfo> wsnDevMap = s.getWsnDevMap();
		Map<String, WSNHost> wsnHostMap = new HashMap<>();
		for (DevInfo temp : wsnDevMap.values()) {
			if (temp instanceof WSNHost) {
				WSNHost host = (WSNHost) temp;
				wsnHostMap.put(host.getIpAddr(), host);
			}
		}
		return wsnHostMap;
	}

	public int getIndex(Switch s) { // HanB 获取交换机在邻接表中的下标
		int index = 0;
		for (int i = 0; i < switchMap.size(); i++) {
			if (s.getDPID().equals(switchMap.get(i)))
				index = i;
		}
		return index;
	}

	private void initFunc(Controller controller) {//初始化数组与对应的Map
		int flag = 0;
		for (int i = 0; i < weight.length; i++)
			for (int j = 0; j < weight[i].length; j++)
				weight[i][j] = M;//初始化二维数组，M值表示不连接，初始为全部未连接
		for (Map.Entry<String, Switch> entry : controller.getSwitchMap().entrySet()) {
			Switch swt = entry.getValue();
			switchMap.put(flag, swt.getDPID());
			flag++;
		}
	}

	public void addController(String groupName,String controllerAddr) {
		Controller newController = new Controller(controllerAddr);
		newController.setGroupName(groupName);
		newController.reflashSwitchMap();
		if(!controllers.containsKey(controllerAddr))
			controllers.put(controllerAddr, newController);
	}

	public static Controller getController(String controllerAddr) {
		for (String addr : controllers.keySet()) {
			if (addr.equals(controllerAddr)) {
				return controllers.get(addr);
			}
		}
		return null;
	}

	public static GlobalUtil getInstance() {
		if (INSTANCE == null) INSTANCE = new GlobalUtil();
		return INSTANCE;
	}

	public void init() {
		//get realtime global info
		reflashGlobleInfo(); // 获取当前所有控制器下交换机

		// 初始化流表
//		initFlows(centerController);
		/*for (Map.Entry<String, Controller> entry : controllers.entrySet()) {
			Controller controller = entry.getValue();
			initFlows(controller);
		}*/
	}

	public void reflashGlobleInfo() {

		centerController = new Controller(AdminMgr.globalControllerAddr);
		//centerController.reflashSwitchMap();
		for (Map.Entry<String, Controller> entry : controllers.entrySet()) {
			Controller controller = entry.getValue();
			if (!controller.isAlive()) {
				controllers.remove(entry.getKey());
				continue;
			}
			controller.reflashSwitchMap();
		}
	}

	public void initFlows(Controller controller) {
		//down init flows
		downFlow(controller, initFlows);
	}

	public void downFlow(Controller controller, List<Flow> flows) {
		for (Flow flow : flows) {
			downFlow(controller, flow);
		}
	}

	public boolean downFlow(Controller controller, Flow flow) {
		List<String> result = RestProcess.doClientPost(controller.getUrl(), flow.getContent().toString());
		return result.size() < 1 || result.get(0).equals("200");
	}


}
