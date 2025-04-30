package com.tools.payhelper.pay.ui.login;

public interface NotifyListener {

    /**
     * 接收到通知栏消息
     * @param type
     */
    void onReceiveMessage(String  type);

    /**
     * 移除掉通知栏消息
     * @param type
     */
    void onRemovedMessage(int type);
}
