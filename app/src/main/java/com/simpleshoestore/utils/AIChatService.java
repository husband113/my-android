package com.simpleshoestore.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIChatService {
    // DeepSeek API 配置
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = "sk-a5950339e4f04790afa63f3a0b89fa08"; // 请替换为您的 DeepSeek API 密钥
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface AIResponseCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void sendMessage(String message, AIResponseCallback callback) {
        executor.execute(() -> {
            try {
                String response = callDeepSeekAPI(message);
                callback.onSuccess(response);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError("AI服务暂时不可用：" + e.getMessage());
            }
        });
    }

    private String callDeepSeekAPI(String message) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            // 设置请求方法和头部
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000); // 30秒连接超时
            connection.setReadTimeout(60000);    // 60秒读取超时

            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "deepseek-chat"); // DeepSeek 的模型名称

            // 构建消息数组
            JSONArray messages = new JSONArray();

            // 系统消息，定义AI助手的角色
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一个专业的鞋类销售顾问和购物助手。你需要：\n" +
                    "1. 为客户推荐合适的鞋子\n" +
                    "2. 提供专业的尺码建议\n" +
                    "3. 给出搭配建议\n" +
                    "4. 解答关于鞋子的各种问题\n" +
                    "5. 保持友好、专业的服务态度\n" +
                    "请用中文回答，回答要简洁明了且实用。");
            messages.put(systemMessage);

            // 用户消息
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.put(userMessage);

            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 800);
            requestBody.put("temperature", 0.7);
            requestBody.put("stream", false);

            // 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 检查响应状态码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP错误: " + responseCode + " - " + connection.getResponseMessage());
            }

            // 读取响应
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // 解析响应
            JSONObject jsonResponse = new JSONObject(response.toString());

            // 检查是否有错误
            if (jsonResponse.has("error")) {
                JSONObject error = jsonResponse.getJSONObject("error");
                throw new IOException("API错误: " + error.getString("message"));
            }

            // 提取AI回复内容
            JSONArray choices = jsonResponse.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject choice = choices.getJSONObject(0);
                JSONObject messageObj = choice.getJSONObject("message");
                return messageObj.getString("content").trim();
            } else {
                throw new IOException("API响应中没有找到回复内容");
            }

        } catch (Exception e) {
            throw new IOException("调用DeepSeek API失败: " + e.getMessage(), e);
        } finally {
            connection.disconnect();
        }
    }

    // 获取预设的快速回复
    public String getQuickReply(String quickType) {
        switch (quickType) {
            case "热门推荐":
                return "根据当前流行趋势，我推荐以下几款热门鞋子：\n\n" +
                        "🔥 Nike Air Jordan 1 - 经典篮球鞋，永不过时\n" +
                        "🔥 Adidas Ultraboost - 舒适跑鞋，科技感十足\n" +
                        "🔥 Converse Chuck Taylor - 百搭帆布鞋\n\n" +
                        "您更偏向哪种风格呢？";

            case "尺码建议":
                return "选择合适尺码的建议：\n\n" +
                        "📏 脚长测量：建议下午测量脚长（脚部略微肿胀时）\n" +
                        "📏 品牌差异：不同品牌尺码可能有差异\n" +
                        "📏 鞋型考虑：运动鞋建议大半码，皮鞋按正常尺码\n" +
                        "📏 试穿原则：脚趾前端留1-1.5cm空间\n\n" +
                        "您的脚长是多少厘米呢？";

            case "搭配建议":
                return "为您提供搭配建议：\n\n" +
                        "👕 休闲风格：运动鞋 + 牛仔裤 + T恤\n" +
                        "👔 商务风格：皮鞋 + 西装裤 + 衬衫\n" +
                        "👗 时尚风格：小白鞋 + 连衣裙/半身裙\n" +
                        "🎨 街头风格：高帮鞋 + 工装裤 + 卫衣\n\n" +
                        "您想要什么场合的搭配呢？";

            case "价格咨询":
                return "我们的价格区间：\n\n" +
                        "💰 入门级：300-800元（帆布鞋、基础运动鞋）\n" +
                        "💰 中档：800-1500元（品牌运动鞋、休闲鞋）\n" +
                        "💰 高端：1500元以上（限量款、高端材质）\n\n" +
                        "经常有优惠活动，您的预算大概是多少呢？";

            default:
                return "您好！我是您的专属购鞋助手，很高兴为您服务！🤖\n\n" +
                        "我可以帮您解决关于鞋子的任何问题，比如推荐、尺码、搭配等。\n" +
                        "请告诉我您的需求吧！";
        }
    }

    // 清理资源
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}