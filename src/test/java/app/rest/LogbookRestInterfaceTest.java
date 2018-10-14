package app.rest;

import app.control.LogbookController;
import app.data.HeapLogbookStorage;
import app.data.LogbookStorage;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;
import java.util.regex.Pattern;

import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static util.TestData.getText;

@ExtendWith(SpringExtension.class)
class LogbookRestInterfaceTest {

    public static final String UUID_REGEX = "\\b[\\d\\D]{8}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{4}-[\\d\\D]{12}";

    private MockMvc mockMvc;

    @InjectMocks
    LogbookRestInterface logbookRestInterface;

    @Spy @InjectMocks
    LogbookController logbookController = new LogbookController();

    @Spy
    LogbookStorage logbookStorage = new HeapLogbookStorage();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(logbookRestInterface)
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
        String expectedPayload = getText("rest/expected/logbookCreated_UuidRemoved.json");
        assertEquals(
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

        String expectedPayload = getText("rest/expected/logbookCreated_UuidRemoved.json");
        assertEquals(
                expectedPayload,
                actualPayload,
                new CustomComparator(
                        JSONCompareMode.LENIENT,
                        Customization.customization(
                                "message",
                                new RegularExpressionValueMatcher(
                                        String.format("Logbook created. Its ID is '%s'.",
                                                UUID_REGEX)
                                )
                        )
                )
        );

        Pattern pattern = Pattern.compile(UUID_REGEX);
        java.util.regex.Matcher jMatcher = pattern.matcher(actualPayload);
        jMatcher.find();
        String logbookUuid = jMatcher.group();

        actualPayload = mockMvc.perform(
                get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        expectedPayload = getText("rest/expected/emptyLogbook_UuidRemoved.json");
        assertEquals(
                expectedPayload,
                actualPayload,
                new CustomComparator(
                        JSONCompareMode.LENIENT,
                        Customization.customization(
                                "id",
                                (o, t1) -> o.toString().equals(logbookUuid)
                        )
                )
        );
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
        String actualPayload =
                mockMvc.perform(
                        post("/logbooks")
                )
                        .andExpect(status().is(201))
                        .andReturn().getResponse().getContentAsString();

        String expectedPayload = getText("rest/expected/logbookCreated_UuidRemoved.json");
        assertEquals(
                expectedPayload,
                actualPayload,
                new CustomComparator(
                        JSONCompareMode.LENIENT,
                        Customization.customization(
                                "message",
                                new RegularExpressionValueMatcher(
                                        String.format("Logbook created. Its ID is '%s'.",
                                                UUID_REGEX)
                                )
                        )
                )
        );

        Pattern uuidPattern = Pattern.compile(UUID_REGEX);
        java.util.regex.Matcher jMatcher = uuidPattern.matcher(actualPayload);
        jMatcher.find();
        String logbookUuid = jMatcher.group();

        String entry_RequestBody = getText("rest/mock/sampleEntry0.json");

        mockMvc.perform(
                post("/logbooks/" + logbookUuid + "/entries")
                        .content(entry_RequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(logbookUuid)))
                .andExpect(jsonPath("$.allEntries[0].entryId", MatchesPattern.matchesPattern(uuidPattern)))
                .andExpect(jsonPath("$.allEntries[0].date[0]", is(2018)))
                .andExpect(jsonPath("$.allEntries[0].date[1]", is(9)))
                .andExpect(jsonPath("$.allEntries[0].date[2]", is(20)))
                .andExpect(jsonPath("$.allEntries[0].aircraft.type", is("PA28")))
                .andExpect(jsonPath("$.allEntries[0].aircraft.registration", is("G-BASJ")))
                .andExpect(jsonPath("$.allEntries[0].captain", is("João Paulo Knox")))
                .andExpect(jsonPath("$.allEntries[0].holdersOperatingCapacity", is("P2")))
                .andExpect(jsonPath("$.allEntries[0].origin", is("EGBJ")))
                .andExpect(jsonPath("$.allEntries[0].destination", is("EGBJ")))
                .andExpect(jsonPath("$.allEntries[0].departure[0]", is(12)))
                .andExpect(jsonPath("$.allEntries[0].departure[1]", is(40)))
                .andExpect(jsonPath("$.allEntries[0].arrival[0]", is(14)))
                .andExpect(jsonPath("$.allEntries[0].arrival[1]", is(40)))
                .andExpect(jsonPath("$.allEntries[0].singleEngineP1TimeMins", is(0)))
                .andExpect(jsonPath("$.allEntries[0].singleEngineP2TimeMins", is(120)))
                .andExpect(jsonPath("$.allEntries[0].numOfTakeoffs", is(1)))
                .andExpect(jsonPath("$.allEntries[0].numOfLandings", is(1)))
                .andExpect(jsonPath("$.allEntries[0].remarks", is("Lovely weather")));

        String response = mockMvc.perform(
                get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(logbookUuid)))
                .andExpect(jsonPath("$.allEntries[0].entryId", MatchesPattern.matchesPattern(uuidPattern)))
                .andExpect(jsonPath("$.allEntries[0].date[0]", is(2018)))
                .andExpect(jsonPath("$.allEntries[0].date[1]", is(9)))
                .andExpect(jsonPath("$.allEntries[0].date[2]", is(20)))
                .andExpect(jsonPath("$.allEntries[0].aircraft.type", is("PA28")))
                .andExpect(jsonPath("$.allEntries[0].aircraft.registration", is("G-BASJ")))
                .andExpect(jsonPath("$.allEntries[0].captain", is("João Paulo Knox")))
                .andExpect(jsonPath("$.allEntries[0].holdersOperatingCapacity", is("P2")))
                .andExpect(jsonPath("$.allEntries[0].origin", is("EGBJ")))
                .andExpect(jsonPath("$.allEntries[0].destination", is("EGBJ")))
                .andExpect(jsonPath("$.allEntries[0].departure[0]", is(12)))
                .andExpect(jsonPath("$.allEntries[0].departure[1]", is(40)))
                .andExpect(jsonPath("$.allEntries[0].arrival[0]", is(14)))
                .andExpect(jsonPath("$.allEntries[0].arrival[1]", is(40)))
                .andExpect(jsonPath("$.allEntries[0].singleEngineP1TimeMins", is(0)))
                .andExpect(jsonPath("$.allEntries[0].singleEngineP2TimeMins", is(120)))
                .andExpect(jsonPath("$.allEntries[0].numOfTakeoffs", is(1)))
                .andExpect(jsonPath("$.allEntries[0].numOfLandings", is(1)))
                .andExpect(jsonPath("$.allEntries[0].remarks", is("Lovely weather")))
                .andReturn().getResponse().getContentAsString();

        String entryUuid = JsonPath.read(response, "$.allEntries[0].entryId");
        mockMvc.perform(
                delete("/logbooks/" + logbookUuid + "/entries/" + entryUuid)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message", is("Entry deleted.")));

        mockMvc.perform(
                get("/logbooks/" + logbookUuid)
        )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(logbookUuid)))
                .andExpect(jsonPath("$.allEntries", empty()));
    }


}