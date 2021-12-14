package edu.nuist.ojs.message.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    EventReps eventReps;
    public Event save(Event e){
        return eventReps.save(e);
    }

    
    public List<Event> findByLogId(String logId){
        return eventReps.findByLogId(logId);
    }
}
