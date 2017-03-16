package com.bupt.wangfu.info.msg;

import java.io.Serializable;

/**
 * @ Created by HanB on 2016/6/21 0021.
 */
public class MsgSdnConfig implements Serializable{
    private static final long serialVersionUID = 1L;
    private int N; //集群规模
    private String OdlAccount; //opendaylight账号密码
    private String OdlPwd;
    private double wq; //平均队列长度权值

    public MsgSdnConfig(String odlAccount, String odlPwd, int n,  double wq) {
        N = n;
        OdlAccount = odlAccount;
        OdlPwd = odlPwd;
        this.wq = wq;

    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public String getOdlAccount() {
        return OdlAccount;
    }

    public void setOdlAccount(String odlAccount) {
        OdlAccount = odlAccount;
    }

    public String getOdlPwd() {
        return OdlPwd;
    }

    public void setOdlPwd(String odlPwd) {
        OdlPwd = odlPwd;
    }

    public double getWq() {
        return wq;
    }

    public void setWq(double wq) {
        this.wq = wq;
    }

}
