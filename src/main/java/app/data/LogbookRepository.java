package app.data;

import app.data.pilot.Logbook;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogbookRepository {

    Logbook get(UUID id);

    UUID create();

    boolean delete(UUID id);

    boolean replace(Logbook logbook);

}
