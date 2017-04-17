package com.bupt.wangfu.test;

import com.bupt.wangfu.info.entry.*;
import com.bupt.wangfu.opendaylight.RestProcess;
import org.json.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by TangLu on 2017/1/11.
 */
public class SwitchUtil {

    public ArrayList<String> getSwitchList(String ControllerAddr){
        ArrayList<String> switchList = new ArrayList<String>();
        String url = ControllerAddr + "/restconf/operational/network-topology:network-topology/";
        String body = RestProcess.doClientGet(url);
        assert body != null;
        JSONObject json;
        try {
            json = new JSONObject(body);
            JSONObject net_topology = json.getJSONObject("network-topology");
            JSONArray topology = net_topology.getJSONArray("topology");
            for (int i = 0; i < topology.length(); i++) {
                JSONObject id = json.getJSONObject("topology-id");
                if(id.equals("flow:1")){
                    JSONArray node = topology.getJSONObject(i).getJSONArray("node");
                    for (int j = 0; j < node.length(); j++) {
                        String node_id = node.getJSONObject(j).getString("node-id");
                        if (node_id.startsWith("openflow")) {
                            switchList.add(node_id);
                            System.out.println("the id of the switch is :" + node_id);
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {

        }
        return switchList;
    }

    public Map<String, ArrayList<String>> getPortList(String controllerAddr,ArrayList<String> switchList){
        Map<String, ArrayList<String>> switchMap = new ConcurrentHashMap<>();
        for(int i = 0; i<switchList.size();i++){
            ArrayList<String> ports = new ArrayList<String>();
            String url = controllerAddr + "/restconf/operational/opendaylight-inventory:nodes/node/" + switchList.get(i);
            String body = RestProcess.doClientGet(url);
            assert body != null;
            try {
                JSONObject json = new JSONObject(body);
                JSONArray node = json.getJSONArray("node");
                JSONArray node_connector = node.getJSONObject(0).getJSONArray("node-connector");
                for (int j = 0; j < node_connector.length(); j ++) {
                    JSONObject term = node_connector.getJSONObject(j);
                    String tpid = term.getString("id");
                    ports.add(tpid);
                }
                switchMap.put(switchList.get(i), ports);
            }
                catch (Exception e) {
                e.printStackTrace();
            }
        }
        return switchMap;
    }

    public void getDevOfSwitch (String controllerAddr, Map<String, Link> linkList,  Map<String, User> hostList){//odl获取交换机所有端口号和对应的设备信息,并更新信息到map中
        String url = controllerAddr + "/restconf/operational/network-topology:network-topology/";
        String body = RestProcess.doClientGet(url);
        try {
            JSONObject json = new JSONObject(body);
            JSONObject net_topology = json.getJSONObject("network-topology");
            JSONArray topology = net_topology.getJSONArray("topology");
            for (int i = 0; i < topology.length(); i++) {
                JSONObject id = json.getJSONObject("topology-id");
                if(id.equals("flow:1")){
                    JSONArray link = topology.getJSONObject(i).getJSONArray("link");
                    for (int j = 0; j < link.length(); j++) {
                        if (!link.getJSONObject(j).getString("link-id").contains("host")) {
                            JSONObject source = link.getJSONObject(j).getJSONObject("source");
                            String sourceNode = source.getString("source-tp");
                            String source_info = source.getString("source-node");
                            JSONObject dest = link.getJSONObject(j).getJSONObject("destination");
                            String dest_info = dest.getString("dest-node");
                            Link lk = new Link();
                            lk.setLinkId(link.getJSONObject(j).getString("link-id"));
                            lk.setSrc(source_info);
                            lk.setDest(dest_info);
                            linkList.put(sourceNode,lk);
                        }
                        else { //HanB ODL获取主机信息
                            if (link.getJSONObject(j).getJSONObject("source").getString("source-node").startsWith("openflow")) {
                                JSONObject source = link.getJSONObject(j).getJSONObject("source");
                                String sourceNode = source.getString("source-tp");
                                JSONObject dest = link.getJSONObject(j).getJSONObject("destination");
                                String dest_node = dest.getString("dest-node");
                                User user = getUser(controllerAddr,dest_node);
                                System.out.println(dest_node +" "+ user.getIpAddr()+" " + user.getLastSeen());
                                hostList.put(sourceNode,user);

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User getUser(String controllerAdd, String portId) {
        String str[] = portId.split(":");
        String sId = str[0];
        String url = controllerAdd+  "/restconf/operational/opendaylight-inventory:nodes/node/"+sId+"/node-connector/"+portId;
        String body = RestProcess.doClientGet(url);
        assert body != null;
        try {
            JSONObject json = new JSONObject(body);
            JSONObject node_connector = json.getJSONObject("node-connector");
            JSONObject address = node_connector.getJSONObject("address");
            String mac = address.getString("mac");
            String ip = address.getString("ip");
            long last_seen =address.getLong("last-seen");
            User user = new User();
            user.setMac(mac);
            user.setIpAddr(ip);
            user.setLastSeen(Long.toString(last_seen));
            return user;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public FlowInfo getFlowInfo(String cUrl, String sId, String portId){
        FlowInfo flowInfo = new FlowInfo();
        flowInfo.setControllerId(cUrl);
        flowInfo.setSwitchId(sId);
        flowInfo.setPortId(portId);
        String url = cUrl + "/restconf/operational/opendaylight-inventory:nodes/node/"+sId+"/node-connector/"+portId;
        String body = RestProcess.doClientGet(url);
        assert body != null;
        JSONObject json;
        try {
            json = new JSONObject(body);
            JSONObject node_connector = json.getJSONObject("node-connector");
            JSONObject bytes = node_connector.getJSONObject("bytes");
            JSONObject packets = node_connector.getJSONObject("packets");
            long transmitted = bytes.getLong("transmitted");
            long received = bytes.getLong("received");
            flowInfo.setTransmitted(transmitted);
            flowInfo.setReceived(received);
            Long packet = packets.getLong("received")+packets.getLong("transmitted");
            Long drop =node_connector.getLong("receive-drops");
            flowInfo.setPackets(packet);
            flowInfo.setDrop(drop);
            flowInfo.setSpeed(node_connector.getLong("current-speed"));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return flowInfo;
    }

    public List<Queue> getQueuesOfPort (String cUrl, String sId, String portId) { //HanB 获取端口所有队列数据并写入数据库
        List<Queue> queues = new ArrayList<>();
        String url = cUrl + "/restconf/operational/opendaylight-inventory:nodes/node/"+sId+"/node-connector/"+portId;
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
                    String queueId = Integer.toString(jq.getInt("queue-id"));
                    String dequeue = Long.toString(jq.getJSONObject("opendaylight-queue-statistics:flow-capable-node-connector-queue-statistics")
                            .getLong("transmitted-bytes"));
                    q.setQueueName("queue:" + queueId);
                    q.setPortId(portId);
                    q.setDequeue(dequeue);
                    //String enqueue = getEnqueue(ctrl, swch, q);
                    //q.setEnqueue(enqueue);
                    //q.setQueueLength(avgQue(enqueue, dequeue));
                    /**
                     * 如何获取队列带宽
                     * */
                    q.setBrandWidth("");
                    queues.add(q);
                    //DataCollect.insertQueue(q);
                }
            } else {
                System.out.println("交换机" + sId + "下端口" + portId + "无队列");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queues;
    }

    public Map<String, ArrayList<String>> getTableList(String controllerAddr,ArrayList<String> switchList){
        Map<String, ArrayList<String>> tableMap = new ConcurrentHashMap<>();
        for(int i = 0; i<switchList.size();i++){
            ArrayList<String> tables = new ArrayList<String>();
            String url = controllerAddr + "/restconf/operational/opendaylight-inventory:nodes/node/" + switchList.get(i);
            String body = RestProcess.doClientGet(url);
            assert body != null;
            try {
                JSONObject json = new JSONObject(body);
                JSONArray node = json.getJSONArray("node");
                JSONArray node_connector = node.getJSONObject(0).getJSONArray("table");
                for (int j = 0; j < node_connector.length(); j ++) {
                    JSONObject term = node_connector.getJSONObject(j);
                    String id = term.getString("id");
                    tables.add(id);
                }
                tableMap.put(switchList.get(i), tables);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tableMap;
    }
}
