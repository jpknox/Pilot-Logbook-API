package app.data.pilot;

import app.data.aircraft.Aircraft;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class LogbookEntry {

    private UUID entryId;
    private LocalDate date;
    private Aircraft aircraft;
    private String captain;
    private String holdersOperatingCapacity;
    private String origin;
    private String destination;
    private LocalTime departure;
    private LocalTime arrival;
    private int singleEngineP1TimeMins;
    private int singleEngineP2TimeMins;
    private int numOfTakeoffs;
    private int numOfLandings;
    private String remarks;

    public LogbookEntry() {
        this.entryId = UUID.randomUUID();   //TODO: Spring uses this constructor. Write test to ensure this ID is set.
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
        this.entryId = UUID.randomUUID();
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
}
