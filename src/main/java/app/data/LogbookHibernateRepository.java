package app.data;

import app.data.pilot.Logbook;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LogbookHibernateRepository extends CrudRepository<Logbook, UUID>{



}
