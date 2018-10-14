package app.data.transfer.internal.logbook;

import app.data.pilot.Logbook;

public class LogbookWithEntryUpdateStatusDto {

    private Logbook logbook;
    private UpdateStatus updateStatus;

    public LogbookWithEntryUpdateStatusDto(Logbook logbook, UpdateStatus updateStatus) {
        this.logbook = logbook;
        this.updateStatus = updateStatus;
    }

    public Logbook getLogbook() {
        return logbook;
    }

    public void setLogbook(Logbook logbook) {
        this.logbook = logbook;
    }

    public UpdateStatus getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(UpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
    }
}
