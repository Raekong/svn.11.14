package edu.nuist.ojs.message.entity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LogReps extends JpaRepository<Log,Long>{
    Log save(Log log);
    Log findById(long id);
    Log findByMessageId(String messageId);
    List<Log> findByRecipientAddressAndStatusIsNot(String recipientAddress,String status);
    List<Log> findByRecipientAddressAndEndDateLessThanEqualAndStatusIsNot(String recipientAddress,String endDate,String status);
    List<Log> findByRecipientAddressAndStartDateGreaterThanEqualAndStatusIsNot(String recipientAddress,String startDate,String status);
    List<Log> findByRecipientAddressAndStartDateGreaterThanEqualAndEndDateLessThanAndStatusIsNot(String recipientAddress,
                                                                                                 String startDate,
                                                                                                 String endDate,
                                                                                                 String status);
     long countByStatus(String status);
     long countByEndDateLessThanEqualAndStatus(String endDate,String status);
     long countByStartDateGreaterThanEqualAndStatus(String startDate,String status);
     long countByEndDateIsLessThanEqualAndStartDateGreaterThanEqualAndStatus(String endDate,String startDate,String status);
    
    
    @Transactional
    @Modifying
    @Query(value = "update log set status=?2 where message_id=?1 and status!='DELIVERED'", nativeQuery = true)
    public void updateStatus(String messageId,String status);


}
