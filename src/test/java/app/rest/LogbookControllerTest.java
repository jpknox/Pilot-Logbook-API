package app.rest;

import app.control.LogbookService;
import app.data.pilot.Logbook;
import app.data.pilot.LogbookEntry;
import app.data.transfer.internal.logbook.CreationStatus;
import app.data.transfer.internal.logbook.LogbookWithEntryCreationStatusDto;
import app.rest.response.success.SuccessSingleMessageResponse;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class LogbookControllerTest {

    @InjectMocks
    LogbookController logbookController;

    @Mock
    LogbookService logbookService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this.getClass());
    }

    @Test
    public void getLogbook_mockedLogbookIsReturned() {
        Logbook logbook = new Logbook();
        logbook.add(new LogbookEntry());
        Mockito.when(logbookService.getLogbook(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(logbook));
        ResponseEntity<Object> responseEntity = logbookController.getLogbook(UUID.randomUUID());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(logbook, responseEntity.getBody());
    }

    @Test
    public void getLogbook_mockedNullLogbookResultsIn404() {
        Mockito.when(logbookService.getLogbook(Mockito.any(UUID.class)))
                .thenReturn(Optional.ofNullable(null));
        ResponseEntity<Object> responseEntity =
                logbookController.getLogbook(UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void createLogbook_mockedUuidIsReturned() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(logbookService.createLogbook())
                .thenReturn(Optional.of(uuid));
        ResponseEntity<Object> responseEntity =
                logbookController.createLogbook();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(
                String.format("Logbook created. Its ID is '%s'.", uuid.toString()),
                ((SuccessSingleMessageResponse) responseEntity.getBody()).getMessage());
    }

    @Test
    public void createEntry_mockEntryCreated() {
        Logbook logbook = Mockito.mock(Logbook.class);
        LogbookWithEntryCreationStatusDto dto = Mockito.mock(LogbookWithEntryCreationStatusDto.class);
        Mockito.when(dto.getUpdateStatus()).thenReturn(CreationStatus.ENTRY_CREATED);
        Mockito.when(dto.getLogbook()).thenReturn(logbook);
        Mockito.when(logbookService.createLogbookEntry(Mockito.any(UUID.class), Mockito.any(LogbookEntry.class)))
                .thenReturn(dto);
        ResponseEntity<Object> responseEntity =
                logbookController.createEntry(UUID.randomUUID(), new LogbookEntry());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(logbook, responseEntity.getBody());
    }

    @Test
    public void createEntry_mockLogbookNotFound() {
        fail();
    }

    @Test
    public void createEntry_mockErrorCreatingEntry() {
        fail();
    }

    @Test
    public void deleteEntry_mockLogbookNotFound() {
        fail();
    }

    @Test
    public void deleteEntry_mockEntryNotFound() {
        fail();
    }

    @Test
    public void deleteEntry_mockEntryDeleted() {
        fail();
    }

    @Test
    public void deleteEntry_mockErrorDeletingEntry() {
        fail();
    }

    @Test
    public void updateEntry_mockLogbookNotFound() {
        fail();
    }

    @Test
    public void updateEntry_mockEntryNotFound() {
        fail();
    }

    @Test
    public void updateEntry_mockEntryUpdated() {
        fail();
    }

    @Test
    public void updateEntry_mockErrorUpdatingEntry() {
        fail();
    }




}
