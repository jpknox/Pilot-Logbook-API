package app.control;

import app.data.LogbookRepository;
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
    LogbookRepository logbookRepository;

    public Optional<Logbook> getLogbook(UUID logbookId) {
        return Optional.ofNullable(
                logbookRepository.get(logbookId)
        );
    }

    public Optional<UUID> createLogbook() {
        return Optional.ofNullable(
                logbookRepository.create()
        );
    }

    public LogbookWithEntryCreationStatusDto createLogbookEntry(UUID logbookId, LogbookEntry logbookEntry) {
        Optional optional = Optional.ofNullable(logbookRepository.get(logbookId));
        if (!optional.isPresent()) {
            return new LogbookWithEntryCreationStatusDto(null, CreationStatus.LOGBOOK_NOT_FOUND);
        }
        Logbook logbook = (Logbook) optional.get();
        if (logbook.add(logbookEntry)) {
            logbookRepository.replace(logbook);
            return new LogbookWithEntryCreationStatusDto(logbook, CreationStatus.ENTRY_CREATED);
        } else {
            return new LogbookWithEntryCreationStatusDto(logbook, CreationStatus.ERROR_CREATING_ENTRY);
        }
    }

    public DeletionStatus deleteLogbookEntry(UUID logbookId, UUID entryId) {
        Optional optional = Optional.ofNullable(logbookRepository.get(logbookId));
        if (!optional.isPresent()) {
            return DeletionStatus.LOGBOOK_NOT_FOUND;
        }
        Logbook logbook = (Logbook) optional.get();
        if (!logbook.get(entryId).isPresent()) {
            return DeletionStatus.ENTRY_NOT_FOUND;
        }
        if (logbook.remove(entryId)) {
            return DeletionStatus.ENTRY_DELETED;
        } else {
            return DeletionStatus.ERROR_DELETING_ENTRY;
        }
    }

    public LogbookWithEntryUpdateStatusDto updateLogbookEntry(UUID logbookId, UUID entryId, LogbookEntry logbookEntry) {
        Logbook logbook = logbookRepository.get(logbookId);
        if (logbook == null) {
            return new LogbookWithEntryUpdateStatusDto(null, UpdateStatus.LOGBOOK_NOT_FOUND);
        }
        Optional optional = logbook.get(entryId);
        if (!optional.isPresent()) {
            return new LogbookWithEntryUpdateStatusDto(null, UpdateStatus.ENTRY_NOT_FOUND);
        }
        if (logbook.replace(entryId, logbookEntry)) {
            logbookRepository.replace(logbook);
            return new LogbookWithEntryUpdateStatusDto(logbook, UpdateStatus.ENTRY_UPDATED);
        } else {
            return new LogbookWithEntryUpdateStatusDto(null, UpdateStatus.ERROR_UPDATING_ENTRY);
        }
    }
}
