package app.rest;

import app.control.LogbookService;
import app.data.pilot.Logbook;
import app.data.pilot.LogbookEntry;
import app.data.transfer.internal.logbook.*;
import app.rest.response.error.ErrorSingleMessageResponse;
import app.rest.response.success.SuccessSingleMessageResponse;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

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
        when(logbookService.getLogbook(any(UUID.class)))
                .thenReturn(Optional.of(logbook));
        ResponseEntity<Object> responseEntity = logbookController.getLogbook(UUID.randomUUID());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(logbook, responseEntity.getBody());
    }

    @Test
    public void getLogbook_mockedNullLogbookResultsIn404() {
        when(logbookService.getLogbook(any(UUID.class)))
                .thenReturn(Optional.ofNullable(null));
        ResponseEntity<Object> responseEntity =
                logbookController.getLogbook(UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void createLogbook_mockedUuidIsReturned() {
        UUID uuid = UUID.randomUUID();
        when(logbookService.createLogbook())
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
        Logbook logbook = mock(Logbook.class);
        LogbookWithEntryCreationStatusDto dto = mock(LogbookWithEntryCreationStatusDto.class);
        when(dto.getCreationStatus()).thenReturn(CreationStatus.ENTRY_CREATED);
        when(dto.getLogbook()).thenReturn(logbook);
        when(logbookService.createLogbookEntry(any(UUID.class), any(LogbookEntry.class)))
                .thenReturn(dto);
        ResponseEntity<Object> responseEntity =
                logbookController.createEntry(UUID.randomUUID(), new LogbookEntry());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(logbook, responseEntity.getBody());
    }

    @Test
    public void createEntry_mockLogbookNotFound() {
        LogbookEntry logbookEntry = mock(LogbookEntry.class);
        UUID uuid = UUID.randomUUID();
        LogbookWithEntryCreationStatusDto dto = mock(LogbookWithEntryCreationStatusDto.class);
        when(dto.getCreationStatus()).thenReturn(CreationStatus.LOGBOOK_NOT_FOUND);
        when(logbookService.createLogbookEntry(any(UUID.class), any(LogbookEntry.class)))
            .thenReturn(dto);
        ResponseEntity<Object> responseEntity =
                logbookController.createEntry(uuid, logbookEntry);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(
                "No logbook exists for that ID.",
                ((ErrorSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }

    @Test
    public void createEntry_mockErrorCreatingEntry() {
        LogbookEntry logbookEntry = mock(LogbookEntry.class);
        UUID uuid = UUID.randomUUID();
        LogbookWithEntryCreationStatusDto dto = mock(LogbookWithEntryCreationStatusDto.class);
        when(dto.getCreationStatus()).thenReturn(CreationStatus.ERROR_CREATING_ENTRY);
        when(logbookService.createLogbookEntry(any(UUID.class), any(LogbookEntry.class)))
                .thenReturn(dto);
        ResponseEntity<Object> responseEntity =
                logbookController.createEntry(uuid, logbookEntry);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(
                String.format(
                        "An error occurred whilst updating logbook identified by ID '%s'.",
                        uuid.toString()
                ),
                ((ErrorSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }

    @Test
    public void deleteEntry_mockLogbookNotFound() {
        UUID logbookUuid = UUID.randomUUID();
        UUID entryUuid = UUID.randomUUID();
        DeletionStatus deletionStatus = DeletionStatus.LOGBOOK_NOT_FOUND;
        when(logbookService.deleteLogbookEntry(any(UUID.class), any(UUID.class)))
                .thenReturn(deletionStatus);
        ResponseEntity<Object> responseEntity =
                logbookController.deleteEntry(logbookUuid, entryUuid);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(
                "Logbook doesn't exist.",
                ((ErrorSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }

    @Test
    public void deleteEntry_mockEntryNotFound() {
        UUID logbookUuid = UUID.randomUUID();
        UUID entryUuid = UUID.randomUUID();
        DeletionStatus deletionStatus = DeletionStatus.ENTRY_NOT_FOUND;
        when(logbookService.deleteLogbookEntry(any(UUID.class), any(UUID.class)))
                .thenReturn(deletionStatus);
        ResponseEntity<Object> responseEntity =
                logbookController.deleteEntry(logbookUuid, entryUuid);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(
                "Entry doesn't exist.",
                ((ErrorSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }

    @Test
    public void deleteEntry_mockEntryDeleted() {
        UUID logbookUuid = UUID.randomUUID();
        UUID entryUuid = UUID.randomUUID();
        DeletionStatus deletionStatus = DeletionStatus.ENTRY_DELETED;
        when(logbookService.deleteLogbookEntry(any(UUID.class), any(UUID.class)))
                .thenReturn(deletionStatus);
        ResponseEntity<Object> responseEntity =
                logbookController.deleteEntry(logbookUuid, entryUuid);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(
                "Entry deleted.",
                ((SuccessSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }

    @Test
    public void deleteEntry_mockErrorDeletingEntry() {
        UUID logbookUuid = UUID.randomUUID();
        UUID entryUuid = UUID.randomUUID();
        DeletionStatus deletionStatus = DeletionStatus.ERROR_DELETING_ENTRY;
        when(logbookService.deleteLogbookEntry(any(UUID.class), any(UUID.class)))
                .thenReturn(deletionStatus);
        ResponseEntity<Object> responseEntity =
                logbookController.deleteEntry(logbookUuid, entryUuid);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(
                "An error has occurred.",
                ((ErrorSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }

    @Test
    public void updateEntry_mockLogbookNotFound() {
        UUID logbookUuid = UUID.randomUUID();
        UUID entryUuid = UUID.randomUUID();
        LogbookEntry logbookEntry = mock(LogbookEntry.class);
        LogbookWithEntryUpdateStatusDto dto =
                mock(LogbookWithEntryUpdateStatusDto.class);
        when(dto.getUpdateStatus()).thenReturn(UpdateStatus.LOGBOOK_NOT_FOUND);
        when(logbookService.updateLogbookEntry(any(UUID.class), any(UUID.class), any(LogbookEntry.class)))
                .thenReturn(dto);
        ResponseEntity<Object> responseEntity =
                logbookController.updateEntry(logbookUuid, entryUuid, logbookEntry);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(
                "Logbook doesn't exist.",
                ((ErrorSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }

    @Test
    public void updateEntry_mockEntryNotFound() {
        UUID logbookUuid = UUID.randomUUID();
        UUID entryUuid = UUID.randomUUID();
        LogbookEntry logbookEntry = mock(LogbookEntry.class);
        LogbookWithEntryUpdateStatusDto dto =
                mock(LogbookWithEntryUpdateStatusDto.class);
        when(dto.getUpdateStatus()).thenReturn(UpdateStatus.ENTRY_NOT_FOUND);
        when(logbookService.updateLogbookEntry(any(UUID.class), any(UUID.class), any(LogbookEntry.class)))
                .thenReturn(dto);
        ResponseEntity<Object> responseEntity =
                logbookController.updateEntry(logbookUuid, entryUuid, logbookEntry);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(
                "Entry doesn't exist.",
                ((ErrorSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }

    @Test
    public void updateEntry_mockEntryUpdated() {
        UUID logbookUuid = UUID.randomUUID();
        UUID entryUuid = UUID.randomUUID();
        Logbook logbook = mock(Logbook.class);
        LogbookEntry logbookEntry = mock(LogbookEntry.class);
        LogbookWithEntryUpdateStatusDto dto =
                mock(LogbookWithEntryUpdateStatusDto.class);
        when(dto.getUpdateStatus()).thenReturn(UpdateStatus.ENTRY_UPDATED);
        when(dto.getLogbook()).thenReturn(logbook);
        when(logbookService.updateLogbookEntry(any(UUID.class), any(UUID.class), any(LogbookEntry.class)))
                .thenReturn(dto);
        ResponseEntity<Object> responseEntity =
                logbookController.updateEntry(logbookUuid, entryUuid, logbookEntry);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(logbook, responseEntity.getBody());
    }

    @Test
    public void updateEntry_mockErrorUpdatingEntry() {
        UUID logbookUuid = UUID.randomUUID();
        UUID entryUuid = UUID.randomUUID();
        LogbookEntry logbookEntry = mock(LogbookEntry.class);
        LogbookWithEntryUpdateStatusDto dto =
                mock(LogbookWithEntryUpdateStatusDto.class);
        when(dto.getUpdateStatus()).thenReturn(UpdateStatus.ERROR_UPDATING_ENTRY);
        when(logbookService.updateLogbookEntry(any(UUID.class), any(UUID.class), any(LogbookEntry.class)))
                .thenReturn(dto);
        ResponseEntity<Object> responseEntity =
                logbookController.updateEntry(logbookUuid, entryUuid, logbookEntry);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(
                "Something has gone wrong. Please contact our system administrator.",
                ((ErrorSingleMessageResponse) responseEntity.getBody()).getMessage()
        );
    }




}
