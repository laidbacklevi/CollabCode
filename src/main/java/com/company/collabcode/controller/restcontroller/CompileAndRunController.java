package com.company.collabcode.controller.restcontroller;

import com.google.api.client.util.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompileAndRunController {

    private final String JDOODLE_CLIENT_ID = "9dcc71c931b5c9537c8d753088cfb7a1";
    private final String JDOODLE_CLIENT_SECRET = "4a13138389ef3d83f4322052174d2cdf7ca50920164e78cd98511c326a7fc213";
    private final String JDOODLE_ENDPOINT_URL = "https://api.jdoodle.com/v1/execute";

    /*
    @RequestMapping(value = "/compile-and-run", method= RequestMethod.GET)
    private String compileAndRun(@Param("code") String code, @Param("input") String stdInput, @Param("language") String language) {
        System.out.println("API_CALLED");
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("script", code);
        dataMap.put("language", language);
        dataMap.put("versionIndex", "0");
        dataMap.put("clientId", JDOODLE_CLIENT_ID);
        dataMap.put("clientSecret", JDOODLE_CLIENT_SECRET);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(dataMap, requestHeaders);

        String resultJSON = restTemplate.postForObject(
                JDOODLE_ENDPOINT_URL,
                httpEntity,
                String.class
        );
        return resultJSON;
    }
    */
}
