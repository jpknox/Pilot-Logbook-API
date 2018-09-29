package app.rest.response.success;

import app.rest.response.SingleMessageResponse;

public class SuccessSingleMessageResponse implements SingleMessageResponse {

    private String message;

    public SuccessSingleMessageResponse() {
    }

    public SuccessSingleMessageResponse(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
