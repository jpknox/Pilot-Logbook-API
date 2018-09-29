package app.rest.response.success;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class SuccessResponseTest {

    @Test
    void testEmptyConstructor() {
        SuccessResponse successResponse = new SuccessResponse();
        assertNotNull(successResponse);
        assertTrue(successResponse instanceof  SuccessResponse);
    }

    @Test
    void testGetMessage_SetThroughConstructor() {
        String message = "Sample message.";
        SuccessResponse successResponse = new SuccessResponse(message);
        assertEquals(message, successResponse.getMessage());
    }

    @Test
    void setMessage_InternalFieldIsSetByCall() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String message = "Sample message.";
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage(message);
        Field f = Class.forName(SuccessResponse.class.getName()).getDeclaredField("message");
        f.setAccessible(true);
        String internalMessage = (String) f.get(successResponse);
        assertEquals(message, internalMessage);
    }
}