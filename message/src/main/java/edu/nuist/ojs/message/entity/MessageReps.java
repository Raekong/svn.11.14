package edu.nuist.ojs.message.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageReps extends JpaRepository<Message,Long> {

    Message save(Message message);

    List<Message> findByRecvId(long recvId);

    Message findById(long id);
    
    @Query(value = "select * from  message where recv_id=?1 and  config_point=?2 order by id desc limit 0,1 ", nativeQuery = true)
    Message findByRecvIdAndConfigPoint(long id, String configPoint);
    @Transactional
    @Modifying
    @Query(value = "update  message set is_read=TRUE where id=?1",nativeQuery = true)
    void read(long id);
    @Transactional
    @Modifying
    @Query(value = "delete from message where id in ( select t.id FROM (select * from message where recv_id=?1 order by id limit 0,?2) as t)",nativeQuery = true)
    void deleteTop(long recvId,int length);
    int countByRecvId(long recvId);
}
