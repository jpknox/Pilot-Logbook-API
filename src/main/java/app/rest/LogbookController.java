package app.rest;

import app.control.LogbookService;
import app.data.pilot.Logbook;
import app.data.pilot.LogbookEntry;
import app.data.transfer.internal.logbook.DeletionStatus;
import app.data.transfer.internal.logbook.LogbookWithEntryCreationStatusDto;
import app.data.transfer.internal.logbook.LogbookWithEntryUpdateStatusDto;
import app.data.transfer.internal.logbook.UpdateStatus;
import app.rest.response.error.ErrorSingleMessageResponse;
import app.rest.response.success.SuccessSingleMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static app.data.transfer.internal.logbook.UpdateStatus.ENTRY_UPDATED;

@RestController
public class LogbookController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogbookService logbookService;

    @RequestMapping(
            path = "/logbooks/{logbookId}",
            method = RequestMethod.GET)
    public ResponseEntity<Object> getLogbook(@PathVariable("logbookId") UUID logbookId) {
        Optional<Logbook> optional = logbookService.getLogbook(logbookId);
        if (!optional.isPresent()) {
            logger.info("Logbook not found for ID '" + logbookId + "'.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Logbook found for ID '" + logbookId + "'.");
        return ResponseEntity.status(200).body(optional.get());
    }

    @RequestMapping(
            path = "/logbooks",
            method = RequestMethod.POST)
    public ResponseEntity<Object> createLogbook() {
        Optional<UUID> optional = logbookService.createLogbook();
        if (optional.isPresent()) {
            logger.info("Created new logbook that has the id '" + optional.get() + "'.");
        }
        return ResponseEntity.status(201).body(
                new SuccessSingleMessageResponse(
                        String.format("Logbook created. Its ID is '%s'.", optional.get())
                )
        );
    }

    @RequestMapping(
            path = "/logbooks/{logbookId}/entries",
            method = RequestMethod.POST)
    public ResponseEntity<Object> createEntry(@PathVariable("logbookId") UUID logbookId,
                                               @RequestBody LogbookEntry logbookEntry) {
        LogbookWithEntryCreationStatusDto dto
                = logbookService.createLogbookEntry(logbookId, logbookEntry);
        switch (dto.getUpdateStatus()) {
            case ENTRY_CREATED:
                logger.info("New aircraft added to logbook '" + logbookId + "'.");
                return ResponseEntity.status(201).body(dto.getLogbook());
            case LOGBOOK_NOT_FOUND:
                logger.info("No logbook found for ID '" + logbookId + "'.");
                return ResponseEntity.status(404).body(new ErrorSingleMessageResponse("No logbook exists for that ID."));
            case ERROR_CREATING_ENTRY:
            default:
                String errorMessage = String.format(
                        "An error occurred whilst updating logbook identified by ID '%s'.",
                        logbookId
                );
                logger.error(errorMessage);
                return ResponseEntity.status(400).body(new ErrorSingleMessageResponse(errorMessage));
        }
    }

    @RequestMapping(
            path = "/logbooks/{logbookId}/entries/{entryId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteEntry(@PathVariable("logbookId") UUID logbookId,
                                              @PathVariable("entryId") UUID entryId) {
        DeletionStatus status = logbookService.deleteLogbookEntry(logbookId, entryId);
        switch (status) {
            case LOGBOOK_NOT_FOUND:
                return ResponseEntity.status(400).body(new ErrorSingleMessageResponse("Logbook doesn't exist."));
            case ENTRY_NOT_FOUND:
                return ResponseEntity.status(400).body(new ErrorSingleMessageResponse("Entry doesn't exist."));
            case ENTRY_DELETED:
                return ResponseEntity.status(200).body(new SuccessSingleMessageResponse("Entry deleted."));
            case ERROR_DELETING_ENTRY:
            default:
                return ResponseEntity.status(500).body(new ErrorSingleMessageResponse("An error has occurred."));
        }
    }

    @RequestMapping(
            path = "/logbooks/{logbookId}/entries/{entryId}",
            method = RequestMethod.PUT)
    public ResponseEntity<Object> updateEntry(@PathVariable("logbookId") UUID logbookId,
                                                                  @PathVariable("entryId") UUID entryId,
                                                                  @RequestBody LogbookEntry logbookEntry) {
        LogbookWithEntryUpdateStatusDto dto
                = logbookService.updateLogbookEntry(logbookId, entryId, logbookEntry);
        switch (dto.getUpdateStatus()) {
            case LOGBOOK_NOT_FOUND:
                return ResponseEntity.status(404).body(
                        new ErrorSingleMessageResponse("Logbook doesn't exist.")
                );
            case ENTRY_NOT_FOUND:
                return ResponseEntity.status(404).body(
                        new ErrorSingleMessageResponse("Entry doesn't exist.")
                );
            case ENTRY_UPDATED:
                return ResponseEntity.status(200).body(dto.getLogbook());
            case ERROR_UPDATING_ENTRY:
            default:
                return ResponseEntity.status(500).body(
                        new ErrorSingleMessageResponse("Something has gone wrong. " +
                                "Please contact our system administrator.")
                );
        }
    }


}
