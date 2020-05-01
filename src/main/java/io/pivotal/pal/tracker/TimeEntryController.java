package io.pivotal.pal.tracker;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository=timeEntryRepository;
    }

    @ResponseBody
    @RequestMapping(value="/time-entries/{id}", method= RequestMethod.GET)
    public ResponseEntity<TimeEntry> read(@PathVariable Long id) {

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
        return new ResponseEntity<>(timeEntryRepository.list(), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value="/time-entries", method=RequestMethod.POST)
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {

        return new ResponseEntity<>(timeEntryRepository.create(timeEntry), HttpStatus.CREATED);
    }

    @ResponseBody
    @RequestMapping(value="/time-entries/{id}", method=RequestMethod.PUT)
    public ResponseEntity<TimeEntry> update(@PathVariable Long id,@RequestBody TimeEntry timeEntry) {

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
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }


}
