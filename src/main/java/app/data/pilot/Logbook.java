package app.data.pilot;

import app.data.aircraft.Aircraft;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Logbook {

    private final UUID id = UUID.randomUUID();
    private LinkedList<LogbookEntry> entries = new LinkedList();

    public Logbook() {
        setupTestData();
    }

    public UUID getId() {
        return id;
    }

    public boolean add(LogbookEntry newEntry) {
        entries.addLast(newEntry);
        return true;
    }

    public List<LogbookEntry> getAll() {
        List<LogbookEntry> entriesToReturn = new LinkedList<>(entries);
        return entriesToReturn;
    }

    public LogbookEntry get(UUID uuid) {
        Optional<LogbookEntry> optional = entries.stream()
                .filter((e) -> e.getEntryId().equals(uuid))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private void setupTestData() {
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
        add(entry);
    }

}
