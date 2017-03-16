package com.bupt.wangfu.mgr.admin;

import com.bupt.wangfu.info.msg.MsgSdnConfig;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @ Created by HanB on 2016/7/24 0024.
 */
public class SdnConfig {
    private static String filePath = "./SdnConfig.properties";
    private static MsgSdnConfig lastMsgSdnConfig;

    public void init() {
        lastMsgSdnConfig = new MsgSdnConfig("admin","admin",50,0.4);
        AdminMgr.bloodMsg(lastMsgSdnConfig);
    }

    public boolean bloodMsgSdnConfig (MsgSdnConfig msc) {
        if (!isSame(lastMsgSdnConfig,msc)) {
            lastMsgSdnConfig = msc;
            writeProp(filePath,"odl账号",msc.getOdlAccount());
            writeProp(filePath,"odl密码",msc.getOdlPwd());
            writeProp(filePath,"集群规模",Integer.toString(msc.getN()));
            writeProp(filePath,"平均队列长度权值",Double.toString(msc.getWq()));

            // 群发消息
            AdminMgr.bloodMsg(msc);
            return true;
        }else
            return false;
    }

    private void writeProp(String filePath,String parameterName, String parameterValue){
        Properties prop = new Properties();
        try {
            InputStream fis = new FileInputStream(filePath);
            //从输入流中读取属性列表（键和元素对）
            prop.load(fis);
            //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
            //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(filePath);
            prop.setProperty(parameterName, parameterValue);
            //以适合使用 load 方法加载到 Properties 表中的格式，
            //将此 Properties 表中的属性列表（键和元素对）写入输出流
            prop.store(fos, "Update '" + parameterName + "' value");
        } catch (IOException e) {
            System.err.println("Visit "+filePath+" for updating "+parameterName+" value error");
        }
    }

    private void readProp(String filePath){
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(filePath));
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String Property = props.getProperty (key);
                System.out.println(key + Property);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSame(MsgSdnConfig m1, MsgSdnConfig m2) {
        return m1 != null && m1.getOdlAccount().equals(m2.getOdlAccount()) && m1.getOdlPwd().equals(m2.getOdlPwd())
                && m1.getN() == m2.getN() && m1.getWq() == m2.getWq();
    }

    public static void main(String[] args) {
        //writeProp(filePath,"name","value");
        SdnConfig sdnConfig = new SdnConfig();
        sdnConfig.readProp(filePath);
    }
}
