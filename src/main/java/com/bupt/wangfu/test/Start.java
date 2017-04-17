package com.bupt.wangfu.test;

import com.bupt.wangfu.info.entry.FlowInfo;
import com.bupt.wangfu.info.entry.Link;
import com.bupt.wangfu.info.msg.GroupUnit;
import com.bupt.wangfu.info.msg.LSA;
import com.bupt.wangfu.info.msg.PolicyDB;
import com.bupt.wangfu.info.msg.FlowInfos;
import com.bupt.wangfu.mgr.admin.database.DataCollect;
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
    Map<String, Info> infoMap;
    Map<String, User> userMap;
    Map<String, Link> linkList;
    int day = 0,month=0,year=0,times=0;

    public Start(){
        SwitchUtil su = new SwitchUtil();
        su.getDevOfSwitch("http://10.108.165.188:8181",linkList,userMap);
        flowInfos.setControllerId( "http://10.108.165.188:8181");
        Timer tmr = new Timer();
        tmr.scheduleAtFixedRate(new CollectTask(),new Date(),  5000);
        tmr.scheduleAtFixedRate(new CheckUser(),new Date(),30000);
    }

    public static void main(String[] args) {
        //new Start();
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

    public class CheckUser extends TimerTask {

        public void run() {
            DataCollect dc = new DataCollect();
            ArrayList<String> switchList;
            Map<String, ArrayList<String>> switchMap;
            SwitchUtil su = new SwitchUtil();
            switchList = su.getSwitchList("http://10.108.165.188:8181");
            switchMap = su.getPortList("http://10.108.165.188:8181",switchList);
            for(String switchId : switchMap.keySet()){
                ArrayList<String> ports = switchMap.get(switchId);
                for(String portId : ports){
                    String result = dc.checkUser("http://10.108.165.188:8181",switchId,portId);
                }
            }
        }

    }

    public void collect(){
        ArrayList<FlowInfo> flowInfo = new ArrayList<>();
        SimpleDateFormat dataForm =new SimpleDateFormat("yyyy-mm-dd");
        String time = dataForm.format(new Date());
        String[] ti = time.split("-");
        int year1=Integer.parseInt(ti[0]);
        int month1=Integer.parseInt(ti[1]);
        int day1=Integer.parseInt(ti[2]);
        SwitchUtil su = new SwitchUtil();
        Analysis analysis = new Analysis();

        ArrayList<String> switchList;
        Map<String, ArrayList<String>> switchMap;
        switchList = su.getSwitchList("http://10.108.165.188:8181");
        switchMap = su.getPortList("http://10.108.165.188:8181",switchList);
        for(String switchId : switchMap.keySet()){
            ArrayList<String> ports = switchMap.get(switchId);
            for(String portId : ports){
                FlowInfo flow = su.getFlowInfo("http://10.108.165.188:8181",switchId,portId);
                flowInfo.add(flow);
            }
        }
        times=times+1;
        if(times==1){
            if(day != day1){
                flowInfos.setNewDay(true);
                day=day1;
                if(month!=month1){
                    flowInfos.setNewMonth(true);
                    month=month1;
                    for(FlowInfo flow : flowInfo){
                        infoMap.get(flow.getPortId()).setInBytes(flow.getReceived()+flow.getTransmitted());
                    }
                    if(year!=year1){
                        flowInfos.setNewYear(true);
                        year=year1;
                    }
                }
            }
            flowInfos.setDay(day);
            flowInfos.setMonth(month);
            flowInfos.setYear(year);
        }
        for(FlowInfo flow : flowInfo){
            flow.setBytes(flow.getBytes()-infoMap.get(flow.getPortId()).getInBytes());
            long bytes = flow.getReceived()+flow.getTransmitted()-infoMap.get(flow.getPortId()).getBytes();
            infoMap.get(flow.getPortId()).setBytes(flow.getReceived()+flow.getTransmitted());
            flow.setAvgSpeed((float)(bytes/5));
            long packet = flow.getPackets()-infoMap.get(flow.getPortId()).getPackets();
            long drop =flow.getDrop()-infoMap.get(flow.getPortId()).getDrop();
            infoMap.get(flow.getPortId()).setPackets(flow.getPackets());
            infoMap.get(flow.getPortId()).setDrop(flow.getDrop());
            flow.setLossRate((float)(drop/packet));
            if(userMap.get(flow.getPortId()) != null){
                String result1 = analysis.limitAnalysis(userMap.get(flow.getPortId()).getBytesLimit(),flow.getBytes());
                String result2 = analysis.lossRateAnalysis(userMap.get(flow.getPortId()).getLossRate(),flow.getLossRate());
                String result3 = analysis.threshold(userMap.get(flow.getPortId()).getThreshold(),flow.getSpeed());
            }
        }
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
