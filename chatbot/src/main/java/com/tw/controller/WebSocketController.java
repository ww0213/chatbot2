package com.tw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

	@Autowired
	private DialogflowController dialogflowController;

	@MessageMapping("/send")
	@SendTo("/topic/messages")
	public String sendMessageToDialogflow(String message) throws Exception {
		
		System.out.println("接收到的訊息: " + message);  // 添加日誌
		
		// 調用DialogflowController的sendMessage方法來獲取Dialogflow的回應。
		String reply = dialogflowController.sendMessage(message);
		
		System.out.println("來自Dialogflow的回覆: " + reply);
		
		return reply;
	}

}
