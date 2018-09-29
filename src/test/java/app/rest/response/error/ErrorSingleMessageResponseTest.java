package app.rest.response.error;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ErrorSingleMessageResponseTest {

    @Test
    void testEmptyConstructor() {
        ErrorSingleMessageResponse errorSingleMessageResponse = new ErrorSingleMessageResponse();
        assertNotNull(errorSingleMessageResponse);
        assertTrue(errorSingleMessageResponse instanceof ErrorSingleMessageResponse);
    }

    @Test
    void testGetMessage_SetThroughConstructor() {
        String message = "Sample message.";
        ErrorSingleMessageResponse errorSingleMessageResponse = new ErrorSingleMessageResponse(message);
        assertEquals(message, errorSingleMessageResponse.getMessage());
    }

    @Test
    void setMessage_InternalFieldIsSetByCall() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String message = "Sample message.";
        ErrorSingleMessageResponse errorSingleMessageResponse = new ErrorSingleMessageResponse();
        errorSingleMessageResponse.setMessage(message);
        Field f = Class.forName(ErrorSingleMessageResponse.class.getName()).getDeclaredField("message");
        f.setAccessible(true);
        String internalMessage = (String) f.get(errorSingleMessageResponse);
        assertEquals(message, internalMessage);
    }

}