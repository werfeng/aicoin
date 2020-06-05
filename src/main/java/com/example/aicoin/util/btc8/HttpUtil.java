package com.example.aicoin.util.btc8;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static OkHttpClient client;

    private static final String DEFAULT_MEDIA_TYPE = "application/json; charset=utf-8";

    private static final int CONNECT_TIMEOUT = 5;

    private static final int READ_TIMEOUT = 7;

    /**
     * 单例模式  获取类实例
     *
     * @return client
     */
    private static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (OkHttpClient.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return client;
    }

    public static String doGet(String url) {

        try {
            Request request = new Request.Builder().url(url)
                    .addHeader(":authority","gate.8btc.com")
                    .addHeader(":method","GET")
                    .addHeader(":path","/w1/news/list?num=20&cat_id=4481&page=1")
                    .addHeader(":scheme","https")
                    .addHeader("from","web")
                    .addHeader("source-site","8btc")
                    .addHeader("sec-fetch-mode","cors")
                    .addHeader("sec-fetch-site","same-site")
                    .addHeader("x-forwarded-for","100.117.56.201")
                    .addHeader("origin","https://www.8btc.com")
                    .addHeader("referer","https://www.8btc.com/flash")
                    .addHeader("accept","application/json, text/plain, */*")
                    .addHeader("Accept-Language","zh-CN,zh;q=0.9")
//                    .addHeader("accept-encoding","gzip, deflate, br")
                    .addHeader("content_type","application/json;utf-8")
                    //author 每几分钟就会变，原来的失效，暂时不知道生成方法
                    .addHeader("authorization","{\"secretKeyVersion\":1,\"sign\":\"gDt1nQ3Ay458FG_Xj-Aum1wFXbzm88YuvASptrV90EaaaaaaaaaaaaaaaaaaaaaaTWeDNrlOJirimPSo2PO0DQ==\"}")
//                    .addHeader("authorization","{\"secretKeyVersion\":1,\"sign\":\"gDt1nQ3Ay458FG_Xj-Aum1wFXbzm88YuvASptrV90EwXaxdGdMBD4jhiz5x2iJHHTWeDNrlOJirimPSo2PO0DQ==\"}")
                    .addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                    .build();
            // 创建一个请求
            Response response = getInstance().newCall(request).execute();
            String result;
            if (response.body() != null) {
                result = response.body().string();
            } else {
                throw new RuntimeException("exception in OkHttpUtil,response body is null");
            }
            return result;
        } catch (Exception ex) {
            return null;
        }
    }


    public static String doPost(String url, String postBody, String mediaType, String callMethod) {
        try {
            MediaType createMediaType = MediaType.parse(mediaType == null ? DEFAULT_MEDIA_TYPE : mediaType);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(createMediaType, postBody))
                    .build();

            Response response = getInstance().newCall(request).execute();
            String result;
            if (response.body() != null) {
                result = response.body().string();
            } else {
                throw new IOException("exception in OkHttpUtil,response body is null");
            }
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String doPost(String url, Map<String, String> parameterMap, String callMethod) {
        try {
            List<String> parameterList = new ArrayList<>();
            FormBody.Builder builder = new FormBody.Builder();
            if (parameterMap.size() > 0) {
                parameterMap.keySet().forEach(parameterName -> {
                    String value = parameterMap.get(parameterName);
                    builder.add(parameterName, value);
                    parameterList.add(parameterName + ":" + value);
                });
            }

            FormBody formBody = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Response response = getInstance().newCall(request).execute();
            String result;
            if (response.body() != null) {
                result = response.body().string();
            } else {
                throw new IOException("exception in OkHttpUtil,response body is null");
            }
            return result;

        } catch (Exception ex) {
            return null;
        }
    }
}
