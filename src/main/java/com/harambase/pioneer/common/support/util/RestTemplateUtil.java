package com.harambase.pioneer.common.support.util;

import com.harambase.pioneer.common.HaramMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtil {

    public static HaramMessage sendRestRequest(String url, HttpMethod method, Object body) {

        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("userId", SessionUtil.getUserId());
        httpHeaders.set("Content-UserType", "application/json;charset=UTF-8");
        HttpEntity httpEntity = new HttpEntity<>(body, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HaramMessage> responseMessage = restTemplate.exchange(url, method, httpEntity, HaramMessage.class);

        return responseMessage.getBody();
    }
}
