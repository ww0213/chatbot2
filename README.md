# chatbot_Springboot
Setup:  
使用Google Cloud Console建立一個項目，並啟用Dialogflow API。  
設定身份驗證。例如，從 Cloud Console 建立一個服務帳號，下載其 json 憑證文件  
設定application.properties  設定成你的專案  
dialogflow.credentials.path=classpath:config/YOUR_credentials_path.json  
dialogflow.project-id=YOUR_project-id  
dialogflow.language-code=zh-TW  
dialogflow.agent-id=YOUR_agent-id  
  
設定氣象API  
weather.api.key=YOUR_api.key  
