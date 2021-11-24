package edu.nuist.ojs.message.controller;

import edu.nuist.ojs.message.entity.Event;
import edu.nuist.ojs.message.entity.EventService;
import edu.nuist.ojs.message.entity.Log;
import edu.nuist.ojs.message.entity.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FailedReportApi {
    @Autowired
    LogService logService;
    @Autowired
    EventService eventService;
    @RequestMapping("/email/log/getFailedLogsByRecipientAddressAndDate")
    @ResponseBody
    public List<Log> getFailedLogs(@RequestAttribute String recipientAddress,
                                   @RequestAttribute(required = false) String startDate,
                                   @RequestAttribute(required = false) String endDate){
        if((startDate==null||startDate.trim().equals("")||startDate.trim().equals("null"))
                &&(endDate==null||endDate.trim().equals("")||endDate.trim().equals("null"))){
            return logService.findByRecipientAddressAndStatusIsNot(recipientAddress,"DELIVERED");
        }
        else if((startDate==null||startDate.trim().equals("")||startDate.trim().equals("null"))
                &&!(endDate==null||endDate.trim().equals("")||endDate.trim().equals("null"))){
            return logService.findByRecipientAddressAndEndDateLessThanEqualAndStatusIsNot(recipientAddress,endDate,"DELIVERED");
        }
        else if(!(startDate==null||startDate.trim().equals("")||startDate.trim().equals("null"))
                &&(endDate==null||endDate.trim().equals("")||endDate.trim().equals("null"))){
            return logService.findByRecipientAddressAndStartDateGreaterThanEqualAndStatusIsNot(recipientAddress,startDate,"DELIVERED");
        }
        else return logService.findByRecipientAddressAndStartDateGreaterThanEqualAndEndDateLessThanAndStatusIsNot(recipientAddress,startDate,endDate,"DELIVERED");
    }
    @RequestMapping("/email/log/getEventsById")
    @ResponseBody
    public List<Event> getEvents(@RequestAttribute String logId){
        long id= Long.parseLong(logId);
        return logService.findEventsById(id);
    }
}
