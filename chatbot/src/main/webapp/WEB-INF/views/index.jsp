<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Chat Window</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
</head>
<body>
    <h1>聊天室</h1>
    <textarea id="chatWindow" rows="20" cols="50"></textarea><br>
    <input type="text" id="userInput">
    <button onclick="sendMessage()">Send</button>
    <button onclick="closeConnection()">Close Chat</button>

    <script>
        var socket = new SockJS('/chat');
        var stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
        	console.log('WebSocket連接成功');
            stompClient.subscribe('/topic/messages', function(messageOutput) {
                document.getElementById('chatWindow').value += '機器人: ' + messageOutput.body + '\n';
            });
        });

        function sendMessage() {
            var message = document.getElementById('userInput').value;
            document.getElementById('chatWindow').value += '我: ' + message + '\n';
            stompClient.send("/app/send", {}, message);
            document.getElementById('userInput').value = '';
        }
        
       
        function closeConnection() {
            if(stompClient !== null) {
                stompClient.disconnect();
            }
            console.log("Disconnected");
        }
    </script>
</body>
</html>