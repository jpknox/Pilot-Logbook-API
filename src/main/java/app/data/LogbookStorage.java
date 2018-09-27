package app.data;

import app.data.pilot.Logbook;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface LogbookStorage {

    Logbook get(UUID id);

    UUID create();

    boolean delete(UUID id);

    boolean replace(Logbook logbook);

}
