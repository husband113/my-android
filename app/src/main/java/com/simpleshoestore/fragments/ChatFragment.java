package com.simpleshoestore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.simpleshoestore.R;
import com.simpleshoestore.utils.AIChatService;

public class ChatFragment extends Fragment {

    private LinearLayout chatContainer;
    private EditText etMessage;
    private Button btnSend;
    private ScrollView scrollView;
    private Button btnQuick1, btnQuick2, btnQuick3, btnQuick4;
    private AIChatService aiChatService;
    private boolean isWaitingForResponse = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        aiChatService = new AIChatService();
        initViews(view);
        setupClickListeners();
        showWelcomeMessage();

        return view;
    }

    private void initViews(View view) {
        chatContainer = view.findViewById(R.id.chat_container);
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.btn_send);
        scrollView = view.findViewById(R.id.scroll_view);

        btnQuick1 = view.findViewById(R.id.btn_quick_1);
        btnQuick2 = view.findViewById(R.id.btn_quick_2);
        btnQuick3 = view.findViewById(R.id.btn_quick_3);
        btnQuick4 = view.findViewById(R.id.btn_quick_4);
    }

    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> sendMessage());

        // 快捷按钮点击事件
        btnQuick1.setOnClickListener(v -> sendQuickMessage("推荐一些热门鞋款"));
        btnQuick2.setOnClickListener(v -> sendQuickMessage("怎么选择合适的尺码？"));
        btnQuick3.setOnClickListener(v -> sendQuickMessage("有什么搭配建议吗？"));
        btnQuick4.setOnClickListener(v -> sendQuickMessage("价格大概是多少？"));

        // 回车键发送消息
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void showWelcomeMessage() {
        addAIMessage("您好！我是您的专属购鞋助手 🤖\n\n我可以帮您：\n• 推荐合适的鞋款\n• 提供尺码建议\n• 搭配指导\n• 价格咨询\n\n请选择下方快捷按钮或直接输入问题！");
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (!message.isEmpty() && !isWaitingForResponse) {
            sendQuickMessage(message);
            etMessage.setText("");
        }
    }

    private void sendQuickMessage(String message) {
        if (isWaitingForResponse) {
            Toast.makeText(getContext(), "请等待上一个回复完成", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查网络连接
        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "网络连接不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }

        isWaitingForResponse = true;
        updateSendButtonState(false);

        addUserMessage(message);
        addTypingIndicator();

        // 调用AI服务
        aiChatService.sendMessage(message, new AIChatService.AIResponseCallback() {
            @Override
            public void onSuccess(String response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        removeTypingIndicator();
                        addAIMessage(response);
                        isWaitingForResponse = false;
                        updateSendButtonState(true);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        removeTypingIndicator();

                        // 根据错误类型显示不同的错误消息
                        String errorMessage = getErrorMessage(error);
                        addAIMessage(errorMessage);

                        isWaitingForResponse = false;
                        updateSendButtonState(true);

                        Toast.makeText(getContext(), "发送失败，请重试", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private String getErrorMessage(String error) {
        if (error.contains("401")) {
            return "抱歉，API密钥配置有误，请联系管理员 🔧";
        } else if (error.contains("429")) {
            return "请求过于频繁，请稍后再试 ⏰";
        } else if (error.contains("timeout") || error.contains("网络")) {
            return "网络连接超时，请检查网络后重试 🌐";
        } else {
            return "AI服务暂时不可用，请稍后重试 🤖\n\n错误信息：" + error;
        }
    }

    private boolean isNetworkAvailable() {
        // 简单的网络检查，您可以根据需要完善
        return true; // 这里可以添加实际的网络检查逻辑
    }

    private void updateSendButtonState(boolean enabled) {
        btnSend.setEnabled(enabled);
        btnSend.setAlpha(enabled ? 1.0f : 0.5f);

        // 更新快捷按钮状态
        btnQuick1.setEnabled(enabled);
        btnQuick2.setEnabled(enabled);
        btnQuick3.setEnabled(enabled);
        btnQuick4.setEnabled(enabled);

        btnQuick1.setAlpha(enabled ? 1.0f : 0.5f);
        btnQuick2.setAlpha(enabled ? 1.0f : 0.5f);
        btnQuick3.setAlpha(enabled ? 1.0f : 0.5f);
        btnQuick4.setAlpha(enabled ? 1.0f : 0.5f);
    }

    private void addUserMessage(String message) {
        View messageView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_message, chatContainer, false);
        TextView tvMessage = messageView.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        chatContainer.addView(messageView);
        scrollToBottom();
    }

    private void addAIMessage(String message) {
        View messageView = LayoutInflater.from(getContext()).inflate(R.layout.item_ai_message, chatContainer, false);
        TextView tvMessage = messageView.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        chatContainer.addView(messageView);
        scrollToBottom();
    }

    private void addTypingIndicator() {
        View typingView = LayoutInflater.from(getContext()).inflate(R.layout.item_typing_indicator, chatContainer, false);
        typingView.setTag("typing_indicator");
        chatContainer.addView(typingView);
        scrollToBottom();
    }

    private void removeTypingIndicator() {
        View typingView = chatContainer.findViewWithTag("typing_indicator");
        if (typingView != null) {
            chatContainer.removeView(typingView);
        }
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 清理AI服务资源
        if (aiChatService != null) {
            aiChatService.shutdown();
        }
    }

    // 清空聊天记录的方法（可选）
    public void clearChat() {
        chatContainer.removeAllViews();
        showWelcomeMessage();
        isWaitingForResponse = false;
        updateSendButtonState(true);
    }

    // 添加一些预设回复（当AI服务不可用时使用）
    private String getFallbackResponse(String message) {
        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("推荐") || lowerMessage.contains("热门")) {
            return aiChatService.getQuickReply("热门推荐");
        } else if (lowerMessage.contains("尺码") || lowerMessage.contains("大小")) {
            return aiChatService.getQuickReply("尺码建议");
        } else if (lowerMessage.contains("搭配") || lowerMessage.contains("穿搭")) {
            return aiChatService.getQuickReply("搭配建议");
        } else if (lowerMessage.contains("价格") || lowerMessage.contains("多少钱")) {
            return aiChatService.getQuickReply("价格咨询");
        } else {
            return "抱歉，AI服务暂时不可用，请稍后重试。您也可以浏览我们的商品页面查看更多信息！";
        }
    }
}