package app.rest;

import app.data.LogbookStorage;
import app.data.pilot.Logbook;
import app.data.pilot.LogbookEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class HttpListener {

    @Autowired
    private LogbookStorage logbookStorage;

    @RequestMapping(path = "/logbooks/{logbookId}",
                    method = RequestMethod.GET)
    public ResponseEntity<Object> getLogbook(@PathVariable("logbookId") UUID logbookId) {
        Logbook logbook = logbookStorage.get(logbookId);
        if (logbook == null) {
            System.out.println("Logbook not found for ID '" + logbookId + "'.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        System.out.println("Logbook found for ID '" + logbookId + "'.");
        return ResponseEntity.status(200).body(logbook);
    }

    @RequestMapping(path = "/logbooks",
                    method = RequestMethod.POST)
    public ResponseEntity<Object> createLogbook() {
        UUID logbookId = logbookStorage.create();
        if (logbookId != null) {
            System.out.println("Created new logbook that has the id '" + logbookId + "'.");
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
            System.out.println("No logbook found for ID '" + logbookId + "'.");
            return ResponseEntity.status(404).body("No logbook exists for that ID.");
        }
        logbook.add(logbookEntry);
        boolean replaced = logbookStorage.replace(logbook);
        if (replaced) {
            System.out.println("New aircraft added to logbook '" + logbookId + "'.");
            return ResponseEntity.status(200).body(logbook);
        }
        System.out.println("An error occurred whilst updating logbook identified by ID " + logbookId + "'.");
        return ResponseEntity.status(409).body(logbook);
    }

}
