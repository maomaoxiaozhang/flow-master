package com.bupt.wangfu.mgr.admin;

import com.bupt.wangfu.info.msg.*;
import com.bupt.wangfu.ldap.WSNTopicObject;
import com.bupt.wangfu.mgr.admin.database.Import;
import com.bupt.wangfu.mgr.topictree.TopicTreeManager;
import com.bupt.wangfu.mgr.wsnPolicy.ShorenUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class AdMsgService extends AdminUtil implements Runnable {

	private AdminMgr admr;
	private MulticastSocket ms;
	private static ByteArrayInputStream bais;
	private static ObjectInputStream ois;
	private byte[] data;

	public AdMsgService(AdminMgr admr, MulticastSocket s) {
		this.admr = admr;
		ms = s;
		if (ms != null) {
			try {
				data = new byte[1024 * 8];
				bais = new ByteArrayInputStream(data);
				DatagramPacket datagramPacket = new DatagramPacket(data,data.length);
				ms.receive(datagramPacket);//接收数据包
				ois = new ObjectInputStream(bais);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		try {
			Object msg = ois.readObject();
			System.out.println("msg type:" + msg.toString());
			process(msg);
			bais.close();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void process(Object msg) {
		if (msg instanceof GroupUnit) {
            GroupUnit gu = (GroupUnit) msg;
            PolicyDB pdb = new PolicyDB();

            if (!groups.keySet().contains(gu.name)) {
                pdb.time = System.currentTimeMillis();
                pdb.clearAll = true;
                pdb.pdb.addAll(ShorenUtils.getAllPolicy());

                System.out.println("groups add: " + gu.name);
                groups.put(gu.name, gu);
                pdb.groupMsg.putAll(groups);

                admr.globalUtil.addController(gu.name,gu.controllerAddr);
                ui.newGroup(gu);
            } else {
                pdb.clearAll = false;
            }
            System.out.println(pdb.clearAll);
            AdminMgr.bloodMsg(pdb);
            System.out.println("write finished!");

        } else if (msg instanceof WSNTopicObject) {
            WSNTopicObject currenTopicTree = TopicTreeManager.topicTree;
            AdminMgr.bloodMsg(currenTopicTree);

        } else if (msg instanceof LSA){
            //System.out.println("new lsa message!!!");
            lsdb.add((LSA)msg);
        }
        else if (msg instanceof NotifyTopics) {
            AdminMgr.bloodMsg(NotifyTopics.NotifyTopicCodeMap);
        }
        else if (msg instanceof MsgException) {
            ui.showException((MsgException)msg);
        }
		else if (msg instanceof FlowInfos){
			FlowInfos flowInfos = (FlowInfos) msg;
			Import imt =  new Import();
			imt.collect(admr.times,flowInfos);
		}
	}
}
