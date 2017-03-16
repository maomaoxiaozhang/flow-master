package com.bupt.wangfu.mgr.admin;

import com.bupt.wangfu.info.msg.GroupLinks;
import com.bupt.wangfu.info.msg.GroupUnit;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Created by HanB on 2016/10/26 0026.
 */
@WebService
public class TopoService implements Runnable{

    public List<GroupUnit> getGroups() {
        //System.out.println("new webservice request groups!");
        List<GroupUnit> list = new ArrayList<>();
        //Collections.addAll(list, AdminMgr.lookupGroups());
        for (int i = 1; i <= 6; i ++) {
            GroupUnit gu = new GroupUnit("10.108.164.15"+i, Integer.parseInt("1001"+i), "G"+i);
            gu.controllerAddr = "http://10.108.165.188:8181";
            list.add(gu);
        }
        return list;
    }

    public List<GroupLinks> getLinks() {
        //System.out.println("new webservice request links!");
        List<GroupLinks> list = new ArrayList<>();
        list.add(new GroupLinks("G1", "G3"));
        list.add(new GroupLinks("G2", "G3"));
        list.add(new GroupLinks("G3", "G4"));
        list.add(new GroupLinks("G4", "G5"));
        list.add(new GroupLinks("G4", "G6"));
        return list;
    }

    @Override
    @WebMethod(exclude=true)
    public void run() {
        Endpoint.publish("http://localhost:1234/TopoService",new TopoService());
    }
}
