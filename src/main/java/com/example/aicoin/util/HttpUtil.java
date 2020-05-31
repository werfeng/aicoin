package com.example.aicoin.util;

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
                    .addHeader("Referer","https://www.aicoin.cn/")
                    .addHeader("accept","*/*")
                    .addHeader("Accept-Language","zh-CN,zh;q=0.9")
                    .addHeader("content_type","application/json;utf-8")
                    .addHeader("connection","Keep-Alive")
                    .addHeader("Cookie","GA1.2.1267179066.1582337235; _pk_id.2.57ea=19be71507ad1ae02.1583394094.2.1584599215.1584599208.; wafatcltime=1590806539678; wafatclconfirm=ndlloECIXJ22q6DnEY2MhqVupC2qWk5+qQHTy4u4RCBnTW3814hMgtIu1+Pvyt89V1Z6hts+mmXuPmjxRta6uV+5dnLf2xWt0fHwuDVQTUkmziAEZjZeYFA1z9dXhG/BAX9nIe0H9d+8rbLBmdcxZXrcmK3HleEiCwl7KYkYqd9TgamsfD1236ZB62+sB8y3F116AYZpF51tBRXrQ5bIWRUtdqTMDD8godSHiDQbpHTNop6bCeL6mW/3nkZgsQKg4pN2qbHh9KWhCFTVqRQHnZY5tEoT9A36e6DA7JFkFooC+gaHLT7WJjCKLmkHqKNnVzQHPiaq0O6HLJu9zdlN1RNJOZXE+Vh9pIBLOGyeKj4kmx7rD2OQoJtRj6xgb7jb5IkzmaGxTCi/RG13UaawYVdIl7ZsmFfGXWvizsMBdNaYzHlYSmZEoIgKcOr0bgT8qSHPZE3LXmC+VwctYxIralI4hkGrGc5evbFqfOMGND5UQ5V/+ezSb+Skc9IgaAsPq5+NZFyFq1SlV98XSObjhdW4G3tsAFNTd1HrUolAQQmYg1HGDJxrtsEFdTRaDA7KSEL32JXgNik9ZZMimNPRqICv7mGpqQRijOiPeDq0k+ATsqrp72Wdxv6zPeSgxDYvyQ9/szCZpmfH7PgUsxzHx3A16SObR/JGBhMSJY0k/lUxHQv5iHmEMZqaNsPzN2SwxbDEt8V5CGHyVi7wG8t0YQZkY6HnH1uLTHwIz3qnLFnt/76cx30uiEAEM6Q0wZAs; wafatcltoken=10ae5b859c0926399ad1c4b1e425cea4; HWWAFSESID=d970479737e5f76238; HWWAFSESTIME=1590806539678; Hm_lvt_3c606e4c5bc6e9ff490f59ae4106beb4=1589852081,1590806542; _gid=GA1.2.1972969306.1590806542; _gat_gtag_UA_108140256_2=1; _pk_testcookie..undefined=1; _pk_ref.2.f745=%5B%22%22%2C%22%22%2C1590806560%2C%22https%3A%2F%2Fwww.aicoin.cn%2F%22%5D; _pk_ses.2.f745=1; XSRF-TOKEN=eyJpdiI6IkdBXC9vRzh1NzMyZTd3Z2Q0MEt0SXd3PT0iLCJ2YWx1ZSI6IlwvSlFPVHQ1OXh5S0FXMHIxYm4rWDNnUmsxbXBiR1ZKRjhpbXk3a2lNaWVsZVBqemxHb0c3U21YT0hTN1wvSWxXc1k2Q1VuNWt4b3dwR00zMzJuUlRTY3c9PSIsIm1hYyI6ImJlZWQxMWMzOTg2OGQ3NDgzODdmZDU4Y2Q2ODA2YjI1NmU0ZDQ5NWRiNTMxZDNiNTkxYjVlYzI0MDAxNjI5MjcifQ%3D%3D; aicoin_session=eyJpdiI6IkRHZ1RiajRxYXFXUEdlRUZjTG8rQnc9PSIsInZhbHVlIjoiTWUxR0RqVkZwVUJRVXpjaVh4aGhjOXlRV0tIVWwrSEZOVk5QR3EzcnpTYjVTcmpPYnQwdDg5RU1BOWV6VTBEcGcxbDV1dXlaTlgxdEMrSjhEdGM0Qmc9PSIsIm1hYyI6IjllZTlhNGFiYjNkZGViYTJkZTEzNDQ3NjY1YzI3MTVmNWRhMDQ0OTdiYWVjY2FhODZkNzlhNTE0MjRhZDIzNzcifQ%3D%3D; _pk_id.2.f745=8ce7276221c01183.1582337235.13.1590806567.1590806560.; Hm_lpvt_3c606e4c5bc6e9ff490f59ae4106beb4=1590806568")
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
