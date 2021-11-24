package edu.nuist.ojs.message.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountService {
    @Autowired
    LogService logService;
    public long count(String type){
      if(type.equals("all")){
          return logService.countByStatus("DELIVERED")
                  +logService.countByStatus("BOUNCED")
                  +logService.countByStatus("REJECTED");
      }
      else if(type.equals("success")){
          return logService.countByStatus("DELIVERED");
      }
      else if(type.equals("failed")){
          return logService.countByStatus("BOUNCED")+logService.countByStatus("REJECTED");
      }
      return 0;
    }
    public long countOnlyEndDate(String type, String endDate){
        if(type.equals("all")){
            return logService.countByEndDateLessThanEqualAndStatus(endDate,"DELIVERED")
                    +logService.countByEndDateLessThanEqualAndStatus(endDate,"BOUNCED")
                    +logService.countByEndDateLessThanEqualAndStatus(endDate,"REJECTED");
        }
        else if(type.equals("success")){
            return logService.countByEndDateLessThanEqualAndStatus(endDate,"DELIVERED");
        }
        else if(type.equals("failed")){
            return logService.countByEndDateLessThanEqualAndStatus(endDate,"BOUNCED")
                    +logService.countByEndDateLessThanEqualAndStatus(endDate,"REJECTED");
        }
        return 0;
    }
    public long countOnlyStartDate(String type, String startDate){
        if(type.equals("all")){
            return logService.countByStartDateGreaterThanEqualAnAndStatus(startDate,"DELIVERED")
                    +logService.countByStartDateGreaterThanEqualAnAndStatus(startDate,"BOUNCED")
                    +logService.countByStartDateGreaterThanEqualAnAndStatus(startDate,"REJECTED");
        }
        else if(type.equals("success")){
            return logService.countByStartDateGreaterThanEqualAnAndStatus(startDate,"DELIVERED");
        }
        else if(type.equals("failed")){
            return logService.countByStartDateGreaterThanEqualAnAndStatus(startDate,"BOUNCED")
                    +logService.countByStartDateGreaterThanEqualAnAndStatus(startDate,"REJECTED");
        }
        return 0;
    }
    public long count(String type, String startDate, String endDate){
        if(type.equals("all")){
            return logService.countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(endDate,startDate,"DELIVERED")
                    +logService.countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(endDate,startDate,"BOUNCED")
                    +logService.countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(endDate,startDate,"REJECTED");
        }
        else if(type.equals("success")){
            return logService.countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(endDate,startDate,"DELIVERED");
        }
        else if(type.equals("failed")){
            return logService.countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(endDate,startDate,"BOUNCED")
                    +logService.countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(endDate,startDate,"REJECTED");
        }
        return 0;
    }
}
