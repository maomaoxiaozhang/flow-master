package com.bupt.wangfu.info.msg;

import java.io.Serializable;

/**
 * @ Created by HanB on 2016/11/2 0002.
 */
public class GroupLinks implements Serializable{
    public String srcGroup;
    public String tarGroup;

    public GroupLinks(String src, String tar) {
        srcGroup = src;
        tarGroup = tar;
    }
}
