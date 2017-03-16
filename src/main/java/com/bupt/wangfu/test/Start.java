package com.bupt.wangfu.test;

import com.bupt.wangfu.info.entry.FlowInfo;
import com.bupt.wangfu.info.msg.GroupUnit;
import com.bupt.wangfu.info.msg.LSA;
import com.bupt.wangfu.info.msg.PolicyDB;
import com.bupt.wangfu.info.msg.FlowInfos;
import com.bupt.wangfu.mgr.wsnPolicy.msgs.WsnPolicyMsg;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ Created by HanB on 2016/7/25 0025.
 */
public class Start {
    FlowInfos flowInfos = new FlowInfos();
    //ArrayList<String> user = new ArrayList<String>();
    Map<String, Long> byteList,packetList,lossList;
    int times = 0;
    boolean reset;



    public Start(){
        flowInfos.setControllerId( "http://10.108.165.188:8181");
        Timer tmr = new Timer();
        tmr.scheduleAtFixedRate(new CollectTask(),new Date(),  5000);
    }

    public static void main(String[] args) {
        new Start();
        GroupUnit gu = new GroupUnit("10.108.164.152",10086,"G1");
        gu.controllerAddr = "http://10.108.165.188:8181";

        LSA lsa = new LSA();
        lsa.initLSA();
        lsa.originator = "192.168.100.50";
        lsa.subsTopics.add("a1");
        lsa.subsTopics.add("b2");

        LSA lsb = new LSA();
        lsb.initLSA();
        lsb.originator = "192.168.100.51";
        lsb.subsTopics.add("a2");
        lsb.subsTopics.add("f1");



        try {
            ByteArrayOutputStream baos;
            ObjectOutputStream oos;
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(gu);
            byte[] msg = baos.toByteArray();
            Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName("FF01:0000:0000:0000:0001:2345:6789:abcd");
            DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length,inetAddress, 30006);
            MulticastSocket multicastSocket = new MulticastSocket();
            multicastSocket.send(datagramPacket);//发送数据包
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ByteArrayOutputStream baos;
            ObjectOutputStream oos;
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(lsa);
            byte[] msg = baos.toByteArray();
            Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName("FF01:0000:0000:0000:0001:2345:6789:abcd");
            DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length,inetAddress, 30006);
            MulticastSocket multicastSocket = new MulticastSocket();
            multicastSocket.send(datagramPacket);//发送数据包
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
            ByteArrayOutputStream baos;
            ObjectOutputStream oos;
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(lsb);
            byte[] msg = baos.toByteArray();
            Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName("FF01:0000:0000:0000:0001:2345:6789:abcd");
            DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length,inetAddress, 30006);
            MulticastSocket multicastSocket = new MulticastSocket();
            multicastSocket.send(datagramPacket);//发送数据包
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void process(Object obj) {
        if (obj instanceof PolicyDB) {
            PolicyDB pdb = (PolicyDB)obj;
            for (WsnPolicyMsg msg : pdb.pdb)
                System.out.println(msg.getTargetTopic() + "   " + msg.getTargetGroups());
        }
    }

    public class CollectTask extends TimerTask {

        public void run() {
            collect();
        }

    }

    public void collect(){
        SimpleDateFormat dataForm =new SimpleDateFormat("dd");
        String time = dataForm.format(new Date());
        int day=Integer.parseInt(time);
        SwitchUtil su = new SwitchUtil();
        ArrayList<FlowInfo> flowInfo = new ArrayList<>();
        ArrayList<String> switchList;
        Map<String, ArrayList<String>> switchMap;
        switchList = su.getSwitchList("http://10.108.165.188:8181");
        switchMap = su.getPortList("http://10.108.165.188:8181",switchList);
        for(String switchId : switchMap.keySet()){
            ArrayList<String> ports = switchMap.get(switchId);
            for(String portId : ports){
                FlowInfo flow = su.getFlowInfo("http://10.108.165.188:8181",switchId,portId);
                if((day==1)&&(!reset)){
                    reset = true;
                    byteList.put(portId,flow.getBytes());
                }
                else if(day==2) {
                    reset = false;
                }
                if (packetList.get(portId) == null){
                    packetList.put(portId,flow.getPackets());
                    lossList.put(portId,flow.getDrop());
                    flow.setLossRate((float)0);
                }
                else{
                    if(flow.getPackets()-packetList.get(portId)==0){
                        packetList.put(portId,flow.getPackets());
                        lossList.put(portId,flow.getDrop());
                        flow.setLossRate((float)0);
                    }
                    else{
                        flow.setLossRate((float)(flow.getDrop()-lossList.get(portId)/flow.getPackets()-packetList.get(portId)));
                        packetList.put(portId,flow.getPackets());
                        lossList.put(portId,flow.getDrop());
                    }

                }





                flow.setBytes(flow.getBytes()-byteList.get(portId));
                flowInfo.add(flow);
            }
        }
        times=times+1;
        flowInfos.getFlowMap().put(times,flowInfo);
        if(times==120){
            try {
                ByteArrayOutputStream baos;
                ObjectOutputStream oos;
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(flowInfos);
                byte[] msg = baos.toByteArray();
                Inet6Address inetAddress = (Inet6Address) Inet6Address.getByName("FF01:0000:0000:0000:0001:2345:6789:abcd");
                DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length,inetAddress, 30006);
                MulticastSocket multicastSocket = new MulticastSocket();
                multicastSocket.send(datagramPacket);//发送数据包
            } catch (Exception e) {
                e.printStackTrace();
            }
            times=0;
            flowInfos.getFlowMap().clear();
        }
    }
}
