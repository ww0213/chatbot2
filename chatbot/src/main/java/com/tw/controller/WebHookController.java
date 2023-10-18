package com.tw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WebHookController {
	
	private static final Logger logger = LoggerFactory.getLogger(WebHookController.class);

	@Value("${weather.api.key}")
    private String WEATHER_API_KEY;

    @PostMapping("/webhook")
    public Map<String, Object> getWeatherInfo(@RequestBody Map<String, Object> request) {
    	
    	logger.info("Entered the /webhook endpoint");
    	
        Map<String, Object> response = new HashMap<>();

        Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
        Map<String, String> parameters = (Map<String, String>) queryResult.get("parameters");
        
        // 從 Dialogflow 獲取城市名稱
        String cityName = parameters.get("CityName");

        if (cityName == null || cityName.trim().isEmpty()) {
            response.put("fulfillmentText", "請問您想查詢哪個城市的天氣資訊?");
            return response;
        }

        String weatherApiUrl = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001";
        String url = String.format("%s?Authorization=%s&locationName=%s", weatherApiUrl, WEATHER_API_KEY, cityName);
        
        // 記錄請求的 URL
        logger.info("Requesting weather data from URL: {}", url);
        

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> weatherResponse = restTemplate.getForObject(url, Map.class);
        
        // 記錄返回的結果
        logger.info("Received weather data response: {}", weatherResponse);

        if (weatherResponse != null && !weatherResponse.isEmpty()) {
            List<Map<String, Object>> locations = (List<Map<String, Object>>) ((Map<String, Object>) weatherResponse.get("records")).get("location");
    
            if (locations != null && !locations.isEmpty()) {
                List<Map<String, Object>> weatherElements = (List<Map<String, Object>>) locations.get(0).get("weatherElement");
                
                StringBuilder weatherInfo = new StringBuilder();
                
             // 元素名稱轉換
                Map<String, String> nameMapping = Map.of(
                        "Wx", "天氣",
                        "PoP", "降雨機率",
                        "CI", "舒適度"
                );
                // 用於存儲最高和最低溫度
                String minT = null;
                String maxT = null;

                for (Map<String, Object> element : weatherElements) {
                    String elementName = (String) element.get("elementName");
                    List<Map<String, Object>> times = (List<Map<String, Object>>) element.get("time");
                    if (times != null && !times.isEmpty()) {
                        Map<String, Object> firstTime = times.get(0);
                        Map<String, Object> parameter = (Map<String, Object>) firstTime.get("parameter");
                        String parameterName = (String) parameter.get("parameterName");

                        // 特殊處理溫度
                        if ("MinT".equals(elementName)) {
                            minT = parameterName;
                            continue;
                        }
                        if ("MaxT".equals(elementName)) {
                            maxT = parameterName;
                            continue;
                        }

                        // 元素名稱轉換
                        String displayName = nameMapping.getOrDefault(elementName, elementName);
                        weatherInfo.append(displayName).append(": ").append(parameterName).append("\n");
                    }
                }

                // 溫度合併處理
                if (minT != null && maxT != null) {
                    weatherInfo.append("溫度: ").append(minT).append("~").append(maxT).append("度\n");
                }
                
                
                
                response.put("fulfillmentText", "這裡是 " + cityName + " 的近12小時天氣資訊: \n" + weatherInfo.toString());
            } else {
                response.put("fulfillmentText", "很抱歉，系統裡面沒有 " + cityName + " 的天氣資訊");
            }
        } else {
            response.put("fulfillmentText", "很抱歉，系統裡面沒有 " + cityName + " 的天氣資訊");
        }

        return response;
    }

}
