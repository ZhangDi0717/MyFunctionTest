package com.zhangdi.myfunctiontest.excel;

import java.util.Map;

/**
 * author: zhangdi45
 * Date: 16:42 2024/1/25
 */
public class FactoryTestMessage {
    private String version;
    private Map<String,String> message;

    public FactoryTestMessage() {
    }

    public FactoryTestMessage(String version, Map<String, String> message) {
        this.version = version;
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getMessage() {
        return message;
    }

    public void setMessage(Map<String, String> message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FactoryTestMessage{" +
                "version='" + version + '\'' +
                ", message=" + message +
                '}';
    }
}
