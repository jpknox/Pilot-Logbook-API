package app.control;

import app.data.LogbookStorage;
import app.data.pilot.Logbook;
import app.data.pilot.LogbookEntry;
import app.rest.response.error.ErrorSingleMessageResponse;
import app.rest.response.success.SuccessSingleMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LogbookController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LogbookStorage logbookStorage;

    public ResponseEntity<Object> getLogbook(UUID logbookId) {
        Logbook logbook = logbookStorage.get(logbookId);
        if (logbook == null) {
            logger.info("Logbook not found for ID '" + logbookId + "'.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Logbook found for ID '" + logbookId + "'.");
        return ResponseEntity.status(200).body(logbook);
    }

    public ResponseEntity<Object> createLogbook() {
        UUID logbookId = logbookStorage.create();
        if (logbookId != null) {
            logger.info("Created new logbook that has the id '" + logbookId + "'.");
        }
        String responseMessage = String.format("Logbook created. Its ID is '%s'.", logbookId);
        return ResponseEntity.status(201).body(new SuccessSingleMessageResponse(responseMessage));
    }

    public ResponseEntity<Object> createLogbookEntry(UUID logbookId, LogbookEntry logbookEntry) {
        Logbook logbook = logbookStorage.get(logbookId);
        if (logbook == null) {
            logger.info("No logbook found for ID '" + logbookId + "'.");
            return ResponseEntity.status(404).body(new ErrorSingleMessageResponse("No logbook exists for that ID."));
        }
        logbook.add(logbookEntry);
        boolean replaced = logbookStorage.replace(logbook);
        if (replaced) {
            logger.info("New aircraft added to logbook '" + logbookId + "'.");
            return ResponseEntity.status(201).body(logbook);
        }
        String errorMessage = String.format("An error occurred whilst updating logbook identified by ID '%s'.", logbookId);
        logger.info(errorMessage);
        return ResponseEntity.status(400).body(new ErrorSingleMessageResponse(errorMessage));
    }

    public ResponseEntity<Object> deleteLogbookEntry(UUID logbookId, UUID entryId) {
        Logbook logbook = logbookStorage.get(logbookId);
        if (logbook == null) {
            return ResponseEntity.status(400).body(new ErrorSingleMessageResponse("Logbook doesn't exist."));
        }
        if (!logbook.containsEntry(entryId)) {
            return ResponseEntity.status(400).body(new ErrorSingleMessageResponse("Entry doesn't exist."));
        }
        boolean removed = logbook.remove(entryId);
        if (removed) {
            return ResponseEntity.status(200).body(new SuccessSingleMessageResponse("Entry deleted."));
        } else {
            return ResponseEntity.status(500).body(new ErrorSingleMessageResponse("An error has occurred."));
        }
    }

    public ResponseEntity<Object> updateLogbookEntry(UUID logbookId, UUID entryId, LogbookEntry logbookEntry) {
        Logbook logbook = logbookStorage.get(logbookId);
        if (logbook == null) {
            return ResponseEntity.status(404).body(
                    new ErrorSingleMessageResponse("Logbook doesn't exist.")
            );
        }
        if (!logbook.containsEntry(entryId)) {
            return ResponseEntity.status(404).body(
                    new ErrorSingleMessageResponse("Entry doesn't exist.")
            );
        }
        if (logbook.replace(entryId, logbookEntry)) {
            return ResponseEntity.status(200).body(logbook);
        }
        return ResponseEntity.status(500).body(
                new ErrorSingleMessageResponse("Something has gone wrong. " +
                        "Please contact our system administrator.")
        );
    }
}
