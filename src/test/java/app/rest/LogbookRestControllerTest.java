package app.rest;

import app.data.HeapLogbookStorage;
import app.data.LogbookStorage;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.text.MatchesPattern;
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
import java.util.regex.Pattern;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(status().is(200))
                .andExpect(content().json(expectedBody));
    }

    @Test
    public void givenNoLogbook_whenGetLogbook_thenReturn404() throws Exception {
        UUID logbookId = UUID.randomUUID();
        String expectedBody = "";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/localhost/" + logbookId.toString())
        )
                .andExpect(status().is(404))
                .andExpect(content().string(expectedBody));

    }

    @Test
    public void givenNoLogbook_whenCreateBook_thenReturnUUID_whenGetBook_thenReturnEmptyBook() throws Exception {
        String uuidRegex = "\\b[\\d\\D]{8}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{12}";
        String expectedBody = String.format(
                "\\{\"message\":\"Logbook created. Its ID is '%s'.\"\\}",
                uuidRegex
        );
        Pattern pattern = Pattern.compile(expectedBody);
        Matcher matcher = new MatchesPattern(pattern);
        String response = mockMvc.perform(
                MockMvcRequestBuilders.post("/logbooks")
        )
                .andExpect(status().is(200))
                .andExpect(content().string(matcher))
                .andReturn().getResponse().getContentAsString();

        pattern = Pattern.compile(uuidRegex);
        java.util.regex.Matcher jMatcher = pattern.matcher(response);
        jMatcher.find();
        String logbookUuid = jMatcher.group();

        expectedBody = String.format(
                "\\{\"id\":\"%s\",\"allEntries\":\\[\\]\\}",
                logbookUuid
        );
        pattern = Pattern.compile(expectedBody);
        matcher = new MatchesPattern(pattern);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andExpect(content().string(matcher));
    }
}