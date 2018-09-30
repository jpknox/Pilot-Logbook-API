package app.rest;

import app.data.HeapLogbookStorage;
import app.data.LogbookStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LogbookRestControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    LogbookRestController logbookRestController;

    @Spy
    LogbookStorage logbookStorage = new HeapLogbookStorage();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(logbookRestController)
                .build();
    }

    @Test
    public void givenNoLogbook_whenCreateLogbook_thenReturnUuid() throws Exception {
        UUID logbookId = UUID.randomUUID();
        when(logbookStorage.create()).thenReturn(logbookId);        //TODO: Remove mocking
        String expectedBody = String.format(
                "{ \"message\": \"Logbook created. Its ID is '%s'.\" }",
                logbookId.toString()
        );
        mockMvc.perform(
                MockMvcRequestBuilders.post("/logbooks")
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().json(expectedBody));
    }
}