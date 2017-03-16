package com.bupt.wangfu.snmp;

import com.bupt.wangfu.info.entry.Switch;
import com.bupt.wangfu.info.entry.WSNHost;
import com.bupt.wangfu.snmp.info.HostInfo;
import com.bupt.wangfu.snmp.info.SwitchInfo;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @ Created by HanB on 2016/7/28 0028.
 */
public class SnmpMgr {
    private String sysInfoOid = "1.3.6.1.2.1.1.1.0"; // 系统信息
    private String sysNameOid = "1.3.6.1.2.1.1.5.0"; // 主机名
    private String sysMemoryOid = "1.3.6.1.2.1.25.2.2.0"; // 系统内存
    private String cpuRateOid = "1.3.6.1.4.1.2021.11.11.0"; // CPU利用率
    private String ifNumOid = "1.3.6.1.2.1.2.1.0"; // 网络接口数量
    public SwitchInfo getSwitchInfo(Switch s) {
        SwitchInfo switchInfo = new SwitchInfo();
        return switchInfo;
    }

    public HostInfo getHostInfo(WSNHost host) {
        ArrayList<String> oid = new ArrayList<>();
        oid.add(sysInfoOid);
        oid.add(sysNameOid);
        oid.add(sysMemoryOid);
        oid.add(cpuRateOid);
        oid.add(ifNumOid);
        PDU pdu = getSnmpInfo("10.108.165.188",oid);
        if (pdu != null) {
            return new HostInfo(
                    pdu.getVariable(new OID(sysInfoOid)).toString(),
                    pdu.getVariable(new OID(sysNameOid)).toString(),
                    pdu.getVariable(new OID(sysMemoryOid)).toString(),
                    pdu.getVariable(new OID(cpuRateOid)).toString(),
                    pdu.getVariable(new OID(ifNumOid)).toString());
        }else {
            System.out.println("获取设备信息失败!");
            return null;
        }
    }

    private PDU getSnmpInfo (String ipAddr, ArrayList<String> oid) {
        try {
            Snmp snmp = new Snmp(new DefaultUdpTransportMapping());

            CommunityTarget target = new CommunityTarget(); // agent对象
            target.setCommunity(new OctetString("public")); // 设置共同体名
            target.setVersion(SnmpConstants.version2c); // 设置版本
            target.setAddress(new UdpAddress(ipAddr + "/161")); // 设置IP地址和端口号,这里竟然用'/'来分隔,
            target.setRetries(1);  //设置重试次数
            target.setTimeout(5000); //设置超时

            snmp.listen(); // 监听

            PDU request = new PDU();  //new request PDU包
            request.setType(PDU.GET); //设置PDU类型
            for (String anOid : oid) {  //  OID添加
                request.add(new VariableBinding(new OID(anOid)));
            }

            ResponseEvent responseEvent = snmp.send(request, target); // 发出request PDU
            PDU response = responseEvent.getResponse(); // 接收response PDU

            //response PDU包解析
            if(response != null){
                if(response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError){
                    return response;
                }else{
                    System.out.println("get error:"+response.getErrorStatusText());
                }
            }else{
                System.out.println("get response error snmp");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
