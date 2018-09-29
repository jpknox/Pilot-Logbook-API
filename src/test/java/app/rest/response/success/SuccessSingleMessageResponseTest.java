package app.rest.response.success;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class SuccessSingleMessageResponseTest {

    @Test
    void testEmptyConstructor() {
        SuccessSingleMessageResponse successSingleMessageResponse = new SuccessSingleMessageResponse();
        assertNotNull(successSingleMessageResponse);
        assertTrue(successSingleMessageResponse instanceof SuccessSingleMessageResponse);
    }

    @Test
    void testGetMessage_SetThroughConstructor() {
        String message = "Sample message.";
        SuccessSingleMessageResponse successSingleMessageResponse = new SuccessSingleMessageResponse(message);
        assertEquals(message, successSingleMessageResponse.getMessage());
    }

    @Test
    void setMessage_InternalFieldIsSetByCall() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String message = "Sample message.";
        SuccessSingleMessageResponse successSingleMessageResponse = new SuccessSingleMessageResponse();
        successSingleMessageResponse.setMessage(message);
        Field f = Class.forName(SuccessSingleMessageResponse.class.getName()).getDeclaredField("message");
        f.setAccessible(true);
        String internalMessage = (String) f.get(successSingleMessageResponse);
        assertEquals(message, internalMessage);
    }
}