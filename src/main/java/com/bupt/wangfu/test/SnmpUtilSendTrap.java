package com.bupt.wangfu.test;

/**
 * @ Created by HanB on 2016/8/1 0001.
 */

import com.sun.jndi.cosnaming.IiopUrl;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SnmpUtilSendTrap {

    private Snmp snmp = null;

    private IiopUrl.Address targetAddress = null;

    public void initComm() throws IOException {
        // 设置管理进程的IP和端口
        targetAddress = (IiopUrl.Address) GenericAddress.parse("udp:10.108.164.152/162");
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
    }
    public void sendPDU() throws IOException {

        // 设置 target
        CommunityTarget target = new CommunityTarget();
        target.setAddress((Address) targetAddress);
        // 通信不成功时的重试次数
        target.setRetries(2);
        // 超时时间
        target.setTimeout(1500);
        // snmp版本
        target.setVersion(SnmpConstants.version2c);
        // 创建 PDU
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.3377.10.1.1.1.1"), new OctetString("Snmp Trap")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.3377.10.1.1.1.2"), new OctetString("Link Error!")));
        pdu.setType(PDU.TRAP);
        // 向Agent发送PDU
        snmp.send(pdu, target);
    }
}
