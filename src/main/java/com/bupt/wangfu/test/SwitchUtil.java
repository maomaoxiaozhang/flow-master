package com.bupt.wangfu.test;

import com.bupt.wangfu.info.entry.FlowInfo;
import com.bupt.wangfu.opendaylight.RestProcess;
import org.json.*;

import java.util.ArrayList;
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
                JSONArray node = topology.getJSONObject(i).getJSONArray("node");
                for (int j = 0; j < node.length(); j++) {
                    String node_id = node.getJSONObject(j).getString("node-id");
                    if (node_id.startsWith("openflow")) {
                        switchList.add(node_id);
                        System.out.println("the id of the switch is :" + node_id);
                    }
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

    public FlowInfo getFlowInfo(String cUrl, String sId, String portId){
        FlowInfo flowInfo = new FlowInfo();
        flowInfo.setControllerId(cUrl);
        flowInfo.setSwitchId(sId);
        flowInfo.setPortId(portId);
        String url = cUrl + "/restconf/operational/opendaylight-inventory:nodes/node/"+sId+"/node-connector/"+sId+":"+portId;
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
            flowInfo.setBytes(transmitted+received);
            Long packet = packets.getLong("received");
            Long drop =node_connector.getLong("receive-drops");
            flowInfo.setPackets(packet);
            flowInfo.setDrop(drop);
            flowInfo.setSpeed(node_connector.getLong("current-speed"));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return flowInfo;
    }
}
