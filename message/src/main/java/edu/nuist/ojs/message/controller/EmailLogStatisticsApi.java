package edu.nuist.ojs.message.controller;

import edu.nuist.ojs.message.entity.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailLogStatisticsApi {
    @Autowired
    CountService countService;
    @RequestMapping("/email/log/statistics")
    @ResponseBody
    public long emailLogStatistics(@RequestAttribute(required = false) String startDate,
                                   @RequestAttribute(required = false) String endDate,
                                   @RequestAttribute String type){
        System.out.println(type);
        if((startDate==null||startDate.trim().equals("")||startDate.trim().equals("null"))
        &&(endDate==null||endDate.trim().equals("")||endDate.trim().equals("null"))){
            return countService.count(type);
        }
        else if((startDate==null||startDate.trim().equals("")||startDate.trim().equals("null"))
                &&!(endDate==null||endDate.trim().equals("")||endDate.trim().equals("null"))){
            return countService.countOnlyEndDate(type,endDate);
        }
        else if(!(startDate==null||startDate.trim().equals("")||startDate.trim().equals("null"))
                &&(endDate==null||endDate.trim().equals("")||endDate.trim().equals("null"))){
            return countService.countOnlyStartDate(type,startDate);
        }
        else return countService.count(type,startDate,endDate);
    }
}
