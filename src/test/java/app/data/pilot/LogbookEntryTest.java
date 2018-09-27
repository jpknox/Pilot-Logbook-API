package app.data.pilot;

import app.data.aircraft.Aircraft;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class LogbookEntryTest {

    @Test
    void testCreateNewEntry_ConstructorSetsAllFields() {
        LocalDate date = LocalDate.now();
        Aircraft aircraft = new Aircraft("PA28", "G-BBXW");
        String captain = "João Paulo Knox";
        String holdersOperatingCapacity = "P2";
        String origin = "EGBJ";
        String destination = "EGBJ";
        LocalTime departure = LocalTime.now().minusHours(1);
        LocalTime arrival = LocalTime.now();
        int singleEngineP1TimeMins = 0;
        int singleEngineP2TimeMins = 60;
        int numOfTakeoffs = 3;
        int numOfLandings = 3;
        String remarks = "Choppy weather";
        LogbookEntry entry = new LogbookEntry(
                date,
                aircraft,
                captain,
                holdersOperatingCapacity,
                origin,
                destination,
                departure,
                arrival,
                singleEngineP1TimeMins,
                singleEngineP2TimeMins,
                numOfTakeoffs,
                numOfLandings,
                remarks
        );
        assertNotNull(entry.getEntryId());
        assertEquals(aircraft, entry.getAircraft());
        assertEquals(captain, entry.getCaptain());
        assertEquals(holdersOperatingCapacity, entry.getHoldersOperatingCapacity());
        assertEquals(origin, entry.getOrigin());
        assertEquals(destination, entry.getDestination());
        assertEquals(departure, entry.getDeparture());
        assertEquals(arrival, entry.getArrival());
        assertEquals(singleEngineP1TimeMins, entry.getSingleEngineP1TimeMins());
        assertEquals(singleEngineP2TimeMins, entry.getSingleEngineP2TimeMins());
        assertEquals(numOfTakeoffs, entry.getNumOfTakeoffs());
        assertEquals(numOfLandings, entry.getNumOfLandings());
        assertEquals(remarks, entry.getRemarks());
    }
}