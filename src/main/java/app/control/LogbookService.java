package app.control;

import app.data.LogbookHibernateRepository;
import app.data.pilot.Logbook;
import app.data.pilot.LogbookEntry;
import app.data.transfer.internal.logbook.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LogbookService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LogbookHibernateRepository logbookRepository;

    public Optional<Logbook> getLogbook(UUID logbookId) {
        return logbookRepository.findById(logbookId);
    }

    public Optional<UUID> createLogbook() {
        UUID id = null;
        try {
            Logbook newLogbook = new Logbook();
            logbookRepository.save(newLogbook);
            id = newLogbook.getId();
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            return Optional.ofNullable(id);
        }
    }

    public LogbookWithEntryCreationStatusDto createLogbookEntry(UUID logbookId, LogbookEntry logbookEntry) {
        Optional<Logbook> optional = logbookRepository.findById(logbookId);
        if (!optional.isPresent()) {
            return new LogbookWithEntryCreationStatusDto(null, CreationStatus.LOGBOOK_NOT_FOUND);
        }
        Logbook logbook = optional.get();
        if (logbook.add(logbookEntry)) {
            logbookRepository.save(logbook);
            return new LogbookWithEntryCreationStatusDto(logbook, CreationStatus.ENTRY_CREATED);
        } else {
            return new LogbookWithEntryCreationStatusDto(logbook, CreationStatus.ERROR_CREATING_ENTRY);
        }
    }

    public DeletionStatus deleteLogbookEntry(UUID logbookId, UUID entryId) {
        Optional<Logbook> optional = logbookRepository.findById(logbookId);
        if (!optional.isPresent()) {
            return DeletionStatus.LOGBOOK_NOT_FOUND;
        }
        Logbook logbook = optional.get();
        if (!logbook.get(entryId).isPresent()) {
            return DeletionStatus.ENTRY_NOT_FOUND;
        }
        if (logbook.remove(entryId)) {
            logbookRepository.save(logbook);
            return DeletionStatus.ENTRY_DELETED;
        } else {
            return DeletionStatus.ERROR_DELETING_ENTRY;
        }
    }

    //TODO: Refactor to use Optional for improved null safety.
    public LogbookWithEntryUpdateStatusDto updateLogbookEntry(UUID logbookId, UUID entryId, LogbookEntry logbookEntry) {
        Logbook logbook = logbookRepository.findById(logbookId).get();
        if (logbook == null) {
            return new LogbookWithEntryUpdateStatusDto(null, UpdateStatus.LOGBOOK_NOT_FOUND);
        }
        Optional optional = logbook.get(entryId);
        if (!optional.isPresent()) {
            return new LogbookWithEntryUpdateStatusDto(null, UpdateStatus.ENTRY_NOT_FOUND);
        }
        if (logbook.replace(entryId, logbookEntry)) {
            logbookRepository.save(logbook);
            return new LogbookWithEntryUpdateStatusDto(logbook, UpdateStatus.ENTRY_UPDATED);
        } else {
            return new LogbookWithEntryUpdateStatusDto(null, UpdateStatus.ERROR_UPDATING_ENTRY);
        }
    }
}
