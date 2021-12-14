package edu.nuist.ojs.message.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LocalLogReps extends JpaRepository<LocalLog,Long> {
    LocalLog save(LocalLog l);
    LocalLog findByMessageId(String messageId);
}
