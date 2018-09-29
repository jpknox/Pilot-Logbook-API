package app.rest.response.success;

import app.rest.response.SingleMessageResponse;

public class SuccessResponse implements SingleMessageResponse {

    private String message;

    public SuccessResponse() {
    }

    public SuccessResponse(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void setMessage() {

    }
}
