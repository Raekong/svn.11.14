package edu.nuist.ojs.message.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventReps extends JpaRepository<Event,Long> {
    Event save(Event event);
    List<Event> findByLogId(String logId);

}
