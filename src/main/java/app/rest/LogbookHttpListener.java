package app.rest;

import app.data.LogbookStorage;
import app.data.pilot.Logbook;
import app.data.pilot.LogbookEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LogbookHttpListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private LogbookStorage logbookStorage;

    @RequestMapping(path = "/logbooks/{logbookId}",
                    method = RequestMethod.GET)
    public ResponseEntity<Object> getLogbook(@PathVariable("logbookId") UUID logbookId) {
        Logbook logbook = logbookStorage.get(logbookId);
        if (logbook == null) {
            logger.info("Logbook not found for ID '" + logbookId + "'.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Logbook found for ID '" + logbookId + "'.");
        return ResponseEntity.status(200).body(logbook);
    }

    @RequestMapping(path = "/logbooks",
                    method = RequestMethod.POST)
    public ResponseEntity<Object> createLogbook() {
        UUID logbookId = logbookStorage.create();
        if (logbookId != null) {
            logger.info("Created new logbook that has the id '" + logbookId + "'.");
        }
        return ResponseEntity.status(200).body(logbookId);
    }

    @RequestMapping(path = "/logbooks/{logbookId}/entries",
                    method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> createEntry(@PathVariable("logbookId") UUID logbookId,
                                              @RequestBody LogbookEntry logbookEntry) {
        Logbook logbook = logbookStorage.get(logbookId);
        if (logbook == null) {
            logger.info("No logbook found for ID '" + logbookId + "'.");
            return ResponseEntity.status(404).body("No logbook exists for that ID.");
        }
        logbook.add(logbookEntry);
        boolean replaced = logbookStorage.replace(logbook);
        if (replaced) {
            logger.info("New aircraft added to logbook '" + logbookId + "'.");
            return ResponseEntity.status(200).body(logbook);
        }
        logger.info("An er  ror occurred whilst updating logbook identified by ID " + logbookId + "'.");
        return ResponseEntity.status(409).body(logbook);
    }

}
