package com.bupt.wangfu.mgr.admin.database;

import com.bupt.wangfu.info.entry.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ Created by HanB on 2016/7/12 0012.
 */
public class DataCollect {
    public static void insertController(Controller controller) {
        String url = controller.url;
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        //if (selectController(controller) != null) return;
        String sql = "insert controller (groupName,url) VALUE ('" + controller.getGroupName() +"','"+ url + "')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            connector.close();
        }
    }

    public static ResultSet selectController(Controller controller) {
        String url = controller.url;
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        String sql = "select * from controller WHERE url = '" + url + "'";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void updateController (Controller controller) {
        String sql = "update controller set url = '" + controller.url + "' where groupName = '" + controller.getGroupName() + "'";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static void deleteController(Controller controller) {
        String sql = "delete from controller where groupName = '" + controller.getGroupName() + "'";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static void insertSwitch(Switch s) {
        String sql = "insert switch (dpid,controllerUrl) values ('"+ s.getDPID() + "','"+ s.getController() + "')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static ResultSet selectSwitch (Switch s) {
        ResultSet rs = null;
        // 查询交换机
        return rs;
    }

    public static void updateSwitch (Switch s) {

    }

    public static void deleteSwitch (Switch s) {

    }

    public static void insertPort(Port p) {
        String sql = "insert port (portName,tpid,switchId,enport,deport) values ('" + p.getPortName() + "','" + p.getTpid() +
                "','" + p.getSwitchId() + "','" + p.getEnport() + "','" + p.getDeport() + "')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static ResultSet selectPort (Port p) {
        ResultSet rs = null;
        // 查询端口
        return rs;
    }

    public static void updatePort (Port p) {

    }

    public static void deletePort (Port p) {

    }

    public static void insertHost(WSNHost host) {
        String sql = "insert host (mac,ip,switchId,portId,lastseen) values ('" + host.getMac() + "','" + host.getIpAddr() + "','"
                + host.getSwitch_id() + "','" + host.getPort() + "','" + host.getLastSeen() + "')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static ResultSet selectHost (WSNHost h) {
        ResultSet rs = null;
        // 查询主机
        return rs;
    }

    public static void updateHost (WSNHost h) {

    }

    public static void deleteHost (WSNHost h) {

    }

    public static void insertQueue(Queue q) {
        String sql = "insert queue (queueName,portId,enqueue,dequeue,brandWidth,queueLength) values ('" + q.getQueueName() +
                "','" + q.getPortId() + "','" + q.getEnqueue() + "','" + q.getDequeue() + "','" + q.getBrandWidth() +
                "','" + q.getQueueLength() + "')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static ResultSet selectQueue (Queue q) {
        ResultSet rs = null;
        // 查询队列
        return rs;
    }

    public static void updateQueue (Queue q) {

    }

    public static void deleteQueue (Queue q) {

    }

    public static void insertLink (Link l) {
        String sql = "insert link (linkId,src,dest) values ('" + l.getLinkId() + "','" + l.getSrc() + "','" + l.getDest() + "')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static ResultSet selectLink (Link l) {
        ResultSet rs = null;
        // 查询连接关系
        return rs;
    }

    public static void updateLink (Link l) {

    }

    public static void deleteLink (Link l) {

    }

    /*public static void insertFlowInfo (FlowInfo flowInfo) {
        String sql = "insert flowInfo (dpid,flowCount,lastseen) values ('" + flowInfo.getDpid() + "','" + flowInfo.getFlowCount() + "','" + flowInfo.getLastseen() +"')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }
        // 检查相关交换机记录数
        ResultSet rs = selectFlowInfoByDPID(flowInfo.getDpid());
        try {
            rs.last();
            if (rs.getRow() > 10) {
                rs.first();
                deleteFlowInfo(rs.getInt(1));
            }
        }catch (Exception e) { e.printStackTrace(); }
    }*/

    public static ResultSet selectFlowInfoByDPID(String dpid) {

        String sql = "select * from flowInfo WHERE dpid = '" + dpid + "' order by lastseen ASC";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void deleteFlowInfo(int flowInfoId) {
        String sql = "DELETE from flowInfo where flowInfoId = " + flowInfoId;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    //创建表avgSpdByPort，表项为次数、控制器、交换机、端口号、速率；主键为次数、控制器、交换机、端口号
    public static void createAvgSpdByPort(){
        String sql = "create table avgSpdByPort(times int, controllerId varchar(20), switchId varchar(20), " +
                "portId varchar(20), speed int, primary key(times, controllerId, switchId, portId))";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    //根据次数、控制器、交换机、端口号存储某一时间内的平均速率
    public static void insertAvgSpdByPort(int times, String controllerId, String switchId, String portId, Long speed){
        String sql = "insert into avgSpdByPort values('" + times + "','" + controllerId + "','" + switchId + "','" +
                portId + "','" + speed + "')";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    //从avgSpdByPort表中根据次数、控制器、交换机、端口号取出某一时间段的平均速率
    public static ResultSet selectAvgSpdByPort(int times, String controllerId, String switchId, String portId){
        String sql = "select speed from avgSpdByPort where times = " + times + " and controllerId = " + "'" +
                controllerId + "'" + " and switchId = " + "'"+ switchId + "'" + " and portId = " + "'" + portId + "'";
//        System.out.println(sql);
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    //创建表sumByPort（本表在每次刷新时需要重置），表项为控制器、交换机、端口号、流量总量，将控制器、交换机、端口号设置为主键
    public static void createSumByPort(){
        String sql = "create table sumByPort(controllerId varchar(20), switchId varchar(20), portId varchar(20)," +
                " sum int, primary key(controllerId, switchId, portId))";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    //根据控制器、交换机、端口号添加流量总量
    public static void insertSumByPort(String controllerId, String switchId, String portId, Long sum){
        String sql = "insert into sumByPort values('" + controllerId + "','" + switchId + "','" +
                portId + "','" + sum + "')";
        System.out.println(sql);
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    //根据控制器、交换机、端口号获取流量总量
    public static ResultSet selectSumByPort(String controllerId, String switchId,String portId){
        String sql = "select sum from sumByPort where controllerId = '" + controllerId + "' and switchId = " +
                "'" + switchId + "' and portId = '" + portId + "'";
        System.out.println(sql);
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    //删除所有表项，保留表的结构（truncate为DDL语言，无法恢复）
    public static void truncateTable(String tableName){
        String sql = "truncate table " + tableName;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }


    public static ResultSet selectMonth() {

        String sql = "select distinct controller, switch,node from  `odl-info`.`day`";
        DBConnector connector = new DBConnector(sql);
        ResultSet resultSet = null;
        try {
            resultSet = connector.pst.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void  updateMonth(String controller,int day) {
        DBConnector connector;
        String sql1 = "select distinct controller,switchId,node from `odl-info`.`day` where controller = '"+controller+"'";
        String switchId=null; String node=null;
        int received=0,transmitted=0,bytes=0,i;
        float lossRate=0,speed=0;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        connector= new DBConnector(sql1);
        try {
            resultSet1 = connector.pst.executeQuery(sql1);
            while(resultSet1.next()){
                switchId = resultSet1.getString("switchId");
                node=resultSet1.getString("node");
                String sql2 = "select * from `odl-info`.day  where controller = '" + controller + "'and switchId = '" +switchId + "' and node ='" + node + "'";
                connector= new DBConnector(sql2);
                resultSet2 = connector.pst.executeQuery(sql2);
                i=0;
                while(resultSet2.next()){
                    speed=speed+resultSet2.getFloat("speed");
                    lossRate=resultSet2.getFloat("lossRate")+lossRate;
                    if((resultSet2.getInt("transmitted")>=transmitted&&(resultSet2.getInt("received")>=received))){
                        transmitted=resultSet2.getInt("transmitted");
                        received=resultSet2.getInt("received");
                        bytes=resultSet2.getInt("bytes");
                    }
                    i++;
                }
                speed=speed/i;
                lossRate=lossRate/i;
                String sql3 = "insert into `odl-info`.month (day, controller, switchId, node, received, transmitted, bytes, speed, lossRate) values ( '" +day +"', '"+ controller +"', '"+switchId+"', '"+node+"', '"+received+"', '"+transmitted+"', '"+bytes+"', '"+speed+"', '"+lossRate+"')";
                connector= new DBConnector(sql3);
                connector.pst.execute(sql3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.close();
        }
    }

    public static void  updateYear(String controller,int month) {
        DBConnector connector;
        String sql1 = "select distinct controller,switchId,node from `odl-info`.`month` where controller = '"+controller+"'";
        String switchId=null; String node=null;
        int received=0,transmitted=0,bytes=0,i;
        float lossRate=0,speed=0;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        connector= new DBConnector(sql1);
        try {
            resultSet1 = connector.pst.executeQuery(sql1);
            while(resultSet1.next()){
                switchId = resultSet1.getString("switchId");
                node=resultSet1.getString("node");
                String sql2 = "select * from `odl-info`.month  where controller = '" + controller + "'and switchId = '" +switchId + "' and node ='" + node + "'";
                connector= new DBConnector(sql2);
                resultSet2 = connector.pst.executeQuery(sql2);
                i=0;
                while(resultSet2.next()){
                    speed=speed+resultSet2.getFloat("speed");
                    lossRate=resultSet2.getFloat("lossRate")+lossRate;
                    if((resultSet2.getInt("transmitted")>=transmitted&&(resultSet2.getInt("received")>=received))){
                        transmitted=resultSet2.getInt("transmitted");
                        received=resultSet2.getInt("received");
                        bytes=resultSet2.getInt("bytes");
                    }
                    i++;
                }
                speed=speed/i;
                lossRate=lossRate/i;
                String sql3 = "insert into `odl-info`.year (month, controller, switchId, node, received, transmitted, bytes, speed, lossRate) values ( '" +month +"', '"+ controller +"', '"+switchId+"', '"+node+"', '"+received+"', '"+transmitted+"', '"+bytes+"', '"+speed+"', '"+lossRate+"')";
                connector= new DBConnector(sql3);
                connector.pst.execute(sql3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.close();
        }
    }

    public static void  updateSum(String controller,int year) {
        DBConnector connector;
        String sql1 = "select distinct controller,switchId,node from `odl-info`.`year` where controller = '"+controller+"'";
        String switchId=null; String node=null;
        int received=0,transmitted=0,bytes=0,i;
        float lossRate=0,speed=0;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        connector= new DBConnector(sql1);
        try {
            resultSet1 = connector.pst.executeQuery(sql1);
            while(resultSet1.next()){
                switchId = resultSet1.getString("switchId");
                node=resultSet1.getString("node");
                String sql2 = "select * from `odl-info`.year  where controller = '" + controller + "'and switchId = '" +switchId + "' and node ='" + node + "'";
                connector= new DBConnector(sql2);
                resultSet2 = connector.pst.executeQuery(sql2);
                i=0;
                while(resultSet2.next()){
                    speed=speed+resultSet2.getFloat("speed");
                    lossRate=resultSet2.getFloat("lossRate")+lossRate;
                    if((resultSet2.getInt("transmitted")>=transmitted&&(resultSet2.getInt("received")>=received))){
                        transmitted=resultSet2.getInt("transmitted");
                        received=resultSet2.getInt("received");
                        bytes=resultSet2.getInt("bytes");
                    }
                    i++;
                }
                speed=speed/i;
                lossRate=lossRate/i;
                String sql3 = "insert into `odl-info`.sum (year, controller, switchId, node, received, transmitted, bytes, speed, lossRate) values ( '" +year +"', '"+ controller +"', '"+switchId+"', '"+node+"', '"+received+"', '"+transmitted+"', '"+bytes+"', '"+speed+"', '"+lossRate+"')";
                connector= new DBConnector(sql3);
                connector.pst.execute(sql3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.close();
        }
    }


    public static void insertDay(FlowInfo flow) {
        String sql = "INSERT INTO `odl-info`.`day` (controller,switch,node,received,transmitted,bytes,speed,lossRate) VALUES( '" + flow.getControllerId() +"','"+flow.getSwitchId()+"','"+flow.getPortId()+"','"+flow.getReceived()+"','"+flow.getTransmitted()+"','"+flow.getBytes()+"','"+flow.getSpeed()+"','"+flow.getLossRate()+"'";
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static void deleteDay(String  controller) {
        String sql = "DELETE from  `odl-info`.`day` where controller = " + controller;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static void deleteMonth(String  controller) {
        String sql = "DELETE from  `odl-info`.`monyh` where controller = " + controller;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }

    public static void deleteYear(String  controller) {
        String sql = "DELETE from  `odl-info`.`year` where controller = " + controller;
        DBConnector connector = new DBConnector(sql);
        try {
            connector.pst.execute(sql);
        }catch (Exception e ) {
            e.printStackTrace();
        }finally {
            connector.close();
        }
    }



    public static void main(String[] args) {
        updateMonth("1",1);
        deleteDay("1");

        //FlowInfo[] flowInfos = new FlowInfo[15];
        //for (int i = 0; i < flowInfos.length; i ++) {
          //  flowInfos[i] = new FlowInfo();
           // flowInfos[i].setDpid("openflow:151943505461327");
            //flowInfos[i].setFlowCount(Integer.toString((i+1) * 452));
            //flowInfos[i].setLastseen(Long.toString(System.currentTimeMillis()));
            //insertFlowInfo(flowInfos[i]);
            //try {
              //  Thread.sleep(5000);
            //}catch ( Exception e) { e.printStackTrace(); }
        //}
        /*Controller ctrl = new Controller("http://10.108.165.188:8181");
        ctrl.setGroupName("G10");
        DataCollect.insertController(ctrl);

        Switch swch = new Switch();
        swch.setController(ctrl.getUrl());
        swch.setDPID("openflow:16359272205130");
        DataCollect.insertSwitch(swch);

        Port port = new Port();
        port.setPortName("tap2");
        port.setTpid(swch.getDPID() + ":3");
        port.setSwitchId(swch.getDPID());
        port.setEnport("4651564");
        port.setDeport("2456185");
        DataCollect.insertPort(port);

        WSNHost host = new WSNHost();
        host.setMac("52:54:00:b4:46:50");
        host.setIpAddr("192.168.100.50");
        host.setSwitch_id(swch.getDPID());
        host.setPort(swch.getDPID() + ":1");
        host.setLastSeen("1467795754278");
        DataCollect.insertHost(host);

        Queue qu = new Queue();
        qu.setQueueName("queue1");
        qu.setPortId(port.getTpid());
        qu.setEnqueue("561564");
        qu.setDequeue("153743");
        qu.setBrandWidth("200000000");
        qu.setQueueLength(Integer.toString(Integer.parseInt(qu.getEnqueue()) - Integer.parseInt(qu.getDequeue())));
        DataCollect.insertQueue(qu);

        Link lk = new Link();
        lk.setLinkId(swch.getDPID() +"/"+ host.getMac());
        lk.setSrc(swch.getDPID());
        lk.setDest(swch.getDPID());
        DataCollect.insertLink(lk);*/
    }
}

