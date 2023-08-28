package demo.ui.utils;

import com.alibaba.fastjson2.JSONObject;

import java.lang.reflect.Type;

public class Response<T> {
    private final int status;
    private final JSONObject data;
    private final String message;

    public Response(int status, JSONObject data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public T getData(Type type) {
        if (data == null) {
            return null;
        }
        return data.to(type);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}