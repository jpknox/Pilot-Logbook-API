package app.rest;

import app.control.LogbookService;
import app.data.LogbookHeapRepository;
import app.data.LogbookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;
import java.util.regex.Pattern;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static util.TestData.getText;

@ExtendWith(SpringExtension.class)
class LogbookControllerTest {

    public static final String UUID_REGEX = "\\b[\\d\\D]{8}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{12}";

    private MockMvc mockMvc;

    @InjectMocks
    LogbookController logbookController;

    @Spy
    @InjectMocks
    LogbookService logbookService = new LogbookService();

    @Spy
    LogbookRepository logbookRepository = new LogbookHeapRepository();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(logbookController)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();
    }

    /*
     * Given no logbook.
     * When creating a new logbook.
     * Return UUID.
     */
    @Test
    public void createLogbook() throws Exception {
        String actualPayload = mockMvc.perform(
                post("/logbooks")
        )
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();
        String expectedPayload = getText("rest/expected/logbookCreated.json");
        assertJsonEquals(expectedPayload, actualPayload);
    }

    /*
     * Given no logbook.
     * When getting the non-existent logbook.
     * Then return 404.
     */
    @Test
    public void getLogbook_return404() throws Exception {
        UUID logbookId = UUID.randomUUID();
        String expectedBody = "";
        mockMvc.perform(
                get("/localhost/" + logbookId.toString())
        )
                .andExpect(status().is(404))
                .andExpect(content().string(expectedBody));

    }

    /*
     * Given no logbook.
     * When create logbook.
     * Then return UUID.
     * When get logbook.
     * Then return empty logbook.
     */
    @Test
    public void createAndGetLogbook() throws Exception {
        String actualPayload =
                mockMvc.perform(
                        post("/logbooks")
                )
                        .andExpect(status().is(201))
                        .andReturn().getResponse().getContentAsString();

        String expectedPayload = getText("rest/expected/logbookCreated.json");
        assertJsonEquals(expectedPayload, actualPayload);

        Pattern pattern = Pattern.compile(UUID_REGEX);
        java.util.regex.Matcher jMatcher = pattern.matcher(actualPayload);
        jMatcher.find();
        String logbookUuid = jMatcher.group();

        actualPayload = mockMvc.perform(
                get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        expectedPayload = getText("rest/expected/emptyLogbook.json");
        Assertions.assertEquals(JsonPath.read(actualPayload, "$.id"), logbookUuid);
        assertJsonEquals(expectedPayload, actualPayload);
    }

    /*
     * Given no logbook.
     * When create logbook.
     * Then return UUID.
     * When add new logbook entry.
     * Then return logbook containing one entry.
     * When get logbook.
     * Then return logbook containing one entry.
     * When delete entry.
     * Then return message stating success of deletion.
     * When get logbook.
     * Then return empty logbook.
     */
    @Test
    public void add_get_delete_LogbookEntry() throws Exception {
        String actualResponse =
                mockMvc.perform(
                        post("/logbooks")
                )
                        .andExpect(status().is(201))
                        .andReturn().getResponse().getContentAsString();

        String expectedPayload = getText("rest/expected/logbookCreated.json");
        assertJsonEquals(expectedPayload, actualResponse);

        String logbookUuid = getLogbookUuid(actualResponse);
        String entry_RequestBody = getText("rest/mock/sampleEntry0.json");
        actualResponse = mockMvc.perform(
                post("/logbooks/" + logbookUuid + "/entries")
                        .content(entry_RequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        expectedPayload = getText("rest/expected/logbook_oneEntry_0.json");
        assertJsonEquals(expectedPayload, actualResponse);
        Assertions.assertEquals(logbookUuid, JsonPath.read(actualResponse, "$.id"));

        actualResponse = mockMvc.perform(
                get("/logbooks/" + logbookUuid).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        assertJsonEquals(expectedPayload, actualResponse);
        Assertions.assertEquals(logbookUuid, JsonPath.read(actualResponse, "$.id"));

        String entryUuid = JsonPath.read(actualResponse, "$.allEntries[0].entryId");
        actualResponse = mockMvc.perform(
                delete("/logbooks/" + logbookUuid + "/entries/" + entryUuid)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        expectedPayload = getText("rest/expected/entryDeleted.json");
        assertJsonEquals(expectedPayload, actualResponse);

        actualResponse = mockMvc.perform(
                get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
                //.andExpect(jsonPath("$.id", is(logbookUuid)))
                //.andExpect(jsonPath("$.allEntries", empty()));
        expectedPayload = getText("rest/expected/emptyLogbook.json");
        assertJsonEquals(expectedPayload, actualResponse);
        Assertions.assertEquals(logbookUuid, JsonPath.read(actualResponse, "$.id"));
        Assertions.assertEquals(new JSONArray(), JsonPath.read(actualResponse, "$.allEntries"));
    }

    /*
     * Given no logbook.
     * When create logbook.
     * Then return UUID.
     * When add new logbook entry.
     * Then return logbook containing one entry.
     * When update logbook entry.
     * Then return logbook containing just the recently updated entry.
     * When get logbook.
     * Then return logbook containing one entry.
     * When delete entry.
     * Then return message stating success of deletion.
     * When get logbook.
     * Then return empty logbook.
     */
    @Test
    public void add_update_delete_LogbookEntry() throws Exception {
        String expectedPayload = getText("rest/expected/logbookCreated.json");
        String actualPayload = mockMvc.perform(
                MockMvcRequestBuilders.post("/logbooks")
        )
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();
        assertJsonEquals(expectedPayload, actualPayload);

        String logbookUuid = getLogbookUuid(actualPayload);

        String entryRequestBody = getText("rest/mock/sampleEntry0.json");
        actualPayload = mockMvc.perform(
                MockMvcRequestBuilders.post("/logbooks/" + logbookUuid + "/entries")
                .content(entryRequestBody)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();
        expectedPayload = getText("rest/expected/logbook_oneEntry_0.json");
        assertJsonEquals(expectedPayload, actualPayload);
        Assertions.assertEquals(logbookUuid, JsonPath.read(actualPayload, "$.id"));

        String entryId = JsonPath.read(actualPayload, "$.allEntries[0].entryId");

        String updatedEntryRequestBody = getText("rest/mock/sampleEntry1.json");
        actualPayload = mockMvc.perform(
                MockMvcRequestBuilders.put("/logbooks/" + logbookUuid + "/entries/" + entryId)
                .content(updatedEntryRequestBody)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        expectedPayload = getText("rest/expected/logbook_oneEntry_1.json");
        assertJsonEquals(expectedPayload, actualPayload);
        Assertions.assertEquals(logbookUuid, JsonPath.read(actualPayload, "$.id"));
        Assertions.assertEquals(entryId, JsonPath.read(actualPayload, "$.allEntries[0].entryId"));

        actualPayload = mockMvc.perform(
                MockMvcRequestBuilders.get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        assertJsonEquals(expectedPayload, actualPayload);
        Assertions.assertEquals(logbookUuid, JsonPath.read(actualPayload, "$.id"));
        Assertions.assertEquals(entryId, JsonPath.read(actualPayload, "$.allEntries[0].entryId"));

        actualPayload = mockMvc.perform(
                MockMvcRequestBuilders.delete("/logbooks/" + logbookUuid + "/entries/" + entryId)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        expectedPayload = getText("rest/expected/entryDeleted.json");
        assertJsonEquals(expectedPayload, actualPayload);

        actualPayload = mockMvc.perform(
                MockMvcRequestBuilders.get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        expectedPayload = getText("rest/expected/emptyLogbook.json");
        assertJsonEquals(expectedPayload, actualPayload);
        Assertions.assertEquals(logbookUuid, JsonPath.read(actualPayload, "$.id"));
    }

    private String getLogbookUuid(String jsonBody) {
        Pattern uuidPattern = Pattern.compile(UUID_REGEX);
        java.util.regex.Matcher jMatcher = uuidPattern.matcher(jsonBody);
        jMatcher.find();
        return jMatcher.group();
    }

}