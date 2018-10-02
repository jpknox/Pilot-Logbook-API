package app.rest;

import app.data.HeapLogbookStorage;
import app.data.LogbookStorage;
import org.hamcrest.Matcher;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class LogbookRestControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    LogbookRestController logbookRestController;

    @Spy
    LogbookStorage logbookStorage = new HeapLogbookStorage();
    public static final String UUID_REGEX = "\\b[\\d\\D]{8}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{12}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(logbookRestController)
                .build();
    }

    @Test
    public void givenNoLogbook_whenCreateLogbook_thenReturnUuid() throws Exception {
        String expectedPayload_template = getText("logbookCreated_UuidRemoved.json");
        String escapedExpectedPayload_template = escapeRegex(expectedPayload_template);
        String expectedBody = String.format(
                escapedExpectedPayload_template,
                UUID_REGEX
        );
        Pattern pattern = Pattern.compile(expectedBody);
        Matcher expectedBodyMatcher = new MatchesPattern(pattern);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/logbooks")
        )
                .andExpect(status().is(200))
                .andExpect(content().string(expectedBodyMatcher));
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
        String expectedPayload_template = getText("logbookCreated_UuidRemoved.json");
        String escapedExpectedPayload_template = escapeRegex(expectedPayload_template);
        String expectedBody = String.format(
                escapedExpectedPayload_template,
                UUID_REGEX
        );
        Pattern pattern = Pattern.compile(expectedBody);
        Matcher expectedBodyMatcher = new MatchesPattern(pattern);
        String response =
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/logbooks")
                )
                        .andExpect(status().is(200))
                        .andExpect(content().string(expectedBodyMatcher))
                        .andReturn().getResponse().getContentAsString();

        pattern = Pattern.compile(UUID_REGEX);
        java.util.regex.Matcher jMatcher = pattern.matcher(response);
        jMatcher.find();
        String logbookUuid = jMatcher.group();

        expectedBody = String.format(
                "\\{\"id\":\"%s\",\"allEntries\":\\[\\]\\}",
                logbookUuid
        );
        pattern = Pattern.compile(expectedBody);
        expectedBodyMatcher = new MatchesPattern(pattern);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andExpect(content().string(expectedBodyMatcher));
    }

    private String getText(String path) throws URISyntaxException, IOException {
        URI u = Thread.currentThread().getContextClassLoader().getResource(path).toURI();
        File f = new File(u);
        Path p = f.toPath();
        byte[] bytes = Files.readAllBytes(p);
        String s = new String(bytes);
        return s;
    }

    private String escapeRegex(String text) {
        return text.replaceAll("[{]", "\\\\{")
                .replaceAll("[}]", "\\\\}")
                .replaceAll("[\"]", "\\\\\"");
    }
}