package app.rest.response.error;

import app.rest.response.SingleMessageResponse;

public class ErrorSingleMessageResponse implements SingleMessageResponse {

    private String message;

    public ErrorSingleMessageResponse() {
    }

    public ErrorSingleMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
