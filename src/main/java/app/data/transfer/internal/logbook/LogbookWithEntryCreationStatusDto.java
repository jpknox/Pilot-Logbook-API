package app.data.transfer.internal.logbook;

import app.data.pilot.Logbook;

public class LogbookWithEntryCreationStatusDto {

    private Logbook logbook;
    private CreationStatus creationStatus;

    public LogbookWithEntryCreationStatusDto(Logbook logbook, CreationStatus creationStatus) {
        this.logbook = logbook;
        this.creationStatus = creationStatus;
    }

    public Logbook getLogbook() {
        return logbook;
    }

    public void setLogbook(Logbook logbook) {
        this.logbook = logbook;
    }

    public CreationStatus getCreationStatus() {
        return creationStatus;
    }

    public void setUpdateStatus(CreationStatus creationStatus) {
        this.creationStatus = creationStatus;
    }
}
