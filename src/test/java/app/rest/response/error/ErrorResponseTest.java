package app.rest.response.error;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testEmptyConstructor() {
        ErrorResponse errorResponse = new ErrorResponse();
        assertNotNull(errorResponse);
        assertTrue(errorResponse instanceof  ErrorResponse);
    }

    @Test
    void testGetMessage_SetThroughConstructor() {
        String message = "Sample message.";
        ErrorResponse errorResponse = new ErrorResponse(message);
        assertEquals(message, errorResponse.getMessage());
    }

    @Test
    void setMessage_InternalFieldIsSetByCall() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String message = "Sample message.";
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        Field f = Class.forName(ErrorResponse.class.getName()).getDeclaredField("message");
        f.setAccessible(true);
        String internalMessage = (String) f.get(errorResponse);
        assertEquals(message, internalMessage);
    }

}