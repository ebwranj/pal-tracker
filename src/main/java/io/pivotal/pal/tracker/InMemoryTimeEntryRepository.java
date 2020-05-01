package io.pivotal.pal.tracker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class InMemoryTimeEntryRepository implements  TimeEntryRepository {

    private Map<Long,TimeEntry> timeEntries =new HashMap<>();
    private Long id=0L;


    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        if (timeEntry.getId() ==0){
            id=id+1;
            timeEntry.setId(id);
        }

        timeEntries.put(timeEntry.getId(),timeEntry);


        return  timeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        if (timeEntries.get(id) == null){
            return null;
        }

        return timeEntries.get(id);

    }

    @Override
    public TimeEntry update(Long id,TimeEntry timeEntry) {
        if (timeEntries.get(id) == null){
            return null;
        }

        timeEntry.setId(id);

        timeEntries.put(id,timeEntry);
        return  timeEntry;
    }

    @Override
    public void delete(Long id) {

        timeEntries.remove(id);
    }


    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> timeEntriesList= new ArrayList<>();
        timeEntries.entrySet().stream().forEach(entry ->timeEntriesList.add(entry.getValue()) );
        System.out.println("list val");
       timeEntriesList.stream().forEach(System.out::print);
        return timeEntriesList;
    }
}
