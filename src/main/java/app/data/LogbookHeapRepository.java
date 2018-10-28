package app.data;

import app.data.pilot.Logbook;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class LogbookHeapRepository implements LogbookRepository {

    private Map<UUID, Logbook> logbooks = new HashMap<UUID, Logbook>();

    @Override
    public Logbook getById(UUID id) {
        return logbooks.get(id);
    }

    @Override
    public UUID create() {
        Logbook newLogbook = new Logbook();
        logbooks.put(newLogbook.getId(), newLogbook);
        return newLogbook.getId();
    }

    @Override
    public boolean deleteById(UUID id) {
        Logbook logbook = logbooks.remove(id);
        if (logbook != null)
            return true;
        return false;
    }

    @Override
    public boolean replaceById(UUID id, Logbook logbook) {
        Logbook prev = logbooks.get(logbook.getId());
        Logbook replaced = logbooks.replace(logbook.getId(), logbook);
        if (prev != null && replaced != null && prev == replaced) {
            return true;
        }
        return false;
    }
}
