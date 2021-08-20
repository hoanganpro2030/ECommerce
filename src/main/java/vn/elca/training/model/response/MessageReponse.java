package vn.elca.training.model.response;

import java.util.List;

public class MessageReponse {
    private String statusCode;
    private List<String> message;
    private Object data;

    public MessageReponse(String statusCode, List<String> message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public MessageReponse(String statusCode, List<String> message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
