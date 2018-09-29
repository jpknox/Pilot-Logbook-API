package app.data.pilot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LogbookTest {

    private Logbook logbook;
    private List<LogbookEntry> internalList;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        logbook = new Logbook();

        Field f = Logbook.class.getDeclaredField("entries");
        f.setAccessible(true);
        internalList = (List<LogbookEntry>) f.get(logbook);
    }

    @AfterEach
    void tearDown() {
        logbook = null;
        internalList = null;
    }

    @Test
    void testAddOneEntry_EntryIsStored() {
        LogbookEntryBuilder builder = new LogbookEntryBuilder();
        LogbookEntry entry = builder.build();
        logbook.add(entry);
        assertEquals(1, internalList.size());
        assertEquals(entry, internalList.get(0));
    }

    @Test
    void testGetAll_MultipleEntriesReturned() {
        LogbookEntryBuilder logbookEntryBuilder = new LogbookEntryBuilder();
        LogbookEntry entry0 = logbookEntryBuilder.build();
        LogbookEntry entry1 = logbookEntryBuilder.build();
        logbook.add(entry0);
        logbook.add(entry1);
        List<LogbookEntry> entries = logbook.getAll();
        assertEquals(2, entries.size());
        assertTrue(entries.contains(entry0));
        assertTrue(entries.contains(entry1));
    }

    @Test
    void testGetAll_ReturnedListIsntSameInstanceAsInternalList() {
        LogbookEntryBuilder logbookEntryBuilder = new LogbookEntryBuilder();
        List<LogbookEntry> entries = logbook.getAll();
        assertTrue(entries != internalList);
    }

    @Test
    void testGet_ExpectedEntryIsReturned() {
        LogbookEntryBuilder logbookEntryBuilder = new LogbookEntryBuilder();
        LogbookEntry entry = logbookEntryBuilder.build();
        logbook.add(entry);
        LogbookEntry returnedEntry = logbook.get(entry.getEntryId());
        assertEquals(entry, returnedEntry);
    }

    @Test
    void testContains_SingleEntryReturnsTrue() {
        LogbookEntryBuilder logbookEntryBuilder = new LogbookEntryBuilder();
        LogbookEntry logbookEntry = logbookEntryBuilder.build();
        logbook.add(logbookEntry);
        boolean contains = logbook.containsEntry(logbookEntry.getEntryId());
        assertTrue(contains);
    }

    @Test
    void testContains_NoEntryReturnsFalse() {
        UUID randomUUID = UUID.randomUUID();
        boolean contains = logbook.containsEntry(randomUUID);
        assertFalse(contains);
    }
}