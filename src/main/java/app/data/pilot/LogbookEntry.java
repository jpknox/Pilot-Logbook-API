package app.data.pilot;

import app.data.aircraft.Aircraft;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class LogbookEntry {

    @Id
    @Type(type = "uuid-char")
    private final UUID entryId = UUID.randomUUID();

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(
            targetEntity = Aircraft.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Aircraft aircraft;

    @Column(name = "captain")
    private String captain;

    @Column(name = "holdersOperatingCapacity")
    private String holdersOperatingCapacity;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "departure")
    private LocalTime departure;

    @Column(name = "arrival")
    private LocalTime arrival;

    @Column(name = "singleEngineP1TimeMins")
    private int singleEngineP1TimeMins;

    @Column(name = "singleEngineP2TimeMins")
    private int singleEngineP2TimeMins;

    @Column(name = "numOfTakeoffs")
    private int numOfTakeoffs;

    @Column(name = "numOfLandings")
    private int numOfLandings;

    @Column(name = "remarks")
    private String remarks;

    public LogbookEntry() {
    }

    public LogbookEntry(LocalDate date,
                        Aircraft aircraft,
                        String captain,
                        String holdersOperatingCapacity,
                        String origin,
                        String destination,
                        LocalTime departure,
                        LocalTime arrival,
                        int singleEngineP1TimeMins,
                        int singleEngineP2TimeMins,
                        int numOfTakeoffs,
                        int numOfLandings,
                        String remarks) {
        this.date = date;
        this.aircraft = aircraft;
        this.captain = captain;
        this.holdersOperatingCapacity = holdersOperatingCapacity;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.singleEngineP1TimeMins = singleEngineP1TimeMins;
        this.singleEngineP2TimeMins = singleEngineP2TimeMins;
        this.numOfTakeoffs = numOfTakeoffs;
        this.numOfLandings = numOfLandings;
        this.remarks = remarks;
    }

    public UUID getEntryId() {
        return entryId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public String getHoldersOperatingCapacity() {
        return holdersOperatingCapacity;
    }

    public void setHoldersOperatingCapacity(String holdersOperatingCapacity) {
        this.holdersOperatingCapacity = holdersOperatingCapacity;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    public LocalTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalTime arrival) {
        this.arrival = arrival;
    }

    public int getSingleEngineP1TimeMins() {
        return singleEngineP1TimeMins;
    }

    public void setSingleEngineP1TimeMins(int singleEngineP1TimeMins) {
        this.singleEngineP1TimeMins = singleEngineP1TimeMins;
    }

    public int getSingleEngineP2TimeMins() {
        return singleEngineP2TimeMins;
    }

    public void setSingleEngineP2TimeMins(int singleEngineP2TimeMins) {
        this.singleEngineP2TimeMins = singleEngineP2TimeMins;
    }

    public int getNumOfTakeoffs() {
        return numOfTakeoffs;
    }

    public void setNumOfTakeoffs(int numOfTakeoffs) {
        this.numOfTakeoffs = numOfTakeoffs;
    }

    public int getNumOfLandings() {
        return numOfLandings;
    }

    public void setNumOfLandings(int numOfLandings) {
        this.numOfLandings = numOfLandings;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void overwriteWith(LogbookEntry replacement) {
        date = replacement.getDate();
        aircraft = replacement.getAircraft();
        captain = replacement.getCaptain();
        holdersOperatingCapacity = replacement.getHoldersOperatingCapacity();
        origin = replacement.getOrigin();
        destination = replacement.getDestination();
        departure = replacement.getDeparture();
        arrival = replacement.getArrival();
        singleEngineP1TimeMins = replacement.getSingleEngineP1TimeMins();
        singleEngineP2TimeMins = replacement.getSingleEngineP2TimeMins();
        numOfTakeoffs = replacement.getNumOfTakeoffs();
        numOfLandings = replacement.getNumOfLandings();
        remarks = replacement.getRemarks();
    }
}
