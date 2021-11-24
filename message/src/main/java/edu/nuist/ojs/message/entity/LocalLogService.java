package edu.nuist.ojs.message.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalLogService {

    @Autowired
    LocalLogReps reps;

    public LocalLog save(LocalLog l){
        return reps.save(l);
    }
    public LocalLog findByMessageId(String messageId){
        return reps.findByMessageId(messageId);
    }
}
