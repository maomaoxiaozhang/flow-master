package com.bupt.wangfu.test;

/**
 * Created by TangLu on 2017/4/2.
 **/

public class Analysis {
    String result;
    public String limitAnalysis( long bytesLimite,long bytes){
        result = "normal";
        if(bytes > bytesLimite){
            result ="overLimit";
        }
        return result;
    }
    public String lossRateAnalysis(float lossRateLimit,float lossRate){
        result = "normal";
        if(lossRate > lossRateLimit){
            result ="packetLoss";
        }
        return result;
    }

    public String threshold(long threshold,long speed){
        result = "normal";
        if(speed > threshold){
            result ="overThreshold";
        }
        return result;
    }
}
