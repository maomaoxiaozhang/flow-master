package com.bupt.wangfu.info.entry;

/**
 * Created by HanB on 2016/7/13 0013.
 */
public class Link {
    private String linkId; // 连接
    private String src; // 源
    private String dest; //端

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
