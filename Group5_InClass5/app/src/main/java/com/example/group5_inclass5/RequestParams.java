package com.example.group5_inclass5;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class RequestParams {
    private HashMap<String,String> params;

    public RequestParams(HashMap<String, String> params) {
        params = new HashMap<>();
    }
    public RequestParams addParams(String key,String value)
    {
        try {
            params.put(key, URLEncoder.encode(value,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }
}
