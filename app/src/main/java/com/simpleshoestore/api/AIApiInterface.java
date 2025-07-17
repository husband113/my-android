package com.simpleshoestore.api;

import com.simpleshoestore.utils.AIChatService;

public class AIApiInterface {
    private AIChatService aiChatService;

    public AIApiInterface() {
        // 实例化 AIChatService
        aiChatService = new AIChatService();
    }

    /**
     * 向 AI 发送消息并获取响应
     * @param message 要发送的消息
     * @param callback 响应回调
     */
    public void sendMessageToAI(String message, AIChatService.AIResponseCallback callback) {
        // 调用 AIChatService 的 sendMessage 方法
        aiChatService.sendMessage(message, callback);
    }
}