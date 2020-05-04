package io.pivotal.pal.tracker;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;
    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry
    ) {
        this.timeEntryRepository=timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @ResponseBody
    @RequestMapping(value="/time-entries/{id}", method= RequestMethod.GET)
    public ResponseEntity<TimeEntry> read(@PathVariable Long id) {
        actionCounter.increment();

        TimeEntry timeEntry=timeEntryRepository.find(id);

        if (timeEntry == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }

    }


    @ResponseBody
    @RequestMapping(value="/time-entries", method= RequestMethod.GET)
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();

        return new ResponseEntity<>(timeEntryRepository.list(), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value="/time-entries", method=RequestMethod.POST)
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        actionCounter.increment();

        ResponseEntity<TimeEntry> responseEntity= new ResponseEntity<>(timeEntryRepository.create(timeEntry), HttpStatus.CREATED);
        timeEntrySummary.record(timeEntryRepository.list().size());
        return responseEntity;
    }

    @ResponseBody
    @RequestMapping(value="/time-entries/{id}", method=RequestMethod.PUT)
    public ResponseEntity<TimeEntry> update(@PathVariable Long id,@RequestBody TimeEntry timeEntry) {
        actionCounter.increment();

        TimeEntry timeEntry1 = timeEntryRepository.update(id,timeEntry);

        if (timeEntry1 == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<>(timeEntry1, HttpStatus.OK);
        }
    }

    @ResponseBody
    @RequestMapping(value="/time-entries/{id}", method=RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable  Long id) {
        timeEntryRepository.delete(id);
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }


}
