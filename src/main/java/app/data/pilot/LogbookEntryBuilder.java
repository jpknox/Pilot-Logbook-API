package app.data.pilot;

import app.data.aircraft.Aircraft;

import java.time.LocalDate;
import java.time.LocalTime;

public class LogbookEntryBuilder {


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

    public LogbookEntryBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LogbookEntryBuilder withAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
        return this;
    }

    public LogbookEntryBuilder withCaptain(String captain) {
        this.captain = captain;
        return this;
    }

    public LogbookEntryBuilder withHoldersOperatingCapacity(String holdersOperatingCapacity) {
        this.holdersOperatingCapacity = holdersOperatingCapacity;
        return this;
    }

    public LogbookEntryBuilder withOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public LogbookEntryBuilder withDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public LogbookEntryBuilder withDeparture(LocalTime departure) {
        this.departure = departure;
        return this;
    }

    public LogbookEntryBuilder withArrival(LocalTime arrival) {
        this.arrival = arrival;
        return this;
    }

    public LogbookEntryBuilder withSingleEngineP1TimeMins(int singleEngineP1TimeMins) {
        this.singleEngineP1TimeMins = singleEngineP1TimeMins;
        return this;
    }

    public LogbookEntryBuilder withSingleEngineP2TimeMins(int singleEngineP2TimeMins) {
        this.singleEngineP2TimeMins = singleEngineP2TimeMins;
        return this;
    }

    public LogbookEntryBuilder withNumOfTakeoffs(int numOfTakeoffs) {
        this.numOfTakeoffs = numOfTakeoffs;
        return this;
    }

    public LogbookEntryBuilder withNumOfLandings(int numOfLandings) {
        this.numOfLandings = numOfLandings;
        return this;
    }

    public LogbookEntryBuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public LogbookEntry build() {
        return new LogbookEntry(
            date,
            aircraft,
            captain,
            holdersOperatingCapacity,
            origin,
            destination,
            departure,
            arrival,
            singleEngineP1TimeMins,
            singleEngineP2TimeMins,
            numOfTakeoffs,
            numOfLandings,
            remarks
        );
    }
}
