package app.rest;

import app.control.LogbookController;
import app.data.pilot.Logbook;
import app.data.pilot.LogbookEntry;
import app.rest.response.error.ErrorSingleMessageResponse;
import app.rest.response.success.SuccessSingleMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LogbookRestInterface {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogbookController logbookController;

    @RequestMapping(
            path = "/logbooks/{logbookId}",
            method = RequestMethod.GET)
    public ResponseEntity<Object> getLogbook(@PathVariable("logbookId") UUID logbookId) {
        return logbookController.getLogbook(logbookId);
    }

    @RequestMapping(
            path = "/logbooks",
            method = RequestMethod.POST)
    public ResponseEntity<Object> createLogbook() {
        return logbookController.createLogbook();
    }

    @RequestMapping(
            path = "/logbooks/{logbookId}/entries",
            method = RequestMethod.POST)
    public ResponseEntity<Object> createEntry(@PathVariable("logbookId") UUID logbookId,
                                              @RequestBody LogbookEntry logbookEntry) {
        return logbookController.createLogbookEntry(logbookId, logbookEntry);
    }

    @RequestMapping(
            path = "/logbooks/{logbookId}/entries/{entryId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteEntry(@PathVariable("logbookId") UUID logbookId,
                                              @PathVariable("entryId") UUID entryId) {
        return logbookController.deleteLogbookEntry(logbookId, entryId);
    }

    @RequestMapping(
            path = "/logbooks/{logbookId}/entries/{entryId}",
            method = RequestMethod.PUT)
    public ResponseEntity<Object> updateEntry(@PathVariable("logbookId") UUID logbookId,
                                              @PathVariable("entryId") UUID entryId,
                                              @RequestBody LogbookEntry logbookEntry) {
        return logbookController.updateLogbookEntry(logbookId, entryId, logbookEntry);
    }


}
