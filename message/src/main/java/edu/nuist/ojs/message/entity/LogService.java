package edu.nuist.ojs.message.entity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    @Autowired
    LogReps logReps;
    @Autowired
    EventService eventService;
    public Log save(Log log){
        return logReps.save(log);
    }
    
    public List<Event> findEventsById(long id){
        Log log=logReps.findById(id);
       return eventService.findByLogId(log.getMessageId());
    }
    
    public Log findByMessageId(String messageId){
        return logReps.findByMessageId(messageId);
    }

    public void updateStatus(String messageId,String status){
        logReps.updateStatus(messageId,status);
    }
    
    public long countByStatus(String status){
        return logReps.countByStatus(status);
    }
    
    public long countByEndDateLessThanEqualAndStatus(String endDate,String status){
        return logReps.countByEndDateLessThanEqualAndStatus(endDate,status);
    }
    
    public long countByStartDateGreaterThanEqualAnAndStatus(String startDate,String status){
        return logReps.countByStartDateGreaterThanEqualAndStatus(startDate,status);
    }
    
    public long countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(String endDate,String startDate,String status){
        return logReps.countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(endDate,startDate,status);
    }
    
    public List<Log> findByRecipientAddressAndStatusIsNot(String recipientAddress, String status){

        return logReps.findByRecipientAddressAndStatusIsNot(recipientAddress,status);
    }
    
    public List<Log> findByRecipientAddressAndEndDateLessThanEqualAndStatusIsNot( String recipientAddress,String endDate,String status){
        return logReps.findByRecipientAddressAndEndDateLessThanEqualAndStatusIsNot(recipientAddress, endDate, status);
    }
    
    public List<Log> findByRecipientAddressAndStartDateGreaterThanEqualAndStatusIsNot(String recipientAddress,String startDate,String status){
        return logReps.findByRecipientAddressAndStartDateGreaterThanEqualAndStatusIsNot(recipientAddress, startDate, status);
    }
    
    public List<Log> findByRecipientAddressAndStartDateGreaterThanEqualAndEndDateLessThanAndStatusIsNot(String recipientAddress,
                                                                                                 String startDate,
                                                                                                 String endDate,
                                                                                                 String status){
        return logReps.findByRecipientAddressAndStartDateGreaterThanEqualAndEndDateLessThanAndStatusIsNot(recipientAddress, startDate, endDate, status);
    }

}
