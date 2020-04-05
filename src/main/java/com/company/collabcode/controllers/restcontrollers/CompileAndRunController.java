package com.company.collabcode.controllers.restcontrollers;

import com.company.collabcode.utils.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CompileAndRunController {
    /*
    @RequestMapping(value = "/compile-and-run", method= RequestMethod.GET)
    private String compileAndRun(@Param("code") String code, @Param("input") String stdInput, @Param("language") String language) {
        System.out.println("API_CALLED");
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("script", code);
        dataMap.put("language", language);
        dataMap.put("versionIndex", "0");
        dataMap.put("clientId", Constant.JDOODLE_CLIENT_ID);
        dataMap.put("clientSecret", Constant.JDOODLE_CLIENT_SECRET);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(dataMap, requestHeaders);

        String resultJSON = restTemplate.postForObject(
                Constant.JDOODLE_ENDPOINT_URL,
                httpEntity,
                String.class
        );
        return resultJSON;
    }
    */
}
