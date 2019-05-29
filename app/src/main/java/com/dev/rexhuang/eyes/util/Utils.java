package com.dev.rexhuang.eyes.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

/**
 * *  created by RexHuang
 * *  on 2019/5/29
 */
public class Utils {

    /**
     * 为get方法拼接url
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(String url, HashMap<String,String> params){
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");
        for (int i = 0 ; i < params.size() ; i ++){
            String value = null;
            try {
                value = URLEncoder.encode(values.next(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuffer.append(keys.next() + "=" + value);
            if (i != params.size() - 1 ){
                stringBuffer.append("&");
            }

        }
        url += stringBuffer.toString();
        Log.d("attachHttpGetParams",url);
        return url ;
    }
}
