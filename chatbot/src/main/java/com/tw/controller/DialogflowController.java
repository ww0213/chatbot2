package com.tw.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;

@RestController
public class DialogflowController {

    @Value("${dialogflow.project-id}")
    private String projectId;

    @Value("${dialogflow.language-code}")
    private String languageCode;

    @Value("${dialogflow.agent-id}")
    private String agentId;

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody String message) throws Exception {
        // 設定認證信息
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                getClass().getClassLoader().getResourceAsStream("config/chat-bot-400012-b4e19b8828d8.json")
        );

        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)) {
            // 設定 Dialogflow 會話 ID，可以是任何唯一的字串
            String sessionId = "Alan";

            // 設定 Dialogflow 查詢輸入（使用從前端接收到的message）
            QueryInput queryInput = QueryInput.newBuilder()
                    .setText(
                            TextInput.newBuilder()
                                    .setText(message)
                                    .setLanguageCode(languageCode)
                                    .build()
                    )
                    .build();

            // 使用指定的代理ID向Dialogflow發送查詢請求
            SessionName session = SessionName.of(projectId, sessionId);
            DetectIntentResponse response = sessionsClient.detectIntent(
                    DetectIntentRequest.newBuilder()
                            .setSession(session.toString())
                            .setQueryInput(queryInput)
                            .build()
            );

            // 解析回應，並獲取Dialogflow的回覆
            QueryResult queryResult = response.getQueryResult();
            String fulfillmentText = queryResult.getFulfillmentText();

            return fulfillmentText;
        }
    }
}