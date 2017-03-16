package com.bupt.wangfu.test;

import com.bupt.wangfu.info.msg.MsgException;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @ Created by HanB on 2016/8/5 0005.
 */
public class Test2 {
    public static void main(String[] args) {

        MsgException msgException = new MsgException();
        msgException.groupName = "G1";
        msgException.switchID = "*999";
        msgException.hostAddr = "192.168.100.50";
        msgException.excpDescrip = "Link Error!";

        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress("10.108.164.152",30006));
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(msgException);
            oos.flush();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
