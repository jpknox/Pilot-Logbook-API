package app.data;

import app.data.pilot.Logbook;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogbookRepository {

    Logbook getById(UUID id);

    UUID create();

    boolean deleteById(UUID id);

    boolean replaceById(UUID id, Logbook logbook);

}
