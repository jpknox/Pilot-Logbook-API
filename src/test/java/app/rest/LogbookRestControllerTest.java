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
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
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

    public static final String UUID_REGEX = "\\b[\\d\\D]{8}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{12}";

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
        String actualPayload = mockMvc.perform(
                MockMvcRequestBuilders.post("/logbooks")
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        String expectedPayload = getText("rest/logbookCreated_UuidRemoved.json");
        JSONAssert.assertEquals(
                expectedPayload,
                actualPayload,
                new CustomComparator(
                        JSONCompareMode.LENIENT,
                        new Customization(
                                "message",
                                new RegularExpressionValueMatcher(
                                        String.format("Logbook created. Its ID is '%s'.",
                                                UUID_REGEX)))));
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
        String actualPayload =
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/logbooks")
                )
                        .andExpect(status().is(200))
                        .andReturn().getResponse().getContentAsString();

        String expectedPayload = getText("rest/logbookCreated_UuidRemoved.json");
        JSONAssert.assertEquals(
                expectedPayload,
                actualPayload,
                new CustomComparator(
                        JSONCompareMode.LENIENT,
                        new Customization(
                                "id",
                                new RegularExpressionValueMatcher(
                                        String.format("Logbook created. Its ID is '%s'.",
                                                UUID_REGEX)
                                )
                        )
                )
        );
/*        Pattern pattern = Pattern.compile(UUID_REGEX);
        java.util.regex.Matcher jMatcher = pattern.matcher(actualPayload);
        jMatcher.find();
        String logbookUuid = jMatcher.group();
        expectedBodyMatcher = matcherForExpectedTextTemplate(
                "emptyLogbook_UuidRemoved.json",
                logbookUuid);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andExpect(content().string(expectedBodyMatcher));*/
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
                .replaceAll("[\"]", "\\\\\"")
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\]", "\\\\]");
    }

    private Matcher matcherForExpectedTextTemplate(String nameOfExpectedFile, String... substitutions)
            throws IOException, URISyntaxException {
        String expectedData_template = getText(nameOfExpectedFile);
        String escapedExpectedData_template = escapeRegex(expectedData_template);
        String expectedData = String.format(
                escapedExpectedData_template,
                (Object[]) substitutions
        );
        Pattern pattern = Pattern.compile(expectedData);
        Matcher expectedDataMatcher = new MatchesPattern(pattern);
        return expectedDataMatcher;
    }
}